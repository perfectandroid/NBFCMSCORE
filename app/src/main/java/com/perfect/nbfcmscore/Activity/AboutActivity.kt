package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class AboutActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        imCompanylogo = findViewById<ImageView>(R.id.imCompanylogo)
        applogo = findViewById<ImageView>(R.id.applogo)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        val imlogo: ImageView = findViewById(R.id.imlogo)
        Glide.with(this).load(R.drawable.aboutusgif).into(imlogo)

        try {
            val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
            val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode",null)
            val CompanyLogoImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF13,0)
            val imagepathcompanylog = Config.IMAGE_URL+CompanyLogoImageCodeSP!!.getString("CompanyLogoImageCode",null)
            PicassoTrustAll.getInstance(this)!!.load(imagepath).error(null).into(applogo)
            PicassoTrustAll.getInstance(this)!!.load(imagepathcompanylog).error(null).into(imCompanylogo)
        }catch (e: Exception) {
            e.printStackTrace()}
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@AboutActivity, HomeActivity::class.java))
            }
        }
    }
}