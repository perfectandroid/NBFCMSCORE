package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class LoginActivity : AppCompatActivity() , View.OnClickListener {

    val TAG: String ="LoginActivity"
    private var progressDialog: ProgressDialog? = null
    var etxt_mob: EditText? = null
    var btlogin: Button? = null
    var tv_loginwithmob: TextView? = null
    var tv_entermobotp:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setRegViews()


        val ID_LoginmobSP = applicationContext.getSharedPreferences(Config.SHARED_PREF45,0)
        val ID_MobotpeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF46,0)
        val ID_EntermobSP = applicationContext.getSharedPreferences(Config.SHARED_PREF42,0)
        //   val ID_last4SP = applicationContext.getSharedPreferences(Config.SHARED_PREF43,0)
        val ID_ContinueSP = applicationContext.getSharedPreferences(Config.SHARED_PREF44,0)

        tv_loginwithmob!!.setText(ID_LoginmobSP.getString("loginwithmobilenumber",null))
        tv_entermobotp!!.setText(ID_MobotpeSP.getString("enteryourmobilenumberwewillsentyouOTPtoverify",null))
        etxt_mob!!.setHint(ID_EntermobSP.getString("entermobilenumber",null))
        //    etxt_accno!!.setText(ID_last4SP.getString("enter last4digitofa/cno",null))
        btlogin!!.setText(ID_ContinueSP.getString("continue",null))


        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

            //  Glide.with(this).load(IMAGRURL).placeholder(R.drawable.login_icon).into(imgLogo);
       // Glide.with(this).load(IMAGRURL).placeholder(null).into(imgLogo);
        PicassoTrustAll.getInstance(this@LoginActivity)!!.load(imagepath).error(android.R.color.transparent).into(imgLogo!!)
        tv_product_name!!.setText(ProductNameSP.getString("ProductName",null))

    }

    private fun setRegViews() {
        val tvforgetpassword = findViewById<TextView>(R.id.tvforgetpassword) as TextView
        tv_loginwithmob = findViewById<TextView>(R.id.tv_loginwithmob) as TextView
        tv_entermobotp = findViewById<TextView>(R.id.tv_entermobotp) as TextView

        etxt_mob = findViewById<EditText>(R.id.etxt_mob) as EditText

        btlogin = findViewById<Button>(R.id.btlogin) as Button
        tvforgetpassword!!.setOnClickListener(this)
        btlogin!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                validation()
            }
            R.id.tvforgetpassword-> {
                intent = Intent(applicationContext, ForgetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validation() {
        if (etxt_mob!!.text.toString() == null || etxt_mob!!.text.toString().isEmpty()) {
            etxt_mob!!.setError("Please Enter Mobile Number")
        }
        else if (etxt_mob!!.text.toString().isNotEmpty() && etxt_mob!!.text.toString().length!=10) {
            etxt_mob!!.setError("Please Enter Valid Mobile Number")
        }else{
            //etxt_mob!!.text=null
            setUrl()
          //  getlogin()
        }
    }

    private fun setUrl() {
        val TestingMobileNoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF311, 0)
        val strTestmobile = TestingMobileNoSP.getString("TestingMobileNo",null)


        val mobileNumber = etxt_mob!!.text.toString()
        if (strTestmobile.equals(mobileNumber) ) {

            val TestingURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF315, 0)
            val TestingImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF317, 0)
            val TestBankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF318, 0)
            val TestBankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF318, 0)
            val sslcertificatetestSP = applicationContext.getSharedPreferences(Config.SHARED_PREF318, 0)

            val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
            val baseurlSPEditer = baseurlSP.edit()
            baseurlSPEditer.putString("baseurl", TestingURLSP.getString("TestingURL",null))
            baseurlSPEditer.commit()

            val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
            val ImageURLSPEditer = ImageURLSP.edit()
            ImageURLSPEditer.putString("ImageURL", TestingImageURLSP.getString("TestingImageURL",null))
            ImageURLSPEditer.commit()


            val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
            val BankKeyEditer = BankKeySP.edit()
            BankKeyEditer.putString("BankKey", TestBankKeySP.getString("testBankKey",null))
            BankKeyEditer.commit()

            val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
            val BankHeaderEditer = BankHeaderSP.edit()
            BankHeaderEditer.putString("BankHeader", TestBankHeaderSP.getString("testBankHeader",null))
            BankHeaderEditer.commit()


            val certificateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF164, 0)
            val certificateSPEditer = certificateSP.edit()
            certificateSPEditer.putString("sslcertificate", sslcertificatetestSP.getString("testsslcertificate",null))
            certificateSPEditer.commit()


        }

            getlogin()




    }

    private fun getlogin() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)

        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LoginActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client:OkHttpClient = okhttp3 . OkHttpClient . Builder ()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(Config.getSSLSocketFactory(this), trustManager)
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
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("6"))
                        requestObject1.put("MobileNumber", MscoreApplication.encryptStart(etxt_mob!!.text.toString()))
                        //requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1 Login 10001   "+requestObject1)

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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getCustomerLoginVerification(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {


                                    val jobjt = jObject.getJSONObject("CustomerLoginVerification")
                                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                    val ID_ResponseSP = applicationContext.getSharedPreferences(Config.SHARED_PREF47,0)

                                    Log.i("popup",jobjt.getString("ResponseMessage"))
                                    if(jobjt.getString("ResponseMessage").equals("User Login Verified"))
                                    {
                                        builder.setMessage(""+ID_ResponseSP.getString("userloginverified",null))
                                    }
                                    else
                                    {
                                        builder.setMessage(""+jobjt.getString("ResponseMessage"))
                                    }

                                    builder.setPositiveButton("Ok"){dialogInterface, which ->




                                        val jobjt = jObject.getJSONObject("CustomerLoginVerification")
                                        intent = Intent(applicationContext, OTPActivity::class.java)
                                        intent.putExtra("FK_Customer", jobjt.getString("FK_Customer"))
                                        intent.putExtra("Token", jobjt.getString("Token"))
                                        intent.putExtra("CusMobile", jobjt.getString("CusMobile"))
                                        startActivity(intent)
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                } else {
                                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                    builder.setMessage(""+jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            }
                            catch (e: Exception) {
                                progressDialog!!.dismiss()
                                val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok"){dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok"){dialogInterface, which ->
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })

                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    e.printStackTrace()
                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            false -> {
                val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok"){dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

}