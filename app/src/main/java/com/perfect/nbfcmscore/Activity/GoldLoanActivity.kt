package com.perfect.nbfcmscore.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.GoldEstimatorAdaptor
import com.perfect.nbfcmscore.Adapter.GoldSlabEstimatorAdaptor
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.CustomBottomSheeet
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import lecho.lib.hellocharts.model.Line
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.ArrayList

class GoldLoanActivity : AppCompatActivity() , View.OnClickListener{

    val TAG: String? = "GoldLoanActivity"
    private var progressDialog: ProgressDialog? = null
    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var img_top: ImageView? = null

    var chk_amount: CheckBox? = null
    var chk_weight: CheckBox? = null
    var tie_amountweight: TextInputEditText? = null
    var til_amountweight: TextInputLayout? = null
    var but_calculate: Button? = null
    var but_reset: Button? = null
    var rv_goldestimator: RecyclerView? = null
    var ll_estimatelist: LinearLayout? = null
    var tv_weight: TextView? = null
    var tv_amount: TextView? = null
    var tv_header: TextView? = null

    var CalcMethod: String? = ""
    var Amount: String? = ""
    var Weight: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_loan)

        setInitialise()
        setRegister()

        val ResetSP = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        but_reset!!.setText(ResetSP.getString("RESET",null))

        val CalcSP = applicationContext.getSharedPreferences(Config.SHARED_PREF190,0)
        but_calculate!!.setText(CalcSP.getString("CALCULATE",null))

        val AmtSP = applicationContext.getSharedPreferences(Config.SHARED_PREF113,0)
        chk_amount!!.setText(AmtSP.getString("Amount",null))

        val goldSP = applicationContext.getSharedPreferences(Config.SHARED_PREF81,0)
        tv_header!!.setText(goldSP.getString("GoldLoanEligibileCalculator",null))




        CalcMethod = "2"
       // tie_amountweight!!.setHint("Enter Amount")
        til_amountweight!!.setHint("Enter Amount")
        img_top!!.setImageResource(R.drawable.imagemoney)


        tie_amountweight!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tie_amountweight!!.removeTextChangedListener(this)
                try {
                    if(CalcMethod.equals("2")){
                        var originalString = s.toString()
                        if (originalString != "") {
                            val longval: Double
                            if (originalString.contains(",")) {
                                originalString = originalString.replace(",".toRegex(), "")
                            }
                            longval = originalString.toDouble()

                            val formattedString: String? = Config!!.getDecimelFormateForEditText(longval)
                            tie_amountweight!!.setText(formattedString)
                            val selection: Int = tie_amountweight!!.length()
                            tie_amountweight!!.setSelection(selection)
                            // tie_amount!!.setSelection(tie_amount!!.getText().length)
                            val amnt: String = tie_amountweight!!.getText().toString().replace(",".toRegex(), "")
                            val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()

                        } else {
                            tie_amountweight!!.setText("")
                        }
                    }


                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                tie_amountweight!!.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if(CalcMethod.equals("2")){
                        if (s.length != 0) {
                            var originalString = s.toString()
                            if (originalString.contains(",")) {
                                originalString = originalString.replace(",".toRegex(), "")
                            }
                            val num = ("" + originalString).toDouble()
                            // btn_submit!!.setText("PAY  " + "\u20B9 " + Config.getDecimelFormate(num))
                        } else {
                            //  btn_submit!!.setText("PAY")
                        }
                    }

                } catch (e: NumberFormatException) {
                }
            }
        })



    }

    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        img_top = findViewById<ImageView>(R.id.img_top)

        tv_header = findViewById<TextView>(R.id.tv_header)


        chk_amount = findViewById<CheckBox>(R.id.chk_amount)
        chk_weight = findViewById<CheckBox>(R.id.chk_weight)
        tie_amountweight = findViewById<TextInputEditText>(R.id.tie_amountweight)
        til_amountweight = findViewById<TextInputLayout>(R.id.til_amountweight)
        but_calculate = findViewById<Button>(R.id.but_calculate)
        but_reset = findViewById<Button>(R.id.but_reset)

        rv_goldestimator = findViewById<RecyclerView>(R.id.rv_goldestimator)
        ll_estimatelist = findViewById<LinearLayout>(R.id.ll_estimatelist)

        tv_amount = findViewById<TextView>(R.id.tv_amount)
        tv_weight = findViewById<TextView>(R.id.tv_weight)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

        chk_amount!!.setOnClickListener(this)
        chk_weight!!.setOnClickListener(this)
        but_calculate!!.setOnClickListener(this)
        but_reset!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()
            }

            R.id.im_home ->{
                startActivity(Intent(this@GoldLoanActivity, HomeActivity::class.java))
                finish()
            }

            R.id.chk_amount ->{
                chk_amount!!.isChecked = true
                chk_weight!!.isChecked = false
                CalcMethod = "2"
                img_top!!.setImageResource(R.drawable.imagemoney)
                tie_amountweight!!.setText("")
              //  tie_amountweight!!.setHint("Enter Amount")
                til_amountweight!!.setHint("Enter Amount")
                ll_estimatelist!!.visibility = View.GONE
            }

            R.id.chk_weight ->{
                chk_weight!!.isChecked = true
                chk_amount!!.isChecked = false
                CalcMethod = "1"
                img_top!!.setImageResource(R.drawable.imagegold)
                tie_amountweight!!.setText("")
                //tie_amountweight!!.setHint("Enter Weight")
                til_amountweight!!.setHint("Enter Weight")
                ll_estimatelist!!.visibility = View.GONE
            }

            R.id.but_calculate ->{
                Config.Utils.hideSoftKeyBoard(this@GoldLoanActivity,v)
                ll_estimatelist!!.visibility = View.GONE
                Amount = tie_amountweight!!.text.toString().replace(",", "");
                if (Amount.equals("")){

                    if (CalcMethod.equals("1")){
                        CustomBottomSheeet.Show(this,"Please enter weight","0")
                    }else if (CalcMethod.equals("2")){
                        CustomBottomSheeet.Show(this,"Please enter amount","0")
                    }
                }else{

                    if ( chk_amount!!.isChecked){
                        Amount = tie_amountweight!!.text.toString().replace(",", "");
                        Weight = "0"
                    }
                    if ( chk_weight!!.isChecked){
                        Amount = "0"
                        Weight = tie_amountweight!!.text.toString()
                    }

                    GoldEstimatorDetails(Amount!!,Weight!!,CalcMethod!!)

                }
            }
            R.id.but_reset ->{

                chk_amount!!.isChecked = true
                chk_weight!!.isChecked = false
                CalcMethod = "2"
                // tie_amountweight!!.setHint("Enter Amount")
                til_amountweight!!.setHint("Enter Amount")
                img_top!!.setImageResource(R.drawable.imagemoney)
                tie_amountweight!!.setText("")
                ll_estimatelist!!.visibility = View.GONE

            }
        }
    }

    private fun GoldEstimatorDetails(Amount: String,Weight: String,CalcMethod: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {

                try {

                    progressDialog = ProgressDialog(this@GoldLoanActivity, R.style.Progress)
                    progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                    progressDialog!!.setCancelable(false)
                    progressDialog!!.setIndeterminate(true)
                    progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                    progressDialog!!.show()

                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@GoldLoanActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(
                            Config.SHARED_PREF8,
                            0
                        )
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                            Config.SHARED_PREF1,
                            0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                       // requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("CalcMethod", MscoreApplication.encryptStart(CalcMethod))
                        requestObject1.put("Weight", MscoreApplication.encryptStart(Weight))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(Amount))

//                        "Token":"kdydnsf","BankKey":"-500","FK_Customer":1772,
//                        "CalcMethod":"1","Weight":14,"Amount":"500"


                        Log.e(TAG,"requestObject1   179   "+requestObject1)


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
                    val call = apiService.getGoldEstimatorDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"response   202   "+response.body())
                                val jObject = JSONObject(response.body())

                                if (jObject.getString("StatusCode") == "0") {
                                    ll_estimatelist!!.visibility = View.VISIBLE
                                    if (CalcMethod.equals("1")){
                                        tv_amount!!.visibility = View.VISIBLE
                                        tv_weight!!.visibility = View.GONE
                                    }
                                    if (CalcMethod.equals("2")){

                                        tv_amount!!.visibility = View.GONE
                                        tv_weight!!.visibility = View.VISIBLE
                                    }
                                    val jobjt = jObject.getJSONObject("GoldEstimatorDetails")
                                    val jarray = jobjt.getJSONArray("GoldEstimatorDetailsList")


                                    val obj_adapter = GoldEstimatorAdaptor(applicationContext!!, jarray,CalcMethod!!)
                                    rv_goldestimator!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                                    rv_goldestimator!!.adapter = obj_adapter




                                } else {
                                    progressDialog!!.dismiss()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                }
            }
            false -> {
                progressDialog!!.dismiss()
                val builder = AlertDialog.Builder(this@GoldLoanActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }
}