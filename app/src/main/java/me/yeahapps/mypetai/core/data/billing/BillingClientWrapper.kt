package me.yeahapps.mypetai.core.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import timber.log.Timber

class BillingClientWrapper(
    private val context: Context, private val listener: PurchasesUpdatedListener
) {
    private var billingClient: BillingClient? = null

    fun startConnection(onReady: () -> Unit) {
        billingClient = BillingClient.newBuilder(context).setListener(listener).enablePendingPurchases().build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Timber.d("onBillingServiceDisconnected")
            }

            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) onReady()
            }
        })
    }

    fun queryProductDetails(productId: String, onResult: (ProductDetails?) -> Unit) {
        val params = QueryProductDetailsParams.newBuilder().setProductList(
            listOf(
                QueryProductDetailsParams.Product.newBuilder().setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS).build()
            )
        ).build()

        billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                onResult(productDetailsList.firstOrNull())
            } else onResult(null)
        }
    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return

        val billingParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
                    .setOfferToken(offerToken).build()
            )
        ).build()

        billingClient?.launchBillingFlow(activity, billingParams)
    }

    fun acknowledgePurchase(purchase: Purchase, onAcknowledged: () -> Unit) {
        val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()

        billingClient?.acknowledgePurchase(params) { result ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) onAcknowledged()
        }
    }
}