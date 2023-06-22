package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.HolidayListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.Branchcode
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class HolidayListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    private var jresult: JSONArray? = null
    private var jresult1: JSONArray? = null
    private var rv_holiday: RecyclerView? = null
    var spnBranch: Spinner? = null
    var imgBack: ImageView? = null
    var imgCalender: ImageView? = null
    var imgList: ImageView? = null
    var imgHome: ImageView? = null
    var tv_header: TextView? = null
    var txtReason: TextView? = null
    var textView5: TextView? = null
    var linCalender: LinearLayout? = null
    var linCalenderMain: LinearLayout? = null
    var linList: LinearLayout? = null
    var calendarView: CalendarView? = null

    var branchid: String? = null
    var arrayList1 = ArrayList<String>()
    public var arrayList2: ArrayList<Branchcode>? = null
    val TAG: String? = "HolidayListActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)
        setRegViews()
        getBranchlist()
        rv_holiday!!.visibility = View.GONE
    }

    private fun setRegViews() {

        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgCalender = findViewById<ImageView>(R.id.imgCalender)
        imgList = findViewById<ImageView>(R.id.imgList)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        textView5 = findViewById<TextView>(R.id.textView5)
        txtReason = findViewById<TextView>(R.id.txtReason)
        linCalender = findViewById<LinearLayout>(R.id.linCalender)
        linList = findViewById<LinearLayout>(R.id.linList)
        linCalenderMain = findViewById<LinearLayout>(R.id.linCalenderMain)
        tv_header = findViewById<TextView>(R.id.tv_header)
        calendarView = findViewById<CalendarView>(R.id.calendarView)
        val HeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF83, 0)
        tv_header!!.setText(HeaderSP.getString("HolidayList", null))


        val SlctbrnchSP = applicationContext.getSharedPreferences(Config.SHARED_PREF224, 0)
        textView5!!.setText(SlctbrnchSP.getString("SelectBranch", null))


        rv_holiday = findViewById(R.id.rv_holiday)
        spnBranch = findViewById(R.id.spnBranch)
        spnBranch!!.onItemSelectedListener = this
        linCalender!!.setOnClickListener(this)
        linList!!.setOnClickListener(this)
    }

    private fun getBranchlist() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                /*progressDialog = ProgressDialog(this@HolidayListActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
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
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
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
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("8"))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response 156", response.body().toString())
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
                                    Log.e(TAG, "Exception  1961   ")
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
                                Log.e(TAG, "Exception  1962   " + e.toString())

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
                            Log.e(TAG, "Exception  1963   " + t.message)
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
                    Log.e(TAG, "Exception  1964   " + e.toString())
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

    private fun setCalender() {
        val events: MutableList<EventDay> = ArrayList()
        val date = Date()
        val myFormat = SimpleDateFormat("dd-MM-yyyy")
        val inputString1 = myFormat.format(date);
        Log.v("dfddddddfsfds", "")
        for (i in 0 until jresult!!.length()) {
            val modelChild = jresult!!.getJSONObject(i)
            var date1 = myFormat.parse(inputString1)
            var date2 = myFormat.parse(modelChild.getString("HolidayDate"))
            val diff: Long = date2.getTime() - date1.getTime()
            val datee = diff / (1000 * 60 * 60 * 24)
            var day = datee.toInt()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, day);
            events.add(EventDay(calendar, R.drawable.round, Color.parseColor("#E91E1E")))
        }
        calendarView!!.setFirstDayOfWeek(CalendarWeekDay.SUNDAY)
        calendarView!!.setCalendarDayLayout(R.layout.custom_calendar_day_row)
        calendarView!!.setEvents(events)
        calendarView!!.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val clickedDayCalendar = eventDay.calendar.timeInMillis
                loadEvent(clickedDayCalendar)

            }

        })
    }

    private fun loadEvent(clickedDayCalendar: Long) {
        val myFormat = SimpleDateFormat("dd-MM-yyyy")
        val dateFromCalender = myFormat.format(clickedDayCalendar)
        Log.v("dsfsdfdfdrggggg", "dateFromCalender " + dateFromCalender)
        var reason = ""
        var haveReason = false;
        for (i in 0 until jresult!!.length()) {
            val modelChild = jresult!!.getJSONObject(i)
            var dateFromArray = modelChild.getString("HolidayDate")
            Log.v("dfsddddddd3ere", "dateFromArray " + dateFromArray)
            if (dateFromCalender.equals(dateFromArray)) {
                reason = modelChild.getString("HolidayReason")
                haveReason = true
                break
            } else {
                haveReason = false
            }

        }
        if (haveReason) {
            txtReason!!.visibility = View.VISIBLE
            txtReason!!.text = reason
        } else {
            txtReason!!.visibility = View.GONE
        }

    }

    private fun getHolidayList(branchid: String?) {
        rv_holiday!!.visibility = View.GONE
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@HolidayListActivity, R.style.Progress)
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
                    val client: OkHttpClient = okhttp3.OkHttpClient.Builder()
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
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("24"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BranchCode", MscoreApplication.encryptStart(branchid))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    //
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
                                        setCalender();
//                                        loadList()
                                        loadCalender()
                                    } else {
                                        if (rv_holiday!!.isShown()) {
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
                                        } else {

                                        }

                                    }


                                } else {
                                    if (rv_holiday!!.isShown()) {
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
                                if (rv_holiday!!.isShown()) {
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
                            if (rv_holiday!!.isShown()) {
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
                    if (rv_holiday!!.isShown()) {
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
        rv_holiday!!.visibility = View.VISIBLE
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
            R.id.linCalender -> {
                loadCalender()
            }
            R.id.linList -> {
                loadList()
            }
        }
    }

    fun loadCalender() {
        Log.v("fdsfdddd", "loadCalender")
        val date = Date()
        val myFormat = SimpleDateFormat("dd-MM-yyyy")
        val inputString1 = myFormat.format(date);
        loadEvent(date.time)
        rv_holiday!!.visibility = View.GONE
        linCalenderMain!!.visibility = View.VISIBLE
        ImageViewCompat.setImageTintList(
            imgCalender!!,
            ColorStateList.valueOf(resources.getColor(R.color.white))
        )
        ImageViewCompat.setImageTintList(
            imgList!!,
            ColorStateList.valueOf(resources.getColor(R.color.black))
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linList!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#b28599")))
            linCalender!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F4CBB6")))
        };
    }

    fun loadList() {
        rv_holiday!!.visibility = View.VISIBLE
        linCalenderMain!!.visibility = View.GONE
        ImageViewCompat.setImageTintList(
            imgList!!,
            ColorStateList.valueOf(resources.getColor(R.color.white))
        )
        ImageViewCompat.setImageTintList(
            imgCalender!!,
            ColorStateList.valueOf(resources.getColor(R.color.black))
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linCalender!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#b28599")))
            linList!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F4CBB6")))
        };
    }


}