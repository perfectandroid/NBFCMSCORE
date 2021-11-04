package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.goodiebag.pinview.Pinview
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.*

class MpinActivity : AppCompatActivity() , View.OnClickListener {

    var show : Boolean = false
    private var progressDialog: ProgressDialog? = null
    var pinview: Pinview? = null
    var tvforgetpassword: TextView? = null
    var tvchangepassword: TextView? = null
    val TAG: String = "MpinActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_mpin)

        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        Glide.with(this).load(R.drawable.otpgif).into(imgLogo)
        setRegViews()

//        val calendar = Calendar.getInstance()
//        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.ENGLISH)
//        val dateTime = simpleDateFormat.format(calendar.time)
//        Log.e(TAG,"dateTime  58   "+dateTime)

//        val cal = Calendar.getInstance()
//        val tz = cal.timeZone
//        Log.e(TAG,"tz  58   "+tz.getDisplayName())
//
//        val timezoneID = TimeZone.getDefault().id
//
//        val sdf2 = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
//        sdf2.setTimeZone(TimeZone.getTimeZone(timezoneID));
//
//        val dateTime1 = sdf2.format(calendar.time)
//
//        Log.e(TAG,"dateTime1  58   "+dateTime1)

//        val cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"))
//        val currentLocalTime = cal1.time
//        val date: DateFormat = SimpleDateFormat("HH:mm a")
//
//        val localTime = date.format(currentLocalTime)
//
//        Log.e(TAG,"dateTime2  583   "+localTime)



// you can get seconds by adding  "...:ss" to it
// you can get seconds by adding  "...:ss" to it
//        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"))
//
//        val localTime: String = date.format(currentLocalTime)



        pinview!!.setPinViewEventListener { pinview, fromUser ->

            val varOtp = pinview!!.value
            getMPINVerification(varOtp)
        }


    }


    private fun setRegViews() {
         tvforgetpassword = findViewById<TextView>(R.id.tvforgetpassword) as TextView
         tvchangepassword = findViewById<TextView>(R.id.tvchangepassword) as TextView
        val btverify = findViewById<Button>(R.id.btverify) as Button
        pinview = findViewById<Pinview>(R.id.pinview) as Pinview
        btverify!!.setOnClickListener(this)
        tvforgetpassword!!.setOnClickListener(this)
        tvchangepassword!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btverify -> {
                val varOtp = pinview!!.value
                if(varOtp.length==6) {
                    getMPINVerification(varOtp)
                }else{
                    val builder = AlertDialog.Builder(
                        this@MpinActivity,
                        R.style.MyDialogTheme
                    )
                    builder.setMessage("Please enter valid 6 digit MPIN.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }
            R.id.tvforgetpassword -> {
                startActivity( Intent(this@MpinActivity, ForgetActivity::class.java) )
            }
            R.id.tvchangepassword -> {
                startActivity( Intent(this@MpinActivity, ChangeMpinActivity::class.java) )
            }
        }
    }

    private fun getMPINVerification(varOtp: String) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@MpinActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@MpinActivity))
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

                        val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("2"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("MPIN", MscoreApplication.encryptStart(varOtp))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))

                        Log.e(TAG,"requestObject1  139   "+requestObject1)

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
                    val call = apiService.getOTP(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                Log.e(TAG,"response  1391   "+response.body())
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("VarificationMaintenance")

                                    startActivity(
                                        Intent(this@MpinActivity, HomeActivity::class.java))

//                                    val builder = AlertDialog.Builder(
//                                        this@MpinActivity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jobjt.getString("ResponseMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                        startActivity(
//                                            Intent(
//                                                this@MpinActivity,
//                                                HomeActivity::class.java
//                                            )
//                                        )
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()
                                } else {
                                    pinview!!.clearValue()
                                    val builder = AlertDialog.Builder(
                                        this@MpinActivity,
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
                                    this@MpinActivity,
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
                                this@MpinActivity,
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
                    val builder = AlertDialog.Builder(this@MpinActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@MpinActivity, R.style.MyDialogTheme)
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
