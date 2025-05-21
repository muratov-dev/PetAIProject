package me.yeahapps.mypetai.feature.root.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.yeahapps.mypetai.R

sealed class BottomNavigationItem(val route: String, @DrawableRes val icon: Int, @StringRes val label: Int) {
    data object Discover : BottomNavigationItem("discover", R.drawable.ic_discover, R.string.discover_label)
    data object Create : BottomNavigationItem("create", R.drawable.ic_create, R.string.discover_label)
    data object Profile : BottomNavigationItem("profile", R.drawable.ic_profile, R.string.discover_label)
}