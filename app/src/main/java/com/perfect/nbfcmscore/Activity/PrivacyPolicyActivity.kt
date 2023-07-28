package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class PrivacyPolicyActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_header: TextView? = null
    var txtv_privcy: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacypolicy)

        setRegViews()

        val PrivcySP = applicationContext.getSharedPreferences(Config.SHARED_PREF57, 0)
        tv_header!!.setText(PrivcySP.getString("privacypolicy", null))

        val PrivcypolicySP = applicationContext.getSharedPreferences(Config.SHARED_PREF238, 0)
        txtv_privcy!!.setText(PrivcypolicySP.getString("Privacypolicytext", null))




        val imlogo: ImageView = findViewById(R.id.imlogo)
        //Glide.with(this).load(R.drawable.privacypolicygif).into(imlogo)

    }

    private fun setRegViews() {
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        tv_header = findViewById<TextView>(R.id.tv_header)
        txtv_privcy = findViewById<TextView>(R.id.txtv_privcy)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@PrivacyPolicyActivity, HomeActivity::class.java))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}