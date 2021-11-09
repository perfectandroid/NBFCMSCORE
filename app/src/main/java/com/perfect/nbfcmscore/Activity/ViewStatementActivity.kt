package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.github.barteksc.pdfviewer.PDFView
import com.perfect.nbfcmscore.R

class ViewStatementActivity : AppCompatActivity(), View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "ViewStatementActivity"

    var im_back: ImageView? = null
    var webView: WebView? = null
    var pdfView: PDFView? = null

    //var attachedfile: String = ""
    var attachedfile: String = "https://maven.apache.org/archives/maven-1.x/maven.pdf"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_statement)

        setInitialise()
        setRegister()

        //  wv1=(WebView)findViewById(R.id.webView);
        // wv1.setWebViewClient(new MyBrowser());

        // attachedfile = Common.getBaseUrl()+"/Mscore/Statement/ASD7.pdf";
      //  attachedfile = intent.getStringExtra("docx")!!
        val myUri = Uri.parse(attachedfile)
        pdfView!!.fromUri(myUri)

//        webView!!.settings.domStorageEnabled = true
//        webView!!.settings.javaScriptEnabled = true
//        webView!!.settings.builtInZoomControls = true
//
//
//        progressDialog = ProgressDialog(this@ViewStatementActivity, R.style.Progress)
//        progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
//        progressDialog!!.setMessage("Loading please wait...")
//        progressDialog!!.setCancelable(false)
//        progressDialog!!.isIndeterminate = true
//        progressDialog!!.setIndeterminateDrawable(resources.getDrawable(R.drawable.progress))
//        progressDialog!!.show()
//
//
//        //        if(attachedfile.indexOf(".jpg")!=-1||attachedfile.indexOf(".jpeg")!=-1||attachedfile.indexOf(".png")!=-1||attachedfile.indexOf(".gif")!=-1) {
////            webView.setWebViewClient(new SSLTolerentWebViewClient());
////            webView.loadUrl(attachedfile);
////        }else {
////            String url = "http://docs.google.com/viewer?embedded=true&url=" + attachedfile;
////            webView.setWebViewClient(new SSLTolerentWebViewClient());
////            webView.loadUrl(url);
////        }
//        //        if(attachedfile.indexOf(".jpg")!=-1||attachedfile.indexOf(".jpeg")!=-1||attachedfile.indexOf(".png")!=-1||attachedfile.indexOf(".gif")!=-1) {
////            webView.setWebViewClient(new SSLTolerentWebViewClient());
////            webView.loadUrl(attachedfile);
////        }else {
////            String url = "http://docs.google.com/viewer?embedded=true&url=" + attachedfile;
////            webView.setWebViewClient(new SSLTolerentWebViewClient());
////            webView.loadUrl(url);
////        }
//        val url = "http://docs.google.com/viewer?embedded=true&url=$attachedfile"
//        webView!!.webViewClient = object : SSLTolerentWebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                if (webView!!.progress == 100) {
//                    progressDialog!!.dismiss()
//                    webView!!.visibility = View.VISIBLE
//                }
//            }
//        }
//        webView!!.loadUrl(url)
//        Handler().postDelayed({
//            //  progressDialog.dismiss();
//        }, 3000)
//
//        if (supportActionBar != null) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        }

    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//    private open class SSLTolerentWebViewClient : WebViewClient() {
//        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//            handler.proceed()
//        }
//    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        webView = findViewById<WebView>(R.id.webView)
        pdfView = findViewById<PDFView>(R.id.pdfView)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()
            }
        }
    }
}