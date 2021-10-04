package com.perfect.nbfcmscore.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.R

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)
        setRegViews()

        val imwelcome: ImageView = findViewById(R.id.imwelcome)
        Glide.with(this).load(R.drawable.welcomegif).into(imwelcome)
    }

    private fun setRegViews() {
        val btlogin = findViewById<Button>(R.id.btlogin) as Button
        val btregistration = findViewById<Button>(R.id.btregistration) as Button
        btlogin!!.setOnClickListener(this)
        btregistration!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btregistration-> {
                intent = Intent(applicationContext, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }
    }

}

