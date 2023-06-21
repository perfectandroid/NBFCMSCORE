package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.MessageAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
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

class MessagesActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_header: TextView? = null
    private var jresult: JSONArray? = null
    private var rv_messages: RecyclerView? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        setRegViews()
        getMessages()


    }

    private fun getMessages() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@MessagesActivity, R.style.Progress)
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
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("46"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))


                        Log.e("TAG", "requestObject1  messages   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getMessages(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response Messages", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("MessagesDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("MessagesList")
                                    if (jresult!!.length() != 0) {

                                        val lLayout =
                                                GridLayoutManager(this@MessagesActivity, 1)
                                        rv_messages!!.layoutManager = lLayout
                                        rv_messages!!.setHasFixedSize(true)
                                        val adapter = MessageAdapter(
                                                this@MessagesActivity,
                                                jresult!!

                                        )
                                        rv_messages!!.adapter = adapter
                                    } else {
                                        rv_messages!!.visibility = View.GONE

                                    }


                                } else {
                                    AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(this@MessagesActivity,this@MessagesActivity,"Alert"," No Internet Connection. ",3);
            }
        }
    }

    private fun setRegViews() {
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)

        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        rv_messages = findViewById(R.id.rv_messages)

        tv_header = findViewById(R.id.tv_header)

        val NotifctnSP = applicationContext.getSharedPreferences(Config.SHARED_PREF62, 0)
        tv_header!!.setText(NotifctnSP.getString("NotificationandMessages",null))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@MessagesActivity, HomeActivity::class.java))
            }
        }
    }
}