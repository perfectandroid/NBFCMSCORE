package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.KsebSectionAdapter
import com.perfect.nbfcmscore.Adapter.RechargeHeaderAdapters
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.ItemClickListener
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

class KsebSectionActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener {

    val TAG: String = "KsebSectionActivity"
    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null
    var img_clear: ImageView? = null

    var act_section: AutoCompleteTextView? = null
    var rvSectionList: RecyclerView? = null
    var jsonArraySection: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kseb_section)

        setInitialise()
        setRegister()

        val ID_slctn= applicationContext.getSharedPreferences(Config.SHARED_PREF288, 0)
        tv_header!!.setText(ID_slctn.getString("Selection", null))

        val Sectionsp = applicationContext.getSharedPreferences(Config.SHARED_PREF289, 0)
        act_section!!.setHint(Sectionsp.getString("Section", null))

        act_section!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    var originalString = s.toString()
                    if (originalString != "") {
                        Log.e(TAG,"originalString   39    "+originalString)
                        getSectionList(originalString)
                    } else {
                      //  act_section!!.setText("")
                        jsonArraySection = null
                        rvSectionList!!.setAdapter(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })


//        act_section!!.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                act_section!!.removeTextChangedListener(this)
////                try {
////                    var originalString = s.toString()
////                    if (originalString != "") {
////                        Log.e(TAG,"originalString   39    "+originalString)
////                        getSectionList(originalString)
////
////                    } else {
////                        act_section!!.setText("")
////                    }
////                } catch (nfe: NumberFormatException) {
////                    nfe.printStackTrace()
////                }
//                act_section!!.addTextChangedListener(this)
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                try {
//                    var originalString = s.toString()
//                    if (s.length != 0) {
//                        Log.e(TAG,"originalString   39    "+originalString)
//                        getSectionList(originalString)
//                    } else {
//
//                    }
//                } catch (e: Exception) {
//                }
//            }
//        })


    }



    private fun setInitialise() {
        tv_header = findViewById<TextView>(R.id.tv_header)
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        img_clear = findViewById<ImageView>(R.id.img_clear)

        act_section = findViewById<AutoCompleteTextView>(R.id.act_section)
        rvSectionList = findViewById<RecyclerView>(R.id.rvSectionList)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        img_clear!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()

            }

            R.id.im_home ->{
                startActivity(Intent(this@KsebSectionActivity, HomeActivity::class.java))
                finish()
            }

            R.id.img_clear ->{
               act_section!!.setText("")
            }

        }
    }


    private fun getSectionList(originalString: String) {

        jsonArraySection = null
        rvSectionList!!.setAdapter(null)
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
//                progressDialog = ProgressDialog(this@BranchDetailActivity, R.style.Progress)
//                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
//                progressDialog!!.setCancelable(false)
//                progressDialog!!.setIndeterminate(true)
//                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
//                progressDialog!!.show()
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

                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                      //  requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("SectioName", MscoreApplication.encryptStart(originalString))

                        Log.e(TAG,"requestObject1  158   "+requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
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
                    val call = apiService.getKSEBSectionDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                Log.e(TAG,"response  1581   "+response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    Log.e(TAG,"response  1582   "+response.body())



                                    val jobjt = jObject.getJSONObject("KSEBSectionDetails")
                                    jsonArraySection = jobjt.getJSONArray("KSEBSectionDetails")

                                    Log.e(TAG,"jsonArraySection  1583   "+jsonArraySection)


                                    rvSectionList!!.setHasFixedSize(false)
                                    val ksebsectAdapter = KsebSectionAdapter(this@KsebSectionActivity, jsonArraySection!!)
                                    rvSectionList!!.setLayoutManager(
                                        LinearLayoutManager(
                                        this@KsebSectionActivity, LinearLayoutManager.VERTICAL, false)
                                    )
                                    rvSectionList!!.setAdapter(ksebsectAdapter)
                                    ksebsectAdapter.setClickListener(this@KsebSectionActivity)





                                } else {
//                                    val builder = AlertDialog.Builder(
//                                        this@BranchDetailActivity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jObject.getString("EXMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                // progressDialog!!.dismiss()

//                                val builder = AlertDialog.Builder(
//                                    this@BranchDetailActivity,
//                                    R.style.MyDialogTheme
//                                )
//                                builder.setMessage("Some technical issues.")
//                                builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                }
//                                val alertDialog: AlertDialog = builder.create()
//                                alertDialog.setCancelable(false)
//                                alertDialog.show()
//                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            //  progressDialog!!.dismiss()

//                            val builder = AlertDialog.Builder(
//                                this@BranchDetailActivity,
//                                R.style.MyDialogTheme
//                            )
//                            builder.setMessage("Some technical issues.")
//                            builder.setPositiveButton("Ok") { dialogInterface, which ->
//                            }
//                            val alertDialog: AlertDialog = builder.create()
//                            alertDialog.setCancelable(false)
//                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    //   progressDialog!!.dismiss()
//                    val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
//                    builder.setMessage("Some technical issues.")
//                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                    }
//                    val alertDialog: AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                    e.printStackTrace()
                }
            }
            false -> {
//                val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
//                builder.setMessage("No Internet Connection.")
//                builder.setPositiveButton("Ok") { dialogInterface, which ->
//                }
//                val alertDialog: AlertDialog = builder.create()
//                alertDialog.setCancelable(false)
//                alertDialog.show()
            }
        }
    }

    override fun onClick(position: Int, data: String) {

        Log.e(TAG,"data   275   "+data+"   "+position)
        try {
            var jsonObject1 = jsonArraySection!!.getJSONObject(position)

            val intent = Intent()
            intent.putExtra("SecName", jsonObject1!!.getString("SecName"))
            intent.putExtra("SecCode", jsonObject1!!.getString("SecCode"))
            setResult(RESULT_OK, intent)
            finish() //finishing activity

        }catch (e: Exception){

        }

    }

}