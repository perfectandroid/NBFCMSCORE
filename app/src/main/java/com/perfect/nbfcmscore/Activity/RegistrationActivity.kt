package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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

class RegistrationActivity : AppCompatActivity()  , View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    var accno  = ""
    var tvlogin: TextView? = null
    var etxt_accno: EditText? = null
    var etxt_mob: EditText? = null
    var txtv_lets:TextView?=null
    var txv_plz:TextView?=null
    var btreg:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val tv_product_name: TextView = findViewById(R.id.tv_product_name)
//        Glide.with(this).load(R.drawable.login_reg_gif).into(imgLogo)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val ProductNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF12,0)
//        var IMAGRURL = Config.IMAGE_URL+AppIconImageCodeSP.getString("AppIconImageCode",null)
        val imagepath = Config.IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

       // Glide.with(this).load(IMAGRURL).placeholder(R.drawable.login_reg_gif).into(imgLogo);
//        Glide.with(this).load(IMAGRURL).placeholder(null).into(imgLogo);
        PicassoTrustAll.getInstance(this@RegistrationActivity)!!.load(imagepath).error(android.R.color.transparent).into(imgLogo!!)
        tv_product_name!!.setText(ProductNameSP.getString("ProductName",null))

        setRegViews()

        val ID_LetsSP = applicationContext.getSharedPreferences(Config.SHARED_PREF40,0)
        val ID_PersnlinfSP = applicationContext.getSharedPreferences(Config.SHARED_PREF41,0)
        val ID_EntermobSP = applicationContext.getSharedPreferences(Config.SHARED_PREF42,0)
     //   val ID_last4SP = applicationContext.getSharedPreferences(Config.SHARED_PREF43,0)
        val ID_ContinueSP = applicationContext.getSharedPreferences(Config.SHARED_PREF44,0)

        txtv_lets!!.setText(ID_LetsSP.getString("Let'sgetstarted",null))
        txv_plz!!.setText(ID_PersnlinfSP.getString("pleaseenteryourpersonalinformation",null))
        etxt_mob!!.setHint(ID_EntermobSP.getString("entermobilenumber",null))
    //    etxt_accno!!.setText(ID_last4SP.getString("enter last4digitofa/cno",null))
        btreg!!.setText(ID_ContinueSP.getString("continue",null))

        Log.i("commit","Test")
    }

    private fun setRegViews() {
         tvlogin = findViewById<TextView>(R.id.tvlogin) as TextView
         txtv_lets = findViewById<TextView>(R.id.txtv_lets) as TextView
         txv_plz = findViewById<TextView>(R.id.txv_plz) as TextView
         btreg = findViewById<Button>(R.id.btreg) as Button
         etxt_mob = findViewById<EditText>(R.id.etxt_mob) as EditText
         etxt_accno = findViewById<EditText>(R.id.etxt_accno) as EditText
         tvlogin!!.setOnClickListener(this)
         btreg!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btreg->{
                Config.Utils.hideSoftKeyBoard(this@RegistrationActivity,v)
                validation()
            }
            R.id.tvlogin-> {
                intent = Intent(applicationContext, LoginActivity::class.java)
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
        }
        else if (etxt_accno!!.text.toString() == null || etxt_accno!!.text.toString().isEmpty()) {
            etxt_accno!!.setError("Please Enter Last Four Digit Of Your Account Number")
        }
        else if (etxt_accno!!.text.toString().isNotEmpty() && etxt_accno!!.text.toString().length!=4) {
            etxt_accno!!.setError("Enter Last 4 Digit Of A/C No.")
        }else{
        /*    etxt_mob!!.text=null
            etxt_accno!!.text=null*/
            getRegister()
        }
    }

    private fun getRegister() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF135, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RegistrationActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@RegistrationActivity))
                        .hostnameVerifier(Config.getHostnameVerifier())
                        .build()
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build()
                    val apiService = retrofit.create(ApiInterface::class.java!!)
                    val requestObject1 = JSONObject()
                    try {
                        requestObject1.put("MobileNumber", MscoreApplication.encryptStart(etxt_mob!!.text.toString()))
                        requestObject1.put("AccountNumber", MscoreApplication.encryptStart(etxt_accno!!.text.toString()))
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
                    val call = apiService.getregistration(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jobjt = jObject.getJSONObject("CustomerRegistration")
                                    val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
                                    builder.setMessage(""+jobjt.getString("ResponseMessage"))
                                    builder.setPositiveButton("Ok"){dialogInterface, which ->

                                        val jobjt = jObject.getJSONObject("CustomerRegistration")
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
                                    val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
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
                                val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
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
                            val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
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
                    val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            false -> {
                val builder = AlertDialog.Builder(this@RegistrationActivity, R.style.MyDialogTheme)
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