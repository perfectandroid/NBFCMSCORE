package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.aceware.cobrandprepaidkit.CobrandPrepaidSdkkit
import com.aceware.cobrandprepaidkit.CobrandPrepaidSdkkit.ResponseListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import com.sun.mail.imap.protocol.FLAGS
import `in`.aabhasjindal.otptextview.OtpTextView
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Kyc : AppCompatActivity(), View.OnClickListener {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var cobrandPrepaidSdkkit: CobrandPrepaidSdkkit? = null
    private var upiBankCode: String = ""
    private var mobile: String = ""
    private var strAgentEmail: String = ""
    var textDialogue: TextView? = null
    var btlogin: TextView? = null
    var lin_start: LinearLayout? = null
    var TAG = "KYC MAIN"
    private var progressDialog: ProgressDialog? = null
    var webview: WebView? = null
    var constrWebview: ConstraintLayout? = null
    private lateinit var dialog1: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc)
        setId()
        cobrandPrepaidSdkkit = CobrandPrepaidSdkkit(this)
        val AceMoneyUpiBankCodeSP =
            applicationContext.getSharedPreferences(Config.SHARED_PREF350, 0)
        upiBankCode = AceMoneyUpiBankCodeSP.getString("AceMoneyUpiBankCode", "").toString()
        val mobileNoSP = getSharedPreferences(Config.SHARED_PREF2, 0)
        mobile = mobileNoSP.getString("CusMobile", "").toString()
        val AgentEmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF351, 0)
        strAgentEmail = AgentEmailSP.getString("AgentEmail", "").toString()


        mobile = "8075115147"
        upiBankCode = "ATA000823"
        strAgentEmail = "kscbp373@gmail.com"
    }

    private fun setId() {
        imgBack = findViewById(R.id.imgBack)
        imgHome = findViewById(R.id.imgHome)
        constrWebview = findViewById<ConstraintLayout>(R.id.constrWebview)
        webview = findViewById(R.id.webview)
        lin_start = findViewById(R.id.lin_start)
        btlogin = findViewById(R.id.btlogin)


        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
        btlogin!!.setOnClickListener(this)
    }

    private fun prepaidCardExistCheck() {
        Log.v("KycSteps", "prepaidCardExistCheck in")
        val map = ArrayList<String>()
        map.add(mobile)
        val dialog1: Dialog? = showLoadingDialog("Checking prepaid card registration")
        Log.v("KycSteps", "map " + map)
        Log.v("KycSteps", "upiBankCode " + upiBankCode)
        cobrandPrepaidSdkkit!!.initService(upiBankCode, "creditCardUserExist", map)
        cobrandPrepaidSdkkit!!.setResponseCall(object : ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {
                //json {"status":true,"meta":{"code":201,"message":"success","plan_type":"0"},"message":"Success","data":[{"message":"User Found"}],"errors":[]}
                Log.e(TAG, "s_card check json $jsonObject")
                Log.e(TAG, "s_card check s $s")
                if (jsonObject["status"].asBoolean == true) {
//                    prepaidCardExist();
                    // TODO: 10-05-2023 please uncomment prepaid card exist and comment aadharDetails
                    aadharDetails(dialog1)
                } else {
                    aadharDetails(dialog1)
                }
            }

            override fun onFailure(s: String, jsonObject: JsonObject) {
                progressDialog?.dismiss()
                Log.e(TAG, "f_card check json $jsonObject")
                dialog1!!.dismiss()
                try {
                    val builder = AlertDialog.Builder(this@Kyc)
                    builder.setMessage("" + jsonObject["message"])
                        .setPositiveButton(
                            "Ok"
                        ) { dialog, which -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                } catch (e: Exception) {
                    Toast.makeText(this@Kyc, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun aadharDetails(dialog1: Dialog?) {
        textDialogue!!.text = "Loading Digilocker"
        //strAgentEmail:kscbp373@gmail.com
        //strAgentEmail:kscbp373@gmail.com
        val map = java.util.ArrayList<String>()
        map.add(strAgentEmail!!)
        cobrandPrepaidSdkkit!!.initService(upiBankCode, "DigiLockerAadharDetails", map)
        cobrandPrepaidSdkkit!!.setResponseCall(object : ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {
                dialog1!!.dismiss()
                progressDialog?.dismiss()
                //                {"status":true,"message":"Success","requestId":"b4990da6be1816659dd5aed04621dbfb","url":"https://api.digitallocker.gov.in/public/oauth2/1/authorize?response_type=code&client_id=CE07F040&redirect_uri=https://sandbox.kyckart.com/page/digilocker-auth-complete&state=b4990da6be1816659dd5aed04621dbfb","aadhar":""}
                Log.e(TAG, "s_aadharDetails json $jsonObject")
                Log.e(TAG, "s_aadharDetails s $s")
                val url = jsonObject["url"].asString
                Log.e(TAG, "s_aadharDetails url $url")
                if (jsonObject["status"].asBoolean == true) {
                    loadWebView(jsonObject["url"].asString)
                } else {
                    dialog1!!.dismiss()
                    progressDialog?.dismiss()
                }
            }

            override fun onFailure(s: String, jsonObject: JsonObject) {
                dialog1!!.dismiss()
                progressDialog?.dismiss()
                Log.e(TAG, "F_aadharDetails json $jsonObject")
                Log.e(TAG, "F_aadharDetails s $s")
                //   loadWebView("http://www.tutorialspoint.com");
            }
        })
    }

    private fun loadWebView(url: String) {
        constrWebview?.setVisibility(View.VISIBLE)
        lin_start?.setVisibility(View.GONE)
        webview?.loadUrl(url)
        val webSettings: WebSettings = webview!!.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webview!!.setWebViewClient(MyWebViewClient(this@Kyc))
    }

    fun showLoadingDialog(msg: String?): Dialog? {
        progressDialog = ProgressDialog(this@Kyc, R.style.Progress)
        progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setIndeterminate(true)
        progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
        progressDialog!!.show()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_loading)
        textDialogue = dialog.findViewById<TextView>(R.id.text)
        textDialogue!!.setText(msg)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.show()
        return dialog
    }

    private class MyWebViewClient(kyc: Kyc) : WebViewClient() {
        val kyc = kyc
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            Log.v("sfsdfsdfdd", "shouldOverrideUrlLoading $url")
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
//            {"status":true,"message":"Success","name":"Nihal P A","dob":"02-04-1993","address":"S\/O Pavithran, Kizhakepurayil, Ekarool P O, , , Unnikulam, Kozhikode, Kerala, 673574","aadhar":"","gender":"Male","city":"Unnikulam","pincode":"673574"}


            //   Log.e(TAG, "onPageFinished url $url")
            if (url.contains("aceneobank")) {
                view!!.setVisibility(View.GONE)

                JsonTask(kyc).execute(url)
            }
        }


    }

    class JsonTask(mContext: Context) : AsyncTask<String?, String?, String?>() {
        private lateinit var dialog1: Dialog
        private var progressDialog: ProgressDialog? = null
        var mContext = mContext
        override fun doInBackground(vararg p0: String?): String? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(p0[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val stream = connection!!.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                val buffer = StringBuffer()
                var line = ""
                try {
                    while (reader.readLine().also { line = it } != null) {
                        Log.v("dfsfsdfsdddd", "reader " + reader.readLine())
                        buffer.append(
                            """
                        $line
                        
                        """.trimIndent()
                        )
                        Log.v("dfsfsdfsdddd", "reader " + line)
                        //  Log.d("Response: ", "> $line") //here u ll get whole response...... :-)
                    }


                } catch (e: Exception) {

                }
                return buffer.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialog1 =
                showLoadingDialog("Fetching data for registering prepaid card user.", mContext)
            Log.v("dfsfsdfsdddd", "pre execute")
        }

        private fun showLoadingDialog(msg: String, mContext: Context): Dialog {
            progressDialog = ProgressDialog(mContext, R.style.Progress)
            progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setIndeterminate(true)
            progressDialog!!.setIndeterminateDrawable(mContext.resources.getDrawable(R.drawable.progress))
            progressDialog!!.show()
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_loading)
            var textDialogue = dialog.findViewById<TextView>(R.id.text)
            textDialogue!!.setText(msg)
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
            dialog.show()
            return dialog
        }

        override fun onPostExecute(result: String?) {
            Log.v("dfsfsdfsdddd", "post execute")
            super.onPostExecute(result)
            Log.e("onPostExecute", "json value  $result")
            try {
                val obj = JSONObject(result)
                val status = obj.getBoolean("status")
                Log.e("KYC MAIN", "status " + status)
                if (status) {
                    progressDialog!!.dismiss()
                    dialog1.dismiss()
                    var intent = Intent(mContext, Kyc2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("obj", obj.toString())
                    mContext.startActivity(intent)

                } else {
                    progressDialog!!.dismiss()
                    dialog1.dismiss()
                    Toast.makeText(
                        mContext,
                        "Alert: " + obj.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: JSONException) {
//                dialog1.dismiss()
                throw RuntimeException(e)
            }
            //            txtJson.setText(result);
        }

    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@Kyc, HomeActivity::class.java))
            }
            R.id.btlogin -> {

                prepaidCardExistCheck()
//                var url =
//                    "https://aceneobank.com/api/callback/get/aadhardetails?state=8b0ead52b9633a157209469c90229471&confirmAuthorization=true&taskId="
//                JsonTask(this@Kyc).execute(url)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        constrWebview?.visibility=View.GONE
        lin_start?.visibility=View.VISIBLE
    }


}