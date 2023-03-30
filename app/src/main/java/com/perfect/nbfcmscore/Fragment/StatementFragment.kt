package com.perfect.nbfcmscore.Fragment
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.coolerfall.download.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Activity.ViewStatementActivity
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.CustomBottomSheeet
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class StatementFragment : Fragment() , View.OnClickListener{

    val TAG: String = "StatementFragment"
    private var progressDialog: ProgressDialog? = null
    var rad_last_month: RadioButton? = null
    var rad_last_3_month: RadioButton? = null
    var rad_last_6_month: RadioButton? = null
    var rad_last_12_month: RadioButton? = null

    var tv_reset: Button? = null
    var tv_download: Button? = null



    var txtv_slctperd: TextView? = null
    var txtv_slctcustomdte: TextView? = null


    var txtv_or: TextView? = null
    var edt_fromDate: EditText? = null
    var edt_toDate: EditText? = null

    var FromDate: String = ""
    var ToDate: String = ""
    var docType: String = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    var AccountNumber: String? = ""
    var SubModule: String? = ""

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    val PERMISSIONS_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val REQUEST_EXTERNAL_PERMISSION_CODE = 666


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_statementdownloadview, container, false)

        rad_last_month = v.findViewById<RadioButton>(R.id.rad_last_month)
        rad_last_3_month = v.findViewById<RadioButton>(R.id.rad_last_3_month)
        rad_last_6_month = v.findViewById<RadioButton>(R.id.rad_last_6_month)
        rad_last_12_month = v.findViewById<RadioButton>(R.id.rad_last_12_month)

        edt_fromDate = v.findViewById<EditText>(R.id.edt_fromDate)
        edt_toDate = v.findViewById<EditText>(R.id.edt_toDate)

        txtv_slctperd = v.findViewById<TextView>(R.id.txtv_slctperd)
        txtv_slctcustomdte = v.findViewById<TextView>(R.id.txtv_slctcustomdte)

        txtv_or = v.findViewById<TextView>(R.id.txtv_or)

        tv_reset = v.findViewById<Button>(R.id.tv_reset)
        tv_download = v.findViewById<Button>(R.id.tv_download)

        val StatemenAccountNumber = context!!.getSharedPreferences(Config.SHARED_PREF155, 0)
        AccountNumber = StatemenAccountNumber.getString("StatementAccountNumber",null)


        val StatementSubModule = context!!.getSharedPreferences(Config.SHARED_PREF156, 0)
        SubModule = StatementSubModule.getString("StatementSubModule",null)

        val ID_selectperd = context!!.getSharedPreferences(Config.SHARED_PREF126,0)
        txtv_slctperd!!.setText(ID_selectperd.getString("Selectaperiodofyourchoice",null))

        val ID_Or = context!!.getSharedPreferences(Config.SHARED_PREF127,0)
        txtv_or!!.setText(ID_Or.getString("OR",null))

        val ID_customdate = context!!.getSharedPreferences(Config.SHARED_PREF128,0)
        txtv_slctcustomdte!!.setText(ID_customdate.getString("Selectacustomdateofyourchoice.",null))

        val ID_View = context!!.getSharedPreferences(Config.SHARED_PREF189,0)
        tv_reset!!.setText(ID_View.getString("RESET",null))


        val ID_downld = context!!.getSharedPreferences(Config.SHARED_PREF130,0)
        tv_download!!.setText(ID_downld.getString("Download",null))

        val ID_lastmnth = context!!.getSharedPreferences(Config.SHARED_PREF131,0)
        rad_last_month!!.setText(ID_lastmnth.getString("LastMonth",null))

        val ID_lastthreemnth = context!!.getSharedPreferences(Config.SHARED_PREF132,0)
        rad_last_3_month!!.setText(ID_lastthreemnth.getString("Last3Months",null))

        val ID_lastsixmnth = context!!.getSharedPreferences(Config.SHARED_PREF133,0)
        rad_last_6_month!!.setText(ID_lastsixmnth.getString("Last6Months",null))

        val ID_Last1yr = context!!.getSharedPreferences(Config.SHARED_PREF134,0)
        rad_last_12_month!!.setText(ID_Last1yr.getString("Last1Year",null))


        val ID_frmdte = context!!.getSharedPreferences(Config.SHARED_PREF200,0)
        edt_fromDate!!.setHint(ID_frmdte.getString("FromDate",null))

        val ID_todte = context!!.getSharedPreferences(Config.SHARED_PREF201,0)
        edt_toDate!!.setHint(ID_todte.getString("EndDate",null))

        rad_last_month!!.setOnClickListener(this)
        rad_last_3_month!!.setOnClickListener(this)
        rad_last_6_month!!.setOnClickListener(this)
        rad_last_12_month!!.setOnClickListener(this)

        edt_fromDate!!.setOnClickListener(this)
        edt_toDate!!.setOnClickListener(this)

        tv_reset!!.setOnClickListener(this)
        tv_download!!.setOnClickListener(this)

        Log.e(TAG,"AccountNumber  69     "+AccountNumber+"   "+SubModule)

//        rad_last_month!!.setOnClickListener {
//
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.add(Calendar.MONTH, -1)
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
//            val FirstDay: Date = calendar.getTime()
//
//            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
//            val LastDay: Date = calendar.getTime()
//
//            rad_last_month!!.isChecked =true
//            rad_last_3_month!!.isChecked =false
//            rad_last_6_month!!.isChecked =false
//            rad_last_12_month!!.isChecked =false
//
//            FromDate = sdf.format(FirstDay)
//            ToDate = sdf.format(LastDay)
//
//            Log.e(TAG,"nextMonthFirstDay    1061   "+FirstDay+ "  "+FromDate)
//            Log.e(TAG,"nextMonthLastDay    1061   "+LastDay+"   "+ToDate)
//        }
//
//        rad_last_3_month!!.setOnClickListener {
//
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.add(Calendar.MONTH, -3)
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
//            val FirstDay: Date = calendar.getTime()
//
//            val calendar1: Calendar = Calendar.getInstance()
//            calendar1.add(Calendar.MONTH, -1)
//            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
//            val LastDay: Date = calendar1.getTime()
//
//            rad_last_month!!.isChecked =false
//            rad_last_3_month!!.isChecked =true
//            rad_last_6_month!!.isChecked =false
//            rad_last_12_month!!.isChecked =false
//
//
//            FromDate = sdf.format(FirstDay)
//            ToDate = sdf.format(LastDay)
//
//            Log.e(TAG,"nextMonthFirstDay    1063   "+FirstDay+ "  "+FromDate)
//            Log.e(TAG,"nextMonthLastDay    1063   "+LastDay+"   "+ToDate)
//        }
//
//        rad_last_6_month!!.setOnClickListener {
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.add(Calendar.MONTH, -6)
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
//            val FirstDay: Date = calendar.getTime()
//
//            val calendar1: Calendar = Calendar.getInstance()
//            calendar1.add(Calendar.MONTH, -1)
//            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
//            val LastDay: Date = calendar1.getTime()
//
//            rad_last_month!!.isChecked =false
//            rad_last_3_month!!.isChecked =false
//            rad_last_6_month!!.isChecked =true
//            rad_last_12_month!!.isChecked =false
//
//            FromDate = sdf.format(FirstDay)
//            ToDate = sdf.format(LastDay)
//
//            Log.e(TAG,"nextMonthFirstDay    1066   "+FirstDay+ "  "+FromDate)
//            Log.e(TAG,"nextMonthLastDay    1066   "+LastDay+"   "+ToDate)
//
//        }
//
//        rad_last_12_month!!.setOnClickListener {
//
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.add(Calendar.MONTH, -12)
//            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
//            val FirstDay: Date = calendar.getTime()
//
//            val calendar1: Calendar = Calendar.getInstance()
//            calendar1.add(Calendar.MONTH, -1)
//            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
//            val LastDay: Date = calendar1.getTime()
//
//            rad_last_month!!.isChecked =false
//            rad_last_3_month!!.isChecked =false
//            rad_last_6_month!!.isChecked =false
//            rad_last_12_month!!.isChecked =true
//
//            FromDate = sdf.format(FirstDay)
//            ToDate = sdf.format(LastDay)
//
//            Log.e(TAG,"nextMonthFirstDay    10612   "+FirstDay+ "  "+FromDate)
//            Log.e(TAG,"nextMonthLastDay    10612   "+LastDay+"   "+ToDate)
//        }

//        edt_fromDate!!.setOnClickListener {
//            fromDatePicker()
//
//        }
//
//        edt_toDate!!.setOnClickListener {
//
//            toDatePicker()
//        }



        return v
    }

    private fun fromDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            val mnt = monthOfYear+1
            edt_fromDate!!.setText("" + dayOfMonth + "-" + mnt + "-" + year)
            FromDate = year.toString()+"-"+mnt.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }
    private fun toDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            val mnt = monthOfYear+1
            edt_toDate!!.setText("" + dayOfMonth + "-" + mnt + "-" + year)
            ToDate = year.toString()+"-"+mnt.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.edt_fromDate ->{
              //  FromDate = ""
                Log.e(TAG,"edt_fromDate    219   ")
                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false

                fromDatePicker()
            }
            R.id.edt_toDate ->{
              //  ToDate = ""
                Log.e(TAG,"edt_toDate    219   ")
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

                rad_last_month!!.isChecked =false
                rad_last_3_month!!.isChecked =false
                rad_last_6_month!!.isChecked =false
                rad_last_12_month!!.isChecked =false
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
           val ID_slctfrmdte= context!!.getSharedPreferences(Config.SHARED_PREF342, 0)
           var frmdte =ID_slctfrmdte.getString("Selectfromdate", null)

           CustomBottomSheeet.Show(activity!!,frmdte!!,"0")

         //  CustomBottomSheeet.Show(activity!!,"Select From Date","0")
       }
       else if (edt_toDate!!.text.toString().equals("")){

           val ID_slcttodte= context!!.getSharedPreferences(Config.SHARED_PREF343, 0)
           var todte =ID_slcttodte.getString("Selecttodate", null)

           CustomBottomSheeet.Show(activity!!,todte!!,"0")
           //CustomBottomSheeet.Show(activity!!,"Select End Date","0")
       }
        else {
           validation()
       }
    }


    private fun validation() {

        var FromNo = AccountNumber!!.replace(AccountNumber!!.substring(AccountNumber!!.indexOf(" (") + 1, AccountNumber!!.indexOf(')') + 1), "")

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if (FromNo.equals("")){
            CustomBottomSheeet.Show(activity!!,"Select Account","0")
        }
        else if (FromDate.equals("") || ToDate.equals("")){
            CustomBottomSheeet.Show(activity!!,"Select a period OR date","0")
        }
        else if(sdf.parse(FromDate).after(sdf.parse(ToDate)))
        {
            CustomBottomSheeet.Show(activity!!,"Check date ","0")
        }
        else if(sdf.parse(FromDate).equals(sdf.parse(ToDate)))
        {
            CustomBottomSheeet.Show(activity!!,"Check date ","0")
        }
        else{
            Log.e(TAG,"VALIDATION   405"
                    +"\n"+"SubModule   "+SubModule
                    +"\n"+"FromNo   "+FromNo
                    +"\n"+"FromDate   "+FromDate
                    +"\n"+"ToDate   "+ToDate)

            getStatementOfAccountDocs(FromNo)

            //startActivity(Intent(this@StatementActivity, ViewStatementActivity::class.java))

//            if (checkExternalStoragePermission()){
//                downloadFile("filename1","ASD.pdf")
//            }


        }
    }

    private fun getStatementOfAccountDocs(FromNo: String) {
        val baseurlSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(activity!!)) {
            true -> {
                progressDialog = ProgressDialog(activity!!, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(activity!!))
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
                        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = activity!!.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = activity!!.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = activity!!.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
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
                        val mySnackbar = Snackbar.make(activity!!.findViewById(R.id.rl_main),
                            " Some technical issues.", Snackbar.LENGTH_SHORT)
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

//                                    val strNew: String? = filename.substringAfterLast("NbfcAndroidAPI\\")
//                                    Log.e(TAG,"strNew  51622   "+strNew)
//
//                                    val ImageURLSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
//                                    val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
////                                    val filename2: String = Config.BASE_URL + "\\" + strNew + "\\" + filename1
//                                    val filename2: String = IMAGE_URL + "\\" + strNew
//                                    Log.e(TAG,"filename2  51623   "+filename2)


                                    val strNew: String? = filename.substringAfterLast("NbfcAndroidAPI\\")
//                                    val strNew: String? = filename.substringAfterLast("NBFC\\")
                                    Log.e(TAG,"strNew  51622   "+strNew)

                                    val ImageURLSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                                    val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
//                                    val filename2: String = Config.BASE_URL + "\\" + strNew + "\\" + filename1
                                    val filename2: String = IMAGE_URL + "" + strNew + "\\"+filename1
//                                    val filename2: String = IMAGE_URL + "" + strNew + "\\"+"SD7.pdf"
                                    Log.e(TAG,"filename2  51623   "+filename2)

                                    if (docType.equals("1")){
                                        /*val i = Intent(activity!!, ViewStatementActivity::class.java)
                                        i.putExtra("docx", filename2)
                                        startActivity(i)*/
                                    }

                                    if (docType.equals("2")){

                                        //  downloadFile(filename1,filename2)

                                        if (checkExternalStoragePermission()){
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
                                        activity!!,
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
                                    activity!!,
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
                                activity!!,
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
                    val builder = AlertDialog.Builder(activity!!, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(activity!!, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    fun checkExternalStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return true
        }
        val readStoragePermissionState =
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeStoragePermissionState =
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


    private fun downloadFile(filename1: String, filename2: String) {
        val pDialog = ProgressDialog(context!!)

        val client = OkHttpClient.Builder()
            .sslSocketFactory(Config.getSSLSocketFactory(context!!))
            .hostnameVerifier(Config.getHostnameVerifier())
            .build()

        //  val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val manager: com.coolerfall.download.DownloadManager? = com.coolerfall.download.DownloadManager.Builder().context(context)
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
        val destPath = File(dir, filename1)

        Log.e(TAG,"destPath  675      "+destPath)
        Log.e(TAG,"appName  675      "+appName)
        val request: DownloadRequest = DownloadRequest.Builder()
//            .url("https://202.164.150.65:14262/NbfcAndroidAPI/Statement/ASD7.pdf")
            .url(filename2)
            .retryTime(5)
            .retryInterval(2, TimeUnit.SECONDS)
            .progressInterval(1, TimeUnit.SECONDS)
            .priority(Priority.HIGH)
            .allowedNetworkTypes(DownloadRequest.NETWORK_ALL)
            .destinationFilePath(destPath.toString())
            .downloadCallback(object : DownloadCallback {
                override fun onStart(downloadId: Int, totalBytes: Long) {
                    Log.e(TAG,"onStart  6751      "+totalBytes)
                    pDialog.setMessage("Downloading...!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
                override fun onRetry(downloadId: Int) {
                    Log.e(TAG,"destPath  6752      "+destPath)
                    pDialog.setMessage("Downloading...!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
                override fun onProgress(downloadId: Int, bytesWritten: Long, totalBytes: Long) {
                    Log.e(TAG,"onProgress  675      "+bytesWritten+"    "+totalBytes)
                }
                override fun onSuccess(downloadId: Int, filePath: String) {
                    Log.e(TAG,"onSuccess  6753      "+filePath)
                    pDialog.dismiss()
                    StatementPopup(filePath,"1")
                }
                override fun onFailure(downloadId: Int, statusCode: Int, errMsg: String) {
                    Log.e(TAG,"onFailure  6754      "+statusCode+"    "+errMsg)
                    StatementPopup(errMsg,"0")
                    pDialog.dismiss()
                }
            })
            .build()

        val downloadId = manager!!.add(request)


    }

    private fun StatementPopup(Msg: String, mode: String) {

        try {
            val builder = AlertDialog.Builder(context)
            val inflater1 = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
                var intent = Intent(context, ViewStatementActivity::class.java)
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
}