package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class OTPActivity : AppCompatActivity() , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    val TAG: String = "OTPActivity"

    var Token: String=""
    var FK_Customer: String=""
    var CusMobile: String=""
    var pinview: Pinview? = null
    var tvotpmsg: TextView? = null

    var txtv_otpverify:TextView?=null
    var one: Button? = null
    var two: Button? = null
    var three: Button? = null
    var four: Button? = null
    var five: Button? = null
    var six: Button? = null
    var seven: Button? = null
    var eight: Button? = null
    var nine: Button? = null
    var zero: Button? = null

    var et_1: EditText? = null
    var et_2: EditText? = null
    var et_3: EditText? = null
    var et_4: EditText? = null
    var et_5: EditText? = null
    var et_6: EditText? = null

    var imgBack: ImageView? = null
    var imgclear: ImageView? = null

    var pinValue: String = ""
    var IMAGRURL: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p)
        setRegViews()
        val ID_OtpverifySP = applicationContext.getSharedPreferences(Config.SHARED_PREF48,0)
        val ID_OtpmsgSP = applicationContext.getSharedPreferences(Config.SHARED_PREF166,0)


        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
      //  Glide.with(this).load(R.drawable.otpgif).into(imgLogo)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        var IMAGRURL = IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)

        Glide.with(this).load(IMAGRURL).placeholder(null).into(imgLogo);

        FK_Customer = intent.getStringExtra("FK_Customer")!!
        Token = intent.getStringExtra("Token")!!
        CusMobile = intent.getStringExtra("CusMobile")!!
        val mask: String = CusMobile.replace("\\w(?=\\w{3})".toRegex(),"*")
     //   tvotpmsg!!.text="Please enter the validation code send to your registered mobile number "+mask
        tvotpmsg!!.setText(ID_OtpmsgSP.getString("please enter validation code senttoyourregisteredmobilenumber",null)+mask)


        txtv_otpverify!!.setText(ID_OtpverifySP.getString("Otpverification",null))

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

        one = findViewById<Button>(R.id.one) as Button
        two = findViewById<Button>(R.id.two) as Button
        three = findViewById<Button>(R.id.three) as Button
        four = findViewById<Button>(R.id.four) as Button
        five = findViewById<Button>(R.id.five) as Button
        six = findViewById<Button>(R.id.six) as Button
        seven = findViewById<Button>(R.id.seven) as Button
        eight = findViewById<Button>(R.id.eight) as Button
        nine = findViewById<Button>(R.id.nine) as Button
        zero = findViewById<Button>(R.id.zero) as Button
        txtv_otpverify= findViewById<TextView>(R.id.txtv_otpverify) as TextView

        et_1 = findViewById<EditText>(R.id.et_1) as EditText
        et_2 = findViewById<EditText>(R.id.et_2) as EditText
        et_3 = findViewById<EditText>(R.id.et_3) as EditText
        et_4 = findViewById<EditText>(R.id.et_4) as EditText
        et_5 = findViewById<EditText>(R.id.et_5) as EditText
        et_6 = findViewById<EditText>(R.id.et_6) as EditText

        imgclear = findViewById<ImageView>(R.id.imgclear) as ImageView
        imgBack = findViewById<ImageView>(R.id.imgBack) as ImageView


        one!!.setOnClickListener(this)
        two!!.setOnClickListener(this)
        three!!.setOnClickListener(this)
        four!!.setOnClickListener(this)
        five!!.setOnClickListener(this)
        six!!.setOnClickListener(this)
        seven!!.setOnClickListener(this)
        eight!!.setOnClickListener(this)
        nine!!.setOnClickListener(this)
        zero!!.setOnClickListener(this)

        imgBack!!.setOnClickListener(this)
        imgclear!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.btverify -> {
//                val varOtp = pinview!!.value
//                if(varOtp.length==6) {
//                    getOtpVerification(varOtp)
//                }else{
//                    val builder = AlertDialog.Builder(
//                        this@OTPActivity,
//                        R.style.MyDialogTheme
//                    )
//                    builder.setMessage("Please enter valid 6 digit OTP number send to your registered mobile number.")
//                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                    }
//                    val alertDialog: AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                }
//
//            }

            R.id.one -> {
//                pinValue+=1
//                Log.e(TAG,"pinValue 186  "+pinValue)
//                pinview!!.value ="123456"

                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("1");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("1");
                                }
                            }
                            else {
                                et_4!!.setText("1");
                            }
                        }
                        else {
                            et_3!!.setText("1");
                        }
                    }
                    else {
                        et_2!!.setText("1");
                    }
                }
                else {
                    et_1!!.setText("1");
                }

            }
            R.id.two -> {
//                pinValue+=2
//                Log.e(TAG,"pinValue 1862  "+pinValue)
//                pinview!!.value = pinValue
                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("2");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("2");
                                }
                            }
                            else {
                                et_4!!.setText("2");
                            }
                        }
                        else {
                            et_3!!.setText("2");
                        }
                    }
                    else {
                        et_2!!.setText("2");
                    }
                }
                else {
                    et_1!!.setText("2");
                }

            }
            R.id.three -> {
//                pinValue+=3
//                Log.e(TAG,"pinValue 1863  "+pinValue)
//                pinview!!.value = pinValue
                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("3");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("3");
                                }
                            }
                            else {
                                et_4!!.setText("3");
                            }
                        }
                        else {
                            et_3!!.setText("3");
                        }
                    }
                    else {
                        et_2!!.setText("3");
                    }
                }
                else {
                    et_1!!.setText("3");
                }

            }
            R.id.four -> {
//                pinValue+=4
//                Log.e(TAG,"pinValue 1864  "+pinValue)
//                pinview!!.value = pinValue
                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("4");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("4");
                                }
                            }
                            else {
                                et_4!!.setText("4");
                            }
                        }
                        else {
                            et_3!!.setText("4");
                        }
                    }
                    else {
                        et_2!!.setText("4");
                    }
                }
                else {
                    et_1!!.setText("4");
                }
            }
            R.id.five -> {
//                pinValue+=5
//                Log.e(TAG,"pinValue 1865  "+pinValue)
//                pinview!!.value = pinValue

                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("5");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("5");
                                }
                            }
                            else {
                                et_4!!.setText("5");
                            }
                        }
                        else {
                            et_3!!.setText("5");
                        }
                    }
                    else {
                        et_2!!.setText("5");
                    }
                }
                else {
                    et_1!!.setText("5");
                }

            }
            R.id.six -> {
//                pinValue+=6
//                Log.e(TAG,"pinValue 1866  "+pinValue)
//                pinview!!.value = pinValue

                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("6");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("6");
                                }
                            }
                            else {
                                et_4!!.setText("6");
                            }
                        }
                        else {
                            et_3!!.setText("6");
                        }
                    }
                    else {
                        et_2!!.setText("6");
                    }
                }
                else {
                    et_1!!.setText("6");
                }


            }
            R.id.seven -> {
//                pinValue+=7
//                Log.e(TAG,"pinValue 1867  "+pinValue)
//                pinview!!.value = pinValue

                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("7");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("7");
                                }
                            }
                            else {
                                et_4!!.setText("7");
                            }
                        }
                        else {
                            et_3!!.setText("7");
                        }
                    }
                    else {
                        et_2!!.setText("7");
                    }
                }
                else {
                    et_1!!.setText("7");
                }

            }
            R.id.eight -> {
//                pinValue+=8
//                Log.e(TAG,"pinValue 1868  "+pinValue)
//                pinview!!.value = pinValue
                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("8");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("8");
                                }
                            }
                            else {
                                et_4!!.setText("8");
                            }
                        }
                        else {
                            et_3!!.setText("8");
                        }
                    }
                    else {
                        et_2!!.setText("8");
                    }
                }
                else {
                    et_1!!.setText("8");
                }

            }
            R.id.nine -> {
//                pinValue+=9
//                Log.e(TAG,"pinValue 1869  "+pinValue)
//                pinview!!.value = pinValue
                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("9");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("9");
                                }
                            }
                            else {
                                et_4!!.setText("9");
                            }
                        }
                        else {
                            et_3!!.setText("9");
                        }
                    }
                    else {
                        et_2!!.setText("9");
                    }
                }
                else {
                    et_1!!.setText("9");
                }

            }
            R.id.zero -> {
//                pinValue+=0
//                Log.e(TAG,"pinValue 1860  "+pinValue)
//                pinview!!.value = pinValue

                if(et_1!!.length()==1) {
                    if (et_2!!.length() == 1) {
                        if (et_3!!.length() == 1) {
                            if (et_4!!.length() == 1) {
                                if (et_5!!.length() == 1) {
                                    et_6!!.setText("0");
                                    pinValue = et_1!!.getText().toString()+et_2!!.getText().toString()+et_3!!.getText().toString()+et_4!!.getText().toString()+et_5!!.getText().toString()+et_6!!.getText().toString()
                                    getOtpVerification(pinValue)
                                }
                                else {
                                    et_5!!.setText("0");
                                }
                            }
                            else {
                                et_4!!.setText("0");
                            }
                        }
                        else {
                            et_3!!.setText("0");
                        }
                    }
                    else {
                        et_2!!.setText("0");
                    }
                }
                else {
                    et_1!!.setText("0");
                }

            }
            R.id.imgBack-> {
                Log.e(TAG,"LENGHT   557   "+pinValue.length)
                if (et_6!!.text.length > 0){
                    et_6!!.setText("")
                }
                else if (et_5!!.text.length > 0){
                    et_5!!.setText("")
                }
                else if (et_4!!.text.length > 0){
                    et_4!!.setText("")
                }
                else if (et_3!!.text.length > 0){
                    et_3!!.setText("")
                }
                else if (et_2!!.text.length > 0){
                    et_2!!.setText("")
                }
                else if (et_1!!.text.length > 0){
                    et_1!!.setText("")
                }

            }

            R.id.imgclear -> {

                pinValue = ""
                et_1!!.setText("")
                et_2!!.setText("")
                et_3!!.setText("")
                et_4!!.setText("")
                et_5!!.setText("")
                et_6!!.setText("")
            }
        }
    }

    private fun getOtpVerification(varOtp: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OTPActivity, R.style.Progress)
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
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("OTP", MscoreApplication.encryptStart(varOtp))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1 OTP 10001   "+requestObject1)

                        Log.e("TAG", "requestObject1  varifctn   " + requestObject1)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("VerifictnResponse", response.body().toString())
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

                                    val LoginMobileNoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF321, 0)
                                    val LoginMobileNoEditer = LoginMobileNoSP.edit()
                                    LoginMobileNoEditer.putString("LoginMobileNo",  CusMobile)
                                    LoginMobileNoEditer.commit()

                                    val currentTime = Calendar.getInstance().time
                                    Log.e(TAG,"currentTime  "+currentTime)
                                    val date: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                                    val time: DateFormat = SimpleDateFormat("HH:mm")
//                                    val date: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a")
                                    val localDate = date.format(currentTime)
                                    val localtime = time.format(currentTime)
                                    val timeParts = localtime.split(":").toTypedArray()

                                    var hour = timeParts[0].toInt()
                                    val min = timeParts[1].toInt()

                                    var suffix: String =""
                                    if(hour>11) {
                                        suffix = "PM";
                                        if(hour>12)
                                            hour -= 12;
                                    } else {
                                        suffix = "AM";
                                        if(hour==0)
                                            hour = 12;
                                    }
                                    var hours : String =""
                                    var mins : String =""
                                    if (hour.toString().length==1){
                                        hours = "0"+hour.toString()
                                    }else{
                                        hours = hour.toString()
                                    }

                                    if (min.toString().length==1){
                                        mins = "0"+hour.toString()
                                    }else{
                                        mins = min.toString()
                                    }

                                    val localDateTime = localDate+" "+hours+" : "+mins +" "+suffix
                                    Log.e(TAG,"hr  7192   "+localDateTime)

                                    val LastLoginTimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF29,0)
                                    val LastLoginTimeEditer = LastLoginTimeSP.edit()
                                    LastLoginTimeEditer.putString("LastLoginTime", localDateTime)
                                    LastLoginTimeEditer.commit()


                                    val ID_ResponseSP = applicationContext.getSharedPreferences(Config.SHARED_PREF48,0)
                                    val builder = AlertDialog.Builder(
                                        this@OTPActivity,
                                        R.style.MyDialogTheme
                                    )
                                    if(jobjt.getString("ResponseMessage").equals("OTP Verified"))
                                    {
                                        builder.setMessage(""+ID_ResponseSP.getString("Otpverification",null))
                                    }
                                    else
                                    {
                                        builder.setMessage(""+jobjt.getString("ResponseMessage"))
                                    }
                                   // builder.setMessage("" + jobjt.getString("ResponseMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(
                                            Intent(
                                                this@OTPActivity,
                                                HomeActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                } else {
                                //    pinview!!.clearValue()
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