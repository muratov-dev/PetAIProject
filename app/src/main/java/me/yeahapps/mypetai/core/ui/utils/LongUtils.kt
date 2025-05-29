package me.yeahapps.mypetai.core.ui.utils

import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.formatMillisecondsToMmSs(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes)

    return String.format(Locale("ru", "RU"), "%02d:%02d", minutes, seconds)
}