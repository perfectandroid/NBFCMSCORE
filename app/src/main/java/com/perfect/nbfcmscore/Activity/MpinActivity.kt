package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.perfect.nbfcmscore.R
import javax.net.ssl.*

class MpinActivity : AppCompatActivity() {

    var show : Boolean = false
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_mpin)

    }



}
