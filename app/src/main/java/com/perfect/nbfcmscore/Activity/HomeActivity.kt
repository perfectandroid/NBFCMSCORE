package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.Activity
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
import com.perfect.nbfcmscore.Adapter.BannerAdapter1
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
    var llDeposit:LinearLayout?=null
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
    var llexecutive: LinearLayout? = null
    var llenquiry: LinearLayout? = null

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
    var txtvnotif: TextView? = null
    var txtv_ownbnk: TextView? = null
    var txtv_othrbnk: TextView? = null
    var txtv_quickpay: TextView? = null
    var txtv_prepaid: TextView? = null
    var txtv_pospaid: TextView? = null
    var txtv_landline: TextView? = null
    var txtv_dth: TextView? = null
    var txtv_datacrd: TextView? = null
    var txtv_Kseb: TextView? = null
    var txtv_history: TextView? = null
    var txtv_dashbrd: TextView? = null
    var txtv_virtual: TextView? = null
    var txtv_branch: TextView? = null
    var txtv_loanaplctn: TextView? = null
    var txtv_loanstats: TextView? = null
    var txtv_prdctdetail: TextView? = null
    var tv_viewall: TextView? = null
    var txtv_emi: TextView? = null
    var txtv_deposit: TextView? = null
    var txtv_goldloan: TextView? = null
    var txtv_enqry: TextView? = null
    var txtv_holidy: TextView? = null
    var txtv_exectve: TextView? = null


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
        val ID_NotifSP = applicationContext.getSharedPreferences(Config.SHARED_PREF62,0)
        val ID_OwnBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF63,0)
        val ID_OtherBankSP = applicationContext.getSharedPreferences(Config.SHARED_PREF64,0)
        val ID_QuickpaySP = applicationContext.getSharedPreferences(Config.SHARED_PREF65,0)

        val ID_Prepaid = applicationContext.getSharedPreferences(Config.SHARED_PREF66,0)
        val ID_Postpaid = applicationContext.getSharedPreferences(Config.SHARED_PREF67,0)
        val ID_Landline = applicationContext.getSharedPreferences(Config.SHARED_PREF68,0)
        val ID_DTH = applicationContext.getSharedPreferences(Config.SHARED_PREF69,0)
        val ID_Datacrdpay = applicationContext.getSharedPreferences(Config.SHARED_PREF70,0)
        val ID_KSEB = applicationContext.getSharedPreferences(Config.SHARED_PREF71,0)
        val ID_Histry = applicationContext.getSharedPreferences(Config.SHARED_PREF72,0)

        val ID_Dashbrd = applicationContext.getSharedPreferences(Config.SHARED_PREF73,0)
        val ID_Virtualcrd = applicationContext.getSharedPreferences(Config.SHARED_PREF74,0)
        val ID_Branch = applicationContext.getSharedPreferences(Config.SHARED_PREF75,0)
        val ID_Loanapplictn = applicationContext.getSharedPreferences(Config.SHARED_PREF76,0)
        val ID_Loanstatus = applicationContext.getSharedPreferences(Config.SHARED_PREF77,0)
        val ID_PrdctDetail = applicationContext.getSharedPreferences(Config.SHARED_PREF78,0)

        val ID_Emi = applicationContext.getSharedPreferences(Config.SHARED_PREF79,0)
        val ID_Deposit = applicationContext.getSharedPreferences(Config.SHARED_PREF80,0)
        val ID_Goldloan = applicationContext.getSharedPreferences(Config.SHARED_PREF81,0)
        val ID_Enqry = applicationContext.getSharedPreferences(Config.SHARED_PREF82,0)
        val ID_Holidy = applicationContext.getSharedPreferences(Config.SHARED_PREF83,0)
        val ID_Executve = applicationContext.getSharedPreferences(Config.SHARED_PREF84,0)





        txtv_myacc!!.setText(ID_MyaccSP.getString("Myaccounts",null))
        txtv_pasbk!!.setText(ID_PassbkSP.getString("passbook",null))
        txtv_quickbal!!.setText(ID_QuickbalSP.getString("quickbalance",null))
        txtv_dueremndr!!.setText(ID_DueremindSP.getString("duereminder",null))
        txtvstatmnt!!.setText(ID_StatmntSP.getString("statement",null))
        txtvnotif!!.setText(ID_NotifSP.getString("NotificationandMessages",null))
        txtv_ownbnk!!.setText(ID_OwnBankSP.getString("OwnBank",null))
        txtv_othrbnk!!.setText(ID_OtherBankSP.getString("OtherBank",null))
        txtv_quickpay!!.setText(ID_QuickpaySP.getString("QuickPay",null))

        txtv_prepaid!!.setText(ID_Prepaid.getString("PrepaidMobile",null))
        txtv_pospaid!!.setText(ID_Postpaid.getString("PostpaidMobile",null))
        txtv_landline!!.setText(ID_Landline.getString("Landline",null))
        txtv_dth!!.setText(ID_DTH.getString("DTH",null))
        txtv_datacrd!!.setText(ID_Datacrdpay.getString("DataCard",null))
        txtv_Kseb!!.setText(ID_KSEB.getString("KSEB",null))
        txtv_history!!.setText(ID_Histry.getString("History",null))

        txtv_dashbrd!!.setText(ID_Dashbrd.getString("Dashboard",null))
        txtv_virtual!!.setText(ID_Virtualcrd.getString("VirtualCard",null))
        txtv_branch!!.setText(ID_Branch.getString("BranchDetails",null))
        txtv_loanaplctn!!.setText(ID_Loanapplictn.getString("LoanApplication",null))
        txtv_loanstats!!.setText(ID_Loanstatus.getString("LoanStatus",null))
        txtv_prdctdetail!!.setText(ID_PrdctDetail.getString("ProductDetails",null))

        txtv_emi!!.setText(ID_Emi.getString("EMICalculator",null))
        txtv_deposit!!.setText(ID_Deposit.getString("DepositCalculator",null))
        txtv_goldloan!!.setText(ID_Goldloan.getString("GoldLoanEligibileCalculator",null))
        txtv_enqry!!.setText(ID_Enqry.getString("Enquires",null))
        txtv_holidy!!.setText(ID_Holidy.getString("HolidayList",null))
        txtv_exectve!!.setText(ID_Executve.getString("ExecutiveCallBack",null))




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


                                            mPager!!.adapter = BannerAdapter1(
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
        llDeposit = findViewById(R.id.llDeposit)
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
        llexecutive = findViewById(R.id.llexecutive)

         txtv_myacc= findViewById(R.id.txtv_myacc)
         txtv_pasbk= findViewById(R.id.txtv_pasbk)
         txtv_quickbal= findViewById(R.id.txtv_quickbal)
         txtvstatmnt= findViewById(R.id.txtvstatmnt)
        txtv_dueremndr= findViewById(R.id.txtv_dueremndr)
        txtvnotif= findViewById(R.id.txtvnotif)

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
        llenquiry = findViewById<LinearLayout>(R.id.llenquiry)

        tv_def_account = findViewById<TextView>(R.id.tv_def_account)
        tv_def_availablebal = findViewById<TextView>(R.id.tv_def_availablebal)
        tv_lastlogin = findViewById<TextView>(R.id.tv_lastlogin)
        tv_viewall = findViewById<TextView>(R.id.tv_viewall)

        txtv_ownbnk = findViewById<TextView>(R.id.txtv_ownbnk)
        txtv_othrbnk = findViewById<TextView>(R.id.txtv_othrbnk)
        txtv_quickpay = findViewById<TextView>(R.id.txtv_quickpay)

        txtv_prepaid = findViewById<TextView>(R.id.txtv_prepaid)
        txtv_pospaid = findViewById<TextView>(R.id.txtv_pospaid)
        txtv_landline = findViewById<TextView>(R.id.txtv_landline)
        txtv_dth = findViewById<TextView>(R.id.txtv_dth)
        txtv_datacrd = findViewById<TextView>(R.id.txtv_datacrd)
        txtv_Kseb = findViewById<TextView>(R.id.txtv_Kseb)
        txtv_history = findViewById<TextView>(R.id.txtv_history)

        txtv_dashbrd = findViewById<TextView>(R.id.txtv_dashbrd)
        txtv_virtual = findViewById<TextView>(R.id.txtv_virtual)
        txtv_branch = findViewById<TextView>(R.id.txtv_branch)
        txtv_loanaplctn = findViewById<TextView>(R.id.txtv_loanaplctn)
        txtv_loanstats = findViewById<TextView>(R.id.txtv_loanstats)
        txtv_prdctdetail = findViewById<TextView>(R.id.txtv_prdctdetail)

        txtv_emi = findViewById<TextView>(R.id.txtv_emi)
        txtv_deposit = findViewById<TextView>(R.id.txtv_deposit)
        txtv_goldloan = findViewById<TextView>(R.id.txtv_goldloan)
        txtv_enqry = findViewById<TextView>(R.id.txtv_enqry)
        txtv_holidy = findViewById<TextView>(R.id.txtv_holidy)
        txtv_exectve = findViewById<TextView>(R.id.txtv_exectve)



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
        llenquiry!!.setOnClickListener(this)
        llprofile!!.setOnClickListener(this)
        llquickpay!!.setOnClickListener(this)
        llnotif!!.setOnClickListener(this)
        llstatement!!.setOnClickListener(this)
        tv_viewall!!.setOnClickListener(this)
        llDeposit!!.setOnClickListener(this)
        llexecutive!!.setOnClickListener(this)
    }

    open fun setHomeNavMenu() {
        val ID_AbtusSP = applicationContext.getSharedPreferences(Config.SHARED_PREF54,0)
        val ID_ContactSP = applicationContext.getSharedPreferences(Config.SHARED_PREF55,0)
        val ID_FeebkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF56,0)
        val ID_PrivacySP = applicationContext.getSharedPreferences(Config.SHARED_PREF57,0)
        val ID_TermsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF58,0)
        val ID_SetngsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF60,0)
        val ID_LogoutSP = applicationContext.getSharedPreferences(Config.SHARED_PREF61,0)
        val ID_Quit = applicationContext.getSharedPreferences(Config.SHARED_PREF106,0)


        var abt =ID_AbtusSP.getString("aboutus",null)
        var cntct =ID_ContactSP.getString("contactus",null)
        var feebk =ID_FeebkSP.getString("feedback",null)
        var privacy =ID_PrivacySP.getString("privacypolicy",null)
        var terms =ID_TermsSP.getString("termsandconditions",null)
        var setngs =ID_SetngsSP.getString("settings",null)
        var logout =ID_LogoutSP.getString("logout",null)
        var quit =ID_Quit.getString("quit",null)

        val menulist= arrayOf(abt,cntct,feebk,privacy,terms,setngs,"Language",logout,quit)
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
                R.drawable.feedbacknav, R.drawable.ppnav, R.drawable.tncnav, R.drawable.ic_settings,R.drawable.ic_settings,
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
                startActivity(Intent(this@HomeActivity, FeedbackActivity::class.java))
            } else if (position == 3) {
                startActivity(Intent(this@HomeActivity, PrivacyPolicyActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }else if (position == 4) {
                startActivity(Intent(this@HomeActivity, TermsnconditionsActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 5) {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
               /* val ID_lan = applicationContext.getSharedPreferences(Config.SHARED_PREF9,0)
                var lanid =ID_lan.getString("ID_Languages", null)
                getLabels(lanid)*/
            }
            else if (position == 6) {
              //  startActivity(Intent(this@HomeActivity, LanguageSelectionActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 7) {
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
            else if (position == 8) {
                quit()
            }

        }
    }

    private fun getLabels(lanid: String?) {

        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@LanguageSelectionActivity, R.style.Progress)
                   progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                   progressDialog!!.setCancelable(false)
                   progressDialog!!.setIndeterminate(true)
                   progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                   progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this))
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

                        val FK_CustomerSP = this.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("16"))
                        // requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        //  requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put(
                                "FK_Languages",
                                MscoreApplication.encryptStart(lanid)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                this.getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  labels   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mSnackbar = Snackbar.make((this as Activity).findViewById(android.R.id.content), "Some technical issues.", Snackbar.LENGTH_INDEFINITE)
                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
                            requestObject1.toString()
                    )
                    val call = apiService.getLabels(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                // progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-labels", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("LabelDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    val jresult3 = jsonobj2.getJSONObject("Labels")
                                    //var welcome = jresult3.get("fasterwaytohelpyou") as String

                                    // Log.i("Resultsjson", welcome)

                                    val ID_WelcomeSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF34, 0)
                                    val ID_WelcomeSPEditer = ID_WelcomeSP.edit()
                                    ID_WelcomeSPEditer.putString("welcome", jresult3.get("welcome") as String)
                                    ID_WelcomeSPEditer.commit()


                                    val ID_FasterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF35, 0)
                                    val ID_FasterSPEditer = ID_FasterSP.edit()
                                    ID_FasterSPEditer.putString("fasterwaytohelpyou", jresult3.get("fasterwaytohelpyou") as String)
                                    ID_FasterSPEditer.commit()

                                    val ID_SigninSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF36, 0)
                                    val ID_SigninSPEditer = ID_SigninSP.edit()
                                    ID_SigninSPEditer.putString("sigin", jresult3.get("sigin") as String)
                                    ID_SigninSPEditer.commit()

                                    val ID_RegisterSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF37, 0)
                                    val ID_RegisterSPEditer = ID_RegisterSP.edit()
                                    ID_RegisterSPEditer.putString("registernow", jresult3.get("registernow") as String)
                                    ID_RegisterSPEditer.commit()

                                    val ID_SelctlanSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF38, 0)
                                    val ID_SelctlanSPEditer = ID_SelctlanSP.edit()
                                    ID_SelctlanSPEditer.putString("SelectLanguage", jresult3.get("SelectLanguage") as String)
                                    ID_SelctlanSPEditer.commit()

                                    val ID_SkipSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF39, 0)
                                    val ID_SkipSPEditer = ID_SkipSP.edit()
                                    ID_SkipSPEditer.putString("Skip", jresult3.get("Skip") as String)
                                    ID_SkipSPEditer.commit()


                                    val ID_LetsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF40, 0)
                                    val ID_LetsSPEditer = ID_LetsSP.edit()
                                    ID_LetsSPEditer.putString("Let'sgetstarted", jresult3.get("Let'sgetstarted") as String)
                                    ID_LetsSPEditer.commit()

                                    val ID_PersnlinfSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF41, 0)
                                    val ID_PersnlinfEditer = ID_PersnlinfSP.edit()
                                    ID_PersnlinfEditer.putString("pleaseenteryourpersonalinformation", jresult3.get("pleaseenteryourpersonalinformation") as String)
                                    ID_PersnlinfEditer.commit()

                                    val ID_EntermobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF42, 0)
                                    val ID_EntermobEditer = ID_EntermobSP.edit()
                                    ID_EntermobEditer.putString("entermobilenumber", jresult3.get("entermobilenumber") as String)
                                    ID_EntermobEditer.commit()

                                    /*    val ID_last4SP = mContext.getSharedPreferences(Config.SHARED_PREF43, 0)
                                    val ID_last4SPEditer = ID_last4SP.edit()
                                    ID_last4SPEditer.putString("enter last4digitofa/cno", jresult3.get("enter last4digitofa/cno") as String)
                                    ID_last4SPEditer.commit()*/

                                    val ID_ContinueSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF44, 0)
                                    val ID_ContinueSPEditer = ID_ContinueSP.edit()
                                    ID_ContinueSPEditer.putString("continue", jresult3.get("continue") as String)
                                    ID_ContinueSPEditer.commit()

                                    val ID_LoginmobSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF45, 0)
                                    val ID_LoginMobSPEditer = ID_LoginmobSP.edit()
                                    ID_LoginMobSPEditer.putString("loginwithmobilenumber", jresult3.get("loginwithmobilenumber") as String)
                                    ID_LoginMobSPEditer.commit()

                                    /*  val ID_MobotpeSP = mContext.getSharedPreferences(Config.SHARED_PREF46, 0)
                                    val ID_MobotpSPEditer = ID_MobotpeSP.edit()
                                    ID_MobotpSPEditer.putString("enteryourmobilenumberwewillsentyouOTPtoverify", jresult3.get("enteryourmobilenumberwewillsentyouOTPtoverify") as String)
                                    ID_MobotpSPEditer.commit()
*/
                                    val ID_LoginverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF47, 0)
                                    val ID_LoginVerifySPEditer = ID_LoginverifySP.edit()
                                    ID_LoginVerifySPEditer.putString("userloginverified", jresult3.get("userloginverified") as String)
                                    ID_LoginVerifySPEditer.commit()

                                    val ID_OtpverifySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF48, 0)
                                    val ID_OtpVerifySPEditer = ID_OtpverifySP.edit()
                                    ID_OtpVerifySPEditer.putString("Otpverification", jresult3.get("Otpverification") as String)
                                    ID_OtpVerifySPEditer.commit()

                                    val ID_MyaccSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF50, 0)
                                    val ID_MyaccSPEditer = ID_MyaccSP.edit()
                                    ID_MyaccSPEditer.putString("Myaccounts", jresult3.get("Myaccounts") as String)
                                    ID_MyaccSPEditer.commit()

                                    val ID_PassbkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF51, 0)
                                    val ID_PassbkSPEditer = ID_PassbkSP.edit()
                                    ID_PassbkSPEditer.putString("passbook", jresult3.get("passbook") as String)
                                    ID_PassbkSPEditer.commit()

                                    val ID_QuickbalSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF52, 0)
                                    val ID_QuickbalSPEditer = ID_QuickbalSP.edit()
                                    ID_QuickbalSPEditer.putString("quickbalance", jresult3.get("quickbalance") as String)
                                    ID_QuickbalSPEditer.commit()

                                    val ID_DueremindSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF53, 0)
                                    val ID_DueremindEditer = ID_DueremindSP.edit()
                                    ID_DueremindEditer.putString("duereminder", jresult3.get("duereminder") as String)
                                    ID_DueremindEditer.commit()

                                    val ID_AbtusSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF54, 0)
                                    val ID_AbtusEditer = ID_AbtusSP.edit()
                                    ID_AbtusEditer.putString("aboutus", jresult3.get("aboutus") as String)
                                    ID_AbtusEditer.commit()

                                    val ID_ContactSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF55, 0)
                                    val ID_ContactEditer = ID_ContactSP.edit()
                                    ID_ContactEditer.putString("contactus", jresult3.get("contactus") as String)
                                    ID_ContactEditer.commit()

                                    val ID_FeebkSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF56, 0)
                                    val ID_FeedbkEditer = ID_FeebkSP.edit()
                                    ID_FeedbkEditer.putString("feedback", jresult3.get("feedback") as String)
                                    ID_FeedbkEditer.commit()

                                    val ID_PrivacySP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF57, 0)
                                    val ID_PrivacyEditer = ID_PrivacySP.edit()
                                    ID_PrivacyEditer.putString("privacypolicy", jresult3.get("privacypolicy") as String)
                                    ID_PrivacyEditer.commit()

                                    val ID_TermsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF58, 0)
                                    val ID_TermsEditer = ID_TermsSP.edit()
                                    ID_TermsEditer.putString("termsandconditions", jresult3.get("termsandconditions") as String)
                                    ID_TermsEditer.commit()

                                    val ID_StatmntSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF59, 0)
                                    val ID_StatmntEditer = ID_StatmntSP.edit()
                                    ID_StatmntEditer.putString("statement", jresult3.get("statement") as String)
                                    ID_StatmntEditer.commit()

                                    val ID_SetngsSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF60, 0)
                                    val ID_SetngsSpEditer = ID_SetngsSP.edit()
                                    ID_SetngsSpEditer.putString("settings", jresult3.get("settings") as String)
                                    ID_SetngsSpEditer.commit()

                                    val ID_LogoutSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF61, 0)
                                    val ID_LogoutEditer = ID_LogoutSP.edit()
                                    ID_LogoutEditer.putString("logout", jresult3.get("logout") as String)
                                    ID_LogoutEditer.commit()

                                    val ID_NotifSP = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF62, 0)
                                    val ID_NotifSpEditer = ID_NotifSP.edit()
                                    ID_NotifSpEditer.putString("NotificationandMessages", jresult3.get("NotificationandMessages") as String)
                                    ID_NotifSpEditer.commit()

                                    val ID_OwnBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF63, 0)
                                    val ID_OwnbnkEditer = ID_OwnBank.edit()
                                    ID_OwnbnkEditer.putString("OwnBank", jresult3.get("OwnBank") as String)
                                    ID_OwnbnkEditer.commit()

                                    val ID_OtherBank = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF64, 0)
                                    val ID_OtherBankEditer = ID_OtherBank.edit()
                                    ID_OtherBankEditer.putString("OtherBank", jresult3.get("OtherBank") as String)
                                    ID_OtherBankEditer.commit()

                                    val ID_Quickpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF65, 0)
                                    val ID_QuickpayEditer = ID_Quickpay.edit()
                                    ID_QuickpayEditer.putString("QuickPay", jresult3.get("QuickPay") as String)
                                    ID_QuickpayEditer.commit()

                                    val ID_Prepaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF66, 0)
                                    val ID_PrepaidEditer = ID_Prepaid.edit()
                                    ID_PrepaidEditer.putString("PrepaidMobile", jresult3.get("PrepaidMobile") as String)
                                    ID_PrepaidEditer.commit()

                                    val ID_Postpaid = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF67, 0)
                                    val ID_PostpaidEditer = ID_Postpaid.edit()
                                    ID_PostpaidEditer.putString("PostpaidMobile", jresult3.get("PostpaidMobile") as String)
                                    ID_PostpaidEditer.commit()

                                    val ID_Landline = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF68, 0)
                                    val ID_LandlineEditer = ID_Landline.edit()
                                    ID_LandlineEditer.putString("Landline", jresult3.get("Landline") as String)
                                    ID_LandlineEditer.commit()

                                    val ID_DTH = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF69, 0)
                                    val ID_DTHEditer = ID_DTH.edit()
                                    ID_DTHEditer.putString("DTH", jresult3.get("DTH") as String)
                                    ID_DTHEditer.commit()

                                    val ID_Datacrdpay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF70, 0)
                                    val ID_DatacrdEditer = ID_Datacrdpay.edit()
                                    ID_DatacrdEditer.putString("DataCard", jresult3.get("DataCard") as String)
                                    ID_DatacrdEditer.commit()

                                    val ID_KSEB = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF71, 0)
                                    val ID_KSEBEditer = ID_KSEB.edit()
                                    ID_KSEBEditer.putString("KSEB", jresult3.get("KSEB") as String)
                                    ID_KSEBEditer.commit()

                                    val ID_Histry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF72, 0)
                                    val ID_HistryEditer = ID_Histry.edit()
                                    ID_HistryEditer.putString("History", jresult3.get("History") as String)
                                    ID_HistryEditer.commit()

                                    val ID_Dashbrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF73, 0)
                                    val ID_DashbrdEditer = ID_Dashbrd.edit()
                                    ID_DashbrdEditer.putString("Dashboard", jresult3.get("Dashboard") as String)
                                    ID_DashbrdEditer.commit()

                                    val ID_Virtualcrd = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF74, 0)
                                    val ID_VirtualcrdEditer = ID_Virtualcrd.edit()
                                    ID_VirtualcrdEditer.putString("VirtualCard", jresult3.get("VirtualCard") as String)
                                    ID_VirtualcrdEditer.commit()

                                    val ID_Branch = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF75, 0)
                                    val ID_BranchEditer = ID_Branch.edit()
                                    ID_BranchEditer.putString("BranchDetails", jresult3.get("BranchDetails") as String)
                                    ID_BranchEditer.commit()

                                    val ID_Loanapplictn = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF76, 0)
                                    val ID_LoanapplictnEditer = ID_Loanapplictn.edit()
                                    ID_LoanapplictnEditer.putString("LoanApplication", jresult3.get("LoanApplication") as String)
                                    ID_LoanapplictnEditer.commit()

                                    val ID_Loanstatus = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF77, 0)
                                    val ID_LoanstatusEditer = ID_Loanstatus.edit()
                                    ID_LoanstatusEditer.putString("LoanStatus", jresult3.get("LoanStatus") as String)
                                    ID_LoanstatusEditer.commit()

                                    val ID_PrdctDetail = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF78, 0)
                                    val ID_PrdctDetailEditer = ID_PrdctDetail.edit()
                                    ID_PrdctDetailEditer.putString("ProductDetails", jresult3.get("ProductDetails") as String)
                                    ID_PrdctDetailEditer.commit()

                                    val ID_Emi= this@HomeActivity.getSharedPreferences(Config.SHARED_PREF79, 0)
                                    val ID_EmiEditer = ID_Emi.edit()
                                    ID_EmiEditer.putString("EMICalculator", jresult3.get("EMICalculator") as String)
                                    ID_EmiEditer.commit()

                                    val ID_Deposit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF80, 0)
                                    val ID_DepositEditer = ID_Deposit.edit()
                                    ID_DepositEditer.putString("DepositCalculator", jresult3.get("DepositCalculator") as String)
                                    ID_DepositEditer.commit()

                                    val ID_Goldloan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF81, 0)
                                    val ID_GoldloanEditer = ID_Goldloan.edit()
                                    ID_GoldloanEditer.putString("GoldLoanEligibileCalculator", jresult3.get("GoldLoanEligibileCalculator") as String)
                                    ID_GoldloanEditer.commit()

                                    val ID_Enqry = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF82, 0)
                                    val ID_EnqryEditer = ID_Enqry.edit()
                                    ID_EnqryEditer.putString("Enquires", jresult3.get("Enquires") as String)
                                    ID_EnqryEditer.commit()

                                    val ID_Holidy = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF83, 0)
                                    val ID_HolidyEditer = ID_Holidy.edit()
                                    ID_HolidyEditer.putString("HolidayList", jresult3.get("HolidayList") as String)
                                    ID_HolidyEditer.commit()

                                    val ID_Executve = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF84, 0)
                                    val ID_ExecutveEditer = ID_Executve.edit()
                                    ID_ExecutveEditer.putString("ExecutiveCallBack", jresult3.get("ExecutiveCallBack") as String)
                                    ID_ExecutveEditer.commit()

                                    val ID_DEPOSIT = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF85, 0)
                                    val ID_DEPOSITEditer = ID_DEPOSIT.edit()
                                    ID_DEPOSITEditer.putString("DEPOSIT", jresult3.get("DEPOSIT") as String)
                                    ID_DEPOSITEditer.commit()

                                    val ID_LOAN = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF86, 0)
                                    val ID_LOANEditer = ID_LOAN.edit()
                                    ID_LOANEditer.putString("LOAN", jresult3.get("LOAN") as String)
                                    ID_LOANEditer.commit()

                                    val ID_Active = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF87, 0)
                                    val ID_ActiveEditer = ID_Active.edit()
                                    ID_ActiveEditer.putString("Active", jresult3.get("Active") as String)
                                    ID_ActiveEditer.commit()

                                    val ID_Deposit1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF88, 0)
                                    val ID_Deposit1Editer = ID_Deposit1.edit()
                                    ID_Deposit1Editer.putString("Deposit", jresult3.get("Deposit") as String)
                                    ID_Deposit1Editer.commit()

                                    val ID_Loan1 = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF89, 0)
                                    val ID_Loan1Editer = ID_Loan1.edit()
                                    ID_Loan1Editer.putString("Loan", jresult3.get("Loan") as String)
                                    ID_Loan1Editer.commit()

                                    val ID_Ownacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF90, 0)
                                    val ID_OwnaccEditer = ID_Ownacc.edit()
                                    ID_OwnaccEditer.putString("OWNACCOUNT", jresult3.get("OWNACCOUNT") as String)
                                    ID_OwnaccEditer.commit()

                                    val ID_Otheracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF91, 0)
                                    val ID_OtheraccEditer = ID_Otheracc.edit()
                                    ID_OtheraccEditer.putString("OTHERACCOUNT", jresult3.get("OTHERACCOUNT") as String)
                                    ID_OtheraccEditer.commit()

                                    val ID_Selectacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF92, 0)
                                    val ID_SelectaccEditer = ID_Selectacc.edit()
                                    ID_SelectaccEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SelectaccEditer.commit()

                                    val ID_Payingfrm = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF93, 0)
                                    val ID_PayingfrmEditer = ID_Payingfrm.edit()
                                    ID_PayingfrmEditer.putString("PayingFrom", jresult3.get("PayingFrom") as String)
                                    ID_PayingfrmEditer.commit()

                                    val ID_Payingto = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF94, 0)
                                    val ID_PayingtoEditer = ID_Payingto.edit()
                                    ID_PayingtoEditer.putString("PayingTo", jresult3.get("PayingTo") as String)
                                    ID_PayingtoEditer.commit()

                                    val ID_Amtpayble = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF95, 0)
                                    val ID_AmtpaybleEditer = ID_Amtpayble.edit()
                                    ID_AmtpaybleEditer.putString("AmountPayable", jresult3.get("AmountPayable") as String)
                                    ID_AmtpaybleEditer.commit()

                                    val ID_Remark = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF96, 0)
                                    val ID_RemarkEditer = ID_Remark.edit()
                                    ID_RemarkEditer.putString("Remark", jresult3.get("Remark") as String)
                                    ID_RemarkEditer.commit()


                                    val ID_Pay = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF97, 0)
                                    val ID_PayEditer = ID_Pay.edit()
                                    ID_PayEditer.putString("PAY", jresult3.get("PAY") as String)
                                    ID_PayEditer.commit()

                                    val ID_Receiveracc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF98, 0)
                                    val ID_ReceiveraccEditer = ID_Receiveracc.edit()
                                    ID_ReceiveraccEditer.putString("ReceiverAccountType", jresult3.get("ReceiverAccountType") as String)
                                    ID_ReceiveraccEditer.commit()

                                    val ID_Confirmacc = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF99, 0)
                                    val ID_ConfirmaccEditer = ID_Confirmacc.edit()
                                    ID_ConfirmaccEditer.putString("ConfirmAccountNo", jresult3.get("ConfirmAccountNo") as String)
                                    ID_ConfirmaccEditer.commit()

                                    val ID_Scan = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF100, 0)
                                    val ID_ScanEditer = ID_Scan.edit()
                                    ID_ScanEditer.putString("Scan", jresult3.get("Scan") as String)
                                    ID_ScanEditer.commit()

                                    val ID_Slctaccnt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF101, 0)
                                    val ID_SlctaccntEditer = ID_Slctaccnt.edit()
                                    ID_SlctaccntEditer.putString("SelectYourAccount", jresult3.get("SelectYourAccount") as String)
                                    ID_SlctaccntEditer.commit()

                                    val ID_Rechrgehist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF102, 0)
                                    val ID_RechrgehistEditer = ID_Rechrgehist.edit()
                                    ID_RechrgehistEditer.putString("RechargeHistory", jresult3.get("RechargeHistory") as String)
                                    ID_RechrgehistEditer.commit()

                                    val ID_Frontview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF103, 0)
                                    val ID_FrontviewEditer = ID_Frontview.edit()
                                    ID_FrontviewEditer.putString("FRONTVIEW", jresult3.get("FRONTVIEW") as String)
                                    ID_FrontviewEditer.commit()

                                    val ID_Backview = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF104, 0)
                                    val ID_BackviewEditer = ID_Backview.edit()
                                    ID_BackviewEditer.putString("BACKVIEW", jresult3.get("BACKVIEW") as String)
                                    ID_BackviewEditer.commit()

                                    val ID_Purpose = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF105, 0)
                                    val ID_PurposeEditer = ID_Purpose.edit()
                                    ID_PurposeEditer.putString("PurposeofVirtualCard", jresult3.get("PurposeofVirtualCard") as String)
                                    ID_PurposeEditer.commit()


                                    val ID_Quit = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF106, 0)
                                    val ID_QuitEditer = ID_Quit.edit()
                                    ID_QuitEditer.putString("quit", jresult3.get("quit") as String)
                                    ID_QuitEditer.commit()

                                    val ID_Accno = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF107, 0)
                                    val ID_AccnoEditer = ID_Accno.edit()
                                    ID_AccnoEditer.putString("AccountNo", jresult3.get("AccountNo") as String)
                                    ID_AccnoEditer.commit()

                                    val ID_Enterdist = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF108, 0)
                                    val ID_EnterdistEditer = ID_Enterdist.edit()
                                    ID_EnterdistEditer.putString("EnterDistrict", jresult3.get("EnterDistrict") as String)
                                    ID_EnterdistEditer.commit()




                                    val ID_Mobilenum = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF110, 0)
                                    val ID_MobilenumEditer = ID_Mobilenum.edit()
                                    ID_MobilenumEditer.putString("MobileNumber", jresult3.get("MobileNumber") as String)
                                    ID_MobilenumEditer.commit()

                                    val ID_Operator = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF111, 0)
                                    val ID_OperatorEditer = ID_Operator.edit()
                                    ID_OperatorEditer.putString("Operator", jresult3.get("Operator") as String)
                                    ID_OperatorEditer.commit()

                                    val ID_Circle = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF112, 0)
                                    val ID_CircleEditer = ID_Circle.edit()
                                    ID_CircleEditer.putString("Circle", jresult3.get("Circle") as String)
                                    ID_CircleEditer.commit()


                                    val ID_Amt = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF113, 0)
                                    val ID_AmtEditer = ID_Amt.edit()
                                    ID_AmtEditer.putString("Amount", jresult3.get("Amount") as String)
                                    ID_AmtEditer.commit()

                                    val ID_Rechrg = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF114, 0)
                                    val ID_RechrgEditer = ID_Rechrg.edit()
                                    ID_RechrgEditer.putString("RECHARGE", jresult3.get("RECHARGE") as String)
                                    ID_RechrgEditer.commit()

                                    val ID_Selctop = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF115, 0)
                                    val ID_SelctopEditer = ID_Selctop.edit()
                                    ID_SelctopEditer.putString("SelectOperator", jresult3.get("SelectOperator") as String)
                                    ID_SelctopEditer.commit()


                                    val ID_Subscriber = this@HomeActivity.getSharedPreferences(Config.SHARED_PREF116, 0)
                                    val ID_SubscriberEditer = ID_Subscriber.edit()
                                    ID_SubscriberEditer.putString("SubscriberID", jresult3.get("SubscriberID") as String)
                                    ID_SubscriberEditer.commit()



                                    // mContext.startActivity(Intent(mContext, WelcomeActivity::class.java))


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
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
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
                        }
                    })
                } catch (e: Exception) {
                    //  progressDialog!!.dismiss()
                    val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
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
                val builder = android.app.AlertDialog.Builder(this@HomeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: android.app.AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
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
            R.id.llDeposit->
            {
                var intent = Intent(this@HomeActivity, DepositCalculatorActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_otherbank -> {

                var intent = Intent(this@HomeActivity, OtherBankActivity::class.java)
                startActivity(intent)
            }
            R.id.llexecutive -> {

                var intent = Intent(this@HomeActivity, ExecutiveActivity::class.java)
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
            R.id.llenquiry -> {

                startActivity(Intent(this@HomeActivity, EnquiryActivity::class.java))
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