package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
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


class DashboardActivity : AppCompatActivity(),View.OnClickListener  {
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var myListsAll = JSONArray()
    var arrayList1 = ArrayList<Float>()
    var imgHome: ImageView? = null
    var piechart: PieChart? = null
    var rvOverduelist: RecyclerView? = null
    var lay_chart: LinearLayout? = null
    var lay_nodata:LinearLayout? = null
    var tv_nodata: TextView? = null
    var list2 = ArrayList<String>()
    var jsonArraypie: JSONArray? = null
    private var jresult: JSONArray? = null
    var PieEntryLabelsTotal: ArrayList<String>? = null
    var entriesTotal: ArrayList<Entry>? = null
    var pieDataSetTotal: PieDataSet? = null
    var pieDataTotal: PieData? = null
    var nnnn : Int=0
    var dashTextSize : Float = 10f

    val pieChartData = arrayListOf<String>()

    val color = intArrayOf(
            R.color.colorPrimary,
            R.color.btngreen,
            R.color.colorPrimary,
            R.color.btngreen,
            R.color.colorPrimary
    )
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        setRegviews()


        getDashboardassetlist()
       // getDashboardliabilitylist()
       // getDashboardpaymentandreceiptlist()



    }

    private fun getDashboardassetlist() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@DashboardActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DashboardActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("35"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "ChartType",
                                MscoreApplication.encryptStart("1")
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
                    val call = apiService.getDashBoardAssetsDataDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        @RequiresApi(Build.VERSION_CODES.KITKAT)
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-assets", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("DashBoardDataAssetsDetailsIfo")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("DashBoardAssestDetails")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            var str = json.getInt("Balance")
                                             myListsAll = JSONArray(str)
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                            Log.e("Exception",e.toString())
                                        }
                                    }

                                    //  val json: JSONObject = jresult!!.getJSONObject(0)
                                    //  val json = jresult!!.getJSONObject(0)
                                    //  var str = json.getString("Balance")
                                    //val bal: Double = str.toDouble()
                                    //  val jObject = JSONObject(s)
                                    //val jaTotal1 = json.getJSONArray("Details")
                                    //     val myListsAll = JSONArray(str)

                                    Log.e(
                                            "TAG",
                                            "Response-array   " + myListsAll.length() + "  " + myListsAll
                                    )
                                    entriesTotal = ArrayList()
                                    PieEntryLabelsTotal = ArrayList<String>()


                                    for (x in 0 until myListsAll.length()) {
                                        val jsonobject = myListsAll[x] as JSONObject
                                        entriesTotal!!.add(
                                                BarEntry(
                                                        jsonobject.optString("Value").toFloat(), x
                                                )
                                        )
                                        PieEntryLabelsTotal!!.add(jsonobject.optString("Key"));

                                    }


                                    Log.e("TAG", "Response1  266   " + entriesTotal)
                                    Log.e("TAG", "Response1  267   " + PieEntryLabelsTotal + "  ")


                                    pieDataSetTotal = PieDataSet(entriesTotal, "")
                                    pieDataSetTotal!!.valueFormatter = PercentFormatter()

                                    pieDataTotal = PieData(PieEntryLabelsTotal, pieDataSetTotal)

                                    pieDataSetTotal!!.setColors(color, applicationContext)
                                    pieDataSetTotal!!.setValueTextSize(dashTextSize); // <- here
                                    pieDataSetTotal!!.setDrawValues(true);  // entries enable/disable
                                    pieDataSetTotal!!.setValueTextColor(
                                            ContextCompat.getColor(
                                                    this@DashboardActivity,
                                                    R.color.black
                                            )
                                    );

                                    piechart!!.data = pieDataTotal
                                    piechart!!.setDescription("")
                                    piechart!!.setDrawSliceText(true) //// PieEntryLabels enable/disable

                                    // pieChart!!.animateY(1000)
                                    piechart!!.animateY(1400, Easing.EasingOption.EaseInOutQuad)

                                    val l: Legend = piechart!!.getLegend()
                                    l.setEnabled(false);


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@DashboardActivity,
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
                                        this@DashboardActivity,
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
                                    this@DashboardActivity,
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
                    val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }


    private fun setRegviews() {
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)

        rvOverduelist = findViewById<RecyclerView>(R.id.rvOverduelist)

        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        piechart = findViewById(R.id.piechart)

     //   txtvDate = findViewById<TextView>(R.id.txtvDate)
      //  rv_pie = findViewById(R.id.rv_pie)
        //lay_chart = findViewById<LinearLayout>(R.id.lay_chart)
       // lay_nodata = findViewById<LinearLayout>(R.id.lay_nodata)
       // tv_nodata = findViewById<TextView>(R.id.tv_nodata)
    }


    private fun getDashboardliabilitylist() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@DashboardActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DashboardActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("35"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "ChartType",
                                MscoreApplication.encryptStart("2")
                        )

                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  liability   " + requestObject1)
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
                    val call = apiService.getDashBoardLiabilityDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //   progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-liability", response.body())

                                /* if (jObject.getString("StatusCode") == "0") {
                                     val jsonObj1: JSONObject =
                                         jObject.getJSONObject("PassBookAccountStatement")
                                     val jsonobj2 = JSONObject(jsonObj1.toString())

                                     jresult1 = jsonobj2.getJSONArray("PassBookAccountStatementList")
                                     if (jresult1!!.length() != 0) {
                                         tv_list_days!!.visibility = View.VISIBLE
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
                                 }*/
                            } catch (e: Exception) {
                                //  progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@DashboardActivity,
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

                            val builder = AlertDialog.Builder(
                                    this@DashboardActivity,
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
                    //   progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
    private fun getDashboardpaymentandreceiptlist() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@DashboardActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DashboardActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("35"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "ChartType",
                                MscoreApplication.encryptStart("3")
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
                        //  progressDialog!!.dismiss()
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
                    val call = apiService.getDashBoardpaymentandreceipt(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //     progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-payment", response.body())
                                /* if (jObject.getString("StatusCode") == "0") {
                                     val jsonObj1: JSONObject =
                                         jObject.getJSONObject("PassBookAccountStatement")
                                     val jsonobj2 = JSONObject(jsonObj1.toString())

                                     jresult1 = jsonobj2.getJSONArray("PassBookAccountStatementList")
                                     if (jresult1!!.length() != 0) {
                                         tv_list_days!!.visibility = View.VISIBLE
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
                                 }*/
                            } catch (e: Exception) {
                                // progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@DashboardActivity,
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
                            //    progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    this@DashboardActivity,
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
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@DashboardActivity, R.style.MyDialogTheme)
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
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@DashboardActivity, HomeActivity::class.java))
            }
        }
    }
}