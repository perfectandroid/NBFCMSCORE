package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.Manifest.permission
import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import android.os.AsyncTask
import android.os.Environment
import java.net.HttpURLConnection
import java.net.URL
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import androidx.core.app.ActivityCompat.requestPermissions

import android.content.pm.PackageManager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import androidx.core.content.ContextCompat

import android.Manifest.permission.READ_EXTERNAL_STORAGE

import android.os.Build
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.*
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import android.webkit.URLUtil

import androidx.annotation.RequiresApi
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory


class StatementActivity : AppCompatActivity(), View.OnClickListener {
//    https://github.com/barteksc/AndroidPdfViewer
    private var progressDialog: ProgressDialog? = null
    val TAG: String = "StatementActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var act_account: AutoCompleteTextView? = null


    var rad_last_month: RadioButton? = null
    var rad_last_3_month: RadioButton? = null
    var rad_last_6_month: RadioButton? = null
    var rad_last_12_month: RadioButton? = null

    var txtv_slctperd: TextView? = null
    var txtv_slctcustomdte: TextView? = null

    var tv_view: TextView? = null
    var tv_header: TextView? = null
    var tv_download: TextView? = null
    var txtv_or: TextView? = null
    var edt_fromDate: EditText? = null
    var edt_toDate: EditText? = null

    var FromDate: String = ""
    var ToDate: String = ""
    var docType: String = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    var jArrayAccount: JSONArray? = null
    var FK_Account: String? = ""
    var AccountNumber: String? = ""
    var SubModule: String? = ""
    var BranchName: String? = ""

    val REQUEST_EXTERNAL_PERMISSION_CODE = 666

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    val PERMISSIONS_EXTERNAL_STORAGE = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        setInitialise()
        setRegister()

        val ID_Statmnt = applicationContext.getSharedPreferences(Config.SHARED_PREF59,0)
        tv_header!!.setText(ID_Statmnt.getString("statement",null))
        val ID_selectperd = applicationContext.getSharedPreferences(Config.SHARED_PREF126,0)
        txtv_slctperd!!.setText(ID_selectperd.getString("Selectaperiodofyourchoice",null))
        val ID_Or = applicationContext.getSharedPreferences(Config.SHARED_PREF127,0)
        txtv_or!!.setText(ID_Or.getString("OR",null))
        val ID_customdate = applicationContext.getSharedPreferences(Config.SHARED_PREF128,0)
        txtv_slctcustomdte!!.setText(ID_customdate.getString("Selectacustomdateofyourchoice.",null))
        val ID_View = applicationContext.getSharedPreferences(Config.SHARED_PREF129,0)
        tv_view!!.setText(ID_View.getString("View",null))
         val ID_downld = applicationContext.getSharedPreferences(Config.SHARED_PREF130,0)
        tv_download!!.setText(ID_downld.getString("Download",null))
         val ID_lastmnth = applicationContext.getSharedPreferences(Config.SHARED_PREF131,0)
        rad_last_month!!.setText(ID_lastmnth.getString("LastMonth",null))
        val ID_lastthreemnth = applicationContext.getSharedPreferences(Config.SHARED_PREF132,0)
        rad_last_3_month!!.setText(ID_lastthreemnth.getString("Last3Months",null))
        val ID_lastsixmnth = applicationContext.getSharedPreferences(Config.SHARED_PREF133,0)
        rad_last_6_month!!.setText(ID_lastsixmnth.getString("Last6Months",null))
        val ID_Last1yr = applicationContext.getSharedPreferences(Config.SHARED_PREF134,0)
        rad_last_12_month!!.setText(ID_Last1yr.getString("Last1Year",null))

        val ID_frmdte = applicationContext.getSharedPreferences(Config.SHARED_PREF200,0)
        edt_fromDate!!.setHint(ID_frmdte.getString("FromDate",null))

        val ID_todte = applicationContext.getSharedPreferences(Config.SHARED_PREF201,0)
        edt_toDate!!.setHint(ID_todte.getString("EndDate",null))
        // val ID_Passbk = applicationContext.getSharedPreferences(Config.SHARED_PREF51,0)


        getOwnAccount()



    }

    private fun setInitialise() {
        tv_header = findViewById<TextView>(R.id.tv_header)
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        txtv_or= findViewById(R.id.txtv_or)

        txtv_slctperd = findViewById<TextView>(R.id.txtv_slctperd)
        txtv_slctcustomdte = findViewById<TextView>(R.id.txtv_slctcustomdte)


        act_account = findViewById<AutoCompleteTextView>(R.id.act_account)

        rad_last_month = findViewById<RadioButton>(R.id.rad_last_month)
        rad_last_3_month = findViewById<RadioButton>(R.id.rad_last_3_month)
        rad_last_6_month = findViewById<RadioButton>(R.id.rad_last_6_month)
        rad_last_12_month = findViewById<RadioButton>(R.id.rad_last_12_month)

        edt_fromDate = findViewById<EditText>(R.id.edt_fromDate)
        edt_toDate = findViewById<EditText>(R.id.edt_toDate)

        tv_view = findViewById<TextView>(R.id.tv_view)
        tv_download = findViewById<TextView>(R.id.tv_download)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

        act_account!!.setOnClickListener(this)

        rad_last_month!!.setOnClickListener(this)
        rad_last_3_month!!.setOnClickListener(this)
        rad_last_6_month!!.setOnClickListener(this)
        rad_last_12_month!!.setOnClickListener(this)

        edt_fromDate!!.setOnClickListener(this)
        edt_toDate!!.setOnClickListener(this)

        tv_view!!.setOnClickListener(this)
        tv_download!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
                 onBackPressed()
//                startActivity(Intent(this@StatementActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@StatementActivity, HomeActivity::class.java))
                finish()
            }
            R.id.act_account ->{
                act_account!!.showDropDown()
            }
            R.id.edt_fromDate ->{
                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false

               fromDatePicker()
            }
            R.id.edt_toDate ->{

                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false

                toDatePicker()
            }
            R.id.rad_last_month ->{

                FromDate = ""
                ToDate = ""
                edt_fromDate!!.setText("")
                edt_toDate!!.setText("")

                val calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
                val FirstDay: Date = calendar.getTime()

                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val LastDay: Date = calendar.getTime()

                rad_last_month!!.isChecked =true
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false

                FromDate = sdf.format(FirstDay)
                ToDate = sdf.format(LastDay)

                Log.e(TAG,"nextMonthFirstDay    1061   "+FirstDay+ "  "+FromDate)
                Log.e(TAG,"nextMonthLastDay    1061   "+LastDay+"   "+ToDate)
            }
            R.id.rad_last_3_month ->{

                FromDate = ""
                ToDate = ""
                edt_fromDate!!.setText("")
                edt_toDate!!.setText("")

                val calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -3)
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
                val FirstDay: Date = calendar.getTime()

                val calendar1: Calendar = Calendar.getInstance()
                calendar1.add(Calendar.MONTH, -1)
                calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
                val LastDay: Date = calendar1.getTime()

                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =true
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false


                FromDate = sdf.format(FirstDay)
                ToDate = sdf.format(LastDay)

                Log.e(TAG,"nextMonthFirstDay    1063   "+FirstDay+ "  "+FromDate)
                Log.e(TAG,"nextMonthLastDay    1063   "+LastDay+"   "+ToDate)
            }
            R.id.rad_last_6_month ->{


                FromDate = ""
                ToDate = ""
                edt_fromDate!!.setText("")
                edt_toDate!!.setText("")

                val calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -6)
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
                val FirstDay: Date = calendar.getTime()

                val calendar1: Calendar = Calendar.getInstance()
                calendar1.add(Calendar.MONTH, -1)
                calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
                val LastDay: Date = calendar1.getTime()

                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =true
                rad_last_12_month!!.isChecked =false

                FromDate = sdf.format(FirstDay)
                ToDate = sdf.format(LastDay)

                Log.e(TAG,"nextMonthFirstDay    1066   "+FirstDay+ "  "+FromDate)
                Log.e(TAG,"nextMonthLastDay    1066   "+LastDay+"   "+ToDate)
            }
            R.id.rad_last_12_month ->{

                FromDate = ""
                ToDate = ""
                edt_fromDate!!.setText("")
                edt_toDate!!.setText("")

                val calendar: Calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -12)
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
                val FirstDay: Date = calendar.getTime()

                val calendar1: Calendar = Calendar.getInstance()
                calendar1.add(Calendar.MONTH, -1)
                calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
                val LastDay: Date = calendar1.getTime()

                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =true

                FromDate = sdf.format(FirstDay)
                ToDate = sdf.format(LastDay)

                Log.e(TAG,"nextMonthFirstDay    10612   "+FirstDay+ "  "+FromDate)
                Log.e(TAG,"nextMonthLastDay    10612   "+LastDay+"   "+ToDate)
            }

            R.id.tv_view ->{
                docType = "1"
                validation1()
            }
            R.id.tv_download ->{
                docType = "2"
                validation1()
            }

        }
    }

    private fun validation1() {
        if (rad_last_month!!.isChecked || rad_last_3_month!!.isChecked || rad_last_6_month!!.isChecked || rad_last_12_month!!.isChecked){
            validation()
        }
        else if (edt_fromDate!!.text.toString().equals("")){
            CustomBottomSheeet.Show(this@StatementActivity,"Select From Date","0")
        }
        else if (edt_toDate!!.text.toString().equals("")){
            CustomBottomSheeet.Show(this@StatementActivity,"Select End Date","0")
        }
        else {
            validation()
        }
    }

    private fun fromDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_fromDate!!.setText("" + dayOfMonth + "-" + month + "-" + year)
            FromDate = year.toString()+"-"+month.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }
    private fun toDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_toDate!!.setText("" + dayOfMonth + "-" + month + "-" + year)
            ToDate = year.toString()+"-"+month.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }


    private fun getOwnAccount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@StatementActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@StatementActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  516   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  5161   "+e.toString())
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
                    val call = apiService.getOwnAccounDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  5162   "+response.body())
                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    Log.e(TAG,"jArrayAccount  5164   "+jArrayAccount)

                                    for (i in 0 until jArrayAccount!!.length()) {
                                        var jsonObject = jArrayAccount!!.getJSONObject(i)
                                        if(i == 0){
                                            act_account!!.setText(""+jsonObject.getString("AccountNumber"))
                                            FK_Account = jsonObject.getString("FK_Account")
                                            AccountNumber = jsonObject.getString("AccountNumber")
                                            SubModule= jsonObject.getString("SubModule")
                                            BranchName= jsonObject.getString("BranchName")

//                                            var AccountNos = AccountNumber!!.replace(AccountNumber!!.substring(AccountNumber!!.indexOf(" (") + 1, AccountNumber!!.indexOf(')') + 1), "")
                                        }

                                    }

                                    val accNo: ArrayList<String> = ArrayList()
                                    for (i in 0 until jArrayAccount!!.length()) {
                                        val obj: JSONObject = jArrayAccount!!.getJSONObject(i)
                                        accNo.add(obj.getString("AccountNumber"));
                                    }

                                    val adapter = ArrayAdapter(this@StatementActivity,
                                        android.R.layout.simple_list_item_1, accNo)
                                    act_account!!.setAdapter(adapter)
//                                    act_district!!.showDropDown()


                                    //  AccountNobottomSheet(jArrayAccount!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@StatementActivity,
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
                                Log.e(TAG,"Some  2162   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@StatementActivity,
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
                            Log.e(TAG,"Some  2163   "+t.message)
                            val builder = AlertDialog.Builder(
                                this@StatementActivity,
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
                    Log.e(TAG,"Some  2165   "+e.toString())
                    val builder = AlertDialog.Builder(this@StatementActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@StatementActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun validation() {

        var FromNo = AccountNumber!!.replace(AccountNumber!!.substring(AccountNumber!!.indexOf(" (") + 1, AccountNumber!!.indexOf(')') + 1), "")

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if (FromNo.equals("")){
            CustomBottomSheeet.Show(this,"Select Account","0")
        }
        else if (FromDate.equals("") || ToDate.equals("")){
            CustomBottomSheeet.Show(this,"Select a period OR date","0")
        }
        else if(sdf.parse(FromDate).after(sdf.parse(ToDate)))
        {
            CustomBottomSheeet.Show(this,"Check date ","0")
        }
        else if(sdf.parse(FromDate).equals(sdf.parse(ToDate)))
        {
            CustomBottomSheeet.Show(this,"Check date ","0")
        }
        else{
            Log.e(TAG,"VALIDATION   405"
                    +"\n"+"SubModule   "+SubModule
                    +"\n"+"FromNo   "+FromNo
                    +"\n"+"FromDate   "+FromDate
                    +"\n"+"ToDate   "+ToDate)

            getStatementOfAccountDocs(FromNo)

            //startActivity(Intent(this@StatementActivity, ViewStatementActivity::class.java))


        }
    }

    private fun getStatementOfAccountDocs(FromNo: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@StatementActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@StatementActivity))
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

                      //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FromNo", MscoreApplication.encryptStart(FromNo))
                        requestObject1.put("FromDate", MscoreApplication.encryptStart(FromDate))
                        requestObject1.put("ToDate", MscoreApplication.encryptStart(ToDate))


                        Log.e(TAG,"requestObject1  516   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  5161   "+e.toString())
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
                    val call = apiService.getStatementOfAccount(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  5162   "+response.body())
                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject = jObject.getJSONObject("StatementOfAccountDet")
                                    Log.i("First ", jsonObj1.toString())
                                    val `object` = JSONObject(jsonObj1.toString())
                                    Log.i("First1 ", `object`.toString())
                                    val filename = `object`.getString("FilePath")
                                    val filename1 = `object`.getString("FileName")

                                    Log.e(TAG,"filename  51621   "+filename)
                                    Log.e(TAG,"filename1  51621   "+filename1)

                                    val strNew: String? = filename.substringAfterLast("NbfcAndroidAPI\\")
                                    Log.e(TAG,"strNew  51622   "+strNew)

                                    val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                                    val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
//                                    val filename2: String = Config.BASE_URL + "\\" + strNew + "\\" + filename1
                                    val filename2: String = IMAGE_URL + "\\" + strNew
                                    Log.e(TAG,"filename2  51623   "+filename2)

                                    if (docType.equals("1")){
                                        val i = Intent(this@StatementActivity, ViewStatementActivity::class.java)
                                        i.putExtra("docx", filename2)
                                        startActivity(i)
                                    }

                                    if (docType.equals("2")){

                                      //  downloadFile(filename1,filename2)


//                                           if (checkExternalStoragePermission(this@StatementActivity)){
//                                               DownloadingTask().execute()
//                                           }




//                                     //   val url = URL("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf") //Create Download URl") //Create Download URl
//                                        val url = URL("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf") //Create Download URl
//
//                                        val root = Environment.getExternalStorageDirectory().toString()
//                                        val myDir = File("$root/NBFCC")
//                                        if (!myDir.exists()) {
//                                            myDir.mkdirs()
//                                        }
//                                        val fname = "Test" +".pdf"
//                                        val outputFile = File(myDir, fname)
//                                        if (!outputFile.exists()) {
//                                            outputFile.createNewFile();
//                                            Log.e("TAG", "File Created");
//                                        }
//
//                                        val fileName = "myFile.pdf"
//                                        downloadPdfFromInternet(
//                                            url,
//                                            outputFile,
//                                            fileName
//                                        )

                                    }




                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@StatementActivity,
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
                                Log.e(TAG,"Some  5164   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@StatementActivity,
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
                            Log.e(TAG,"Some  5164   "+t.message)
                            val builder = AlertDialog.Builder(
                                this@StatementActivity,
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
                    Log.e(TAG,"Some  5165   "+e.toString())
                    val builder = AlertDialog.Builder(this@StatementActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@StatementActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }



    private fun downloadFile(filename1: String, filename2: String) {

        Log.e(TAG,"filename1  6491 "+filename1)
        Log.e(TAG,"filename2  6492 "+filename2)


        try {
//            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val uri = Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
          //  val uri = Uri.parse("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf")
//            val uri = Uri.parse(filename2)
//            val request = DownloadManager.Request(uri)
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            val reference = manager.enqueue(request)
//            Log.e(TAG,"Download Successfully  701  "+reference)
//
//            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val uri = Uri.parse(filename2)
//            val request = DownloadManager.Request(uri)
//            val nameOfFile = URLUtil.guessFileName(uri.toString(), null, MimeTypeMap.getFileExtensionFromUrl(uri.toString()))
//            val destinationInExternalPublicDir = request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS)
//            request.setAllowedOverMetered(true)
//            request.setAllowedOverRoaming(true)
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            request.allowScanningByMediaScanner()
//            downloadManager.enqueue(request)

//            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)

//            val fileName = "xyz"
//            val fileExtension = ".pdf"

//           download pdf file.


//           download pdf file.
//            val url = URL("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
//            val c = url.openConnection() as HttpURLConnection
//            c.requestMethod = "GET"
//            c.doOutput = true
//            c.connect()
//            val PATH = Environment.getExternalStorageDirectory().toString() + "/mydownload/"
//            val file: File = File(PATH)
//            file.mkdirs()
//            val outputFile = File(file, fileName + fileExtension)
//            val fos = FileOutputStream(outputFile)
//            val `is` = c.inputStream
//            val buffer = ByteArray(1024)
//            var len1 = 0
//            while (`is`.read(buffer).also { len1 = it } != -1) {
//                fos.write(buffer, 0, len1)
//            }
//            fos.close()
//            `is`.close()
//
//
//            Log.e(TAG,"Download  701  "+outputFile)


            ////////


//            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
//            val folder = File(extStorageDirectory, "PDF DOWNLOAD")
//            folder.mkdir()
//
//            val pdfFile = File(folder, "NBFCC")
//
//
//                pdfFile.createNewFile()
//                Log.e(TAG,"Download  701  "+pdfFile)

    // working
  //  val url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
            val url = URL("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf").toString() //Create Download URl") //Create Download URl

            val request = DownloadManager.Request(Uri.parse(url))
//            request.setMimeType(mimeType.toString())
            request.setDescription("Downloading file...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Testing"+".pdf")
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            request.setMimeType("application/pdf")
            request.allowScanningByMediaScanner()
            //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
            dm.enqueue(request)


            /////////////////

//            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val uri = Uri.parse("url")
//            val request = DownloadManager.Request(uri)
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//            request.setAllowedOverRoaming(false)
//            request.setTitle("" + "filename" + ".pdf")
//            request.setVisibleInDownloadsUi(true)
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            val reference: Long = downloadManager.enqueue(request)
//            request.setDestinationInExternalFilesDir(Environment.DIRECTORY_DOWNLOADS, "/" + "filename")
//            refid = downloadManager.enqueue(request)


        }catch (e: Exception){
            Log.e(TAG,"IOException  701  "+e.toString())
        }

//        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val uri = Uri.parse(filename2)
//        val request = DownloadManager.Request(uri)
//        val nameOfFile = URLUtil.guessFileName(uri.toString(), null, MimeTypeMap.getFileExtensionFromUrl(uri.toString()))
//        val destinationInExternalPublicDir = request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS).pa
//        request.setAllowedOverMetered(true)
//        request.setAllowedOverRoaming(true)
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//        request.allowScanningByMediaScanner()
//        downloadManager.enqueue(request)

//        try {
//            val url = URL(filename2)
//            val c: HttpURLConnection = url.openConnection() as HttpURLConnection
//            c.setRequestMethod("GET")
//            c.setDoOutput(true)
//            c.connect()
//            val PATH = (Environment.getExternalStorageDirectory().toString()
//                    + "/load")
//            Log.v("LOG_TAG", "PATH: $PATH")
//            val file = File(PATH)
//            file.mkdirs()
//            val outputFile = File(file, "NBFC")
//            val fos = FileOutputStream(outputFile)
//            val `is`: InputStream = c.getInputStream()
//            val buffer = ByteArray(4096)
//            var len1 = 0
//            while (`is`.read(buffer).also { len1 = it } != -1) {
//                fos.write(buffer, 0, len1)
//            }
//            fos.close()
//            `is`.close()
//            Toast.makeText(
//                this, " A new file is downloaded successfully",
//                Toast.LENGTH_LONG
//            ).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Log.e(TAG,"IOException  701  "+e.toString())
//        }

    }



    ////////////////////

    class DownloadingTask () : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String? {

            var result: String ="false"
            try {

                ////////////
                val url = URL("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf") //Create Download URl
              //  val url = URL("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf") //Create Download URl") //Create Download URl
               // HttpsURLConnection.setDefaultSSLSocketFactory(Config.getSSLSocketFactory(StatementActivity.));
                val c: HttpURLConnection = url.openConnection() as HttpURLConnection
                c.setRequestMethod("GET")
                c.connect()
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("TAG", "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());
                }
                val root = Environment.getExternalStorageDirectory().toString()
                val myDir = File("$root/NBFCC")
                if (!myDir.exists()) {
                    myDir.mkdirs()
                }

                val fname = "Test1" +".pdf"
                val outputFile = File(myDir, fname)
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e("TAG", "File Created");
                }

                val fos = FileOutputStream(outputFile)
                val `is`: InputStream = c.inputStream //Get InputStream for connection


                val buffer = ByteArray(1024) //Set buffer type

                var len1 = 0 //init length

                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }

                //Close all connection after doing task

                //Close all connection after doing task
                fos.close()
                `is`.close()

                Log.e("TAG","Successs  825  ")


                result = "true"
            }catch (e: Exception){
                result = "false"
                Log.e("TAG","Exception  825  "+e.toString())
            }
            return ""
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.e("TAG","onPreExecute  825  ")

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // ...
        }
    }


    fun checkExternalStoragePermission(activity: Activity?): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return true
        }
        val readStoragePermissionState =
            ContextCompat.checkSelfPermission(activity!!, READ_EXTERNAL_STORAGE)
        val writeStoragePermissionState =
            ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE)
        val externalStoragePermissionGranted =
            readStoragePermissionState == PackageManager.PERMISSION_GRANTED &&
                    writeStoragePermissionState == PackageManager.PERMISSION_GRANTED
        if (!externalStoragePermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_EXTERNAL_PERMISSION_CODE)
            }
        }
        return externalStoragePermissionGranted
    }


 /*   private fun downloadPdfFromInternet(url: URL, dirPath: File, fileName: String) {

        PRDownloader.download(
            url.toString(),
            dirPath.toString(),
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(this@StatementActivity, "downloadComplete", Toast.LENGTH_LONG)
                        .show()
                    val downloadedFile = File(dirPath, fileName)
//                    progressBar.visibility = View.GONE
//                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        this@StatementActivity,
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })

    }*/


}