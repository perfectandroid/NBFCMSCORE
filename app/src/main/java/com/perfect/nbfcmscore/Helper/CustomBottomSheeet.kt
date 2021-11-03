package com.perfect.nbfcmscore.Helper

import android.app.Activity
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.perfect.nbfcmscore.R

object CustomBottomSheeet {

    fun Show(activity: Activity, message: String, mode: String) {

        var bottomSheetDialog = BottomSheetDialog(activity,R.style.BottomSheetDialog)
        bottomSheetDialog.setContentView(R.layout.custom_bottom_sheet)

        val tv_message = bottomSheetDialog.findViewById<TextView>(R.id.tv_message)

        tv_message!!.setText(""+message)

        bottomSheetDialog!!.setCancelable(true)
        bottomSheetDialog!!.show()
    }


}