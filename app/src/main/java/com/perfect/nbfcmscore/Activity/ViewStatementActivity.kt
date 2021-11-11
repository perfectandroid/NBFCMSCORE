package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.github.barteksc.pdfviewer.PDFView
import com.perfect.nbfcmscore.R
import android.widget.Toast

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.print.PrintJob
import android.webkit.*
import android.webkit.WebViewClient
import android.print.PrintAttributes

import android.print.PrintDocumentAdapter

import android.print.PrintManager
import androidx.annotation.RequiresApi
import android.content.Intent

import android.webkit.DownloadListener





class ViewStatementActivity : AppCompatActivity(), View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "ViewStatementActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var webView: WebView? = null
    var pdfView: PDFView? = null

    var attachedfile: String = ""
   // var attachedfile: String = "https://maven.apache.org/archives/maven-1.x/maven.pdf"

    var printWeb: WebView? = null
    var printJob: PrintJob? = null
    var printBtnPressed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_statement)

        setInitialise()
        setRegister()

        //  wv1=(WebView)findViewById(R.id.webView);
        // wv1.setWebViewClient(new MyBrowser());

        // attachedfile = Common.getBaseUrl()+"/Mscore/Statement/ASD7.pdf";
        attachedfile = intent.getStringExtra("docx")!!
        Log.e(TAG,"attachedfile  44   "+attachedfile)
//        val myUri = Uri.parse(attachedfile)
//        pdfView!!.fromUri(myUri)

        webView!!.settings.domStorageEnabled = true
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.builtInZoomControls = true


        progressDialog = ProgressDialog(this@ViewStatementActivity, R.style.Progress)
        progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
        progressDialog!!.setMessage("Loading please wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setIndeterminateDrawable(resources.getDrawable(R.drawable.progress))
        progressDialog!!.show()


        //        if(attachedfile.indexOf(".jpg")!=-1||attachedfile.indexOf(".jpeg")!=-1||attachedfile.indexOf(".png")!=-1||attachedfile.indexOf(".gif")!=-1) {
//            webView.setWebViewClient(new SSLTolerentWebViewClient());
//            webView.loadUrl(attachedfile);
//        }else {
//            String url = "http://docs.google.com/viewer?embedded=true&url=" + attachedfile;
//            webView.setWebViewClient(new SSLTolerentWebViewClient());
//            webView.loadUrl(url);
//        }
        //        if(attachedfile.indexOf(".jpg")!=-1||attachedfile.indexOf(".jpeg")!=-1||attachedfile.indexOf(".png")!=-1||attachedfile.indexOf(".gif")!=-1) {
//            webView.setWebViewClient(new SSLTolerentWebViewClient());
//            webView.loadUrl(attachedfile);
//        }else {
//            String url = "http://docs.google.com/viewer?embedded=true&url=" + attachedfile;
//            webView.setWebViewClient(new SSLTolerentWebViewClient());
//            webView.loadUrl(url);
//        }
        val url = "http://docs.google.com/viewer?embedded=true&url=$attachedfile"
        Log.e(TAG,"79 url    "+url)
        webView!!.webViewClient = object : SSLTolerentWebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (webView!!.progress == 100) {
                    progressDialog!!.dismiss()
                    webView!!.visibility = View.VISIBLE
                  //  printWeb = webView

                //    downloadLis()
                }
            }
        }
        webView!!.loadUrl(url)
        Handler().postDelayed({
            //  progressDialog.dismiss();
        }, 3000)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

//        webView!!.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
//            val request = DownloadManager.Request(Uri.parse(url))
//            request.setMimeType(mimeType)
//            //------------------------COOKIE!!------------------------
////            val cookies: String = CookieManager.getInstance().getCookie(url)
////            request.addRequestHeader("cookie", cookies)
////            //------------------------COOKIE!!------------------------
////            request.addRequestHeader("User-Agent", userAgent)
//            request.setDescription("Downloading file...")
//            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
//            request.allowScanningByMediaScanner()
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalPublicDir(
//                Environment.DIRECTORY_DOWNLOADS,
//                URLUtil.guessFileName(url, contentDisposition, mimeType)
//            )
//            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//            dm.enqueue(request)
//            Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG).show()
//        })


//        webView!!.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                // initializing the printWeb Object
//                printWeb = webView
//            }
//        }



        ////////////////////




    }

    private fun downloadLis() {
        webView!!.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(
                Uri.parse(url)
            )
            val mimeType: String? = null
            request.setMimeType(mimeType)
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(url, contentDisposition, mimeType)
            )
            val dm = applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT) //This is important!
            intent.addCategory(Intent.CATEGORY_OPENABLE) //CATEGORY.OPENABLE
            intent.type = "*/*" //any application,any extension
            Toast.makeText(
                applicationContext,
                "Downloading File",  //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG
            ).show()
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private open class SSLTolerentWebViewClient : WebViewClient() {
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }
    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        webView = findViewById<WebView>(R.id.webView)
        pdfView = findViewById<PDFView>(R.id.pdfView)

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
//                printBtnPressed = true;
//                if (printWeb != null) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        // Calling createWebPrintJob()
//                        PrintTheWebPage(printWeb!!);
//                    } else {
//                        // Showing Toast message to user
//                        Toast.makeText(this@ViewStatementActivity, "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    // Showing Toast message to user
//                    Toast.makeText(this@ViewStatementActivity, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    private fun PrintTheWebPage(webView: WebView) {
//
//        // set printBtnPressed true
//      //  printBtnPressed = true
//
//        // Creating  PrintManager instance
//        val printManager = this.getSystemService(Context.PRINT_SERVICE) as PrintManager
//
//        // setting the name of job
//        val jobName = getString(R.string.app_name) + " webpage" + webView.url
//
//        // Creating  PrintDocumentAdapter instance
//        val printAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.createPrintDocumentAdapter(jobName)
//        } else {
//            TODO("VERSION.SDK_INT < LOLLIPOP")
//        }
//        assert(printManager != null)
//        printJob = printManager.print(
//            jobName, printAdapter,
//            PrintAttributes.Builder().build()
//        )
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onResume() {
//        super.onResume()
//        if (printJob != null && printBtnPressed) {
//            if (printJob!!.isCompleted) {
//                // Showing Toast Message
//                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
//            } else if (printJob!!.isStarted) {
//                // Showing Toast Message
//                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show()
//            } else if (printJob!!.isBlocked) {
//                // Showing Toast Message
//                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show()
//            } else if (printJob!!.isCancelled) {
//                // Showing Toast Message
//                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show()
//            } else if (printJob!!.isFailed) {
//                // Showing Toast Message
//                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show()
//            } else if (printJob!!.isQueued) {
//                // Showing Toast Message
//                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show()
//            }
//            // set printBtnPressed false
//            printBtnPressed = false
//        }
//    }

}