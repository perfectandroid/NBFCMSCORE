package com.perfect.nbfcmscore.Helper

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.perfect.nbfcmscore.R
import java.util.*

class IdleUtil {
    companion object {
        lateinit var context1: Context
        var longTimer: Timer? = null
        var activity: Activity? = null
        val LOGOUT_TIME =300000 // delay in milliseconds i.e. 5 min = 300000 ms or use timeout argument
//        val LOGOUT_TIME = 3000
        var mRunnable: Runnable? = null
        private var mHandler: Handler? = null
        val mTime = 1000
        fun startLogoutTimer(context: Context, activity1: Activity) {
            context1 = context
            activity = activity1
            stopHandler()
            startHandler()
            if (longTimer != null) {
                longTimer?.cancel()
                longTimer = null
            }
            if (longTimer == null) {
                longTimer = Timer()
                longTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        cancel()
                        longTimer = null
                        try {
                            Handler(Looper.getMainLooper()).post { // write your code here
                                showSuccessDialog()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }, LOGOUT_TIME.toLong())
            }
        }

        fun startHandler() {
            mHandler = Handler(Looper.getMainLooper())
            mRunnable?.let { mHandler!!.postDelayed(it, mTime.toLong()) }
        }

        // stop handler function
        fun stopHandler() {
            mHandler = Handler(Looper.getMainLooper())
            mRunnable?.let { mHandler!!.removeCallbacks(it) }
        }

        fun showSuccessDialog() {
            val bottomSheetDialog = BottomSheetDialog(context1)
            bottomSheetDialog.setContentView(R.layout.session_alert)
            val submit = bottomSheetDialog.findViewById<TextView>(R.id.submit)
            submit!!.setOnClickListener {
                activity!!.finishAffinity()
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()
        }
    }


}