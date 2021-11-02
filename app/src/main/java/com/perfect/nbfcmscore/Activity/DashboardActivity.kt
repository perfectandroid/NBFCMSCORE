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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AssetsAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.PieChartView
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class DashboardActivity : AppCompatActivity(),View.OnClickListener  {
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var pieChartView: PieChartView? = null
    var ivAsset1: ImageView? = null
    var ivAsset2:android.widget.ImageView? = null
    var ivAsset3:android.widget.ImageView? = null
    var ivAsset4:android.widget.ImageView? = null
    var ivAsset5:android.widget.ImageView? = null
    var tvAsset1: TextView? = null
    var tvAsset2:TextView? = null
    var tvAsset3:TextView? = null
    var tvAsset4:TextView? = null
    var tvAsset5:TextView? = null
    var txtvDate:TextView? = null
    var rv_pie: RecyclerView? = null
    var lay_chart: LinearLayout? = null
    var lay_nodata:LinearLayout? = null
    var tv_nodata: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        setRegviews()
        getDashboardassetlist()
        getDashboardliabilitylist()
        getDashboardpaymentandreceiptlist()


    }

    private fun setRegviews() {
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        pieChartView = findViewById(R.id.chart)
        ivAsset1 = findViewById<ImageView>(R.id.ivAsset1)
        ivAsset2 = findViewById<ImageView>(R.id.ivAsset2)
        ivAsset3 = findViewById<ImageView>(R.id.ivAsset3)
        ivAsset4 = findViewById<ImageView>(R.id.ivAsset4)
        ivAsset5 = findViewById<ImageView>(R.id.ivAsset5)
        tvAsset1 = findViewById<TextView>(R.id.tvAsset1)
        tvAsset2 = findViewById<TextView>(R.id.tvAsset2)
        tvAsset3 = findViewById<TextView>(R.id.tvAsset3)
        tvAsset4 = findViewById<TextView>(R.id.tvAsset4)
        tvAsset5 = findViewById<TextView>(R.id.tvAsset5)
     //   txtvDate = findViewById<TextView>(R.id.txtvDate)
        rv_pie = findViewById(R.id.rv_pie)
        //lay_chart = findViewById<LinearLayout>(R.id.lay_chart)
        lay_nodata = findViewById<LinearLayout>(R.id.lay_nodata)
        tv_nodata = findViewById<TextView>(R.id.tv_nodata)
    }

    private fun getDashboardassetlist() {
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
                    val call = apiService.getDashBoardAssetsDataDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-assets", response.body())
                                val jobj = jObject.getJSONObject("DashBoardDataAssetsDetailsIfo")

                                val jarray = jobj.getJSONArray("DashBoardAssestDetails")
                                val startDate = jobj.getString("StartDate")
                                val endDate = jobj.getString("EndDate")

                                //int a[]=new int[jarray.length()];
                                try {
                                    val a = intArrayOf(
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen),
                                            resources.getColor(R.color.colorPrimary),
                                            resources.getColor(R.color.btngreen)
                                    )
                                    val pieData: MutableList<*> = ArrayList<Any>()
                                    for (i in 0 until jarray.length()) {
                                        val qstnArray = jarray.getJSONObject(i)
                                        val intColor = ChartUtils.pickColor()
                                        // a[i]=intColor;//initialization
                                      //  pieData.add(SliceValue(qstnArray.getLong("Balance").toFloat(), a[i]).setLabel(qstnArray.getString("Balance")))
                                      /*  val lLayout = GridLayoutManager(applicationContext, 1)
                                        rv_pie!!.layoutManager = lLayout
                                        rv_pie!!.setHasFixedSize(true)
                                        val adapter = AssetsAdapter(applicationContext, jarray, a)
                                        rv_pie!!.adapter = adapter*/
                                        //pieData.add(new SliceValue(qstnArray.getLong("Balance"), ChartUtils.pickColor()).setLabel(qstnArray.getString("Account")));
                                        /*   if(i==0) {
                                            pieData.add(new SliceValue(qstnArray.getLong("Balance"),getResources().getColor(R.color.graph1)).setLabel(qstnArray.getString("Balance")));
                                            ivAsset1.setBackgroundColor(getResources().getColor(R.color.graph1));
                                            tvAsset1.setText(qstnArray.getString("Account"));
                                        }
                                        if(i==1) {
                                            pieData.add(new SliceValue(qstnArray.getLong("Balance"), getResources().getColor(R.color.graph2)).setLabel(qstnArray.getString("Balance")));
                                            ivAsset2.setBackgroundColor(getResources().getColor(R.color.graph2));
                                            tvAsset2.setText(qstnArray.getString("Account"));
                                        }
                                        if(i==2) {
                                            pieData.add(new SliceValue(qstnArray.getLong("Balance"), getResources().getColor(R.color.graph3)).setLabel(qstnArray.getString("Balance")));
                                            ivAsset3.setBackgroundColor(getResources().getColor(R.color.graph3));
                                            tvAsset3.setText(qstnArray.getString("Account"));
                                        }
                                        if(i==3) {
                                            pieData.add(new SliceValue(qstnArray.getLong("Balance"), getResources().getColor(R.color.graph4)).setLabel(qstnArray.getString("Balance")));
                                            ivAsset4.setBackgroundColor(getResources().getColor(R.color.graph4));
                                            tvAsset4.setText(qstnArray.getString("Account"));
                                        }
                                        if(i==4) {
                                            pieData.add(new SliceValue(qstnArray.getLong("Balance"),getResources().getColor(R.color.graph5)).setLabel(qstnArray.getString("Balance")));
                                            ivAsset5.setBackgroundColor(getResources().getColor(R.color.graph5));
                                            tvAsset5.setText(qstnArray.getString("Account"));
                                        }*/
                                    }
                                  /*  val pieChartData = PieChartData(pieData)
                                    pieChartData.setHasLabels(true).valueLabelTextSize = 12
                                    pieChartData.setHasCenterCircle(true).setCenterText1("Assets").setCenterText1FontSize(22).centerText1Color = Color.parseColor("#0097A7")
                                    pieChartView!!.pieChartData = pieChartData*/
                                } catch (e: java.lang.Exception) {
                                    Log.e("TAG", "Exception   149   $e")
                                }
                                //  txtvDate!!.text = "Data From :$startDate to $endDate"
                            } catch (e: Exception) {
                                //progressDialog!!.dismiss()

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
                    // progressDialog!!.dismiss()
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