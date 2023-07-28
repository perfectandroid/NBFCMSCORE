package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.perfect.nbfcmscore.Fragment.DepositFragment
import com.perfect.nbfcmscore.Fragment.LoanlistFragment
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import java.util.*

class AccountlistActivity : AppCompatActivity(), View.OnClickListener {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_mycart: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myaccounts)
        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        tv_mycart = findViewById<View>(R.id.tv_mycart) as TextView

        val ID_Myacc = applicationContext.getSharedPreferences(Config.SHARED_PREF50,0)
        tv_mycart!!.setText(ID_Myacc.getString("Myaccounts",null))


    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val ID_DEPOSIT = applicationContext.getSharedPreferences(Config.SHARED_PREF85,0)
        val ID_LOAN = applicationContext.getSharedPreferences(Config.SHARED_PREF86,0)

        var deposit =ID_DEPOSIT.getString("DEPOSIT",null)
        var loan =ID_LOAN.getString("LOAN",null)

        if (deposit != null) {
            adapter.addFragment(DepositFragment(), deposit)
        }
        if (loan != null) {
            adapter.addFragment(LoanlistFragment(), loan)
        }
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

    @SuppressLint("WrongConstant")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@AccountlistActivity, HomeActivity::class.java))
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