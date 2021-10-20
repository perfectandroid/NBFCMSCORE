package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.perfect.nbfcmscore.Fragment.DepositFragment
import com.perfect.nbfcmscore.Fragment.LoanlistFragment
import com.perfect.nbfcmscore.Fragment.MiniStatementFragment
import com.perfect.nbfcmscore.Fragment.MoreOptionFragment
import com.perfect.nbfcmscore.R
import java.util.ArrayList

class AccountDetailsActivity : AppCompatActivity() , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null

    var LoanType: String=""
    var Balance: String=""
    var AccountNumber: String=""
    var BranchName: String=""
    var FK_Account: String=""
    var Status: String=""
    var SubModule: String=""
    var FundTransferAccount: String=""
    var IFSCCode: String=""
    var IsShareAc: String=""
    var EnableDownloadStatement: String=""
    var IsDue: String=""

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tvaccounttype: TextView? = null
    var tvaccountno: TextView? = null
    var tvbal: TextView? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountdetails)
        setRegViews()

        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)

        LoanType = intent.getStringExtra("LoanType")!!
        Balance = intent.getStringExtra("Balance")!!
        AccountNumber = intent.getStringExtra("AccountNumber")!!

        BranchName = intent.getStringExtra("BranchName")!!
        FK_Account = intent.getStringExtra("FK_Account")!!
        Status = intent.getStringExtra("Status")!!

        SubModule = intent.getStringExtra("SubModule")!!
        FundTransferAccount = intent.getStringExtra("FundTransferAccount")!!
        IFSCCode = intent.getStringExtra("IFSCCode")!!

        IsShareAc = intent.getStringExtra("IsShareAc")!!
        EnableDownloadStatement = intent.getStringExtra("EnableDownloadStatement")!!
        IsDue = intent.getStringExtra("IsDue")!!

        tvaccounttype!!.setText(LoanType)
        tvaccountno!!.setText(AccountNumber)
        tvbal!!.setText(Balance)
    }

    private fun setRegViews() {
        tvaccounttype = findViewById<TextView>(R.id.tvaccounttype)
        tvaccountno = findViewById<TextView>(R.id.tvaccountno)
        tvbal = findViewById<TextView>(R.id.tvbal)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@AccountDetailsActivity, HomeActivity::class.java))
            }        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MiniStatementFragment(), "Mini Statement")
        adapter.addFragment(MiniStatementFragment(), "Account Statement")
        adapter.addFragment(MoreOptionFragment(), "More Options")
        viewPager!!.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(
        manager!!
    ) {
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
}