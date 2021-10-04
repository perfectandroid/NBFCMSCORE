package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.perfect.nbfcmscore.R

class LanguageSelectionActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languagesectection)
        setRegViews()
    }

    private fun setRegViews() {
        val tvskip = findViewById<TextView>(R.id.tvskip) as TextView
        tvskip!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tvskip->{
                intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}