package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class LanguageSelectionActivity : AppCompatActivity(), View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    var rv_Languagelist: RecyclerView? = null
    var from: String? = null
    var tvHeader2: TextView? = null
    var tvskip: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languagesectection)
        setRegViews()

        val imglogo: ImageView = findViewById(R.id.imglogo)
        Glide.with(this).load(R.drawable.language).into(imglogo)

        from = intent.getStringExtra("From")
       /* if(from.equals("welcome"))
        {
            getLanguagelist()
        }
        else
        {
            this.recreate();*/
            getLanguagelist()
      /*  val SelectlanSP = applicationContext.getSharedPreferences(Config.SHARED_PREF38,0)
        val SkipSP = applicationContext.getSharedPreferences(Config.SHARED_PREF39,0)

        tvskip!!.setText(SelectlanSP!!.getString("SelectLanguage",null))
        tvHeader2!!.setText(SkipSP.getString("Skip",null))*/
        //}

       /* val SkipSP = this.getSharedPreferences(Config.SHARED_PREF39, 0)
        val LanguageslctSP = this.getSharedPreferences(Config.SHARED_PREF38, 0)

        tvHeader2!!.setText(LanguageslctSP.getString("SelectLanguage", null))
        tvskip!!.setText(SkipSP.getString("Skip", null))*/


    }

    private fun setRegViews() {
         tvskip = findViewById<TextView>(R.id.tvskip) as TextView
        tvHeader2 = findViewById<TextView>(R.id.tvHeader2) as TextView
        rv_Languagelist = findViewById<RecyclerView>(R.id.rv_Languagelist) as RecyclerView
        tvskip!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tvskip->{
                intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getLanguagelist() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LanguageSelectionActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@LanguageSelectionActivity))
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("7"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey))
                        )

                        Log.e("TAG", "requestObject1  language   " + requestObject1)
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
                    val call = apiService.getLanguages(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                 //   val jobjt = jObject.getJSONObject("VarificationMaintenance")

                                    val jobjt =
                                        jObject.getJSONObject("Languages")
                                    val jarray =
                                        jobjt.getJSONArray("LanguagesList")

                                    val obj_adapter = LanguageLsitAdaptor(applicationContext!!, jarray)
                                    rv_Languagelist!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                                    rv_Languagelist!!.adapter = obj_adapter

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@LanguageSelectionActivity,
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
                                    this@LanguageSelectionActivity,
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
                                this@LanguageSelectionActivity,
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
                    val builder = AlertDialog.Builder(this@LanguageSelectionActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@LanguageSelectionActivity, R.style.MyDialogTheme)
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