package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
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
import java.text.SimpleDateFormat
import java.util.*

class StatementFragment : Fragment() , View.OnClickListener{

    val TAG: String = "StatementFragment"
    private var progressDialog: ProgressDialog? = null
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
    var AccountNumber: String? = ""
    var SubModule: String? = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_statementdownloadview, container, false)


        rad_last_month = v.findViewById<RadioButton>(R.id.rad_last_month)
        rad_last_3_month = v.findViewById<RadioButton>(R.id.rad_last_3_month)
        rad_last_6_month = v.findViewById<RadioButton>(R.id.rad_last_6_month)
        rad_last_12_month = v.findViewById<RadioButton>(R.id.rad_last_12_month)

        edt_fromDate = v.findViewById<EditText>(R.id.edt_fromDate)
        edt_toDate = v.findViewById<EditText>(R.id.edt_toDate)

        tv_view = v.findViewById<TextView>(R.id.tv_view)
        tv_download = v.findViewById<TextView>(R.id.tv_download)

        val StatemenAccountNumber = context!!.getSharedPreferences(Config.SHARED_PREF155, 0)
        AccountNumber = StatemenAccountNumber.getString("StatementAccountNumber",null)


        val StatementSubModule = context!!.getSharedPreferences(Config.SHARED_PREF156, 0)
        SubModule = StatementSubModule.getString("StatementSubModule",null)

        rad_last_month!!.setOnClickListener(this)
        rad_last_3_month!!.setOnClickListener(this)
        rad_last_6_month!!.setOnClickListener(this)
        rad_last_12_month!!.setOnClickListener(this)

        edt_fromDate!!.setOnClickListener(this)
        edt_toDate!!.setOnClickListener(this)

        tv_view!!.setOnClickListener(this)
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

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_toDate!!.setText("" + dayOfMonth + "-" + month + "-" + year)
            ToDate = year.toString()+"-"+month.toString()+"-"+dayOfMonth.toString()
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

            R.id.tv_view ->{
                docType = "1"
                validation1()
            }
            R.id.tv_download ->{
                docType = "2"
                //validation1()
            }
        }
    }

    private fun validation1() {
       if (rad_last_month!!.isChecked || rad_last_3_month!!.isChecked || rad_last_6_month!!.isChecked || rad_last_12_month!!.isChecked){
           validation()
       }
        else if (edt_fromDate!!.text.toString().equals("")){
           CustomBottomSheeet.Show(activity!!,"Select From Date","0")
       }
       else if (edt_toDate!!.text.toString().equals("")){
           CustomBottomSheeet.Show(activity!!,"Select End Date","0")
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

                                    val strNew: String? = filename.substringAfterLast("NbfcAndroidAPI\\")
                                    Log.e(TAG,"strNew  51622   "+strNew)

                                    val ImageURLSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                                    val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
//                                    val filename2: String = Config.BASE_URL + "\\" + strNew + "\\" + filename1
                                    val filename2: String = IMAGE_URL + "\\" + strNew
                                    Log.e(TAG,"filename2  51623   "+filename2)

                                    if (docType.equals("1")){
                                        /*val i = Intent(activity!!, ViewStatementActivity::class.java)
                                        i.putExtra("docx", filename2)
                                        startActivity(i)*/
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
}