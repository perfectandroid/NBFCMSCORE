package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
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
import com.perfect.nbfcmscore.R
import me.relex.circleindicator.CircleIndicator
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class HomeActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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
    var llpassbook: LinearLayout? = null
    var llduereminder: LinearLayout? = null
    var ll_virtualcard: LinearLayout? = null
    var ll_otherbank: LinearLayout? = null
    var llquickbalance: LinearLayout? = null
    var llstatement: LinearLayout? = null

    var tv_def_account: TextView? = null
    var tv_def_availablebal: TextView? = null
    var tv_lastlogin: TextView? = null

    private var mPager: ViewPager? = null
    private var indicator: CircleIndicator? = null
    private var currentPage = 0
    private val XMEN = arrayOf<Int>(R.drawable.ban1, R.drawable.ban2, R.drawable.ban3, R.drawable.ban4)
    private val XMENArray = ArrayList<Int>()

    var jArrayAccount: JSONArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        setInitialise()
        setRegister()
        setHomeNavMenu()
        init()

        setdefaultAccountDetails()


    }

    private fun setdefaultAccountDetails() {


        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24,0)
        val DefaultBalanceSP = applicationContext.getSharedPreferences(Config.SHARED_PREF27,0)
        val LastLoginTimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF29,0)

        tv_lastlogin!!.setText("Last Login : "+LastLoginTimeSP.getString("LastLoginTime",null))

        if (DefaultAccountSP.getString("DefaultAccount",null) == null){
            tv_def_account!!.setText("")
            tv_def_availablebal!!.setText("")
            getOwnAccount()

        }else{
            tv_def_account!!.setText(DefaultAccountSP.getString("DefaultAccount",null))
            val balance = DefaultBalanceSP.getString("DefaultBalance",null)!!.toDouble()
            tv_def_availablebal!!.setText("Rs. "+Config.getDecimelFormate(balance))
        }

    }

    private fun init() {
        for (i in 0 until 4)
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
        }, 3000, 3000)
    }

    open fun setInitialise() {
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
        ll_dth = findViewById(R.id.ll_dth)
        llEmi = findViewById(R.id.llEmi)
        llpassbook = findViewById<LinearLayout>(R.id.llpassbook)
        llduereminder = findViewById<LinearLayout>(R.id.lldueremindrer)
        llgoldslab = findViewById<LinearLayout>(R.id.llgoldslab)
        ll_virtualcard = findViewById<LinearLayout>(R.id.ll_virtualcard)
        ll_otherbank = findViewById<LinearLayout>(R.id.ll_otherbank)
        llquickbalance = findViewById<LinearLayout>(R.id.llquickbalance)
        llstatement = findViewById<LinearLayout>(R.id.llstatement)

        tv_def_account = findViewById<TextView>(R.id.tv_def_account)
        tv_def_availablebal = findViewById<TextView>(R.id.tv_def_availablebal)
        tv_lastlogin = findViewById<TextView>(R.id.tv_lastlogin)

    }

    open fun setRegister() {
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
        llstatement!!.setOnClickListener(this)
    }

    open fun setHomeNavMenu() {
        val menulist = arrayOf("Home", "About Us", "Notification", "Contact Us", "Feedback", "Privacy Policies", "Terms & Conditions", "Settings", "Rate Us", "Share", "Logout", "Quit")
        val imageId = arrayOf<Int>(
                R.drawable.homenav, R.drawable.aboutnav, R.drawable.notinav, R.drawable.contnav,
                R.drawable.feedbacknav, R.drawable.ppnav, R.drawable.tncnav, R.drawable.ic_settings,
                R.drawable.ratenav,R.drawable.sharenav, R.drawable.logoutnav, R.drawable.exitnav
        )
        val adapter = NavMenuAdapter(this@HomeActivity, menulist, imageId)
        lvNavMenu!!.adapter = adapter
        lvNavMenu!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                startActivity(Intent(this@HomeActivity, HomeActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            } else if (position == 1) {

                startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            } else if (position == 2) {

                startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            } else if (position == 7) {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                drawer!!.closeDrawer(GravityCompat.START)
            }
            else if (position == 3) {

            }

        }
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
            R.id.lldashboard ->{
                startActivity(Intent(this@HomeActivity, DashboardActivity::class.java))
            }
            R.id.llprdctdetail ->{

                startActivity(Intent(this@HomeActivity, ProductListActivity::class.java))
            }
            R.id.llmyaccounts ->{
                startActivity(Intent(this@HomeActivity, AccountlistActivity::class.java))
            }
            R.id.ll_branschDetails ->{
                startActivity(Intent(this@HomeActivity, BranchDetailActivity::class.java))
            }

            R.id.ll_holidaylist ->{
                startActivity(Intent(this@HomeActivity, HolidayListActivity::class.java))
            }

            R.id.llEmi ->{
                startActivity(Intent(this@HomeActivity, EMIActivity::class.java))
            }
            R.id.lldueremindrer ->{
                startActivity(Intent(this@HomeActivity, DueReminderActivity::class.java))
            }
            R.id.llpassbook ->{
                startActivity(Intent(this@HomeActivity, PassbookActivity::class.java))
            }
            R.id.llquickbalance ->{
                startActivity(Intent(this@HomeActivity, QuickBalanceActivity::class.java))
            }
            R.id.llownbank ->{
                startActivity(Intent(this@HomeActivity, OwnBankFundTransferServiceActivity::class.java))
            }
            R.id.llgoldslab ->{
                startActivity(Intent(this@HomeActivity, GoldSlabEstimatorActivity::class.java))
            }
            R.id.ll_prepaid ->{

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "prepaid")
                startActivity(intent)
            }
            R.id.ll_postpaid ->{

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "postpaid")
                startActivity(intent)
            }
            R.id.ll_landline ->{

//                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
//                intent.putExtra("from", "landline")
//                startActivity(intent)
            }
            R.id.ll_dth ->{

                var intent = Intent(this@HomeActivity, RechargeActivity::class.java)
                intent.putExtra("from", "dth")
                startActivity(intent)
            }

            R.id.ll_virtualcard ->{

                var intent = Intent(this@HomeActivity, VirtualActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_otherbank ->{

                var intent = Intent(this@HomeActivity, OtherBankActivity::class.java)
                startActivity(intent)
            }
            R.id.ll_rechargehistory ->{

                var intent = Intent(this@HomeActivity, RechargeHistoryActivity::class.java)
                startActivity(intent)
            } R.id.llstatement ->{

                var intent = Intent(this@HomeActivity, StatementActivity::class.java)
                startActivity(intent)
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))


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
                                        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24,0)
                                        if (DefaultAccountSP.getString("DefaultAccount",null) == null){
                                            if (i == 0){

                                                val balance = obj.getString("Balance").toDouble()
                                                tv_def_availablebal!!.setText("Rs. "+Config.getDecimelFormate(balance))
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

}