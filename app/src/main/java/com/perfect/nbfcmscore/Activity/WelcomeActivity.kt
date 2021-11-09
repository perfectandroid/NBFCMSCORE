package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
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
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        setRegViews()

        val imwelcome: ImageView = findViewById(R.id.imwelcome)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)
//        Glide.with(this).load(R.drawable.welcomegif).into(imwelcome)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        var IMAGRURL = Config.IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)

        Glide.with(this).load(IMAGRURL).placeholder(null).into(imwelcome);
        tv_product_name!!.setText(""+ProductNameSP.getString("ProductName",null))


        getlabels()
    }

    private fun setRegViews() {
        val btlogin = findViewById<Button>(R.id.btlogin) as Button
        val btregistration = findViewById<Button>(R.id.btregistration) as Button
        btlogin!!.setOnClickListener(this)
        btregistration!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btregistration-> {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun getlabels() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@LanguageSelectionActivity, R.style.Progress)
                   progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                   progressDialog!!.setCancelable(false)
                   progressDialog!!.setIndeterminate(true)
                   progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                   progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@WelcomeActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("16"))
                        // requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        //  requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put(
                                "FK_Languages",
                                MscoreApplication.encryptStart("1")
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  labels   " + requestObject1)
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
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getLabels(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                // progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-labels", response.body())
                                /*  if (jObject.getString("StatusCode") == "0") {
                                      val jsonObj1: JSONObject =
                                          jObject.getJSONObject("PassBookAccountDetails")
                                      val jsonobj2 = JSONObject(jsonObj1.toString())

                                      jresult = jsonobj2.getJSONArray("PassBookAccountDetailsList")

                                      for (i in 0 until jresult!!.length()) {
                                          try {
                                              val json = jresult!!.getJSONObject(i)
                                              arrayList1!!.add(json.getString("AccountNumber"))
                                          } catch (e: JSONException) {
                                              e.printStackTrace()
                                          }
                                      }
                                      spnAccountNum!!.adapter = ArrayAdapter(
                                          this@PassbookActivity,
                                          android.R.layout.simple_spinner_dropdown_item, arrayList1
                                      )


                                  } else {
                                      val builder = AlertDialog.Builder(
                                          this@PassbookActivity,
                                          R.style.MyDialogTheme
                                      )
                                      builder.setMessage("" + jObject.getString("EXMessage"))
                                      builder.setPositiveButton("Ok") { dialogInterface, which ->
                                      }
                                      val alertDialog: AlertDialog = builder.create()
                                      alertDialog.setCancelable(false)
                                      alertDialog.show()
                                  }*/
                            } catch (e: Exception) {
                                //  progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@WelcomeActivity,
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
                            // progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    this@WelcomeActivity,
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
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(this@WelcomeActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@WelcomeActivity, R.style.MyDialogTheme)
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

