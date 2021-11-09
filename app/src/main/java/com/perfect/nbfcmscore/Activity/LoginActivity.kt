package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
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
    var etxt_mob: EditText? = null
    var btlogin: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setRegViews()

        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
        val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

            //  Glide.with(this).load(IMAGRURL).placeholder(R.drawable.login_icon).into(imgLogo);
       // Glide.with(this).load(IMAGRURL).placeholder(null).into(imgLogo);
        PicassoTrustAll.getInstance(this@LoginActivity)!!.load(imagepath).error(android.R.color.transparent).into(imgLogo!!)
        tv_product_name!!.setText(ProductNameSP.getString("ProductName",null))

    }

    private fun setRegViews() {
        val tvforgetpassword = findViewById<TextView>(R.id.tvforgetpassword) as TextView
        etxt_mob = findViewById<EditText>(R.id.etxt_mob) as EditText
        btlogin = findViewById<Button>(R.id.btlogin) as Button
        tvforgetpassword!!.setOnClickListener(this)
        btlogin!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btlogin->{
                validation()
            }
            R.id.tvforgetpassword-> {
                intent = Intent(applicationContext, ForgetActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validation() {
        if (etxt_mob!!.text.toString() == null || etxt_mob!!.text.toString().isEmpty()) {
            etxt_mob!!.setError("Please Enter Mobile Number")
        }
        else if (etxt_mob!!.text.toString().isNotEmpty() && etxt_mob!!.text.toString().length!=10) {
            etxt_mob!!.setError("Please Enter Valid Mobile Number")
        }else{
            //etxt_mob!!.text=null
            getlogin()
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("6"))
                        requestObject1.put("MobileNumber", MscoreApplication.encryptStart(etxt_mob!!.text.toString()))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
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
                    val call = apiService.getCustomerLoginVerification(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("CustomerLoginVerification")
                                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                    builder.setMessage(""+jobjt.getString("ResponseMessage"))
                                    builder.setPositiveButton("Ok"){dialogInterface, which ->

                                        val jobjt = jObject.getJSONObject("CustomerLoginVerification")
                                        intent = Intent(applicationContext, OTPActivity::class.java)
                                        intent.putExtra("FK_Customer", jobjt.getString("FK_Customer"))
                                        intent.putExtra("Token", jobjt.getString("Token"))
                                        intent.putExtra("CusMobile", jobjt.getString("CusMobile"))
                                        startActivity(intent)
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                } else {
                                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                    builder.setMessage(""+jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            }
                            catch (e: Exception) {
                                progressDialog!!.dismiss()
                                val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok"){dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok"){dialogInterface, which ->
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })

                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    e.printStackTrace()
                    val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            false -> {
                val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok"){dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

}