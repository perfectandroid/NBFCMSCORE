package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.perfect.nbfcmscore.Adapter.BannerAdapter
import com.perfect.nbfcmscore.Adapter.NavMenuAdapter
import com.perfect.nbfcmscore.R
import me.relex.circleindicator.CircleIndicator
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
    var llquickpay: LinearLayout? = null
    private var mPager: ViewPager? = null
    private var indicator: CircleIndicator? = null
    private var currentPage = 0
    private val XMEN = arrayOf<Int>(R.drawable.ban1, R.drawable.ban2, R.drawable.ban3, R.drawable.ban4)
    private val XMENArray = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        setInitialise()
        setRegister()
        setHomeNavMenu()
        init()

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
        llquickpay = findViewById<LinearLayout>(R.id.llquickpay)

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
        llquickpay!!.setOnClickListener(this)
    }

    open fun setHomeNavMenu() {
        val menulist = arrayOf("Home", "About Us", "Notification", "Contact Us", "Feedback", "Privacy Policies", "Terms & Conditions", "Rate Us", "Share", "Logout", "Quit")
        val imageId = arrayOf<Int>(
                R.drawable.homenav, R.drawable.aboutnav, R.drawable.notinav, R.drawable.contnav,
                R.drawable.feedbacknav, R.drawable.ppnav, R.drawable.tncnav, R.drawable.ratenav,
                R.drawable.sharenav, R.drawable.logoutnav, R.drawable.exitnav
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
            } else if (position == 3) {

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
            R.id.llquickpay ->{

                var intent = Intent(this@HomeActivity, QuickPayActivity::class.java)
                startActivity(intent)
            }
        }
    }

}