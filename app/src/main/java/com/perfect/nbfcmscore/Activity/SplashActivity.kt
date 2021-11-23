package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Secure
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


class SplashActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    var tv_error_message: TextView? = null
    var btn_proceed: Button? = null
    var imglogo: ImageView? = null

    val CERT_NAME = "staticvm.pem"  //QA
    val BASE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/api/"  //DEVELOPMENT
    val IMAGE_URL = "https://202.164.150.65:14262/NbfcAndroidAPI/"

    /*val BASE_URL = "https://202.164.150.65:14262/NbfcAndroidAPIQA/api/"  //QA
    val IMAGE_URL = "https://202.164.150.65:14262/NbfcAndroidAPIQA/"*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
      //  doSplash()

        Log.e("BASE_URL","BASE_URL  49    "+BASE_URL)

        tv_error_message = findViewById<TextView>(R.id.tv_error_message)
        btn_proceed = findViewById<Button>(R.id.btn_proceed)


        val imgSplash: ImageView = findViewById(R.id.imsplashlogo)
        val imglogo: ImageView = findViewById(R.id.imglogo)
        Glide.with(this).load(R.drawable.splashgif).into(imgSplash)
        try {
            val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            PicassoTrustAll.getInstance(this@SplashActivity)!!.load(imagepath).error(android.R.color.transparent).into(imglogo!!)

        }catch (e: Exception) {
            e.printStackTrace()}


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
        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164,0)
        val certificateSPEditer = certificateSP.edit()
        certificateSPEditer.putString("sslcertificate", CERT_NAME)
        certificateSPEditer.commit()
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163,0)
        val baseurlSPEditer = baseurlSP.edit()
        baseurlSPEditer.putString("baseurl", BASE_URL)
        baseurlSPEditer.commit()
        val baseUrlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseUrlSP.getString("baseurl", null)
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
                            .baseUrl(baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("5"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))

                        Log.e("requestObject1", "requestObject1  113   " + requestObject1)
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
                                Log.e("response", "response  1131   " + response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("ResellerDetails")

                                    val AppStoreLinkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF10, 0)
                                    val AppStoreLinkEditer = AppStoreLinkSP.edit()
                                    AppStoreLinkEditer.putString("AppStoreLink", jobjt.getString("AppStoreLink"))
                                    AppStoreLinkEditer.commit()

                                    val PlayStoreLinkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
                                    val PlayStoreLinkEditer = PlayStoreLinkSP.edit()
                                    PlayStoreLinkEditer.putString("PlayStoreLink", jobjt.getString("PlayStoreLink"))
                                    PlayStoreLinkEditer.commit()

                                    val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12, 0)
                                    val ProductNameEditer = ProductNameSP.edit()
                                    ProductNameEditer.putString("ProductName", jobjt.getString("ProductName"))
                                    ProductNameEditer.commit()

                                    val CompanyLogoImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF13, 0)
                                    val CompanyLogoImageCodeEditer = CompanyLogoImageCodeSP.edit()
                                    CompanyLogoImageCodeEditer.putString("CompanyLogoImageCode", jobjt.getString("CompanyLogoImageCode"))
                                    CompanyLogoImageCodeEditer.commit()

                                    val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
                                    val AppIconImageCodeEditer = AppIconImageCodeSP.edit()
                                    AppIconImageCodeEditer.putString("AppIconImageCode", jobjt.getString("AppIconImageCode"))
                                    AppIconImageCodeEditer.commit()

                                    val ResellerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF15, 0)
                                    val ResellerNameEditer = ResellerNameSP.edit()
                                    ResellerNameEditer.putString("ResellerName", jobjt.getString("ResellerName"))
                                    ResellerNameEditer.commit()

                                    val ContactNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF30,0)
                                    val ContactNumberEditer = ContactNumberSP.edit()
                                    ContactNumberEditer.putString("ContactNumber", jobjt.getString("ContactNumber"))
                                    ContactNumberEditer.commit()

                                    val ContactEmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF31,0)
                                    val ContactEmailEditer = ContactEmailSP.edit()
                                    ContactEmailEditer.putString("ContactEmail", jobjt.getString("ContactEmail"))
                                    ContactEmailEditer.commit()

                                    val ContactAddressSP = applicationContext.getSharedPreferences(Config.SHARED_PREF32,0)
                                    val ContactAddressEditer = ContactAddressSP.edit()
                                    ContactAddressEditer.putString("ContactAddress", jobjt.getString("ContactAddress"))
                                    ContactAddressEditer.commit()

                                    val IsNBFCSP = applicationContext.getSharedPreferences(Config.SHARED_PREF33,0)
                                    val IsNBFCEditer = IsNBFCSP.edit()
                                    IsNBFCEditer.putString("IsNBFC", jobjt.getString("IsNBFC"))
                                    IsNBFCEditer.commit()

                                    val m_androidId = Secure.getString(contentResolver, Secure.ANDROID_ID)
                                    if(m_androidId.equals(jobjt.getString("TestingMachineId"))){
                                        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163,0)
                                        val baseurlSPEditer = baseurlSP.edit()
                                        baseurlSPEditer.putString("baseurl", jobjt.getString("TestingURL"))
                                        baseurlSPEditer.commit()
                                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165,0)
                                        val ImageURLSPEditer = ImageURLSP.edit()
                                        ImageURLSPEditer.putString("ImageURL", jobjt.getString("TestingImageURL"))
                                        ImageURLSPEditer.commit()
                                    }else{
                                        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163,0)
                                        val baseurlSPEditer = baseurlSP.edit()
                                        baseurlSPEditer.putString("baseurl", BASE_URL)
                                        baseurlSPEditer.commit()
                                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165,0)
                                        val ImageURLSPEditer = ImageURLSP.edit()
                                        ImageURLSPEditer.putString("ImageURL", IMAGE_URL)
                                        ImageURLSPEditer.commit()}

                                    if(jobjt.getString("CertificateStatus").equals("Live")) {
                                        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
                                        val certificateSPEditer = certificateSP.edit()
                                        certificateSPEditer.putString("sslcertificate", Config.CERT_NAME)
                                        certificateSPEditer.commit()
                                    }else{
                                        val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
                                        val certificateSPEditer = certificateSP.edit()
                                        certificateSPEditer.putString("sslcertificate", Config.CERT_NAME)
                                        certificateSPEditer.commit()}

                                    try {
                                        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
                                        PicassoTrustAll.getInstance(this@SplashActivity)!!.load(imagepath).error(android.R.color.transparent).into(imglogo!!)

                                    }catch (e: Exception) {
                                        e.printStackTrace()}

                                    doSplash()
                                    getMaintenanceMessage()
                                  //  getlabels()
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

    private fun getMaintenanceMessage() {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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
                            .baseUrl(baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("43"))
                      //  requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                       /* requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart("")
                        )*/
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  maintenance   " + requestObject1)
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
                    val call = apiService.getMaintenanceMsg(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-Maintenance", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("MaintenanceMessage")
                                    val maintenancejobjt = jobjt.getJSONArray("MaintenanceMessageList")
                                    val MaintenanceMessageList = maintenancejobjt.getJSONObject(0)
                                    val type = MaintenanceMessageList.getString("Type")

                                    var message = ""
                                    if (maintenancejobjt.length() != 0) {
                                        for (i in 0 until maintenancejobjt.length()) {
                                            val MaintenanceMessageL = maintenancejobjt.getJSONObject(i)
                                            message += if (type == "1") {
                                                "" + MaintenanceMessageL.getString("Description")
                                            } else {
                                                if (i == 0) {
                                                    "" + (i + 1) + " - " + MaintenanceMessageL.getString("Description")
                                                } else {
                                                    """
                                              ${i + 1} - ${MaintenanceMessageL.getString("Description")}"""
                                                }
                                            }
                                        }
                                        if (type == "1") {
                                            tv_error_message!!.setVisibility(View.VISIBLE)
                                            tv_error_message!!.setText(message)
                                        } else if (type == "0") {
                                            tv_error_message!!.setVisibility(View.VISIBLE)
                                            btn_proceed!!.setVisibility(View.VISIBLE)
                                            tv_error_message!!.setText(message)
                                        } else if (type == "-1") {
                                            val splashScreen = getString(R.string.splash_screen)
                                            if (splashScreen == "ON") {
                                                Handler().postDelayed({ this@SplashActivity.startUserregistrationActivity() }, 3000)
                                            } else {
                                                startUserregistrationActivity()
                                            }
                                        }
                                    }

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

    private fun startUserregistrationActivity() {
        var intent = Intent(this@SplashActivity, MpinActivity::class.java)
        startActivity(intent)
    }


}


