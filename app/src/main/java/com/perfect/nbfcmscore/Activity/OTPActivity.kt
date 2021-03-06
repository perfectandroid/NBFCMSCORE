package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

class OTPActivity : AppCompatActivity() , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null

    var Token: String=""
    var FK_Customer: String=""
    var CusMobile: String=""
    var pinview: Pinview? = null
    var tvotpmsg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p)
        setRegViews()


        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        Glide.with(this).load(R.drawable.otpgif).into(imgLogo)

        FK_Customer = intent.getStringExtra("FK_Customer")!!
        Token = intent.getStringExtra("Token")!!
        CusMobile = intent.getStringExtra("CusMobile")!!
        val mask: String = CusMobile.replace("\\w(?=\\w{3})".toRegex(),"*")
        tvotpmsg!!.text="Please enter the validation code send to your registered mobile number "+mask

        pinview!!.setPinViewEventListener { pinview, fromUser ->

            val varOtp = pinview!!.value
            getOtpVerification(varOtp)
        }
    }

    private fun setRegViews() {
        tvotpmsg = findViewById<TextView>(R.id.tvotpmsg) as TextView
        val btverify = findViewById<Button>(R.id.btverify) as Button
         pinview = findViewById<Pinview>(R.id.pinview) as Pinview
        btverify!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btverify -> {
                val varOtp = pinview!!.value
                if(varOtp.length==6) {
                    getOtpVerification(varOtp)
                }else{
                    val builder = AlertDialog.Builder(
                        this@OTPActivity,
                        R.style.MyDialogTheme
                    )
                    builder.setMessage("Please enter valid 6 digit OTP number send to your registered mobile number.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }
        }
    }

    private fun getOtpVerification(varOtp: String) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OTPActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@OTPActivity))
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("OTP", MscoreApplication.encryptStart(varOtp))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )


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
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("VarificationMaintenance")

                                    val loginSP = applicationContext.getSharedPreferences(Config.SHARED_PREF,0)
                                    val loginEditer = loginSP.edit()
                                    loginEditer.putString("loginsession", "Yes")
                                    loginEditer.commit()

                                    val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1,0)
                                    val FK_CustomerEditer = FK_CustomerSP.edit()
                                    FK_CustomerEditer.putString("FK_Customer", jobjt.getString("FK_Customer"))
                                    FK_CustomerEditer.commit()

                                    val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2,0)
                                    val CusMobileEditer = CusMobileSP.edit()
                                    CusMobileEditer.putString("CusMobile", jobjt.getString("CusMobile"))
                                    CusMobileEditer.commit()

                                    val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF3,0)
                                    val CustomerNameEditer = CustomerNameSP.edit()
                                    CustomerNameEditer.putString("CustomerName", jobjt.getString("CustomerName"))
                                    CustomerNameEditer.commit()

                                    val AddressSP = applicationContext.getSharedPreferences(Config.SHARED_PREF4,0)
                                    val AddressEditer = AddressSP.edit()
                                    AddressEditer.putString("Address", jobjt.getString("Address"))
                                    AddressEditer.commit()

                                    val EmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF5,0)
                                    val EmailEditer = EmailSP.edit()
                                    EmailEditer.putString("Email", jobjt.getString("Email"))
                                    EmailEditer.commit()

                                    val GenderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF6,0)
                                    val GenderEditer = GenderSP.edit()
                                    GenderEditer.putString("Gender", jobjt.getString("Gender"))
                                    GenderEditer.commit()

                                    val DateOfBirthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF7,0)
                                    val DateOfBirthEditer = DateOfBirthSP.edit()
                                    DateOfBirthEditer.putString("DateOfBirth", jobjt.getString("DateOfBirth"))
                                    DateOfBirthEditer.commit()

                                    val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8,0)
                                    val TokenEditer = TokenSP.edit()
                                    TokenEditer.putString("Token", jobjt.getString("Token"))
                                    TokenEditer.commit()

                                    val CustomerNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF19,0)
                                    val CustomerNumberEditer = CustomerNumberSP.edit()
                                    CustomerNumberEditer.putString("CustomerNumber", jobjt.getString("CustomerNumber"))
                                    CustomerNumberEditer.commit()

                                    val builder = AlertDialog.Builder(
                                        this@OTPActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jobjt.getString("ResponseMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(
                                            Intent(
                                                this@OTPActivity,
                                                HomeActivity::class.java
                                            )
                                        )
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                } else {
                                    pinview!!.clearValue()
                                    val builder = AlertDialog.Builder(
                                        this@OTPActivity,
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
                                    this@OTPActivity,
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
                                this@OTPActivity,
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
                    val builder = AlertDialog.Builder(this@OTPActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@OTPActivity, R.style.MyDialogTheme)
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