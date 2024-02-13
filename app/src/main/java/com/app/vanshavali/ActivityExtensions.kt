package com.app.vanshavali

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.setStatusBarColor(color: Int?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && color != null) {
        val statusColor = if (color == 0) Color.BLACK else color
        try {
            window.statusBarColor = statusColor
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun Activity.hideKeyBoard(view: View?) {
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.appInstalledOrNot(pkg: String): Boolean {
    return try {
        packageManager?.getApplicationInfo(pkg, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.openBrowser(url: String?) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



