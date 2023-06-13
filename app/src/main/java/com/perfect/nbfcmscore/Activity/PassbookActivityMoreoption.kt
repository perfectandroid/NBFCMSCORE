package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.PassbookTranscationListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.Config.getSSLSocketFactory
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class PassbookActivityMoreoption : AppCompatActivity(), OnItemSelectedListener,View.OnClickListener {
    var arrayList1 = ArrayList<String>()
    var spnAccountNum: Spinner? = null
    private var jresult: JSONArray? = null
    private var jresult1: JSONArray? = null
    private var Account: TextView? = null
    private var available_balance: TextView? = null
    private var unclear_balance: TextView? = null
    private var txtLastUpdatedAt: TextView? = null
    private var empty_list: TextView? = null
    private var tv_list_days: TextView? = null
    private var rv_passbook: RecyclerView? = null
    private var ll_balance1: CardView? = null
    var tv_mycart: TextView? = null
    private var ll_balance: LinearLayout? = null
    var noofdays = 0
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var fkaccmore: String? = null
    var submodlemore:kotlin.String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passbookmoreoptn)
        setRegViews()

        tv_list_days!!.visibility=View.VISIBLE

        fkaccmore = intent.getStringExtra("fkaccount")
        submodlemore = intent.getStringExtra("submodule")
        Log.i("Checkget",fkaccmore+"\n"+submodlemore)
        //getAccList()

        val ID_Passbk = applicationContext.getSharedPreferences(Config.SHARED_PREF51,0)
        tv_mycart!!.setText(ID_Passbk.getString("passbook",null))

        val updateDaysSP = applicationContext.getSharedPreferences(Config.SHARED_PREF21,0)

        val ID_Listdata = applicationContext.getSharedPreferences(Config.SHARED_PREF217, 0)
        var listdata = ID_Listdata.getString("ListingDataforpast", null)

        val ID_Days = applicationContext.getSharedPreferences(Config.SHARED_PREF218, 0)
        var days = ID_Days.getString("days", null)

        val ID_chngest = applicationContext.getSharedPreferences(Config.SHARED_PREF219, 0)
        var chngst = ID_chngest.getString("youcanchangeitfromsettings", null)


        if (updateDaysSP.getString("updateDays",null) == null){
            tv_list_days!!.text = "**"+listdata+"30"+days+","+"\n"+chngst
          //  tv_list_days!!.text = "**Listing Data For Past 30 Days.\nYou Can Change It From Settings."
        }else{
            noofdays = (updateDaysSP.getString("updateDays",null))!!.toInt()
            tv_list_days!!.text = "**"+listdata+noofdays+days+","+"\n"+chngst
         //   tv_list_days!!.text = "**Listing Data For Past $noofdays Days.\nYou Can Change It From Settings."
        }


        getPassBookAccountStatement()
    }
    private fun setRegViews() {
        tv_mycart = findViewById(R.id.tv_mycart)
        tv_list_days = findViewById(R.id.tv_list_days)
        ll_balance = findViewById(R.id.ll_balance)
        ll_balance1 = findViewById(R.id.ll_balance1)
        spnAccountNum = findViewById(R.id.spnAccountNum)
        Account = findViewById(R.id.Account)
        available_balance = findViewById(R.id.available_balance)
        unclear_balance = findViewById(R.id.unclear_balance)
        txtLastUpdatedAt = findViewById(R.id.txtLastUpdatedAt)
        empty_list = findViewById(R.id.empty_list)
        rv_passbook = findViewById(R.id.rv_passbook)
        spnAccountNum!!.onItemSelectedListener = this

        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)


    }
    private fun getAccList() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@PassbookActivityMoreoption, R.style.Progress)
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

                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("12"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("Mode", MscoreApplication.encryptStart("1"))

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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getPassbookAccount(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("PassBookAccountDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            arrayList1!!.add(json.getString("AccountNumber"))
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spnAccountNum!!.adapter = ArrayAdapter(
                                            this@PassbookActivityMoreoption,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1
                                    )


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@PassbookActivityMoreoption,
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
                                        this@PassbookActivityMoreoption,
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
                                    this@PassbookActivityMoreoption,
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
                    val builder = AlertDialog.Builder(this@PassbookActivityMoreoption, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@PassbookActivityMoreoption, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }







    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
        try {
            val json = jresult!!.getJSONObject(position)
            if (json.getString("IsShowBalance").equals("1")) {

            //if (isshowbal.equals("1") ) {
                ll_balance!!.visibility = View.VISIBLE
                ll_balance1!!.visibility = View.VISIBLE
                available_balance!!.text =
                    "\u20B9 " + Config.getDecimelFormate(json.getDouble("AvailableBalance"))
                if (json.getDouble("UnclearAmount") <= 0) {
                    unclear_balance!!.text =
                        "\u20B9 " + Config.getDecimelFormate(json.getDouble("UnclearAmount"))
                    unclear_balance!!.setTextColor(Color.RED)
                } else {
                    unclear_balance!!.text =
                        "\u20B9 " + Config.getDecimelFormate(json.getDouble("UnclearAmount"))
                    unclear_balance!!.setTextColor(Color.parseColor("#7E5858"))
                }
            }
            else  {
                ll_balance!!.visibility = View.GONE
                ll_balance1!!.visibility = View.GONE
                tv_list_days!!.visibility = View.GONE
                rv_passbook!!.visibility = View.GONE
            }

            Account!!.text = json.getString("AccountType")
          /*  getPassBookAccountStatement(
                    json.getString("FK_Account"),
                    json.getString("SubModule"),
                    noofdays
            )*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPassBookAccountStatement() {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@PassbookActivityMoreoption, R.style.Progress)
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
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("13"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(fkaccmore))
                        requestObject1.put("FK_Customer",MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(submodlemore))
                        requestObject1.put("NoOfDays", MscoreApplication.encryptStart("" + noofdays))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
                        Log.e("TAG", "noofdays  171   " + noofdays)
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
                    val call = apiService.getPassbookAccountstatement(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountStatement")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult1 = jsonobj2.getJSONArray("PassBookAccountStatementList")
                                    if (jresult1!!.length() != 0) {
                                        tv_list_days!!.visibility = View.VISIBLE
                                        rv_passbook!!.visibility = View.VISIBLE
                                        empty_list!!.visibility = View.GONE
                                        val lLayout =
                                                GridLayoutManager(this@PassbookActivityMoreoption, 1)
                                        rv_passbook!!.layoutManager = lLayout
                                        rv_passbook!!.setHasFixedSize(true)
                                        val adapter = PassbookTranscationListAdapter(
                                                this@PassbookActivityMoreoption,
                                                jresult1!!,
                                                submodlemore!!
                                        )
                                        rv_passbook!!.adapter = adapter
                                    } else {
                                        rv_passbook!!.visibility = View.GONE
                                        tv_list_days!!.visibility = View.GONE
                                        empty_list!!.visibility = View.VISIBLE
                                        empty_list!!.text =
                                                "There are no transactions to display for the last $noofdays days"
                                    }


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@PassbookActivityMoreoption,
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
                                        this@PassbookActivityMoreoption,
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
                                    this@PassbookActivityMoreoption,
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
                    val builder = AlertDialog.Builder(this@PassbookActivityMoreoption, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@PassbookActivityMoreoption, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@PassbookActivityMoreoption, HomeActivity::class.java))
            }
        }
    }
}