package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
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

class QuickBalanceActivity : AppCompatActivity(), OnItemSelectedListener,View.OnClickListener {
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
    private var tv_mycart: TextView? = null
    private var rv_passbook: RecyclerView? = null
    private var ll_balance1: CardView? = null
    private var txtv_acnttype: TextView? = null
    private var txtvaccno: TextView? = null
    private var ll_balance: LinearLayout? = null
    private var txtv_availbal: TextView? = null
    var noofdays = 0
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quickbalance)
        setRegViews()

        val ID_Quick = applicationContext.getSharedPreferences(Config.SHARED_PREF52,0)
        tv_mycart!!.setText(ID_Quick.getString("quickbalance",null))

        val ID_acntno = applicationContext.getSharedPreferences(Config.SHARED_PREF158,0)
        txtvaccno!!.setText(ID_acntno.getString("AccountNumber",null))


        val ID_availbal = applicationContext.getSharedPreferences(Config.SHARED_PREF119,0)
        txtv_availbal!!.setText(ID_availbal.getString("AvailableBalance",null))


        val imlogo: ImageView = findViewById(R.id.imlogo)
        Glide.with(this).load(R.drawable.quickbalgif).into(imlogo)

        getAccList()
    }
    private fun setRegViews() {
        tv_mycart = findViewById(R.id.tv_mycart)
        txtv_acnttype= findViewById(R.id.txtv_acnttype)
        txtv_availbal= findViewById(R.id.txtv_availbal)

        txtvaccno= findViewById(R.id.txtvaccno)
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
                progressDialog = ProgressDialog(this@QuickBalanceActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@QuickBalanceActivity))
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
                                            arrayList1!!.add(json.getString("AccountNumber"))
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spnAccountNum!!.adapter = ArrayAdapter(
                                            this@QuickBalanceActivity,
                                            R.layout.simple_spinner_item, arrayList1
                                    )


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@QuickBalanceActivity,
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
                                        this@QuickBalanceActivity,
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
                                    this@QuickBalanceActivity,
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
                    val builder = AlertDialog.Builder(this@QuickBalanceActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@QuickBalanceActivity, R.style.MyDialogTheme)
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
//                    unclear_balance!!.setTextColor(Color.RED)
                   // unclear_balance!!.setTextColor(Color.BLUE)
                } else {
                    unclear_balance!!.text =
                        "\u20B9 " + Config.getDecimelFormate(json.getDouble("UnclearAmount"))
                  //  unclear_balance!!.setTextColor(Color.WHITE)
//                    unclear_balance!!.setTextColor(Color.parseColor("#7E5858"))
                }
            }
            else  {
                ll_balance!!.visibility = View.GONE
                ll_balance1!!.visibility = View.GONE
                tv_list_days!!.visibility = View.GONE
                rv_passbook!!.visibility = View.GONE
            }

            Account!!.text = json.getString("AccountType")
         /*   getPassBookAccountStatement(
                    json.getString("FK_Account"),
                    json.getString("SubModule"),
                    noofdays
            )*/
        } catch (e: Exception) {
            e.printStackTrace()
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
                startActivity(Intent(this@QuickBalanceActivity, HomeActivity::class.java))
            }
        }
    }
}