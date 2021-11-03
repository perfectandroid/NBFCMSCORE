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
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import java.util.*

class OtherfundTransferHistory : AppCompatActivity() ,View.OnClickListener{
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    private var rv_fundtransfer: RecyclerView? = null
    private var jresult: JSONArray? = null
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
        sendData()

    }

    public fun sendData(): String? {
            return submode
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OtherBankFundTransferHistoryFragment(), "Today's Status")
        adapter.addFragment(OtherBankFundTransferPreviousHistoryFragment(), "Previous Status")
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
}

