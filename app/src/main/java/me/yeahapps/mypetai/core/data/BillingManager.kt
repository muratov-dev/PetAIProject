package me.yeahapps.mypetai.core.data

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.yeahapps.mypetai.core.di.ApplicationCoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context, @ApplicationCoroutineScope private val scope: CoroutineScope
) : PurchasesUpdatedListener {

    private val billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()

    private val _availableSubscriptions = MutableStateFlow<List<ProductDetails>>(emptyList())
    val availableSubscriptions: StateFlow<List<ProductDetails>>
        get() = _availableSubscriptions

    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed: StateFlow<Boolean> = _isSubscribed

    private val _billingError = MutableStateFlow<String?>(null)
    val billingError: StateFlow<String?> = _billingError

    init {
        startBillingConnection()
    }

    /**
     * Устанавливает соединение с BillingClient, и сразу запрашивает список подписок и проверяет уже купленные.
     */
    private fun startBillingConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Клиент готов к взаимодействию, запрашиваем продукты и текущие покупки
                    queryAvailableSubscriptions()
                    queryExistingPurchases()
                } else {
                    _billingError.value = "Billing setup failed: code = ${billingResult.responseCode}"
                }
            }

            override fun onBillingServiceDisconnected() {
                _billingError.value = "Billing service disconnected"
            }
        })
    }

    /**
     * Запрашивает все подписки по ID (annual_premium и weekly_premium).
     * Результат приходит в callback queryProductDetailsAsync.
     */
    private fun queryAvailableSubscriptions() {
        val productList = listOf(
            createProductDetailsParams("me.yeahapps.mypetai.weekly"),
            createProductDetailsParams("me.yeahapps.mypetai.yearly")
        )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Обновляем StateFlow списком доступных подписок
                _availableSubscriptions.value = productDetailsList
            } else {
                _billingError.value = "Error querying products: code = ${billingResult.responseCode}"
            }
        }
    }

    private fun createProductDetailsParams(productId: String): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder().setProductId(productId)
            .setProductType(BillingClient.ProductType.SUBS).build()
    }

    /**
     * Запрашивает существующие покупки (подписки), чтобы восстановить состояние.
     */
    private fun queryExistingPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchaseList(purchaseList)
            } else {
                _billingError.value = "Error querying existing purchases: code = ${billingResult.responseCode}"
            }
        }
    }

    /**
     * Запускает флоу покупки для выбранного продукта.
     * @param activity – Activity, из которой вызываем launchBillingFlow.
     * @param productDetails – ProductDetails выбираемого продукта (из availableSubscriptions).
     */
    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        // Выбираем первый оффер у подписки (offerToken обязателен)
        val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
        val offerToken = offer?.offerToken
        if (offerToken == null) {
            _billingError.value = "No offer token for product ${productDetails.productId}"
            return
        }

        // Формируем параметры для BillingClient
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
            .setOfferToken(offerToken).build()

        val billingFlowParams =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(productParams)).build()

        // Запускаем экран покупки
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    /**
     * Callback после попытки покупки.
     * В случае OK нужно передать полученный purchase в handlePurchase.
     */
    override fun onPurchasesUpdated(
        billingResult: BillingResult, purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                // Пользователь отменил покупку
                _billingError.value = "Purchase canceled by user"
            }

            else -> {
                // Другая ошибка
                _billingError.value = "Error code: ${billingResult.responseCode}"
            }
        }
    }

    /**
     * Обрабатывает один объект Purchase (подписку).
     * Если purchaseState == PURCHASED и ещё не подтверждена, подтверждаем её (acknowledge).
     * После успешного acknowledge устанавливаем isSubscribed = true.
     */
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Проверяем, подтверждена ли уже покупка
            if (!purchase.isAcknowledged) {
                val acknowledgeParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                billingClient.acknowledgePurchase(acknowledgeParams) { ackResult ->
                    if (ackResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // Покупка подтверждена: выдаём доступ
                        _isSubscribed.value = true
                    } else {
                        _billingError.value = "Acknowledge failed: code = ${ackResult.responseCode}"
                    }
                }
            } else {
                // Уже подтверждена (либо приложение перезапущено), даём доступ
                _isSubscribed.value = true
            }
        }
    }

    /**
     * Обрабатывает несколько Purchase (например, при восстановлении).
     */
    private fun handlePurchaseList(purchaseList: List<Purchase>) {
        // Если хотя бы одна подписка активна (PURCHASED и !isAcknowledged → нужно подтвердить), считаем, что пользователь подписан
        var subscribed = false
        for (purchase in purchaseList) {
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                subscribed = true
                if (!purchase.isAcknowledged) {
                    // Если есть неподтверждённая транзакция, подтверждаем её
                    val acknowledgeParams =
                        AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                    billingClient.acknowledgePurchase(acknowledgeParams) { ackResult ->
                        // Игнорируем результат – если OK, доступ уже предоставлен
                    }
                }
            }
        }
        _isSubscribed.value = subscribed
    }
}