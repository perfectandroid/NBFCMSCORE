package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Adapter.LanguageLsitAdaptor
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class SplashActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
      //  doSplash()
        val imgSplash: ImageView = findViewById(R.id.imsplashlogo)
        Glide.with(this).load(R.drawable.splashgif).into(imgSplash)
        getResellerDetails()
    }

    private fun doSplash() {
        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep((4 * 1000).toLong())
                    val Loginpref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
                    if (Loginpref.getString("loginsession", null) == null) {
                        val i = Intent(this@SplashActivity, WelcomeSliderActivity::class.java)
                        startActivity(i)
                        finish()
                    } else if (Loginpref.getString("loginsession", null) != null && !Loginpref.getString(
                            "loginsession",
                            null
                        )!!.isEmpty() && Loginpref.getString("loginsession", null) == "Yes") {
                        val i = Intent(this@SplashActivity, MpinActivity::class.java)
                        startActivity(i)
                        finish()
                    } else if (Loginpref.getString("loginsession", null) != null && !Loginpref.getString(
                            "loginsession",
                            null
                        )!!.isEmpty() && Loginpref.getString("loginsession", null) == "No") {
                        val i = Intent(this@SplashActivity, WelcomeActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        val i = Intent(this@SplashActivity, WelcomeSliderActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    private fun getResellerDetails() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SplashActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@SplashActivity))
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("5"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))

                        Log.e("requestObject1","requestObject1  113   "+requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                            findViewById(R.id.rl_main),
                            " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestObject1.toString()
                    )
                    val call = apiService.getResellerDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e("response","response  1131   "+response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("ResellerDetails")

                                    val AppStoreLinkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF10,0)
                                    val AppStoreLinkEditer = AppStoreLinkSP.edit()
                                    AppStoreLinkEditer.putString("AppStoreLink", jobjt.getString("AppStoreLink"))
                                    AppStoreLinkEditer.commit()

                                    val PlayStoreLinkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF11,0)
                                    val PlayStoreLinkEditer = PlayStoreLinkSP.edit()
                                    PlayStoreLinkEditer.putString("PlayStoreLink", jobjt.getString("PlayStoreLink"))
                                    PlayStoreLinkEditer.commit()

                                    val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
                                    val ProductNameEditer = ProductNameSP.edit()
                                    ProductNameEditer.putString("ProductName", jobjt.getString("ProductName"))
                                    ProductNameEditer.commit()

                                    val CompanyLogoImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF13,0)
                                    val CompanyLogoImageCodeEditer = CompanyLogoImageCodeSP.edit()
                                    CompanyLogoImageCodeEditer.putString("CompanyLogoImageCode", jobjt.getString("CompanyLogoImageCode"))
                                    CompanyLogoImageCodeEditer.commit()

                                    val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
                                    val AppIconImageCodeEditer = AppIconImageCodeSP.edit()
                                    AppIconImageCodeEditer.putString("AppIconImageCode", jobjt.getString("AppIconImageCode"))
                                    AppIconImageCodeEditer.commit()

                                    val ResellerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF15,0)
                                    val ResellerNameEditer = ResellerNameSP.edit()
                                    ResellerNameEditer.putString("ResellerName", jobjt.getString("ResellerName"))
                                    ResellerNameEditer.commit()

                                    doSplash()
                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@SplashActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@SplashActivity,
                                    R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                this@SplashActivity,
                                R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(this@SplashActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = AlertDialog.Builder(this@SplashActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }
}