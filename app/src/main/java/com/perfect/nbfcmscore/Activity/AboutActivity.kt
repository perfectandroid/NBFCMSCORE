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

class AboutActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    var tv_header: TextView? = null
    var txtv_abt: TextView? = null
    var txtv_licnse: TextView? = null
    var tv_technlgy: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setRegViews()

        val ID_abtus = applicationContext.getSharedPreferences(Config.SHARED_PREF54,0)
        tv_header!!.setText(ID_abtus.getString("aboutus",null))

        val ID_abtus1 = applicationContext.getSharedPreferences(Config.SHARED_PREF237,0)
        txtv_abt!!.setText(ID_abtus1.getString("Aboutustext",null))

        val ID_lcnse = applicationContext.getSharedPreferences(Config.SHARED_PREF305,0)
        txtv_licnse!!.setText(ID_lcnse.getString("LICENSEDTO",null))

        val ID_technl = applicationContext.getSharedPreferences(Config.SHARED_PREF306,0)
        tv_technlgy!!.setText(ID_technl.getString("TECHNOLOGYPARTNER",null))



        val imlogo: ImageView = findViewById(R.id.imlogo)
        //Glide.with(this).load(R.drawable.aboutusgif).into(imlogo)

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        try {
            val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode",null)

            val CompanyLogoImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF13,0)
            val imagepathcompanylog = IMAGE_URL+CompanyLogoImageCodeSP!!.getString("CompanyLogoImageCode",null)

            PicassoTrustAll.getInstance(this@AboutActivity)!!.load(imagepath).error(android.R.color.transparent).into(applogo!!)
            PicassoTrustAll.getInstance(this@AboutActivity)!!.load(imagepathcompanylog).error(android.R.color.transparent).into(imCompanylogo!!)

//            PicassoTrustAll.getInstance(this)!!.load(imagepath).error(null).into(applogo)
//            PicassoTrustAll.getInstance(this)!!.load(imagepathcompanylog).error(null).into(imCompanylogo)
        }catch (e: Exception) {
            e.printStackTrace()}
    }

    private fun setRegViews() {
        imCompanylogo = findViewById<ImageView>(R.id.imCompanylogo)

        txtv_abt= findViewById<TextView>(R.id.txtv_abt)
        txtv_licnse= findViewById<TextView>(R.id.txtv_licnse)
        tv_technlgy= findViewById<TextView>(R.id.tv_technlgy)


        applogo = findViewById<ImageView>(R.id.applogo)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        tv_header= findViewById<TextView>(R.id.tv_header)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
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

    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}