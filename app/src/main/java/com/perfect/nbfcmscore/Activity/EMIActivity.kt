package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.EmiListAdapter
import com.perfect.nbfcmscore.Adapter.LoanSlabAdaptor
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.EMIModel
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

class EMIActivity : AppCompatActivity()  , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null

    private var rv_loanslab: RecyclerView? = null
    var list_view: ListView? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var principal: EditText? = null
    var interest: EditText? = null
    var years: EditText? = null
    var llemitype: LinearLayout? = null
    var sadapter: EmiListAdapter? = null
    var txt_emi: TextView? = null
    var emiTotal: TextView? = null
    var interest_total: TextView? = null

    var ID_EmiMethod: String? = null
    var btn_calculate: Button? = null

    var EMIArrayList: ArrayList<EMIModel> = ArrayList<EMIModel>()
    var array_sort: ArrayList<EMIModel> = ArrayList<EMIModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emi)

        btn_calculate  = findViewById<View>(R.id.btn_calculate) as Button?
        emiTotal  = findViewById<View>(R.id.emiTotal) as TextView?
        txt_emi  = findViewById<View>(R.id.txt_emi) as TextView?
        interest_total  = findViewById<View>(R.id.interest_total) as TextView?
        llemitype  = findViewById<View>(R.id.llemitype) as LinearLayout?
        principal  = findViewById<View>(R.id.principal) as EditText?
        interest  = findViewById<View>(R.id.interest) as EditText?
        years  = findViewById<View>(R.id.years) as EditText?
        rv_loanslab  = findViewById<View>(R.id.rv_loanslab) as RecyclerView?
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        llemitype!!.setOnClickListener(this)
        btn_calculate!!.setOnClickListener(this)
    }

    private fun getEMICalculators() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@EMIActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@EMIActivity))
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

                        val sdf = SimpleDateFormat("dd-M-yyyy")
                        val currentDate = sdf.format(Date())

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("22"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("LoanAmount", MscoreApplication.encryptStart(principal!!.text.toString()))
                        requestObject1.put("RateOfInterset", MscoreApplication.encryptStart(interest!!.text.toString()))
                        requestObject1.put("MonthOrYear",  MscoreApplication.encryptStart("M"))
                        requestObject1.put("TenureValue", MscoreApplication.encryptStart(years!!.text.toString()))
                        requestObject1.put("Id_Method", MscoreApplication.encryptStart(ID_EmiMethod))
                        requestObject1.put("LoanDate", MscoreApplication.encryptStart(currentDate))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(
                                    R.string.BankKey
                                )))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(
                                       R.string.BankHeader
                                   )))
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
                    val call = apiService.getEMICalculatorDateils(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("EMICalculatorDateils")

                                    emiTotal!!.text=jobjt.getString("InstallmentAmount")
                                    interest_total!!.text=jobjt.getString("TotalInterest")

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@EMIActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@EMIActivity,
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
                                this@EMIActivity,
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
                    val builder = AlertDialog.Builder(this@EMIActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@EMIActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    private fun getEMITypeList(alertDialog: AlertDialog) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@EMIActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@EMIActivity))
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


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("21"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )
                           requestObject1.put(
                               "BankHeader", MscoreApplication.encryptStart(
                                   getResources().getString(
                                       R.string.BankHeader
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
                    val call = apiService.getEMIMethodDateils(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("EMIMethodDateils")
                                    val jarray =
                                        jobjt.getJSONArray("EMIMethodDateilsList")
                                    array_sort =
                                        ArrayList<EMIModel>()
                                    for (k in 0 until jarray.length()) {
                                        val jsonObject = jarray.getJSONObject(k)
                                        EMIArrayList.add(
                                            EMIModel(
                                                jsonObject.getString("ID_EMIMethod"),
                                                jsonObject.getString("MethodName")
                                            )
                                        )
                                        array_sort.add(
                                            EMIModel(
                                                jsonObject.getString("ID_EMIMethod"),
                                                jsonObject.getString("MethodName")
                                            )
                                        )
                                    }
                                    sadapter = EmiListAdapter(
                                        this@EMIActivity,array_sort
                                    )
                                    list_view!!.adapter = sadapter
                                    list_view!!.onItemClickListener =
                                        OnItemClickListener { parent, view, position, id ->
                                            txt_emi!!.setText(array_sort.get(
                                                    position
                                                ).getProductName()
                                            )
                                            ID_EmiMethod =array_sort.get(
                                                    position
                                                ).getID_Product()
                                            alertDialog.dismiss()
                                        }

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@EMIActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@EMIActivity,
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
                                this@EMIActivity,
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
                    val builder = AlertDialog.Builder(this@EMIActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@EMIActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_calculate ->{
                getEMICalculators()
            }
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@EMIActivity, HomeActivity::class.java))
            }
            R.id.llemitype ->{
                getEMIType()            }
        }
    }

    private fun getEMIType() {
        try {
            val builder = AlertDialog.Builder(this)
            val inflater1 = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout: View = inflater1.inflate(R.layout.emitype_popup, null)
            list_view = layout.findViewById<View>(R.id.list_view) as ListView
            builder.setView(layout)
            val alertDialog = builder.create()
            getEMITypeList(alertDialog)
            alertDialog.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}