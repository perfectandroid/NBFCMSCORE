package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RechargeActivity : AppCompatActivity() , View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "RechargeActivity"
    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null

    var tie_mobilenumber: TextInputEditText? = null
    var tie_operator: TextInputEditText? = null

    var jArrayOperator: JSONArray? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)

        setInitialise()
        setRegister()

        if(intent.getStringExtra("from")!!.equals("prepaid")){

            tv_header!!.text = "Prepaid Mobile"
        }
        if(intent.getStringExtra("from")!!.equals("postpaid")){
            tv_header!!.text = "Postpaid Mobile"
        }
        if(intent.getStringExtra("from")!!.equals("landline")){
            tv_header!!.text = "Landline"
        }
        if(intent.getStringExtra("from")!!.equals("dth")){
            tv_header!!.text = "DTH"
        }


    }



    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_header = findViewById<TextView>(R.id.tv_header)

        tie_mobilenumber = findViewById<TextInputEditText>(R.id.tie_mobilenumber)
        tie_operator = findViewById<TextInputEditText>(R.id.tie_operator)




    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        tie_operator!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
               // onBackPressed()
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }
            R.id.tie_operator ->{
              //  tie_operator!!.setText("Airtel")
                Log.e(TAG,"tie_operator")
                getOperator()
//                val dialog = BottomSheetDialog(this)
//
//                // on below line we are inflating a layout file which we have created.
//                val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
//
//                val tv_Close = view.findViewById<TextView>(R.id.tv_Close)
//
//                tv_Close.setOnClickListener {
//                    tie_operator!!.setText("Airtel")
//                    dialog.dismiss()
//                }
//
//                dialog.setCancelable(true)
//
//                dialog.setContentView(view)
//
//                dialog.show()
            }
        }
    }


    private fun getOperator() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@RechargeActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("17"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  434   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  2161   "+e.toString())
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
                    val call = apiService.getProvidersList(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  4342   "+response.body())
                                Log.e(TAG,"response  4343   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
                                //    OperatorbottomSheet(jArrayOperator!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@RechargeActivity,
                                    R.style.MyDialogTheme
                                )
                                builder.setMessage("Some technical issues.")
                                builder.setPositiveButton("Ok") { dialogInterface, which ->
                                }
                                val alertDialog: AlertDialog = builder.create()
                                alertDialog.setCancelable(false)
                                alertDialog.show()
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
                            val builder = AlertDialog.Builder(
                                this@RechargeActivity,
                                R.style.MyDialogTheme
                            )
                            builder.setMessage("Some technical issues.")
                            builder.setPositiveButton("Ok") { dialogInterface, which ->
                            }
                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                    builder.setMessage("Some technical issues.")
                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    e.printStackTrace()
                }
            }
            false -> {

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun OperatorbottomSheet(jArrayOperator: JSONArray) {
        Log.e(TAG,"jArrayOperator  272     "+jArrayOperator)

        val dialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.operator_bottom_sheet, null)

     //   val tv_Close = view.findViewById<TextView>(R.id.tv_Close)
        val rvOperator = view.findViewById<RecyclerView>(R.id.tv_Close)

//        val lLayout = GridLayoutManager(this@RechargeActivity, 1)
//        rvOperator.setLayoutManager(lLayout)
//        rvOperator.setHasFixedSize(true)
//        val obj_adapter = BranchListAdapter(applicationContext!!, jArrayOperator!!)
//        rvOperator!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
//        rvOperator!!.adapter = obj_adapter
     //   obj_adapter.setClickListener(this@RechargeActivity)
//
//        tv_Close.setOnClickListener {
//            tie_operator!!.setText("Airtel")
//            dialog.dismiss()
//        }

        dialog.setCancelable(true)

        dialog.setContentView(view)

        dialog.show()
    }
}