package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountSummaryAdaptor
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


class MyaccountsummaryActivity : AppCompatActivity() , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    private var rv_accountsummary: RecyclerView? = null

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var AccountStatus: String=""
    var tv_header: TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountsummary)
        rv_accountsummary = findViewById<View>(R.id.rv_accountsummary) as RecyclerView?
        imgBack = findViewById<ImageView>(R.id.imgBack)
        tv_header= findViewById<TextView>(R.id.tv_header)

        val ID_accsummry = this.getSharedPreferences(Config.SHARED_PREF214,0)
        tv_header!!.setText(ID_accsummry.getString("AccountSummary",null))


        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        getAccountDetails()

    }


    private fun getAccountDetails() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@MyaccountsummaryActivity, R.style.Progress)
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

                        val FK_AccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF16, 0)
                        val FK_Account = FK_AccountSP.getString("FK_Account", null)

                        val SubModuleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF17, 0)
                        val SubModule = SubModuleSP.getString("SubModule", null)

                        val StatusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF18, 0)
                        val Status = StatusSP.getString("Status", null)
                        if (Status.equals("Open")){
                            AccountStatus = "1"
                        }else{
                            AccountStatus = "0"
                        }

                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("10"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("AccountStatus", MscoreApplication.encryptStart(AccountStatus))


                        requestObject1.put("FK_Account",  MscoreApplication.encryptStart(FK_Account))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))

                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e("TAG","requestObject1  155  "+requestObject1)

                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getAccountModuleWiseDetailsList(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e("TAG","response  155  "+response.body())

                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {


                                    val jobj = jObject.getJSONObject("AccountModuleWiseDetailsList")
                                    val jarray = jobj.getJSONArray("Data")
                                    val  jsonArrayKey = jarray.getJSONObject(0).getJSONArray("Details")
                                    val obj_adapter = AccountSummaryAdaptor(applicationContext!!, jsonArrayKey)
                                    rv_accountsummary!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                                    rv_accountsummary!!.adapter = obj_adapter
                                } else {
                                    AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(this@MyaccountsummaryActivity,this@MyaccountsummaryActivity,"Alert"," No Internet Connection. ",3);
            }
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@MyaccountsummaryActivity, HomeActivity::class.java))
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