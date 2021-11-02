package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.Helper.FullLenghRecyclertview
import com.perfect.nbfcmscore.R

class RechargeOfferActivity : AppCompatActivity() , View.OnClickListener{


    private var progressDialog: ProgressDialog? = null
    val TAG: String = "RechargeOfferActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_offer)

        setInitialise()
        setRegister()
    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)



    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
                 onBackPressed()

            }

            R.id.im_home ->{
                startActivity(Intent(this@RechargeOfferActivity, HomeActivity::class.java))
                finish()
            }

        }
    }

}