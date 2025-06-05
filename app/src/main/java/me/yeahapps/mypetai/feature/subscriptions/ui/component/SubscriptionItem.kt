package me.yeahapps.mypetai.feature.subscriptions.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import me.yeahapps.mypetai.core.ui.theme.PetAITheme

@Composable
fun SubscriptionItem(
    details: ProductDetails, modifier: Modifier = Modifier, isSelected: Boolean = false, salePercent: Int = 0
) {
    val selectedBackgroundColor = PetAITheme.colors.buttonPrimaryDefault.copy(alpha = 0.1f)
    val unselectedBackgroundColor = Color(0xFF0E0C18)
    val backgroundColor = if (isSelected) selectedBackgroundColor else unselectedBackgroundColor

    val selectedBorderColor = PetAITheme.colors.buttonPrimaryDefault
    val unselectedBorderColor = Color.White.copy(alpha = 0.5f)
    val borderColor = if (isSelected) selectedBorderColor else unselectedBorderColor

    val shape = RoundedCornerShape(100.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = shape)
            .clip(shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = details.name)
            Text(text = details.description)
        }
        if (salePercent > 0) {
            Text(text = "${salePercent}% OFF", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
        }
        val price =
            details.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
                ?: "—"
        val a =
            details.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.billingPeriod
        Column() {
            Text(text = "$${price}", textAlign = TextAlign.End)
            Text(text = "per $a", textAlign = TextAlign.End)
        }
    }
}