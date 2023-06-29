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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
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
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class ChangeMpinActivity : AppCompatActivity(), View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    var etxt_oldpin: EditText? = null
    var etxt_newpin: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changempin)
        setRegViews()


        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        var IMAGRURL = IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)

        try { val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            Log.e("TAG","imagepath  116   "+imagepath)
            //PicassoTrustAll.getInstance(this)!!.load(imagepath).error(null).into(im_applogo)
            PicassoTrustAll.getInstance(this@ChangeMpinActivity)!!.load(imagepath).error(android.R.color.transparent).into(imgLogo!!)

        }catch (e: Exception) {
            e.printStackTrace()}

//        Glide.with(this).load(IMAGRURL).placeholder(R.drawable.login_icon).into(imgLogo);
        tv_product_name!!.setText(ProductNameSP.getString("ProductName",null))
    }

    private fun setRegViews() {
        etxt_oldpin = findViewById<EditText>(R.id.etxt_oldpin) as EditText
        etxt_newpin = findViewById<EditText>(R.id.etxt_newpin) as EditText
        val btcontinue = findViewById<Button>(R.id.btcontinue) as Button
        btcontinue!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btcontinue->{
                validation()

            }
        }
    }

    private fun validation() {
        if (etxt_oldpin!!.text.toString() == null || etxt_oldpin!!.text.toString().isEmpty()) {
            etxt_oldpin!!.setError("Please Enter MPIN")
        }
        else if (etxt_oldpin!!.text.toString().isNotEmpty() && etxt_oldpin!!.text.toString().length!=6) {
            etxt_oldpin!!.setError("Please Enter Valid 6 Digit MPIN")
        }
        else if (etxt_newpin!!.text.toString() == null || etxt_newpin!!.text.toString().isEmpty()) {
            etxt_newpin!!.setError("Please Enter New MPIN")
        }
        else if (etxt_newpin!!.text.toString().isNotEmpty() && etxt_newpin!!.text.toString().length!=6) {
            etxt_newpin!!.setError("Please Enter Valid 6 Digit New MPIN")
        }
        else{
            getChangeMpin(etxt_oldpin!!.text.toString(), etxt_newpin!!.text.toString())
        }
    }

    private fun getChangeMpin(varmpin: String,varnewmpin: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@ChangeMpinActivity, R.style.Progress)
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

                        val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("3"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("OldMPIN", MscoreApplication.encryptStart(varmpin))
                        requestObject1.put("MPIN", MscoreApplication.encryptStart(varnewmpin))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )

                        //   val nidhiSP = applicationContext.getSharedPreferences(Config.SHARED_PREF346, 0)
                        //   val nidhicode = BankHeaderSP.getString("nidhicode", "")

                        //  requestObject1.put("nidhicode", MscoreApplication.encryptStart(nidhicode))

                        Log.e("TAG", "requestObject1  varifctn   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getOTP(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("VarificationMaintenance")
                                    alertMessage(
                                        "Success",
                                        "" + jobjt.getString("ResponseMessage"),
                                        2
                                    )

                                } else {
                                    AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(this@ChangeMpinActivity,this@ChangeMpinActivity,"Alert","No Internet Connection.",3);
            }
        }

    }

    fun alertMessage(header: String, message: String, type: Int) {
        val bottomSheetDialog = BottomSheetDialog(this@ChangeMpinActivity)
        bottomSheetDialog.setContentView(R.layout.alert_message)
        val txt_ok = bottomSheetDialog.findViewById<TextView>(R.id.txt_ok)
        val img = bottomSheetDialog.findViewById<ImageView>(R.id.img)
        val txt_cancel = bottomSheetDialog.findViewById<TextView>(R.id.txt_cancel)
        val txtheader = bottomSheetDialog.findViewById<TextView>(R.id.header)
        val txtmessage = bottomSheetDialog.findViewById<TextView>(R.id.message)
        txtmessage!!.setText(message)
        txtheader!!.setText(header)
        txt_cancel!!.setText("OK")
        txt_cancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(
                Intent(
                    this@ChangeMpinActivity,
                    MpinActivity::class.java
                )
            )
            finish()
        }
        if (type == 1) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_alert)
        } else if (type == 2) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_success)
        } else if (type == 3) {
            txt_ok!!.visibility = View.GONE
            txt_cancel!!.visibility = View.VISIBLE
            img!!.setImageResource(R.drawable.new_nonetwork)
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }
}