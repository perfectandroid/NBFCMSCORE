package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.BeneficiaryListAdapter
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


class BeneficiaryListActivity : AppCompatActivity() , View.OnClickListener , ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "BeneficiaryListActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null

    var rvBeneficiaryList: RecyclerView? = null
    var jArrayBeneficiary: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beneficiary_list)

        setInitialise()
        setRegister()

        getBeneficiary()
    }


    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_header = findViewById<TextView>(R.id.tv_header)

        rvBeneficiaryList = findViewById<RecyclerView>(R.id.rvBeneficiaryList)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                 onBackPressed()
//                startActivity(Intent(this@BeneficiaryListActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@BeneficiaryListActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun getBeneficiary() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@BeneficiaryListActivity, R.style.Progress)
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("29"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        //   val nidhiSP = applicationContext.getSharedPreferences(Config.SHARED_PREF346, 0)
                        //   val nidhicode = BankHeaderSP.getString("nidhicode", "")

                        //  requestObject1.put("nidhicode", MscoreApplication.encryptStart(nidhicode))

                        Log.e(TAG,"requestObject1  119   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  1192   "+e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getBeneficiaryDeatils(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  1193   "+response.body())
                                Log.e(TAG,"response  1194   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("BeneficiaryDeatils")
                                    jArrayBeneficiary = jobjt.getJSONArray("BeneficiaryDeatilsList")
                                    Log.e(TAG,"jArrayBeneficiary  1195   "+jArrayBeneficiary)

                                    val lLayout = GridLayoutManager(this@BeneficiaryListActivity, 1)
                                    rvBeneficiaryList!!.setLayoutManager(lLayout)
                                    rvBeneficiaryList!!.setHasFixedSize(true)
                                    val bene_adapter = BeneficiaryListAdapter(applicationContext!!, jArrayBeneficiary!!)
                                    rvBeneficiaryList!!.adapter = bene_adapter
                                    bene_adapter.setClickListener(this@BeneficiaryListActivity)



                                    //  AccountNobottomSheet(jArrayAccount!!)

                                } else {
                                    AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
                            AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
                    AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {

                AlertMessage().alertMessage(this@BeneficiaryListActivity,this@BeneficiaryListActivity,"Alert"," No Internet Connection. ",3);
            }
        }
    }

    override fun onClick(position: Int, data: String) {

        Log.e(TAG,"position  244  "+position+"  "+data)
        var jsonObject1 = jArrayBeneficiary!!.getJSONObject(position)

        val intent = Intent()
        intent.putExtra("BeneName", jsonObject1.getString("BeneName"))
        intent.putExtra("BeneIFSC", jsonObject1.getString("BeneIFSC"))
        intent.putExtra("BeneAccNo", jsonObject1.getString("BeneAccNo"))
        setResult(RESULT_OK, intent)
        finish()
    }

}