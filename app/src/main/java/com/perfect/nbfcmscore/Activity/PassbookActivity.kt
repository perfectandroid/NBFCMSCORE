package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
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

class PassbookActivity : AppCompatActivity(), OnItemSelectedListener,View.OnClickListener {
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
    private var card_list: CardView? = null

    private var ll_balance: LinearLayout? = null
    var noofdays = 0
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_mycart: TextView? = null

    var act_account: AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passbook)
        setRegViews()
        val ID_Passbk = applicationContext.getSharedPreferences(Config.SHARED_PREF51,0)
        tv_mycart!!.setText(ID_Passbk.getString("passbook",null))


        getAccList()
    }
    private fun setRegViews() {
        tv_list_days = findViewById(R.id.tv_list_days)
        tv_mycart = findViewById(R.id.tv_mycart)
        ll_balance = findViewById(R.id.ll_balance)
        ll_balance1 = findViewById(R.id.ll_balance1)
        spnAccountNum = findViewById(R.id.spnAccountNum)
        Account = findViewById(R.id.Account)
        available_balance = findViewById(R.id.available_balance)
        unclear_balance = findViewById(R.id.unclear_balance)
        txtLastUpdatedAt = findViewById(R.id.txtLastUpdatedAt)
        empty_list = findViewById(R.id.empty_list)
        rv_passbook = findViewById(R.id.rv_passbook)
        act_account = findViewById(R.id.act_account)
        card_list = findViewById(R.id.card_list)
        spnAccountNum!!.onItemSelectedListener = this

        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        act_account!!.setOnClickListener(this)

    }
    private fun getAccList() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@PassbookActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("12"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


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
                    val call = apiService.getPassbookAccount(body)
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
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("PassBookAccountDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            if(i == 0){
                                                act_account!!.setText(json.getString("AccountNumber"))
                                                Account!!.setText(json.getString("AccountType"))
                                                getPassBookAccountStatement(
                                                    json.getString("FK_Account"),
                                                    json.getString("SubModule"),
                                                    noofdays
                                                )

                                            }
                                            arrayList1!!.add(json.getString("AccountNumber"))
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spnAccountNum!!.adapter = ArrayAdapter(
                                            this@PassbookActivity,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1
                                    )

                                    val adapter = ArrayAdapter(this@PassbookActivity,
                                        android.R.layout.simple_list_item_1, arrayList1)
                                    act_account!!.setAdapter(adapter)
//                                    act_district!!.showDropDown()
                                    act_account!!.threshold =1

                                    act_account!!.setOnItemClickListener { parent, view, position, id ->

                                       // for (i in 0 until jresult!!.length()) {
                                            val json: JSONObject = jresult!!.getJSONObject(position)
                                            if (json.getString("IsShowBalance").equals("1")) {

                                                //if (isshowbal.equals("1") ) {
                                                ll_balance!!.visibility = View.VISIBLE
                                                ll_balance1!!.visibility = View.VISIBLE
                                                available_balance!!.text =
                                                    "\u20B9 " + Config.getDecimelFormate(json.getDouble("AvailableBalance"))
                                                if (json.getDouble("UnclearAmount") <= 0) {
                                                    unclear_balance!!.text =
                                                        "\u20B9 " + Config.getDecimelFormate(json.getDouble("UnclearAmount"))
                                                    unclear_balance!!.setTextColor(Color.WHITE)
                                                } else {
                                                    unclear_balance!!.text =
                                                        "\u20B9 " + Config.getDecimelFormate(json.getDouble("UnclearAmount"))
//                                                    unclear_balance!!.setTextColor(Color.parseColor("#7E5858"))
                                                    unclear_balance!!.setTextColor(Color.WHITE)
                                                }
                                            }
                                            else  {
                                                ll_balance!!.visibility = View.GONE
                                                ll_balance1!!.visibility = View.GONE
                                                tv_list_days!!.visibility = View.GONE
                                                card_list!!.visibility = View.GONE
                                                rv_passbook!!.visibility = View.GONE
                                            }

                                            Account!!.text = json.getString("AccountType")
                                            getPassBookAccountStatement(
                                                json.getString("FK_Account"),
                                                json.getString("SubModule"),
                                                noofdays
                                            )
                                      //  }
                                    }

//                                    act_account!!.addTextChangedListener(object : TextWatcher {
//                                        override fun onTextChanged(
//                                            s: CharSequence,
//                                            start: Int,
//                                            before: Int,
//                                            count: Int) {
//
//
//
//                                        }
//
//                                        override fun beforeTextChanged(
//                                            s: CharSequence,
//                                            start: Int,
//                                            count: Int,
//                                            after: Int
//                                        ) {
//                                        }
//
//                                        override fun afterTextChanged(s: Editable) {}
//                                    })


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@PassbookActivity,
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
                                        this@PassbookActivity,
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
                                    this@PassbookActivity,
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
                    val builder = AlertDialog.Builder(this@PassbookActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@PassbookActivity, R.style.MyDialogTheme)
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
                card_list!!.visibility = View.GONE
                rv_passbook!!.visibility = View.GONE
            }

            Account!!.text = json.getString("AccountType")
            getPassBookAccountStatement(
                    json.getString("FK_Account"),
                    json.getString("SubModule"),
                    noofdays
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPassBookAccountStatement(fkaccount: String, submodule: String, noofdays: Int) {

        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@PassbookActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("13"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Account",
                                MscoreApplication.encryptStart(fkaccount)
                        )
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(submodule)
                        )
                        requestObject1.put(
                                "NoOfDays",
                                MscoreApplication.encryptStart("" + noofdays)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


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
                    val call = apiService.getPassbookAccountstatement(body)
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
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountStatement")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult1 = jsonobj2.getJSONArray("PassBookAccountStatementList")
                                    if (jresult1!!.length() != 0) {
                                        tv_list_days!!.visibility = View.VISIBLE
                                        card_list!!.visibility = View.VISIBLE
                                        rv_passbook!!.visibility = View.VISIBLE
                                        empty_list!!.visibility = View.GONE
                                        val lLayout =
                                                GridLayoutManager(this@PassbookActivity, 1)
                                        rv_passbook!!.layoutManager = lLayout
                                        rv_passbook!!.setHasFixedSize(true)
                                        val adapter = PassbookTranscationListAdapter(
                                                this@PassbookActivity,
                                                jresult1!!,
                                                submodule
                                        )
                                        rv_passbook!!.adapter = adapter
                                    } else {
                                        rv_passbook!!.visibility = View.GONE
                                        tv_list_days!!.visibility = View.GONE
                                        card_list!!.visibility = View.GONE
                                        empty_list!!.visibility = View.VISIBLE
                                        empty_list!!.text =
                                                "There are no transactions to display for the last $noofdays days"
                                    }


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@PassbookActivity,
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
                                        this@PassbookActivity,
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
                                    this@PassbookActivity,
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
                    val builder = AlertDialog.Builder(this@PassbookActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@PassbookActivity, R.style.MyDialogTheme)
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
        TODO("Not yet implemented")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@PassbookActivity, HomeActivity::class.java))
            }
            R.id.act_account ->{
                act_account!!.showDropDown()
            }
        }
    }
}