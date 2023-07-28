package com.perfect.nbfcmscore.Activity

import com.perfect.nbfc.Notifctnlist
import com.perfect.nbfc.Notificatnlistadapter
import com.perfect.nbfcmscore.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.IdleUtil
import java.util.ArrayList

class NotificationActivity : AppCompatActivity() {
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val recyclerView = findViewById(R.id.rvActionlist) as RecyclerView
        //   val textViewtitle = findViewById(R.id.tvcustname) as TextView

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        //crating an arraylist to store users using the data class user
        val users = ArrayList<Notifctnlist>()

        //adding some dummy data to the list
        users.add(Notifctnlist("Free Delivery", "We have an exciting offers for you","29 oct 2020"))
        users.add(Notifctnlist("Free Delivery", "We have an exciting offers for you","29 oct 2020"))
        users.add(Notifctnlist("Free Delivery", "We have an exciting offers for you","29 oct 2020"))
        //creating our adapter
        val adapter = Notificatnlistadapter(users)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
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