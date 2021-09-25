package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.bizcorelite.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import javax.net.ssl.*

class LoginActivity : AppCompatActivity() , View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setRegViews()
    }

    private fun setRegViews() {
        val tvforgetpassword = findViewById<TextView>(R.id.tvforgetpassword) as TextView
        val btlogin = findViewById<Button>(R.id.btlogin) as Button
        tvforgetpassword!!.setOnClickListener(this)
        btlogin!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                getlogin()
                intent = Intent(applicationContext, OTPActivity::class.java)
                startActivity(intent)
            }
            R.id.tvforgetpassword-> {
                intent = Intent(applicationContext, ForgetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getlogin() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LoginActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@LoginActivity))
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(Config.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()

                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("MobileNumber", MscoreApplication.encryptStart(""))
                        requestObject1.put("AccountNumber", MscoreApplication.encryptStart("2454"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        val mySnackbar = Snackbar.make(
                            findViewById(R.id.rl_main),
                            " Some technical issues.", Snackbar.LENGTH_SHORT
                        )
                        mySnackbar.show()
                    }

                    val body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestObject1.toString()
                    )
                    //   val call = apiService.getLogin(body)
                    val call = apiService.getloginverification(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {




                                } else if (jObject.getString("StatusCode") == "-12") {


                                } else {
                                    // val jobjt = jObject.getJSONObject("LogInfo")
                                   /* val dialogBuilder = AlertDialog.Builder(
                                        this@LoginActivity,
                                        R.style.MyDialogTheme
                                    )
                                    dialogBuilder.setMessage(jObject.getString("EXMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(
                                            "OK",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                dialog.dismiss()

                                            })
                                    val alert = dialogBuilder.create()
                                    alert.show()
                                    val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
                                    pbutton.setTextColor(Color.MAGENTA)*/
                                }
                            }
                            catch (e: Exception) {
                                progressDialog!!.dismiss()
                               /* Log.e(TAG," 382   "+e.toString())
                                val mySnackbar = Snackbar.make(
                                    findViewById(R.id.rl_main),
                                    " Some technical issues.", Snackbar.LENGTH_SHORT
                                )
                                mySnackbar.show()*/
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                          //  Log.e(TAG," 394   "+t.toString())
                            val mySnackbar = Snackbar.make(
                                findViewById(R.id.rl_main),
                                " Some technical issues.", Snackbar.LENGTH_SHORT
                            )
                            mySnackbar.show()
                        }
                    })

                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    e.printStackTrace()
                   // Log.e(TAG," 406   "+e.toString())
                    val mySnackbar = Snackbar.make(
                        findViewById(R.id.rl_main),
                        " Some technical issues.", Snackbar.LENGTH_SHORT
                    )
                    mySnackbar.show()
                }
            }
            false -> {
                val mySnackbar = Snackbar.make(
                    findViewById(R.id.rl_main),
                    "No Internet Connection!!",
                    Snackbar.LENGTH_SHORT
                )
                mySnackbar.show()
            }
        }

    }

}