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
import com.perfect.nbfcmscore.Fragment.BackViewFragment
import com.perfect.nbfcmscore.Fragment.DepositFragment
import com.perfect.nbfcmscore.Fragment.FrontViewFragment
import com.perfect.nbfcmscore.Fragment.LoanlistFragment
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import java.util.*

class VirtualActivity : AppCompatActivity() , View.OnClickListener{

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "VirtualActivity"
    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null

    private var tabLayout: TabLayout? = null
    var reqmode: String? = null
    var token: String? = null
    var cusid: String? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_virtual)

        setInitialise()
        setRegister()

        val ID_Virtual = applicationContext.getSharedPreferences(Config.SHARED_PREF74,0)
        tv_header!!.setText(ID_Virtual.getString("VirtualCard",null))



        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout!!.setupWithViewPager(viewPager)
    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_header = findViewById<TextView>(R.id.tv_header)

    }

    private fun setRegister() {

        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                // onBackPressed()
                startActivity(Intent(this@VirtualActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@VirtualActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val ID_Front = applicationContext.getSharedPreferences(Config.SHARED_PREF103,0)
        val ID_Back = applicationContext.getSharedPreferences(Config.SHARED_PREF104,0)


        var front =ID_Front.getString("FRONTVIEW",null)
        var back =ID_Back.getString("BACKVIEW",null)

        if (front != null) {
            adapter.addFragment(FrontViewFragment(), front)
        }
        if (back != null) {
            adapter.addFragment(BackViewFragment(), back)
        }
        //adapter.addFragment(FrontViewFragment(), "Frontview")
      //  adapter.addFragment(BackViewFragment(), "Backview")
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