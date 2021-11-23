package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.DuedateAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DuedateActivity : AppCompatActivity() , View.OnClickListener{

    val TAG : String = "DuedateActivity"
    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    var rv_standinginst: RecyclerView? = null
    var cusid: String? = null
    private var jresult: JSONArray? = null
    var token:kotlin.String? = null
    var acctype:kotlin.String? = null
    var progressDialog: ProgressDialog? = null
    var ll_standnginstr: LinearLayout? = null
    var llreminder:LinearLayout? = null
    var tvDeposit: TextView? = null
    var tvLoan:TextView? = null
    var tvTitle:TextView? = null
    var tv_header:TextView? = null
    var txtv_Accno:TextView? = null
    var tvamt:TextView? = null
    var txtv_Duedate:TextView? = null





    var strHeader = "Deposit"
    var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_duedate)
        setRegViews()

        val DepositSp = applicationContext.getSharedPreferences(Config.SHARED_PREF88,0)
        tvDeposit!!.setText(DepositSp.getString("Deposit",null))

        val LoanSp = applicationContext.getSharedPreferences(Config.SHARED_PREF89,0)
        tvLoan!!.setText(LoanSp.getString("Loan",null))

        val HeaderSp = applicationContext.getSharedPreferences(Config.SHARED_PREF202,0)
        tv_header!!.setText(HeaderSp.getString("DueDatesCalender",null))


       val Accountnosp = applicationContext.getSharedPreferences(Config.SHARED_PREF107,0)
        txtv_Accno!!.setText(Accountnosp.getString("AccountNo",null))

        val Amtsp = applicationContext.getSharedPreferences(Config.SHARED_PREF113,0)
        tvamt!!.setText(Amtsp.getString("Amount",null))

        val duedtesp = applicationContext.getSharedPreferences(Config.SHARED_PREF204,0)
        txtv_Duedate!!.setText(duedtesp.getString("Duedate",null))


    }

    private fun setRegViews() {

        tv_header = findViewById(R.id.tv_header)
        txtv_Accno= findViewById(R.id.txtv_Accno)
        txtv_Duedate= findViewById(R.id.txtv_Duedate)
        tvamt= findViewById(R.id.tvamt)

        ll_standnginstr = findViewById(R.id.ll_standnginstr)
        llreminder = findViewById(R.id.llreminder)
        rv_standinginst = findViewById(R.id.rv_standinginst)
        tvTitle = findViewById(R.id.tvTitle)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)


        tvDeposit = findViewById(R.id.tvDeposit)
        tvDeposit!!.setOnClickListener(this)
        tvLoan = findViewById(R.id.tvLoan)
        tvLoan!!.setOnClickListener(this)

        acctype = "1"
        getDuedate()

        try {
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            var sdf = SimpleDateFormat("dd-MM-yyyy")
            val c = Calendar.getInstance()
            c.time = sdf.parse(currentDate)
            c.add(Calendar.DATE, 14)
            sdf = SimpleDateFormat("dd-MM-yyyy")
            val resultdate = Date(c.timeInMillis)
            val lastDate = sdf.format(resultdate)
            // tvTitle.setText("Due Date List [ "+currentDate+" to "+lastDate+" ]");
         //   tvTitle!!.setText("Due Date list for upcoming two weeks")
            val DuedaatelistSp= applicationContext.getSharedPreferences(Config.SHARED_PREF203,0)
            tvTitle!!.setText(DuedaatelistSp.getString("Duedatelistforupcomingtwoweeks.",null))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@DuedateActivity, HomeActivity::class.java))
            }
            R.id.tvDeposit -> {
                val DuedaatelistSp= applicationContext.getSharedPreferences(Config.SHARED_PREF203,0)
                tvTitle!!.setText(DuedaatelistSp.getString("Duedatelistforupcomingtwoweeks.",null))
              //  tvTitle!!.text = "Due Date list for upcoming two weeks"
                tvLoan!!.background = ContextCompat.getDrawable(this@DuedateActivity, R.drawable.tab_unselect)
                tvDeposit!!.background = ContextCompat.getDrawable(this@DuedateActivity, R.drawable.tab_select)
//                tvLoan!!.setTextColor(Color.parseColor("#000000"))
//                tvDeposit!!.setTextColor(Color.parseColor("#ffffff"))
                acctype = "1"
                getDuedate()
                strHeader = "Deposit"
            }
            R.id.tvLoan -> {
                tvTitle!!.text = "Demand list for upcoming two weeks"
                tvLoan!!.background = ContextCompat.getDrawable(this@DuedateActivity, R.drawable.tab_select)
                tvDeposit!!.background = ContextCompat.getDrawable(this@DuedateActivity, R.drawable.tab_unselect)
//                tvLoan!!.setTextColor(Color.parseColor("#ffffff"))
//                tvDeposit!!.setTextColor(Color.parseColor("#000000"))
                acctype = "2"
                getDuedate()
                strHeader = "Loan"
            }
            R.id.tvLoan -> {
                startActivity(Intent(this@DuedateActivity, HomeActivity::class.java))
            }
        }
    }

    private fun getDuedate() {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        ll_standnginstr!!.visibility = View.GONE
        llreminder!!.visibility = View.GONE
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@DuedateActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DuedateActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("18"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "SubMode",
                                MscoreApplication.encryptStart(acctype)
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
                        Log.e(TAG, "Exception  198   ")
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
                    val call = apiService.getAccountduedetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.e("Response-duedate","  19821   "+ response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    ll_standnginstr!!.visibility = View.VISIBLE
                                    llreminder!!.visibility = View.VISIBLE
                                    val jsonObj1: JSONObject = jObject.getJSONObject("AccountDueDateDetailsIfo")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("AccountDueDateDetails")
                                    if (jresult!!.length() != 0) {
                                        val lLayout = GridLayoutManager(this@DuedateActivity, 1)
                                        rv_standinginst!!.layoutManager = lLayout
                                        rv_standinginst!!.setHasFixedSize(true)
                                        val adapter = DuedateAdapter(this@DuedateActivity, jresult!!, strHeader)
                                        rv_standinginst!!.adapter = adapter
                                    } else {
                                        val builder = AlertDialog.Builder(
                                                this@DuedateActivity,
                                                R.style.MyDialogTheme
                                        )
                                        builder.setMessage("" + jObject.getString("EXMessage"))
                                        builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        }
                                        val alertDialog: AlertDialog = builder.create()
                                        alertDialog.setCancelable(false)
                                        alertDialog.show()
                                    }

                                } else {
                                    ll_standnginstr!!.visibility = View.GONE
                                    llreminder!!.visibility = View.GONE
                                    val builder = AlertDialog.Builder(
                                            this@DuedateActivity,
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
                                Log.e(TAG, "Exception  1981   "+e.toString())

                                val builder = AlertDialog.Builder(
                                        this@DuedateActivity,
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
                            Log.e(TAG, "onFailure  1982   "+t.message)
                            val builder = AlertDialog.Builder(
                                    this@DuedateActivity,
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
                    Log.e(TAG, "Exception  1983   "+e.toString())
                    val builder = AlertDialog.Builder(this@DuedateActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@DuedateActivity, R.style.MyDialogTheme)
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