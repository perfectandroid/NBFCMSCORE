package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.SourceAccListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.AlertMessage
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.Config.getSSLSocketFactory
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
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

class OwnBankFundTransferServiceActivity : AppCompatActivity(), View.OnClickListener {
    var rltv_ownaccount: LinearLayout? = null
    var rltv_otheraccount: LinearLayout? = null
    var rv_source_acc_list_own: RecyclerView? = null
    var rv_source_acc_list_other: RecyclerView? = null
    var ll_source_acc_list_own: LinearLayout? = null
    var ll_source_acc_list_other: LinearLayout? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var txt_ownacc: TextView? = null
    var txtotheracc: TextView? = null
    var tv_mycart: TextView? = null
    var txtv_Selacc: TextView? = null
    var txtv_Selacc1: TextView? = null


    private var jresult: JSONArray? = null
    private var jresult1: JSONArray? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_bank_fund_transfer_service_chooser)

        setRegViews()
    }

    private fun setRegViews() {
        rltv_ownaccount = findViewById<LinearLayout>(R.id.rltv_ownaccount)
        rltv_otheraccount = findViewById<LinearLayout>(R.id.rltv_otheraccount)
        rv_source_acc_list_own = findViewById(R.id.rv_source_acc_list_own)
        rv_source_acc_list_other = findViewById(R.id.rv_source_acc_list_other)
        ll_source_acc_list_other = findViewById<LinearLayout>(R.id.ll_source_acc_list_other)
        ll_source_acc_list_own = findViewById<LinearLayout>(R.id.ll_source_acc_list_own)

        txt_ownacc = findViewById<TextView>(R.id.txt_ownacc)
        txtotheracc = findViewById<TextView>(R.id.txtotheracc)
        tv_mycart = findViewById<TextView>(R.id.tv_mycart)
        txtv_Selacc = findViewById<TextView>(R.id.txtv_Selacc)
        txtv_Selacc1 = findViewById<TextView>(R.id.txtv_Selacc1)

        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)


        rltv_ownaccount!!.setOnClickListener(this)
        rltv_otheraccount!!.setOnClickListener(this)

        val ID_Ownacc = applicationContext.getSharedPreferences(Config.SHARED_PREF90, 0)
        val ID_Otheracc = applicationContext.getSharedPreferences(Config.SHARED_PREF91, 0)
        val ID_ownbnk = applicationContext.getSharedPreferences(Config.SHARED_PREF63, 0)
        val SelectaccSP = applicationContext.getSharedPreferences(Config.SHARED_PREF101, 0)

        txt_ownacc!!.setText(ID_Ownacc.getString("OWNACCOUNT", null))
        txtotheracc!!.setText(ID_Otheracc.getString("OTHERACCOUNT", null))
        tv_mycart!!.setText(ID_ownbnk.getString("OwnBank", null))
        txtv_Selacc!!.setText(SelectaccSP.getString("SelectYourAccount", null))
        txtv_Selacc1!!.setText(SelectaccSP.getString("SelectYourAccount", null))

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(
                    Intent(
                        this@OwnBankFundTransferServiceActivity,
                        HomeActivity::class.java
                    )
                )
            }
            R.id.rltv_ownaccount -> {
                if (ll_source_acc_list_own!!.getVisibility() == View.VISIBLE) {
                    ll_source_acc_list_other!!.setVisibility(View.GONE)
                    ll_source_acc_list_own!!.setVisibility(View.GONE)
                } else {
                    showOwnAccFromList("1")
                    ll_source_acc_list_other!!.setVisibility(View.GONE)
                    //  ll_source_acc_list_own!!.setVisibility(View.VISIBLE)
                }
            }
            R.id.rltv_otheraccount -> {
                if (ll_source_acc_list_other!!.visibility == View.VISIBLE) {
                    ll_source_acc_list_other!!.visibility = View.GONE
                    ll_source_acc_list_own!!.visibility = View.GONE
                } else {
                    showOtherAccFromList("1")
                    ll_source_acc_list_own!!.visibility = View.GONE
                    //  ll_source_acc_list_other!!.setVisibility(View.VISIBLE)
                }


            }
        }
    }

    private fun showOtherAccFromList(s: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                /*  progressDialog = ProgressDialog(this@OwnBankFundTransferServiceActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
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
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
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
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                            "SubMode",
                            MscoreApplication.encryptStart(s)
                        )

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
                    } catch (e: Exception) {
                        //  progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@OwnBankFundTransferServiceActivity,
                            this@OwnBankFundTransferServiceActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getOwnbankownaccountdetail(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                //   progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("OwnAccountdetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult1 = jsonobj2.getJSONArray("OwnAccountdetailsList")
                                    if (jresult1!!.length() != 0) {
                                        //    ll_source_acc_list_own!!.visibility = View.VISIBLE
                                        ll_source_acc_list_other!!.visibility = View.VISIBLE
                                        val lLayout = GridLayoutManager(
                                            this@OwnBankFundTransferServiceActivity,
                                            1
                                        )
                                        val adapter =
                                            SourceAccListAdapter(applicationContext, jresult1!!, 1)

                                        rv_source_acc_list_other!!.layoutManager = lLayout
                                        rv_source_acc_list_other!!.setHasFixedSize(true)
                                        rv_source_acc_list_other!!.adapter = adapter
                                    } else {


                                        AlertMessage().alertMessage(
                                            this@OwnBankFundTransferServiceActivity,
                                            this@OwnBankFundTransferServiceActivity,
                                            "Alert",
                                            jObject.getString("EXMessage"),
                                            1
                                        );
                                    }


                                } else {
                                    AlertMessage().alertMessage(
                                        this@OwnBankFundTransferServiceActivity,
                                        this@OwnBankFundTransferServiceActivity,
                                        "Alert",
                                        jObject.getString("EXMessage"),
                                        1
                                    );
                                }
                            } catch (e: Exception) {
                                // progressDialog!!.dismiss()

                                AlertMessage().alertMessage(
                                    this@OwnBankFundTransferServiceActivity,
                                    this@OwnBankFundTransferServiceActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            // progressDialog!!.dismiss()

                            AlertMessage().alertMessage(
                                this@OwnBankFundTransferServiceActivity,
                                this@OwnBankFundTransferServiceActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    //   progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@OwnBankFundTransferServiceActivity,
                        this@OwnBankFundTransferServiceActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@OwnBankFundTransferServiceActivity,
                    this@OwnBankFundTransferServiceActivity,
                    "Alert",
                    " No Internet Connection. ",
                    3
                );
            }
        }
    }

    private fun showOwnAccFromList(s: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@OwnBankFundTransferServiceActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
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
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                            "SubMode",
                            MscoreApplication.encryptStart(s)
                        )

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(
                            this@OwnBankFundTransferServiceActivity,
                            this@OwnBankFundTransferServiceActivity,
                            "Alert",
                            "Some technical issues.",
                            1
                        );
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getOwnbankownaccountdetail(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                // progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("OwnAccountdetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("OwnAccountdetailsList")
                                    if (jresult!!.length() != 0) {
                                        ll_source_acc_list_own!!.visibility = View.VISIBLE
                                        val Layout = GridLayoutManager(
                                            this@OwnBankFundTransferServiceActivity,
                                            1
                                        )
                                        val adapterown =
                                            SourceAccListAdapter(applicationContext!!, jresult!!, 0)

                                        rv_source_acc_list_own!!.layoutManager = Layout
                                        rv_source_acc_list_own!!.setHasFixedSize(true)
                                        rv_source_acc_list_own!!.adapter = adapterown
                                    } else {

                                        AlertMessage().alertMessage(
                                            this@OwnBankFundTransferServiceActivity,
                                            this@OwnBankFundTransferServiceActivity,
                                            "Alert",
                                            jObject.getString("EXMessage"),
                                            1
                                        );
                                    }


                                } else {
                                    AlertMessage().alertMessage(
                                        this@OwnBankFundTransferServiceActivity,
                                        this@OwnBankFundTransferServiceActivity,
                                        "Alert",
                                        jObject.getString("EXMessage"),
                                        1
                                    );
                                }
                            } catch (e: Exception) {
                                // progressDialog!!.dismiss()

                                AlertMessage().alertMessage(
                                    this@OwnBankFundTransferServiceActivity,
                                    this@OwnBankFundTransferServiceActivity,
                                    "Alert",
                                    "Some technical issues.",
                                    1
                                );
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            // progressDialog!!.dismiss()

                            AlertMessage().alertMessage(
                                this@OwnBankFundTransferServiceActivity,
                                this@OwnBankFundTransferServiceActivity,
                                "Alert",
                                "Some technical issues.",
                                1
                            );
                        }
                    })
                } catch (e: Exception) {
                    //   progressDialog!!.dismiss()
                    AlertMessage().alertMessage(
                        this@OwnBankFundTransferServiceActivity,
                        this@OwnBankFundTransferServiceActivity,
                        "Alert",
                        "Some technical issues.",
                        1
                    );
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(
                    this@OwnBankFundTransferServiceActivity,
                    this@OwnBankFundTransferServiceActivity,
                    "Alert",
                    " No Internet Connection. ",
                    3
                );
            }
        }

    }
}