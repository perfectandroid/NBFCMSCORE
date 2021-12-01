package com.perfect.nbfcmscore.Activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.R.attr
import android.annotation.SuppressLint
import android.app.*
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.coolerfall.download.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.R
import com.squareup.okhttp.ResponseBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import java.util.concurrent.TimeUnit
import android.R.attr.path
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import java.text.ParseException


class StatementActivity : AppCompatActivity(), View.OnClickListener {
//    https://github.com/barteksc/AndroidPdfViewer
//    https://www.journaldev.com/22475/android-retrofit-download-file-progress
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

    var tv_reset: Button? = null
    var tv_header: TextView? = null
    var tv_download: Button? = null
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

    val REQUEST_PERMISSIONS_CODE_WRITE_STORAGE: Int = 200
    var mContext: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        mContext = this@StatementActivity
        setInitialise()
        setRegister()

        val imemilogo: ImageView = findViewById(R.id.imemilogo)
        Glide.with(this).load(R.drawable.statementgif).into(imemilogo)



        val ID_Statmnt = applicationContext.getSharedPreferences(Config.SHARED_PREF59,0)
        tv_header!!.setText(ID_Statmnt.getString("statement",null))
        val ID_selectperd = applicationContext.getSharedPreferences(Config.SHARED_PREF126,0)
        txtv_slctperd!!.setText(ID_selectperd.getString("Selectaperiodofyourchoice",null))
        val ID_Or = applicationContext.getSharedPreferences(Config.SHARED_PREF127,0)
        txtv_or!!.setText(ID_Or.getString("OR",null))
        val ID_customdate = applicationContext.getSharedPreferences(Config.SHARED_PREF128,0)
        txtv_slctcustomdte!!.setText(ID_customdate.getString("Selectacustomdateofyourchoice.",null))
        val ID_View = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        tv_reset!!.setText(ID_View.getString("RESET",null))
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

        tv_reset = findViewById<Button>(R.id.tv_reset)
        tv_download = findViewById<Button>(R.id.tv_download)

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

        tv_reset!!.setOnClickListener(this)
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

            R.id.tv_reset ->{

                edt_fromDate!!.setText("")
                edt_toDate!!.setText("")
                getOwnAccount()
                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false

            }
            R.id.tv_download ->{
                docType = "2"
                validation1()

               // downloadTest()


            }

        }
    }

    private fun downloadTest() {
//        Download_Uri =
//            Uri.parse("http://www.gadgetsaint.com/wp-content/uploads/2016/11/cropped-web_hi_res_512.png")



        try {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
           // val uri = Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
            val uri = Uri.parse("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf")

            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setTitle("GadgetSaint Downloading " + "Sample" + ".png")
            request.setDescription("Downloading " + "Sample" + ".png")
            request.setVisibleInDownloadsUi(true)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "/GadgetSaint/" + "/" + "Sample" + ".png"
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.allowScanningByMediaScanner()
            downloadManager.enqueue(request)

            // refid = downloadManager.enqueue(request)

            Log.e(TAG,"Successs    387    ")
        }catch (e  :Exception){
            Log.e(TAG,"Exception    387    "+e.toString())
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
//            if (checkExternalStoragePermission(this@StatementActivity)){
//                downloadFile("filename1","ASD.pdf")
//            }





        }
    }

    private fun downloadFile2() {
        val client = OkHttpClient.Builder()
            .sslSocketFactory(Config.getSSLSocketFactory(this@StatementActivity))
            .hostnameVerifier(Config.getHostnameVerifier())
            .build()

      //  val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val manager: com.coolerfall.download.DownloadManager? = com.coolerfall.download.DownloadManager.Builder().context(this)
            .downloader(OkHttpDownloader.create(client))
            .threadPoolSize(3)
            .logger(object : Logger {
                override fun log(message: String?) {
                    Log.e("TAG", "  675   "+message!!)
                }
            })
            .build()

        val destPath =
            Environment.getExternalStorageDirectory().toString() + File.separator + "test1.pdf"
        Log.e(TAG,"destPath  675      "+destPath)
        val request: DownloadRequest = DownloadRequest.Builder()
//            .url("http://something.to.download")
//            .url("https://maven.apache.org/archives/maven-1.x/maven.pdf")
            .url("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf")
            .retryTime(5)
            .retryInterval(2, TimeUnit.SECONDS)
            .progressInterval(1, TimeUnit.SECONDS)
            .priority(Priority.HIGH)
            .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
            .destinationFilePath(destPath)
            .downloadCallback(object : DownloadCallback {
                override fun onStart(downloadId: Int, totalBytes: Long) {
                    Log.e(TAG,"onStart  675      "+totalBytes)
                }
                override fun onRetry(downloadId: Int) {
                    Log.e(TAG,"destPath  675      "+destPath)
                }
                override fun onProgress(downloadId: Int, bytesWritten: Long, totalBytes: Long) {
                    Log.e(TAG,"onRetry  675      "+bytesWritten+"    "+totalBytes)
                }
                override fun onSuccess(downloadId: Int, filePath: String) {
                    Log.e(TAG,"onSuccess  675      "+filePath)
                }
                override fun onFailure(downloadId: Int, statusCode: Int, errMsg: String) {
                    Log.e(TAG,"onFailure  675      "+statusCode+"    "+errMsg)
                }
            })
            .build()

        val downloadId = manager!!.add(request)
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
                                        /*val i = Intent(this@StatementActivity, ViewStatementActivity::class.java)
                                        i.putExtra("docx", filename2)
                                        startActivity(i)*/
                                    }

                                    if (docType.equals("2")){
                                        if (checkExternalStoragePermission(this@StatementActivity)){
                                            downloadFile(filename1,filename2)
                                        }



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


        val client = OkHttpClient.Builder()
            .sslSocketFactory(Config.getSSLSocketFactory(this@StatementActivity))
            .hostnameVerifier(Config.getHostnameVerifier())
            .build()

        //  val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val manager: com.coolerfall.download.DownloadManager? = com.coolerfall.download.DownloadManager.Builder().context(this)
            .downloader(OkHttpDownloader.create(client))
            .threadPoolSize(3)
            .logger(object : Logger {
                override fun log(message: String?) {
                    Log.e("TAG", "  675   "+message!!)
                }
            })
            .build()
//        val dir =
//            File(Environment.getExternalStorageDirectory().toString() + "/Download/AldoFiles")
        val appName = getResources().getString(R.string.app_name)
        val dir =
            File(Environment.getExternalStorageDirectory().toString() + "/"+appName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val destPath = File(dir, filename2)

        Log.e(TAG,"destPath  675      "+destPath)
        Log.e(TAG,"appName  675      "+appName)
        val request: DownloadRequest = DownloadRequest.Builder()
//            .url("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf")
            .url(filename1)
            .retryTime(5)
            .retryInterval(2, TimeUnit.SECONDS)
            .progressInterval(1, TimeUnit.SECONDS)
            .priority(Priority.HIGH)
            .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
            .destinationFilePath(destPath.toString())
            .downloadCallback(object : DownloadCallback {
                override fun onStart(downloadId: Int, totalBytes: Long) {
                    Log.e(TAG,"onStart  675      "+totalBytes)
                }
                override fun onRetry(downloadId: Int) {
                    Log.e(TAG,"destPath  675      "+destPath)
                }
                override fun onProgress(downloadId: Int, bytesWritten: Long, totalBytes: Long) {
                    Log.e(TAG,"onProgress  675      "+bytesWritten+"    "+totalBytes)
                }
                override fun onSuccess(downloadId: Int, filePath: String) {
                    Log.e(TAG,"onSuccess  675      "+filePath)

                    StatementPopup(filePath,"1")
                }
                override fun onFailure(downloadId: Int, statusCode: Int, errMsg: String) {
                    Log.e(TAG,"onFailure  675      "+statusCode+"    "+errMsg)
                    StatementPopup(errMsg,"0")
                }
            })
            .build()

        val downloadId = manager!!.add(request)


    }

    private fun StatementPopup(Msg: String, mode: String) {

        try {
            val builder = AlertDialog.Builder(mContext)
            val inflater1 = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater1.inflate(R.layout.alert_statement, null)
            val ll_close = layout.findViewById<View>(R.id.ll_close) as LinearLayout
            val ll_view = layout.findViewById<View>(R.id.ll_view) as LinearLayout
            val tv_path = layout.findViewById<View>(R.id.tv_path) as TextView
            val tv_line = layout.findViewById<View>(R.id.tv_line) as TextView

            builder.setView(layout)
            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.setView(layout, 0, 0, 0, 0);

            if (mode.equals("0")){
                ll_view!!.visibility = View.GONE
                tv_line!!.visibility = View.GONE
                tv_path!!.setText(""+Msg)

            }else{
                ll_view!!.visibility = View.VISIBLE
                tv_line!!.visibility = View.VISIBLE
                tv_path!!.setText("Download Path : "+Msg)

            }



            ll_view!!.setOnClickListener {

                alertDialog.dismiss()
                var intent = Intent(this@StatementActivity, ViewStatementActivity::class.java)
                intent.putExtra("path", Msg)
                startActivity(intent)
            }

            ll_close!!.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun hasWriteStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(applicationContext!!, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE_WRITE_STORAGE
                )

                return false
            }
        }

        return true
    }


}