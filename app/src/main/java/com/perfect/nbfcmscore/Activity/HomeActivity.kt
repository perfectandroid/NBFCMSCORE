package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.BannerAdapter
import com.perfect.nbfcmscore.Adapter.NavMenuAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import me.relex.circleindicator.CircleIndicator
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class HomeActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    var llloanapplication: LinearLayout? = null
    var lldashboard: LinearLayout? = null
    var llprdctdetail: LinearLayout? = null
    var llgoldslab: LinearLayout? = null
    var llmyaccounts: LinearLayout? = null
    var lvNavMenu: ListView? = null
    var drawer: DrawerLayout? = null
    var imgMenu: ImageView? = null
    var ll_branschDetails : LinearLayout?=null
    var ll_prepaid : LinearLayout?=null
    var ll_postpaid : LinearLayout?=null
    var ll_landline : LinearLayout?=null
    var ll_rechargehistory : LinearLayout?=null
    var ll_holidaylist : LinearLayout?=null
    var llownbank : LinearLayout?=null
    var ll_dth : LinearLayout?=null
    var llEmi : LinearLayout?=null
    var llnotif : LinearLayout?=null
    var llpassbook: LinearLayout? = null
    var llduereminder: LinearLayout? = null
    var ll_virtualcard: LinearLayout? = null
    var ll_otherbank: LinearLayout? = null
    var llquickbalance: LinearLayout? = null
    var llstatement: LinearLayout? = null
    var llquickpay: LinearLayout? = null
    var llprofile: LinearLayout? = null

    var tv_def_account: TextView? = null
    var tv_def_availablebal: TextView? = null
    var tv_lastlogin: TextView? = null

    var improfile: ImageView? = null
    var im_applogo: ImageView? = null
    var tv_header: TextView? = null
    var tvuser: TextView? = null
    var tv_mobile: TextView? = null
    var txtv_myacc: TextView? = null
    var txtv_pasbk: TextView? = null
    var txtv_quickbal: TextView? = null
    var txtvstatmnt: TextView? = null
    var txtv_dueremndr: TextView? = null
    var tv_viewall: TextView? = null

    private var mPager: ViewPager? = null
    private var indicator: CircleIndicator? = null
    private var currentPage = 0
   // private val XMEN = arrayOf<String>
   // private val XMEN = arrayOf<String>(R.drawable.ban1, R.drawable.ban2, R.drawable.ban3, R.drawable.ban4)
    public val XMENArray = ArrayList<String>()
    var XMEN = intArrayOf(0)
    var jArrayAccount: JSONArray? = null

    private var jresult: JSONArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        setInitialise()
        setRegister()
        setHomeNavMenu()
        init()
        versioncheck()

        setdefaultAccountDetails()

        val ID_MyaccSP = applicationContext.getSharedPreferences(Config.SHARED_PREF50,0)
        val ID_PassbkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF51,0)
        val ID_QuickbalSP = applicationContext.getSharedPreferences(Config.SHARED_PREF52,0)
        val ID_DueremindSP = applicationContext.getSharedPreferences(Config.SHARED_PREF53,0)
        val ID_StatmntSP = applicationContext.getSharedPreferences(Config.SHARED_PREF59,0)


        txtv_myacc!!.setText(ID_MyaccSP.getString("Myaccounts",null))
        txtv_pasbk!!.setText(ID_PassbkSP.getString("passbook",null))
        txtv_quickbal!!.setText(ID_QuickbalSP.getString("quickbalance",null))
        txtv_dueremndr!!.setText(ID_DueremindSP.getString("duereminder",null))
        txtvstatmnt!!.setText(ID_StatmntSP.getString("statement",null))


        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12, 0)
        try {
            val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString(
                    "AppIconImageCode",
                    null
            )
            Log.e("TAG","imagepath  116   "+imagepath)
            //PicassoTrustAll.getInstance(this)!!.load(imagepath).error(null).into(im_applogo)
            PicassoTrustAll.getInstance(this@HomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(im_applogo!!)

        }catch (e: Exception) {
            e.printStackTrace()}
        tv_header!!.setText(ProductNameSP.getString("ProductName", null))

        val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF3, 0)
        tvuser!!.setText(CustomerNameSP.getString("CustomerName", null))
        val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2, 0)
        tv_mobile!!.setText(CusMobileSP.getString("CusMobile", null))

    }

    private fun versioncheck() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                 progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                 progressDialog!!.setCancelable(false)
                 progressDialog!!.setIndeterminate(true)
                 progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                 progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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
                        val versionNumber = getCurrentVersionNumber(this@HomeActivity)
                        // requestObject1.put("Reqmode", MscoreApplication.encryptStart("42"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "VersionNo",
                                MscoreApplication.encryptStart(versionNumber.toString())
                        )
                        requestObject1.put(
                                "OsType",
                                MscoreApplication.encryptStart("0")
                        )

                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1 versionchk   " + requestObject1)
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
                    val call = apiService.getVersioncheck(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-Versioncheck", response.body())

                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("VersionCheck")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    var status = jsonobj2.getString("Status")
                                    if (status.equals("10")) {
                                        goToPlayStore()
                                    } else {

                                    }


                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            //  progressDialog!!.dismiss()

                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    // progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(
                            this@HomeActivity,
                            R.style.MyDialogTheme
                    )
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(
                        this@HomeActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun goToPlayStore() {
        try {
//            String url = getResources().getString(R.string.app_link );
            val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
            val url = pref.getString("PlayStoreLink", null)
            URL(url)
        } catch (e: MalformedURLException) {
            val alertDialogBuilder = AlertDialog.Builder(this@HomeActivity)
            alertDialogBuilder.setMessage("The app is under maintenance. Sorry for the inconvenience.")
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setPositiveButton("Ok") { dialog: DialogInterface?, which: Int -> finish() }
            alertDialogBuilder.show()
            return
        }
        val dialogBuilder = android.app.AlertDialog.Builder(this@HomeActivity)
        val inflater: LayoutInflater = this@HomeActivity.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.alert_layout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
        val tv_msg = dialogView.findViewById<TextView>(R.id.txt1)
        val tv_msg2 = dialogView.findViewById<TextView>(R.id.txt2)
        tv_msg.text = "New Version Available"
        tv_msg2.text = "New version of this application is available.\n" +
                "Click OK to upgrade now"
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.setOnClickListener { alertDialog.dismiss() }
        tv_share.setOnClickListener { //  finishAffinity();

            val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF11, 0)
            val url = pref.getString("PlayStoreLink", null)
            Log.i("URL",url.toString())
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
            alertDialog.dismiss()
        }

        alertDialog.show()


    }


    private fun setdefaultAccountDetails() {


        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24, 0)
        val DefaultBalanceSP = applicationContext.getSharedPreferences(Config.SHARED_PREF27, 0)
        val LastLoginTimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF29, 0)

        tv_lastlogin!!.setText("Last Login : " + LastLoginTimeSP.getString("LastLoginTime", null))

        if (DefaultAccountSP.getString("DefaultAccount", null) == null){
            tv_def_account!!.setText("")
            tv_def_availablebal!!.setText("")
            getOwnAccount()

        }else{
            tv_def_account!!.setText(DefaultAccountSP.getString("DefaultAccount", null))
            val balance = DefaultBalanceSP.getString("DefaultBalance", null)!!.toDouble()
            tv_def_availablebal!!.setText("Rs. " + Config.getDecimelFormate(balance))
        }

    }

    private fun init() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /* progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("42"))
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
                    val call = apiService.getBannerdetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-Banner", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("BannerDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("BannerDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            var s = "https://202.164.150.65:14262/NbfcAndroidAPI" + json.getString("ImagePath")


                                            XMENArray!!.add(s)


                                            mPager!!.adapter = BannerAdapter(
                                                    this@HomeActivity,
                                                    XMENArray
                                            )
                                            indicator!!.setViewPager(mPager)
                                            val handler = Handler()
                                            val Update = Runnable {
                                                if (currentPage === jresult!!.length()) {
                                                    currentPage = 0
                                                }
                                                mPager!!.setCurrentItem(currentPage++, true)
                                            }
                                            val swipeTimer = Timer()
                                            swipeTimer.schedule(object : TimerTask() {
                                                override fun run() {
                                                    handler.post(Update)
                                                }
                                            }, 3000, 3000)
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }


                                } else {
                                    val builder = android.app.AlertDialog.Builder(
                                            this@HomeActivity,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: android.app.AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = android.app.AlertDialog.Builder(
                                        this@HomeActivity,
                                        R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: android.app.AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            //  progressDialog!!.dismiss()

                            val builder = android.app.AlertDialog.Builder(
                                    this@HomeActivity,
                                    R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: android.app.AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    // progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(
                            this@HomeActivity,
                            R.style.MyDialogTheme
                    )
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: android.app.AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {
                val builder = android.app.AlertDialog.Builder(
                        this@HomeActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
       /* for (i in 0 until 4)
            XMENArray.add(XMEN[i])
        mPager!!.adapter = BannerAdapter(this, XMENArray)
        indicator!!.setViewPager(mPager)
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === 4) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)*/
    }

    open fun setInitialise() {


        im_applogo = findViewById(R.id.im_applogo)


        tv_mobile = findViewById(R.id.tv_mobile)
        tvuser = findViewById(R.id.tvuser)
        llprofile = findViewById(R.id.llprofile)
        improfile = findViewById(R.id.improfile)
        tv_header = findViewById(R.id.tv_header)
        llloanapplication = findViewById(R.id.llloanapplication)
        lldashboard = findViewById(R.id.lldashboard)
        llprdctdetail = findViewById(R.id.llprdctdetail)
        llmyaccounts = findViewById(R.id.llmyaccounts)
        imgMenu = findViewById(R.id.imgMenu)
        drawer = findViewById(R.id.drawer_layout)
        lvNavMenu = findViewById(R.id.lvNavMenu)
        ll_branschDetails = findViewById(R.id.ll_branschDetails)
        llownbank = findViewById(R.id.llownbank)
        ll_holidaylist= findViewById(R.id.ll_holidaylist)
        ll_prepaid = findViewById(R.id.ll_prepaid)
        ll_postpaid = findViewById(R.id.ll_postpaid)
        ll_landline = findViewById(R.id.ll_landline)
        ll_rechargehistory = findViewById(R.id.ll_rechargehistory)


         txtv_myacc= findViewById(R.id.txtv_myacc)
         txtv_pasbk= findViewById(R.id.txtv_pasbk)
         txtv_quickbal= findViewById(R.id.txtv_quickbal)
         txtvstatmnt= findViewById(R.id.txtvstatmnt)
        txtv_dueremndr= findViewById(R.id.txtv_dueremndr)

        llnotif= findViewById(R.id.llnotif)

        ll_dth = findViewById(R.id.ll_dth)
        llEmi = findViewById(R.id.llEmi)
        llpassbook = findViewById<LinearLayout>(R.id.llpassbook)
        llduereminder = findViewById<LinearLayout>(R.id.lldueremindrer)
        llgoldslab = findViewById<LinearLayout>(R.id.llgoldslab)
        ll_virtualcard = findViewById<LinearLayout>(R.id.ll_virtualcard)
        ll_otherbank = findViewById<LinearLayout>(R.id.ll_otherbank)
        llquickbalance = findViewById<LinearLayout>(R.id.llquickbalance)
        llstatement = findViewById<LinearLayout>(R.id.llstatement)
        llquickpay = findViewById<LinearLayout>(R.id.llquickpay)

        tv_def_account = findViewById<TextView>(R.id.tv_def_account)
        tv_def_availablebal = findViewById<TextView>(R.id.tv_def_availablebal)
        tv_lastlogin = findViewById<TextView>(R.id.tv_lastlogin)
        tv_viewall = findViewById<TextView>(R.id.tv_viewall)

    }

    open fun setRegister() {
        improfile!!.setOnClickListener(this)
        lldashboard!!.setOnClickListener(this)
        llprdctdetail!!.setOnClickListener(this)
        llgoldslab!!.setOnClickListener(this)
        llquickbalance!!.setOnClickListener(this)
        llmyaccounts!!.setOnClickListener(this)
        imgMenu!!.setOnClickListener(this)
        mPager = findViewById(R.id.pager)
        indicator =findViewById(R.id.indicator)
        llownbank!!.setOnClickListener(this)
        llpassbook!!.setOnClickListener(this)
        llduereminder!!.setOnClickListener(this)
        ll_holidaylist!!.setOnClickListener(this)
        ll_branschDetails!!.setOnClickListener(this)
        ll_prepaid!!.setOnClickListener(this)
        ll_postpaid!!.setOnClickListener(this)
        ll_landline!!.setOnClickListener(this)
        ll_rechargehistory!!.setOnClickListener(this)
        ll_dth!!.setOnClickListener(this)
        llEmi!!.setOnClickListener(this)
        ll_virtualcard!!.setOnClickListener(this)
        ll_otherbank!!.setOnClickListener(this)
        llloanapplication!!.setOnClickListener(this)
        llprofile!!.setOnClickListener(this)
        llquickpay!!.setOnClickListener(this)
        llnotif!!.setOnClickListener(this)
        llstatement!!.setOnClickListener(this)
        tv_viewall!!.setOnClickListener(this)
    }

    open fun setHomeNavMenu() {
        val ID_AbtusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF54,0)
        val ID_ContactSP = applicationContext.getSharedPreferences(Config.SHARED_PREF55,0)
        val ID_FeebkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF56,0)
        val ID_PrivacySP = applicationContext.getSharedPreferences(Config.SHARED_PREF57,0)
        val ID_TermsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF58,0)
        val ID_SetngsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF60,0)
        val ID_LogoutSP = applicationContext.getSharedPreferences(Config.SHARED_PREF61,0)


        var abt =ID_AbtusSP.getString("aboutus",null)
        var cntct =ID_ContactSP.getString("contactus",null)
        var feebk =ID_FeebkSP.getString("feedback",null)
        var privacy =ID_PrivacySP.getString("privacypolicy",null)
        var terms =ID_TermsSP.getString("termsandconditions",null)
        var setngs =ID_SetngsSP.getString("settings",null)
        var logout =ID_LogoutSP.getString("logout",null)

        val menulist= arrayOf(abt,cntct,feebk,privacy,terms,setngs,logout,"Quit")
      /*  val menulist = arrayOf(
                "About Us",
                "Contact Us",
                "Feedback",
                "Privacy Policies",
                "Terms & Conditions",
                "Settings",
                "Logout",
                "Quit"
        )*/
        val imageId = arrayOf<Int>(
                R.drawable.aboutnav, R.drawable.contnav,
                R.drawable.feedbacknav, R.drawable.ppnav, R.drawable.tncnav, R.drawable.ic_settings,
                R.drawable.logoutnav, R.drawable.exitnav
        )
        val adapter = NavMenuAdapter(this@HomeActivity, menulist, imageId)
        lvNavMenu!!.adapter = adapter
        lvNavMenu!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            } else if (position == 1) {
                startActivity(Intent(this@HomeActivity, ContactUsActivity::class.java))
            } else if (position == 2) {
                //feedback
            } else if (position == 3) {
                startActivity(Intent(this@HomeActivity, PrivacyPolicyActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }else if (position == 4) {
                startActivity(Intent(this@HomeActivity, TermsnconditionsActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }else if (position == 5) {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 6) {
                try {

                    val dialogBuilder = android.app.AlertDialog.Builder(this@HomeActivity)
                    val inflater: LayoutInflater = this@HomeActivity.getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.logout_popup, null)
                    dialogBuilder.setView(dialogView)
                    val alertDialog = dialogBuilder.create()
                    val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
                    val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)


                    tv_cancel.setOnClickListener { alertDialog.dismiss() }
                    tv_share.setOnClickListener {
                        alertDialog.dismiss()
                        logout()
                        val intent = Intent(this, WelcomeActivity::class.java)
                        intent.putExtra("from", "true")
                        this.startActivity(intent)
                        this.finish()
                    }
                    alertDialog.show()





                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            else if (position == 7) {
                quit()
            }

        }
    }

    private fun quit() {
        try {
            val dialog1 = Dialog(this)
            dialog1 .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog1 .setCancelable(false)
            dialog1 .setContentView(R.layout.quit_popup)
            val btn_Yes = dialog1.findViewById(R.id.tv_share) as TextView
            val btn_cancel = dialog1.findViewById(R.id.tv_cancel) as TextView
            val imglogo = dialog1.findViewById(R.id.imglogo) as ImageView

            val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
            try {
                val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
                PicassoTrustAll.getInstance(this@HomeActivity)!!.load(imagepath).error(android.R.color.transparent).into(imglogo!!)
            }catch (e: Exception) {
                e.printStackTrace()
            }

            btn_cancel.setOnClickListener {
                dialog1 .dismiss()
            }
            btn_Yes.setOnClickListener {
                dialog1.dismiss()
                finish()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity()
                }
            }
            dialog1.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logout() {
        val loginSP = this!!.getSharedPreferences(Config.SHARED_PREF, 0)
        val loginEditer = loginSP.edit()
        loginEditer.putString("loginsession", "No")
        loginEditer.commit()


        val FK_CustomerSP = this!!.getSharedPreferences(Config.SHARED_PREF1, 0)
        val FK_CustomerEditer = FK_CustomerSP.edit()
        FK_CustomerEditer.putString("FK_Customer", "")
        FK_CustomerEditer.commit()

        val FK_CustomerMobSp = this!!.getSharedPreferences(Config.SHARED_PREF2, 0)
        val FK_CustomerMobEditer = FK_CustomerMobSp.edit()
        FK_CustomerMobEditer.putString("CusMobile", "")
        FK_CustomerMobEditer.commit()

        val CustomerNameSP = this!!.getSharedPreferences(Config.SHARED_PREF3, 0)
        val CustomerNameEditer = CustomerNameSP.edit()
        CustomerNameEditer.putString("CustomerName", "")
        CustomerNameEditer.commit()

        val CustomerAddressSP = this!!.getSharedPreferences(Config.SHARED_PREF4, 0)
        val CustomerAddressEditer = CustomerAddressSP.edit()
        CustomerAddressEditer.putString("Address", "")
        CustomerAddressEditer.commit()

        val CustomerEmailSP = this!!.getSharedPreferences(Config.SHARED_PREF5, 0)
        val CustomerEmailEditer = CustomerEmailSP.edit()
        CustomerEmailEditer.putString("Email", "")
        CustomerEmailEditer.commit()

        val CustomerGenderSP = this!!.getSharedPreferences(Config.SHARED_PREF6, 0)
        val CustomerGenderEditer = CustomerGenderSP.edit()
        CustomerGenderEditer.putString("Gender", "")
        CustomerGenderEditer.commit()

        val CustomerDobSP = this!!.getSharedPreferences(Config.SHARED_PREF7, 0)
        val CustomerDobEditer = CustomerDobSP.edit()
        CustomerDobEditer.putString("DateOfBirth", "")
        CustomerDobEditer.commit()

        val TokenSP = this!!.getSharedPreferences(Config.SHARED_PREF8, 0)
        val TokenEditer = TokenSP.edit()
        TokenEditer.putString("Token", "")
        TokenEditer.commit()

        val AppstoreSP = this!!.getSharedPreferences(Config.SHARED_PREF10, 0)
        val AppstoreEditer = AppstoreSP.edit()
        AppstoreEditer.putString("AppStoreLink", "")
        AppstoreEditer.commit()


        val ID_PlaystoreSP = this!!.getSharedPreferences(Config.SHARED_PREF11, 0)
        val ID_PlaystoreEditer = ID_PlaystoreSP.edit()
        ID_PlaystoreEditer.putString("PlayStoreLink", "")
        ID_PlaystoreEditer.commit()

        val FKAccountSP = this!!.getSharedPreferences(Config.SHARED_PREF16, 0)
        val FKAccountEditer = FKAccountSP.edit()
        FKAccountEditer.putString("FK_Account", "")
        FKAccountEditer.commit()

        val SubmoduleeSP = this!!.getSharedPreferences(Config.SHARED_PREF17, 0)
        val SubmoduleEditer = SubmoduleeSP.edit()
        SubmoduleEditer.putString("SubModule", "")
        SubmoduleEditer.commit()

        val StatusSP = this!!.getSharedPreferences(Config.SHARED_PREF18, 0)
        val StatusEditer = StatusSP.edit()
        StatusEditer.putString("Status", "")
        StatusEditer.commit()

        val CustnoSP = this!!.getSharedPreferences(Config.SHARED_PREF19, 0)
        val CustnoEditer = CustnoSP.edit()
        CustnoEditer.putString("CustomerNumber", "")
        CustnoEditer.commit()

        val Custno1SP = this!!.getSharedPreferences(Config.SHARED_PREF20, 0)
        val Custno1Editer = Custno1SP.edit()
        Custno1Editer.putString("CustomerNumber", "")
        Custno1Editer.commit()

        val LastloginSP = this!!.getSharedPreferences(Config.SHARED_PREF29, 0)
        val LastloginEditer = LastloginSP.edit()
        LastloginEditer.putString("LastLoginTime", "")
        LastloginEditer.commit()



    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgMenu ->
                drawer!!.openDrawer(Gravity.START)
            R.id.lldashboard -> {
                startActivity(Intent(this@HomeActivity, DashboardActivity::class.java))
            }
            R.id.llprdctdetail -> {

                startActivity(Intent(this@HomeActivity, ProductListActivity::class.java))
            }
            R.id.llmyaccounts -> {
                startActivity(Intent(this@HomeActivity, AccountlistActivity::class.java))
            }
            R.id.ll_branschDetails -> {
                startActivity(Intent(this@HomeActivity, BranchDetailActivity::class.java))
            }

            R.id.ll_holidaylist -> {
                startActivity(Intent(this@HomeActivity, HolidayListActivity::class.java))
            }

            R.id.llEmi -> {
                startActivity(Intent(this@HomeActivity, EMIActivity::class.java))
            }
            R.id.lldueremindrer -> {
                startActivity(Intent(this@HomeActivity, DueReminderActivity::class.java))
            }
            R.id.improfile -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            R.id.llprofile -> {
                startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            }
            R.id.llpassbook -> {
                startActivity(Intent(this@HomeActivity, PassbookActivity::class.java))
            }
            R.id.llquickbalance -> {
                startActivity(Intent(this@HomeActivity, QuickBalanceActivity::class.java))
            }
            R.id.llownbank -> {
                startActivity(
                        Intent(
                                this@HomeActivity,
                                OwnBankFundTransferServiceActivity::class.java
                        )
                )
            }
            R.id.llgoldslab -> {
                startActivity(Intent(this@HomeActivity, GoldSlabEstimatorActivity::class.java))
            }
            R.id.llloanapplication -> {
                startActivity(Intent(this@HomeActivity, LoanApplicationActivity::class.java))
            }
            R.id.ll_prepaid -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "prepaid")
                startActivity(intent)
            }
            R.id.ll_postpaid -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "postpaid")
                startActivity(intent)
            }
            R.id.llnotif -> {

                var intent = Intent(this@HomeActivity, DuedateActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_landline -> {

//                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
//                intent.putExtra("from", "landline")
//                startActivity(intent)
            }
            R.id.ll_dth -> {

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "dth")
                startActivity(intent)
            }

            R.id.ll_virtualcard -> {

                var intent = Intent(this@HomeActivity, VirtualActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_otherbank -> {

                var intent = Intent(this@HomeActivity, OtherBankActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_rechargehistory -> {

                var intent = Intent(this@HomeActivity, RechargeHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.llstatement -> {

                var intent = Intent(this@HomeActivity, StatementActivity::class.java)
                startActivity(intent)
            }
            R.id.llquickpay -> {

                var intent = Intent(this@HomeActivity, QuickPayActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_viewall -> {

                startActivity(Intent(this@HomeActivity, AccountlistActivity::class.java))
            }
        }
    }

    private fun getOwnAccount() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {

                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@HomeActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
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
                    val call = apiService.getOwnAccounDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {

                                val jObject = JSONObject(response.body())

                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    val accountItems: ArrayList<String> = ArrayList()
                                    for (i in 0 until jArrayAccount!!.length()) {
                                        val obj: JSONObject = jArrayAccount!!.getJSONObject(i)
                                        accountItems.add(obj.getString("AccountNumber"));
                                        val DefaultAccountSP =
                                                applicationContext.getSharedPreferences(
                                                        Config.SHARED_PREF24,
                                                        0
                                                )
                                        if (DefaultAccountSP.getString(
                                                        "DefaultAccount",
                                                        null
                                                ) == null
                                        ) {
                                            if (i == 0) {

                                                val balance = obj.getString("Balance").toDouble()
                                                tv_def_availablebal!!.setText(
                                                        "Rs. " + Config.getDecimelFormate(
                                                                balance
                                                        )
                                                )
                                                tv_def_account!!.setText(obj.getString("AccountNumber"))

                                            }
                                        }
                                    }

                                } else {

                                }
                            } catch (e: Exception) {

                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {

                        }
                    })
                } catch (e: Exception) {

                }
            }
            false -> {

                val builder = AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
    private fun getCurrentVersionNumber(context: Context): Int {
        try {
            return context.packageManager
                .getPackageInfo(context.packageName, 0)!!.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            // Do nothing
        }
        return 1
    }

    override fun onBackPressed() {

        quit()

    }
}