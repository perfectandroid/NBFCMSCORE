package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.HolidayListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.Branchcode
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*


class HolidayListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    private var jresult: JSONArray? = null
    private var jresult1: JSONArray? = null
    private var rv_holiday: RecyclerView? = null
    var spnBranch: Spinner? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_header: TextView? = null

    var branchid:String?=null
    var arrayList1 = ArrayList<String>()
    public var arrayList2: ArrayList<Branchcode>? = null
    val TAG: String? = "HolidayListActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        setRegViews()
        getBranchlist()
        rv_holiday!!.visibility=View.GONE
//        getHolidayList(branchid)




    }
    private fun setRegViews() {

        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)


        tv_header= findViewById<TextView>(R.id.tv_header)

        val HeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF83, 0)
        tv_header!!.setText(HeaderSP.getString("HolidayList", null))

        rv_holiday = findViewById(R.id.rv_holiday)
        spnBranch = findViewById( R.id.spnBranch)
        spnBranch!!.onItemSelectedListener = this
    }
    private fun getBranchlist() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*progressDialog = ProgressDialog(this@HolidayListActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@HolidayListActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("8"))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )
                        requestObject1.put(
                            "FK_District",
                            MscoreApplication.encryptStart("0")
                        )
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                    val call = apiService.getBankBranchDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                              //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response 156", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("BankBranchDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult1 = jsonobj2.getJSONArray("BankBranchDetailsListInfo")
                                    arrayList2 = ArrayList<Branchcode>()
                                    for (i in 0 until jresult1!!.length()) {
                                        try {
                                            val json = jresult1!!.getJSONObject(i)
                                            arrayList1!!.add(json.getString("BranchName"))
                                            arrayList2!!.add(
                                                Branchcode(
                                                    json.getString("BranchName"), json.getString(
                                                        "ID_Branch"
                                                    )
                                                )
                                            )

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }


                                    spnBranch!!.adapter = ArrayAdapter(
                                        this@HolidayListActivity,
                                        android.R.layout.simple_spinner_dropdown_item, arrayList2!!
                                    )

                                //    getHolidayList(branchid)
                                } else {
                                    Log.e(TAG,"Exception  1961   ")
                                    val builder = AlertDialog.Builder(
                                        this@HolidayListActivity,
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
                                //  progressDialog!!.dismiss()
                                    Log.e(TAG,"Exception  1962   "+e.toString())

                                val builder = AlertDialog.Builder(
                                    this@HolidayListActivity,
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
                            //  progressDialog!!.dismiss()
                            Log.e(TAG,"Exception  1963   "+t.message)
                            val builder = AlertDialog.Builder(
                                this@HolidayListActivity,
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
                    Log.e(TAG,"Exception  1964   "+e.toString())
                    // progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                        this@HolidayListActivity,
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
            false -> {
                val builder = AlertDialog.Builder(this@HolidayListActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            }
    }



    private fun getHolidayList(branchid: String?) {
        rv_holiday!!.visibility=View.GONE
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@HolidayListActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@HolidayListActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("24"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BranchCode", MscoreApplication.encryptStart(branchid))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                    val call = apiService.getHolidayList(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    rv_holiday!!.visibility=View.VISIBLE
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("HolidayDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("HolidayDetailsList")

                                    if (jresult!!.length() != 0) {

                                        val lLayout =
                                            GridLayoutManager(this@HolidayListActivity, 1)
                                        rv_holiday!!.layoutManager = lLayout
                                        rv_holiday!!.setHasFixedSize(true)
                                        val adapter = HolidayListAdapter(
                                            applicationContext!!,
                                            jresult!!
                                        )

                                        rv_holiday!!.adapter = adapter

                                    } else {
                                        if(rv_holiday!!.isShown())
                                        {
                                            val builder = AlertDialog.Builder(
                                                    this@HolidayListActivity,
                                                    R.style.MyDialogTheme
                                            )
                                            builder.setMessage("" + jObject.getString("EXMessage"))
                                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                                            }
                                            val alertDialog: AlertDialog = builder.create()
                                            alertDialog.setCancelable(false)
                                            alertDialog.show()
                                        }
                                        else
                                        {

                                        }

                                    }


                                } else {
                                    if(rv_holiday!!.isShown())
                                    {
                                        val builder = AlertDialog.Builder(
                                                this@HolidayListActivity,
                                                R.style.MyDialogTheme
                                        )
                                        builder.setMessage("" + jObject.getString("EXMessage"))
                                        builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        }
                                        val alertDialog: AlertDialog = builder.create()
                                        alertDialog.setCancelable(false)
                                        alertDialog.show()
                                    }

                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                if(rv_holiday!!.isShown())
                                {
                                    val builder = AlertDialog.Builder(
                                            this@HolidayListActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("Some technical issues.")
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }

                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            if(rv_holiday!!.isShown()){
                                val builder = AlertDialog.Builder(
                                        this@HolidayListActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                            }

                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    if(rv_holiday!!.isShown()){
                        val builder = AlertDialog.Builder(
                                this@HolidayListActivity,
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
            }
            false -> {
                val builder = AlertDialog.Builder(this@HolidayListActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val json = jresult1!!.getJSONObject(position)


        // TextView textView = (TextView)mAccountTypeSpinner.getSelectedView();
        val branchcode: Branchcode = arrayList2!!.get(position)
        branchid = branchcode.getId()

      //  Toast.makeText(this, "ID: " + branchcode.getId() + "\nBranch: " + branchcode.getBranch(),
        //    Toast.LENGTH_SHORT).show();
      //
        rv_holiday!!.visibility=View.VISIBLE
        getHolidayList(branchid)

       // var id=arrayList2.get(position).
        // TextView textView = (TextView)mAccountTypeSpinner.getSelectedView();
       // var result = adapter1!!.getItem(position).branchcode()
    //   Toast.makeText(applicationContext, "Branchcode :" + result, Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@HolidayListActivity, HomeActivity::class.java))
            }
        }
    }


}