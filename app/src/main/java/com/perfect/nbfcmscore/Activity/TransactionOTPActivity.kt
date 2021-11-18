package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class TransactionOTPActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var etxt_otp: AppCompatEditText? = null
    var btn_resend_otp: Button? = null
    var btn_submit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactionotp)


       setRegviews()

    }

    private fun setRegviews() {
        etxt_otp = findViewById(R.id.etxt_otp)
        btn_resend_otp = findViewById(R.id.btn_resend_otp)
        btn_submit = findViewById(R.id.btn_submit)

        btn_submit!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {

           /* R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@TransactionOTPActivity, HomeActivity::class.java))
            }*/
        }
    }
}