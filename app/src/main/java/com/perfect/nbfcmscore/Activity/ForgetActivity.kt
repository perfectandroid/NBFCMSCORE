package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.perfect.nbfcmscore.R

class ForgetActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        setRegViews()
    }

    private fun setRegViews() {
        val tvreg = findViewById<TextView>(R.id.tvreg) as TextView
        val btforgrt = findViewById<Button>(R.id.btforgrt) as Button
        tvreg!!.setOnClickListener(this)
        btforgrt!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btforgrt->{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.tvreg-> {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}