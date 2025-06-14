package me.yeahapps.mypetai.core.ui.component

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory
import timber.log.Timber

@Composable
fun RequestInAppReview(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    context: Context = LocalContext.current
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Оцените приложение")
            },
            text = {
                Text("Если вам нравится наше приложение, пожалуйста, оцените его в Play Маркете.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        launchInAppReview(context)
                        onDismiss()
                    }
                ) {
                    Text("Оценить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Позже")
                }
            }
        )
    }
}

fun launchInAppReview(context: Context) {
    val reviewManager = ReviewManagerFactory.create(context)
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            val activity = context as? Activity
            if (activity != null) {
                reviewManager.launchReviewFlow(activity, reviewInfo)
            }
        } else {
            Timber.tag("Review").w("Не удалось получить ReviewInfo: ${task.exception}")
        }
    }
}
