package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
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
import android.app.DatePickerDialog




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

    var tv_view: TextView? = null
    var tv_download: TextView? = null

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        setInitialise()
        setRegister()


        getOwnAccount()



    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

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
               fromDatePicker()
            }
            R.id.edt_toDate ->{
                toDatePicker()
            }
            R.id.rad_last_month ->{
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
               validation()
            }
            R.id.tv_download ->{
                docType = "2"
                validation()
            }

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
                        .baseUrl(Config.BASE_URL)
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
                        .baseUrl(Config.BASE_URL)
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

                                    val filename2: String = Config.BASE_URL + "\\" + strNew + "\\" + filename1
                                    Log.e(TAG,"filename2  51623   "+filename2)

                                    val i = Intent(this@StatementActivity, ViewStatementActivity::class.java)
                                    i.putExtra("docx", filename2)
                                    startActivity(i)


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


}