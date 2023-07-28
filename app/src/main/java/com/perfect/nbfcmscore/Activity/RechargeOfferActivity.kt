package com.perfect.nbfcmscore.Activity

import android.R.id.message
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.RechargeHeaderAdapters
import com.perfect.nbfcmscore.Adapter.offerListAdapter
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


class RechargeOfferActivity : AppCompatActivity() , View.OnClickListener, ItemClickListener{


    private var progressDialog: ProgressDialog? = null
    val TAG: String = "RechargeOfferActivity"
    private val REACHARGE_OFFER = 10


    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var rv_offerlist: RecyclerView? = null
    var rv_rechrgehead: RecyclerView? = null

    var arrayHeader: ArrayList<String>? = null
    var jObject4: JSONObject? = null
    var jsonArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge_offer)

        setInitialise()
        setRegister()


//        Intent intent = new Intent();
//        intent.putExtra("editTextValue","250.1" );
//        setResult(RESULT_OK, intent);
//        finish();
        val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF20, 0)
        val ID_Providers = pref.getString("ID_Providers", null)

//        val sessionId = intent.getStringExtra("ID_Providers")
        Log.e(TAG, "ID_Providers  29  " + ID_Providers)

        getOfferList(ID_Providers.toString())
    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        rv_rechrgehead = findViewById<RecyclerView>(R.id.rv_rechrgehead)
        rv_offerlist = findViewById<RecyclerView>(R.id.rv_offerlist)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back -> {
                onBackPressed()

            }

            R.id.im_home -> {
                startActivity(Intent(this@RechargeOfferActivity, HomeActivity::class.java))
                finish()
            }

        }
    }

    private fun getOfferList(ID_Providers: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeOfferActivity, R.style.Progress)
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
                        val TokenSP = applicationContext.getSharedPreferences(
                            Config.SHARED_PREF8,
                            0
                        )
                        val Token = TokenSP.getString("Token", null)
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF1,
                            0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        // requestObject1.put("Reqmode", MscoreApplication.encryptStart("17"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("Operator", MscoreApplication.encryptStart(ID_Providers))

                        Log.e(TAG, "requestObject1  150   " + requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG, "Some  1501   " + e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getRechargeOffers(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG, "response  1502   " + response.body())
                                Log.e(TAG, "response  1503   " + jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("RechargeOffersDets")

//                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
//                                    Log.e(TAG, "jArrayOperator  4344   " + jArrayOperator)
//                                    OperatorbottomSheet(jArrayOperator!!)

                                    val strBody: String = jobjt.getString("OffersDetails")
                                    val strStart =
                                        "{" + strBody.replace("\\r\\n".toRegex(), "") + "}"
                                    Log.e(TAG, "onResponse  183   $strStart")
                                    val jObject3 = JSONObject(
                                        strStart.replace(
                                            "\\\\".toRegex(),
                                            ""
                                        )
                                    )
                                    Log.e(TAG, "onResponse  185   $jObject3")

                                    val iterator1: Iterator<*> = jObject3!!.keys()
                                    while (iterator1.hasNext()) {
                                        val ope = iterator1.next() as String
                                        Log.e(TAG, "iterator   190   $ope")
                                        arrayHeader = ArrayList()

                                        jObject4 = jObject3.getJSONObject("" + ope)
                                        Log.e(TAG, "Oper    193   $jObject4")

                                        val iterator2: Iterator<*> = jObject4!!.keys()
                                        while (iterator2.hasNext()) {
                                            val key = iterator2.next() as String
                                            Log.e(TAG, "iterator   201   $key")
                                            if (key != "key") {
                                                arrayHeader!!.add("" + key)
                                            }
                                        }

                                    }

                                    Log.e(TAG, "jObject   213    $arrayHeader")

//                                    rvofferlist!!.setHasFixedSize(false)
//                                    val offer_adapter = RechargeHeaderAdapter(applicationContext!!, arrayHeader)
//                                    rvofferlist!!.setLayoutManager(LinearLayoutManager(this@RechargeOfferActivity, LinearLayoutManager.HORIZONTAL, false))
//                                    rvofferlist!!.setAdapter(offer_adapter)


                                    rv_rechrgehead!!.setHasFixedSize(false)
                                    val rechargeHeaderAdapter = RechargeHeaderAdapters(this@RechargeOfferActivity, arrayHeader!!)
                                    rv_rechrgehead!!.setLayoutManager(LinearLayoutManager(
                                        this@RechargeOfferActivity, LinearLayoutManager.HORIZONTAL, false))
                                    rv_rechrgehead!!.setAdapter(rechargeHeaderAdapter)
                                    rechargeHeaderAdapter.setClickListener(this@RechargeOfferActivity)
                                   // rechargeHeaderAdapter.setOnItemClickListener(this@RechargeOfferActivity)

                                    if (jObject4 != null) {
                                        setOfferList(jObject4!!, arrayHeader!![0])
                                    }


                                } else {
                                    AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG, "Some  2162   " + e.toString())
                                AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG, "Some  2163   " + t.message)
                            AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG, "Some  2165   " + e.toString())
                    AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {

                AlertMessage().alertMessage(this@RechargeOfferActivity,this@RechargeOfferActivity,"Alert","No Internet Connection.",3);
            }
        }
    }


    private fun setOfferList(jObject4: JSONObject, header: String) {
        try {
            Log.e(TAG, "jsonArray   319    $header")
            Log.e(TAG, "jsonArray   232029    $jObject4")

            val jObject5 = jObject4.getString(header)
            Log.e(TAG, "jObject5   338    $jObject5")
            jsonArray = JSONArray(jObject5)
            Log.e(TAG, "jsonArray   340    $jsonArray")


            rv_offerlist!!.setHasFixedSize(false)
            val lLayout = GridLayoutManager(this@RechargeOfferActivity, 1)
            rv_offerlist!!.setLayoutManager(lLayout)
            rv_offerlist!!.setHasFixedSize(true)
            val offer_adapter = offerListAdapter(
                applicationContext!!,
                jsonArray!!
            )
            rv_offerlist!!.adapter = offer_adapter
            offer_adapter.setClickListener(this@RechargeOfferActivity)

//            offer_adapter.setOnItemClickListener(this@RechargeOfferActivity)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e(TAG, "Exception   346    $e")
        }
    }

    override fun onClick(position: Int, data: String) {
        Log.e(TAG, "position  315  " + position + "  " + data)

        if (position==0){
            setOfferList(jObject4!!, data!!)
        }
        if (position==1){
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish() //finishing activity
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