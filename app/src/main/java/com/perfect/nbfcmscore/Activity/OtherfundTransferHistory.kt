package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.perfect.nbfcmscore.Fragment.OtherBankFundTransferHistoryFragment
import com.perfect.nbfcmscore.Fragment.OtherBankFundTransferPreviousHistoryFragment
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import java.util.*

class OtherfundTransferHistory : AppCompatActivity() ,View.OnClickListener{
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    private var rv_fundtransfer: RecyclerView? = null
    private var jresult: JSONArray? = null
    var tv_header: TextView? = null
    var tv_status: TextView? = null
    var token: String? = null
    var submode: String? = null
    var cusid:kotlin.String? = null
    var loantype:kotlin.String? = null
    var status_spinner: Spinner? = null
    var status = arrayOf<String>()
    var reqSubMode = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        var tabLayout: TabLayout? = null
        var viewPager: ViewPager? = null
        var llviews: LinearLayout? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherfundtransferstatus)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewPager)
        setupViewPager(viewPager)

        submode = intent.getStringExtra("submode")

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        llviews = findViewById<View>(R.id.llviews) as LinearLayout
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        tv_header= findViewById<TextView>(R.id.tv_header)

        val fundtransfrstatsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF154, 0)
        tv_header!!.setText(fundtransfrstatsSP.getString("FUNDTRANSFERSTATUS", null))



        sendData()

    }

    public fun sendData(): String? {
            return submode
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val todaySP = applicationContext.getSharedPreferences(Config.SHARED_PREF338, 0)
        val prevSP = applicationContext.getSharedPreferences(Config.SHARED_PREF339, 0)

        var tod =todaySP.getString("TODAYSSTATUS", null)
        var prev =prevSP.getString("PREVIOUSSTATUS", null)

        adapter.addFragment(OtherBankFundTransferHistoryFragment(), tod!!)
        //adapter.addFragment(OtherBankFundTransferHistoryFragment(), "Today's Status")
        adapter.addFragment(OtherBankFundTransferPreviousHistoryFragment(), prev!!)
       // adapter.addFragment(OtherBankFundTransferPreviousHistoryFragment(), "Previous Status")
        viewPager.adapter = adapter
    }


    internal class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@OtherfundTransferHistory, HomeActivity::class.java))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}

