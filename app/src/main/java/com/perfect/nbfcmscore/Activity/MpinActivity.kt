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
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
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
  //  var pinview: Pinview? = null
    var tvforgetpassword: TextView? = null
    var tvchangepassword: TextView? = null
    val TAG: String = "MpinActivity"

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

    var txtv_mpinverf: TextView? = null
    var txtv_unlock: TextView? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_mpin)

        Config.Utils.hideSoftKeyBoard(this@MpinActivity,getWindow().getDecorView())
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)
       // Glide.with(this).load(R.drawable.otpgif).into(imgLogo)

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val CompanyLogoImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF13,0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        IMAGRURL = IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)
        Log.e(TAG,"IMAGRURL  86  "+IMAGRURL)


//        Glide.with(this).load("https://picsum.photos/200").into(imgLogo)
//        Glide.with(this).load(IMAGRURL).placeholder(null)
//                    .into(imgLogo);
        try {
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            PicassoTrustAll.getInstance(this@MpinActivity)!!.load(imagepath).error(android.R.color.transparent).into(imgLogo!!)
            tv_product_name!!.setText(""+ProductNameSP.getString("ProductName",null))
        }catch (e: Exception){

        }





        setRegViews()
        val MpinverifSP = applicationContext.getSharedPreferences(Config.SHARED_PREF168, 0)
        val UnlockSP = applicationContext.getSharedPreferences(Config.SHARED_PREF169, 0)
        val ForgotMpinsp = applicationContext.getSharedPreferences(Config.SHARED_PREF141, 0)
        val ChangeMpinSP = applicationContext.getSharedPreferences(Config.SHARED_PREF170, 0)

        txtv_mpinverf!!.setText(MpinverifSP.getString("MPINVerification",null))
        txtv_unlock!!.setText(UnlockSP.getString("PleaseunlockwithyourMPIN",null))
        tvchangepassword!!.setText(ChangeMpinSP.getString("ChangeMPIN",null))
        tvforgetpassword!!.setText(ForgotMpinsp.getString("ForgotMPIN",null))





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



//        pinview!!.setPinViewEventListener { pinview, fromUser ->
//
//            val varOtp = pinview!!.value
//            getMPINVerification(varOtp)
//        }


    }


    private fun setRegViews() {
         tvforgetpassword = findViewById<TextView>(R.id.tvforgetpassword) as TextView
         tvchangepassword = findViewById<TextView>(R.id.tvchangepassword) as TextView

        txtv_mpinverf = findViewById<TextView>(R.id.txtv_mpinverf) as TextView
        txtv_unlock = findViewById<TextView>(R.id.txtv_unlock) as TextView


//        val btverify = findViewById<Button>(R.id.btverify) as Button
       // pinview = findViewById<Pinview>(R.id.pinview) as Pinview
//        btverify!!.setOnClickListener(this)
        tvforgetpassword!!.setOnClickListener(this)
        tvchangepassword!!.setOnClickListener(this)


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
//                    getMPINVerification(varOtp)
//                }else{
//                    val builder = AlertDialog.Builder(
//                        this@MpinActivity,
//                        R.style.MyDialogTheme
//                    )
//                    builder.setMessage("Please enter valid 6 digit MPIN.")
//                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                    }
//                    val alertDialog: AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                }
//
//            }
            R.id.tvforgetpassword -> {
                startActivity( Intent(this@MpinActivity, ForgetActivity::class.java) )
            }
            R.id.tvchangepassword -> {
                startActivity( Intent(this@MpinActivity, ChangeMpinActivity::class.java) )
            }


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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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
                                    getMPINVerification(pinValue)
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

    private fun getMPINVerification(varOtp: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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
                        .baseUrl(baseurl)
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
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  139   "+requestObject1)
                        Log.e(TAG,"varOtp  139   "+varOtp)

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
                    val call = apiService.getMPINVarificationMaintenance(body)
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

                                    intent = Intent(applicationContext, HomeActivity::class.java)
                                    startActivity(intent)
//                                    startActivity(Intent(this@MpinActivity, HomeActivity::class.java))
                                    finish()

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
                                    pinValue = ""
                                  //  pinview!!.clearValue()
                                    et_1!!.setText("")
                                    et_2!!.setText("")
                                    et_3!!.setText("")
                                    et_4!!.setText("")
                                    et_5!!.setText("")
                                    et_6!!.setText("")
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
                                pinValue = ""
//                                pinview!!.clearValue()
                                et_1!!.setText("")
                                et_2!!.setText("")
                                et_3!!.setText("")
                                et_4!!.setText("")
                                et_5!!.setText("")
                                et_6!!.setText("")

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
                            pinValue = ""
//                            pinview!!.clearValue()
                            et_1!!.setText("")
                            et_2!!.setText("")
                            et_3!!.setText("")
                            et_4!!.setText("")
                            et_5!!.setText("")
                            et_6!!.setText("")

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
                    pinValue = ""
//                    pinview!!.clearValue()
                    et_1!!.setText("")
                    et_2!!.setText("")
                    et_3!!.setText("")
                    et_4!!.setText("")
                    et_5!!.setText("")
                    et_6!!.setText("")
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
                pinValue = ""
//                pinview!!.clearValue()
                et_1!!.setText("")
                et_2!!.setText("")
                et_3!!.setText("")
                et_4!!.setText("")
                et_5!!.setText("")
                et_6!!.setText("")
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
