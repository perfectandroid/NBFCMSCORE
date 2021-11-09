package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        setRegViews()

        val imwelcome: ImageView = findViewById(R.id.imwelcome)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)
//        Glide.with(this).load(R.drawable.welcomegif).into(imwelcome)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        var IMAGRURL = Config.IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)
        val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

      //  Glide.with(this).load(IMAGRURL).placeholder(null).into(imwelcome!!);
        PicassoTrustAll.getInstance(this@WelcomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(imwelcome!!)
        tv_product_name!!.setText(""+ProductNameSP.getString("ProductName",null))


     //   getlabels()
    }

    private fun setRegViews() {
        val btlogin = findViewById<Button>(R.id.btlogin) as Button
        val btregistration = findViewById<Button>(R.id.btregistration) as Button
        btlogin!!.setOnClickListener(this)
        btregistration!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btregistration-> {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
    }

}

