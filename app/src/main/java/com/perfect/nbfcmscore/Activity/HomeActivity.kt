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

    var lvNavMenu: ListView? = null
    var drawer: DrawerLayout? = null
    var imgMenu: ImageView? = null

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
        imgMenu = findViewById<ImageView>(R.id.imgMenu)
        drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        lvNavMenu = findViewById<ListView>(R.id.lvNavMenu)
    }

    open fun setRegister() {
        imgMenu!!.setOnClickListener(this)
        mPager = findViewById(R.id.pager)
        indicator =findViewById(R.id.indicator)
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
        }
    }

}