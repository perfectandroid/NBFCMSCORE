package com.perfect.nbfcmscore.Helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

object ConnectivityUtils {
    @SuppressLint("MissingPermission")
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}