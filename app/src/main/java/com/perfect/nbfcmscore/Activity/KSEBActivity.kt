package com.perfect.nbfcmscore.Activity

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.R

class KSEBActivity : AppCompatActivity(), View.OnClickListener {

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null

    var tie_consumername: TextInputEditText? = null
    var tie_mobilenumber: TextInputEditText? = null
    var tie_consumerno: TextInputEditText? = null
    var tie_sectionname: TextInputEditText? = null
    var tie_billno: TextInputEditText? = null
    var tie_amount: TextInputEditText? = null
    var tie_account: TextInputEditText? = null

    var im_sectionname: ImageView? = null

    private val KSEB_SECTION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksebactivity)

        setInitialise()
        setRegister()
    }

    private fun setInitialise() {
        tv_header = findViewById<TextView>(R.id.tv_header)
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tie_consumername = findViewById<TextInputEditText>(R.id.tie_consumername)
        tie_mobilenumber = findViewById<TextInputEditText>(R.id.tie_mobilenumber)
        tie_consumerno = findViewById<TextInputEditText>(R.id.tie_consumerno)
        tie_sectionname = findViewById<TextInputEditText>(R.id.tie_sectionname)
        tie_billno = findViewById<TextInputEditText>(R.id.tie_billno)
        tie_amount = findViewById<TextInputEditText>(R.id.tie_amount)
        tie_account = findViewById<TextInputEditText>(R.id.tie_account)

        im_sectionname = findViewById<ImageView>(R.id.im_sectionname)
    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        tie_account!!.setOnClickListener(this)
        im_sectionname!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()
//                startActivity(Intent(this@StatementActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@KSEBActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_sectionname ->{

                val intent = Intent(this@KSEBActivity, RechargeOfferActivity::class.java)
                startActivityForResult(intent, KSEB_SECTION) // Activity is started with requestCode 2
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.e("TAG","tempContact  698  "+requestCode+"  "+resultCode)
//        if (requestCode == KSEB_SECTION && resultCode == RESULT_OK && applicationContext != null) {
//            try {
//                val value = data?.getStringExtra("data")
//                Log.e("TAG", "AMOUNT   932   " + value)
//                tie_amount!!.setText(value)
//            } catch (e: Exception) {
//                Log.e("TAG", "AMOUNT   932   " + e.toString())
//            }
//        }
//
//    }
//
}