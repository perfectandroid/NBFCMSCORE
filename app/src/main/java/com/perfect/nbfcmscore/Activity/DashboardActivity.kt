package com.perfect.nbfcmscore.Activity

import android.R.attr
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
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
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*


class DashboardActivity : AppCompatActivity(),View.OnClickListener, OnChartValueSelectedListener {
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var myListsAll = JSONArray()
    var arrayList1 = ArrayList<Float>()
    var imgHome: ImageView? = null
    var status: String? = null
    var piechart: PieChart? = null
    var rvOverduelist: RecyclerView? = null
    private var jresult: JSONArray? = null
    private var jresult2: JSONArray? = null
    private var jresult3: JSONArray? = null
    var linechart1: LineChart? = null
    var linechart: LineChart? = null
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
        getDashboardliabilitylist()
        getDashboardpaymentandreceiptlist()



    }

    private fun getDashboardassetlist() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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


                                    //val jaTotal1 = json.getJSONArray("Details")
                                    //   val myListsAll = JSONArray(str)
                                    val yvalues = ArrayList<Entry>()
                                    val xVals = ArrayList<String>()
                                    for (x in 0 until jresult!!.length()) {
                                        val json = jresult!!.getJSONObject(x)

                                        val str = json.getString("Balance")
                                        val str1 = json.getString("Account")
                                        val tokens = StringTokenizer(str1, "(")
                                        val first = tokens.nextToken() // this will contain "Fruit"
                                        yvalues.add(Entry(str.toFloat(), 0))
                                        xVals.add(first)
                                    }

                                    // Log.e("TAG", "Response1  254   " + jresult!!.length() + "  " + myListsAll)

                                    /*entriesTotal = ArrayList()
                                    PieEntryLabelsTotal = ArrayList<String>()*/


                                    val dataSet = PieDataSet(yvalues, "")

                                    /*  xVals.add("Earned")
                                    xVals.add("Redeemed")
                                    xVals.add("Balance")*/

                                    val data = PieData(xVals, dataSet)
                                    data.setValueFormatter(DefaultValueFormatter(0))
                                    piechart!!.setData(data)
                                    //pieChart.setDescription("This is Pie Chart");

                                    piechart!!.setDrawHoleEnabled(true);
                                    piechart!!.setTransparentCircleRadius(25f);
                                    piechart!!.setHoleRadius(25f);

                                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                                    data.setValueTextSize(8f);
                                    data.setValueTextColor(Color.DKGRAY);
                                    piechart!!.setOnChartValueSelectedListener(this@DashboardActivity);
                                    piechart!!.animateXY(1400, 1400);
                                    /*  for (x in 0 until jresult!!.length()) {
                                        val jsonobject = myListsAll[x] as JSONObject
                                        entriesTotal!!.add(
                                                BarEntry(
                                                        jsonobject.optString("Value").toFloat(), x
                                                )
                                        )
                                        PieEntryLabelsTotal!!.add(jsonobject.optString("Key"));

                                    }
*/

                                    /*  Log.e("TAG", "Response1  266   " + entriesTotal)
                                    Log.e("TAG", "Response1  267   " + PieEntryLabelsTotal + "  ")
*/

                                    /*   pieDataSetTotal = PieDataSet(entriesTotal, "")
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
*/

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
        linechart1 = findViewById(R.id.linechart1);

        rvOverduelist = findViewById<RecyclerView>(R.id.rvOverduelist)

        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        piechart = findViewById(R.id.piechart)
        linechart = findViewById(R.id.linechart)

     //   txtvDate = findViewById<TextView>(R.id.txtvDate)
      //  rv_pie = findViewById(R.id.rv_pie)
        //lay_chart = findViewById<LinearLayout>(R.id.lay_chart)
       // lay_nodata = findViewById<LinearLayout>(R.id.lay_nodata)
       // tv_nodata = findViewById<TextView>(R.id.tv_nodata)
    }


    private fun getDashboardliabilitylist() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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

                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("DashBoardDataLaibilityDetailsIfo")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult2 = jsonobj2.getJSONArray("DashBoardLabilityDetails")
                                    var entries = ArrayList<Entry>()
                                   // val entries: ArrayList<BarEntry> = ArrayList()
                                    val labels = ArrayList<String>()

                                    for (x in 0 until jresult2!!.length()) {
                                        val json = jresult2!!.getJSONObject(x)

                                        val str = json.getString("Balance")
                                        val str1 = json.getString("Account")
                                        val tokens = StringTokenizer(str1, "(")
                                        val first = tokens.nextToken() // this will contain "Fruit"
                                        entries.add(Entry(str.toFloat(), 0))
                                       // entries.add(BarEntry(str.toFloat(), 0))
                                        labels.add(first)
                                    }
                                    /*val dataset = BarDataSet(entries, "")
                                    val bardata = BarData(labels, dataset)
                                    dataset.setColors(ColorTemplate.JOYFUL_COLORS);
                                    barChart!!.setData(bardata);
                                    barChart!!.animateY(5000);
                                    barChart!!.animateX(3000);*/


                                    val dataset = LineDataSet(entries, "")
                                    val linedata = LineData(labels, dataset)

                                    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                                    linechart1!!.setData(linedata);
                                    linechart1!!.animateY(5000);
                                    linechart1!!.animateX(3000);
                                    /*=====for cubic form========*/
                                    dataset.setDrawCubic(true);
                                    /*========Fill the color below the line=========*/
                                    dataset.setDrawFilled(true);

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
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("DashBoardDataPaymentAndReceiptDetailsIfo")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult3 = jsonobj2.getJSONArray("DashBoardDataPaymentDetails")

                                    //  val entries1 = arrayListOf<Entry>()

                                    var entries1 = ArrayList<Entry>()
                                    val labels1 = ArrayList<String>()
                                    for (x in 0 until jresult3!!.length()) {
                                        val json = jresult3!!.getJSONObject(x)

                                        val str = json.getString("Amount")
                                        val str1 = json.getString("TransType")
                                        if (str1.equals("R")) {
                                            status = "Receipt"
                                        } else if (str1.equals("P")) {
                                            status = "Payment"
                                        }
                                        // val tokens = StringTokenizer(str1, "(")
                                        // val first = tokens.nextToken() // this will contain "Fruit"
                                        //   entries1.add("what you want to add", 0)
                                        //  entries1.toMutableList().add(str.toFloat,0)
                                        // entries1.addAll(listOf(str.toFloat(), 0))
                                        //  entries1.add(MutableMap.MutableEntry<Any?, Any?>(str.toFloat(), 0))
                                        //  val chart = entries1.map{ LineChart(it) }.toTypedArray()
                                        entries1.add(Entry(str.toFloat(), 0))
                                        labels1.add(status.toString())

                                    }

                                    val dataset1 = LineDataSet(entries1, "")
                                    val data1 = LineData(labels1, dataset1)

                                    dataset1.setColors(ColorTemplate.COLORFUL_COLORS);
                                    linechart!!.setData(data1);
                                    linechart!!.animateY(5000);
                                    linechart!!.animateX(3000);
                                    /*=====for cubic form========*/
                                    dataset1.setDrawCubic(true);
                                    /*========Fill the color below the line=========*/
                                    dataset1.setDrawFilled(true);
                                    //  lineChart.setDescription(&quot;Description&quot;);
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

    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }
}