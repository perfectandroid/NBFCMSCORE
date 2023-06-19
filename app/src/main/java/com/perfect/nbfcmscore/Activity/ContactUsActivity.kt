package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class ContactUsActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_mobile: TextView? = null
    var tv_email: TextView? = null
    var tv_address: TextView? = null
    var tv_header: TextView? = null
    var tv_customername: TextView? = null
    var txtv_msg: TextView? = null
    var txtv_loctn: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)

        setRegViews()

        val ID_contctus = applicationContext.getSharedPreferences(Config.SHARED_PREF55,0)
        tv_header!!.setText(ID_contctus.getString("contactus",null))



        val imlogo: ImageView = findViewById(R.id.imlogo)
      //  Glide.with(this).load(R.drawable.contactusgif).into(imlogo)

        val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF31,0)
        tv_email!!.setText(CustomerNameSP.getString("ContactEmail",null))
        val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF30,0)
        tv_mobile!!.setText(CusMobileSP.getString("ContactNumber",null))

        val AddressSP = applicationContext.getSharedPreferences(Config.SHARED_PREF32,0)
        tv_address!!.setText(AddressSP.getString("ContactAddress",null))

        val giveusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF247,0)
        tv_customername!!.setText(giveusSP.getString("Giveusacall",null))

        val sendusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF248,0)
        txtv_msg!!.setText(sendusSP.getString("Sendusamessage",null))

        val visitSP = applicationContext.getSharedPreferences(Config.SHARED_PREF249,0)
        txtv_loctn!!.setText(visitSP.getString("Visitourlocation",null))
    }

    private fun setRegViews() {
        tv_mobile = findViewById<TextView>(R.id.tv_mobile)
        tv_email = findViewById<TextView>(R.id.tv_email)
        tv_address = findViewById<TextView>(R.id.tv_address)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        tv_customername = findViewById<TextView>(R.id.tv_customername)
        txtv_msg = findViewById<TextView>(R.id.txtv_msg)
        txtv_loctn = findViewById<TextView>(R.id.txtv_loctn)


        tv_header= findViewById<TextView>(R.id.tv_header)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@ContactUsActivity, HomeActivity::class.java))
            }
        }
    }
}