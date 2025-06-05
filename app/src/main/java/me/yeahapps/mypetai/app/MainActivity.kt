package me.yeahapps.mypetai.app

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.ProductDetails
import dagger.hilt.android.AndroidEntryPoint
import me.yeahapps.mypetai.core.ui.component.RequestInAppReview
import me.yeahapps.mypetai.core.ui.theme.PetAITheme
import me.yeahapps.mypetai.feature.onboarding.ui.screen.OnboardingScreen
import me.yeahapps.mypetai.feature.root.ui.screen.RootScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
        setContent {
            val navController = rememberNavController()
            PetAITheme {
//                SubscriptionScreen(this)
                PetAIApp(navController, startDestination = if (false) OnboardingScreen else RootScreen)
                if (!isFirstLaunch) RequestInAppReview(LocalContext.current)
            }
        }
    }
}
//    @Composable
//    fun SubscriptionScreen(activity: Activity, viewModel: BillingViewModel = hiltViewModel()) {
//        val subscriptions by viewModel.availableSubscriptions.collectAsState()  // список из BillingClient
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            for (product in subscriptions) {
//                SubscriptionItem(
//                    productDetails = product, onBuyClick = { selectedProduct ->
//                        viewModel.launchPurchaseFlow(activity, selectedProduct)
//                    })
//            }
//        }
//    }
//
//    @Composable
//    fun SubscriptionItem(
//        productDetails: ProductDetails, onBuyClick: (ProductDetails) -> Unit
//    ) {
//        // Получаем заголовок и цену из ProductDetails
//        val title = productDetails.name    // название продукта
//        productDetails.description
//        // Предполагаем, что первый оффер содержит нужную цену
//        val price =
//            productDetails.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
//                ?: "—"
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = title, style = MaterialTheme.typography.titleLarge)
//                Text(
//                    text = price,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = androidx.compose.ui.graphics.Color.Gray
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = { onBuyClick(productDetails) }) {
//                    Text(text = "Подписаться")
//                }
//            }
//        }
//    }
//}