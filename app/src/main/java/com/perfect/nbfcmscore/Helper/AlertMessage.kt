package com.perfect.nbfcmscore.Helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.perfect.nbfcmscore.Activity.WelcomeActivity
import com.perfect.nbfcmscore.R

public class AlertMessage {


//    fun alertMessage(context:Context,activity:Activity,message:String,) {
//
//        val dialogBuilder = android.app.AlertDialog.Builder(context)
//        val inflater: LayoutInflater = activity.getLayoutInflater()
//        val dialogView: View = inflater.inflate(R.layout.bottom_update, null)
//        dialogBuilder.setView(dialogView)
//        val alertDialog = dialogBuilder.create()
//        val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
//        val tv_msg = dialogView.findViewById<TextView>(R.id.txt1)
//        val tv_msg2 = dialogView.findViewById<TextView>(R.id.txt2)
//        tv_msg.text = message
//        tv_msg2.text = "New version of this application is available.\n" +
//                "Click update for new version"
//        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
//        tv_cancel.setOnClickListener { alertDialog.dismiss() }
//        tv_share.setOnClickListener { //  finishAffinity();
//
////            val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
////            val url = pref.getString("PlayStoreLink", null)
////            Log.i("URL", url.toString())
////            try {
////                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
////            } catch (anfe: ActivityNotFoundException) {
////                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
////            }
////            //  alertDialog.dismiss()
//        }
//
//        alertDialog.window?.setBackgroundDrawableResource(R.color.transparent)
//        alertDialog.setCancelable(false)
//        alertDialog.show()
//
//
//    }

    fun alertMessage(context:Context,activity:Activity,header:String,message:String,type:Int) {
        //type 1=failed alert
        //type 2=Success alert
        //type 3=No network
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.alert_message)
        val txt_ok = bottomSheetDialog.findViewById<TextView>(R.id.txt_ok)
        val img = bottomSheetDialog.findViewById<ImageView>(R.id.img)
        val txt_cancel = bottomSheetDialog.findViewById<TextView>(R.id.txt_cancel)
        val txtheader = bottomSheetDialog.findViewById<TextView>(R.id.header)
        val txtmessage = bottomSheetDialog.findViewById<TextView>(R.id.message)
        txtmessage!!.setText(message)
        txtheader!!.setText(header)
        txt_cancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        if(type==1)
        {
            txt_ok!!.visibility=View.GONE
            txt_cancel!!.visibility=View.VISIBLE
            img!!.setImageResource(R.drawable.new_alert)
        }
        else if(type==2)
        {

        }
        else if(type==3)
        {
            txt_ok!!.visibility=View.GONE
            txt_cancel!!.visibility=View.VISIBLE
            img!!.setImageResource(R.drawable.new_nonetwork)
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }

}