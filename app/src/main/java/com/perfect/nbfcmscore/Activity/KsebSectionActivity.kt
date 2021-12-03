package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.R

class KsebSectionActivity : AppCompatActivity(), View.OnClickListener {

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kseb_section)

        setInitialise()
        setRegister()
    }

    private fun setInitialise() {
        tv_header = findViewById<TextView>(R.id.tv_header)
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()

            }

            R.id.im_home ->{
                startActivity(Intent(this@KsebSectionActivity, HomeActivity::class.java))
                finish()
            }

        }
    }
}