package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.barteksc.pdfviewer.PDFView
import com.perfect.nbfcmscore.R
import java.io.File

class ViewStatementActivity : AppCompatActivity() , View.OnClickListener{

    val TAG: String = "ViewStatementActivity"
    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null
    var pdfView: PDFView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_statement2)

        setInitialise()
        setRegister()


        tv_header!!.setText("Statement")


       val path =  intent.getStringExtra("path")
        Log.e(TAG,"path   18  "+path)
        val file = File(path);

        pdfView!!.fromFile(file)
//            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .load();
    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
    }

    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        pdfView = findViewById<PDFView>(R.id.pdfView)
        tv_header = findViewById<TextView>(R.id.tv_header)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                 onBackPressed()

            }

            R.id.im_home ->{
                startActivity(Intent(this@ViewStatementActivity, HomeActivity::class.java))
                finish()
            }
        }
    }
}