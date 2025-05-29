package me.yeahapps.mypetai.core.ui.component

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.android.play.core.review.ReviewManagerFactory

@Composable
fun RequestInAppReview(context: Context) {
    val activity = context as? Activity ?: return

    LaunchedEffect(Unit) {

    }
}