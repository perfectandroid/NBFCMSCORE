package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Model.Beneflist
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class DepositCalculatorActivity : AppCompatActivity(),View.OnClickListener,AdapterView.OnItemSelectedListener {

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    private var jresult: JSONArray? = null
    var spn_deposit_type: Spinner? = null
    var spn_beneficiary: Spinner? = null
    var spn_tenure: Spinner? = null
    var peroidtype: String? = null
    var llOutput: LinearLayout? = null
    var beneficiary: String? = null
    private var progressDialog: ProgressDialog? = null
    var txt_amtinword: TextView? = null
    var etxt_amount: EditText? = null
    var edt_txt_tenure: EditText? = null
    var tv_type: TextView? = null
    var tv_period: TextView? = null
    var tv_rateofinterest: TextView? = null
    var tv_amt: TextView? = null
    var tv_maturityamt: TextView? = null

    var btn_submit: Button? = null
    var tv_header: TextView? = null
    var submodule: String? = null
    var benefid: String? = null
    var deposittype = arrayOfNulls<String>(0)
    var benefcry = arrayOfNulls<String>(0)
    var tenure = arrayOfNulls<String>(0)
    public var arrayList1: ArrayList<Beneflist>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        setRegViews()



    }



    private fun setRegViews() {
        imgBack = findViewById(R.id.imgBack)
        imgHome = findViewById(R.id.imgHome)
        txt_amtinword= findViewById(R.id.txt_amtinword)

        spn_deposit_type = findViewById(R.id.spn_deposit_type)
        spn_beneficiary = findViewById(R.id.spn_beneficiary)
        spn_tenure = findViewById(R.id.spn_tenure)

        etxt_amount = findViewById(R.id.etxt_amount)
        edt_txt_tenure = findViewById(R.id.edt_txt_tenure)

        spn_deposit_type = findViewById(R.id.spn_deposit_type)

        tv_header = findViewById(R.id.tv_header)
        llOutput = findViewById(R.id.llOutput)

        btn_submit = findViewById(R.id.btn_submit)

        btn_submit!!.setOnClickListener(this)

        tv_type = findViewById(R.id.tv_type)
        tv_period = findViewById(R.id.tv_period)
        tv_rateofinterest = findViewById(R.id.tv_rateofinterest)
        tv_amt = findViewById(R.id.tv_amt)
        tv_maturityamt = findViewById(R.id.tv_maturityamt)



        spn_deposit_type!!.setOnItemSelectedListener(this)
        spn_beneficiary!!.setOnItemSelectedListener(this)
        spn_tenure!!.setOnItemSelectedListener(this)

        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)

        deposittype = arrayOf<String?>("Fixed Deposit", "Cumulative Deposit")
        getDepositType()

        benefcry = arrayOf<String?>("Normal", "Senior Citizen")
        getBenefcryType()

        tenure = arrayOf<String?>("Month", "Day")
        getTenure()

        etxt_amount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                etxt_amount!!.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString != "") {
                        val longval: Double
                        if (originalString.contains(",")) {
                            originalString = originalString.replace(",".toRegex(), "")
                        }
                        longval = originalString.toDouble()

//                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                        formatter.applyPattern("#,###,###,###");
//                        String formattedString = formatter.format(longval);
                        val formattedString: String? =
                                Config!!.getDecimelFormateForEditText(longval)
                        //
                        //setting text after format to EditText
                        etxt_amount!!.setText(formattedString)
                        etxt_amount!!.setSelection(etxt_amount!!.getText().length)
                        val amnt: String = etxt_amount!!.getText().toString().replace(
                                ",".toRegex(),
                                ""
                        )
                        val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()
                        var amountInWordPop = ""
                        if (netAmountArr[0].toInt() != 0) {
                            if (netAmountArr.size > 0) {
                                val integerValue = netAmountArr[0].toInt()
                                amountInWordPop = "Rupees " + NumberToWord.convertNumberToWords(
                                        integerValue
                                )
                                if (netAmountArr.size > 1) {
                                    val decimalValue = netAmountArr[1].toInt()
                                    if (decimalValue != 0) {
                                        amountInWordPop += " and " + NumberToWord.convertNumberToWords(
                                                decimalValue
                                        ).toString() + " paise"
                                    }
                                }
                                amountInWordPop += " only"
                            }
                            txt_amtinword!!.setText("" + amountInWordPop)
                        }
                    } else {
                        txt_amtinword!!.setText("")
                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                etxt_amount!!.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    if (s.length != 0) {
                        var originalString = s.toString()
                        if (originalString.contains(",")) {
                            originalString = originalString.replace(",".toRegex(), "")
                        }
                        val num = ("" + originalString).toDouble()

                        // btn_submit!!.setText(PAYSP.getString("PAY", null)+ "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        // btn_submit!!.setText(PAYSP.getString("PAY", null))
                    }
                } catch (e: NumberFormatException) {
                }
            }
        })

    }

    private fun getTenure() {
        val cc: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, tenure)
        cc.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_tenure!!.adapter = cc
    }

    private fun getBenefcryType() {
      /*  val bb: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, benefcry)
        bb.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_beneficiary!!.adapter = bb
*/
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {

            true -> {
                /*  progressDialog = ProgressDialog(this@PassbookActivity, R.style.Progress)
                  progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                  progressDialog!!.setCancelable(false)
                  progressDialog!!.setIndeterminate(true)
                  progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                  progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DepositCalculatorActivity))
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("47"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )

                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
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
                    val call = apiService.getBenefdetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Responsebeneficiary", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("BenefitDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    val jresult = jsonobj2.getJSONArray("BenefitDetailsList")
                                    arrayList1 = ArrayList<Beneflist>()
                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            arrayList1!!.add(
                                                    Beneflist(
                                                            json.getString("ID_Benefit"),
                                                            json.getString(
                                                                    "BenName"
                                                            ))

                                            )

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spn_beneficiary!!.adapter = ArrayAdapter(
                                            this@DepositCalculatorActivity,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1!!
                                    )

                                    //    spn_account_num!!.setSelection(arrayList1.indexOf("Select Account"));

                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@DepositCalculatorActivity,
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
                                //  progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@DepositCalculatorActivity,
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
                            //  progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    this@DepositCalculatorActivity,
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
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                            this@DepositCalculatorActivity,
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
            false -> {
                val builder = AlertDialog.Builder(
                        this@DepositCalculatorActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun getDepositType() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, deposittype)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_deposit_type!!.adapter = aa
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@DepositCalculatorActivity, HomeActivity::class.java))
            }
            R.id.btn_submit -> {
                if (isValid()) {

                    getDepositCalculatr()
                }
            }

        }

    }

    private fun getDepositCalculatr() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {

            true -> {
                  progressDialog = ProgressDialog(this@DepositCalculatorActivity, R.style.Progress)
                  progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                  progressDialog!!.setCancelable(false)
                  progressDialog!!.setIndeterminate(true)
                  progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                  progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@DepositCalculatorActivity))
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        //requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(submodule)
                        )
                        requestObject1.put(
                                "PeriodType",
                                MscoreApplication.encryptStart(peroidtype)
                        )
                        requestObject1.put(
                                "Period",
                                MscoreApplication.encryptStart(edt_txt_tenure!!.text.toString())
                        )
                        requestObject1.put(
                                "BenefitType",
                                MscoreApplication.encryptStart(benefid)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  deposit   " + requestObject1)
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
                    val call = apiService.getDepositcalcltr(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response Deposit", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("DepositCalculator")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())


                                    llOutput!!.visibility = View.VISIBLE

                                    tv_type!!.setText("" + jsonobj2.getString("TypeName"))
                                    tv_period!!.setText("" + jsonObj1.getString("Period"))
                                    tv_rateofinterest!!.setText("" + jsonObj1.getString("ROI"))
                                    tv_amt!!.setText("" + jsonObj1.getString("Amount"))
                                    tv_maturityamt!!.setText("" + jsonObj1.getString("MaturityAmount"))


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@DepositCalculatorActivity,
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

                                val builder = AlertDialog.Builder(
                                        this@DepositCalculatorActivity,
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

                            val builder = AlertDialog.Builder(
                                    this@DepositCalculatorActivity,
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
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                            this@DepositCalculatorActivity,
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
            false -> {
                val builder = AlertDialog.Builder(
                        this@DepositCalculatorActivity,
                        R.style.MyDialogTheme
                )
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun isValid(): Boolean {

        val amt = etxt_amount!!.text.toString()
        val tnr = edt_txt_tenure!!.text.toString()


        if (TextUtils.isEmpty(amt)) {
            etxt_amount!!.error = "Please Enter Amount"
            return false
        }
        etxt_amount!!.setError(null)

        if (TextUtils.isEmpty(tnr)) {
            edt_txt_tenure!!.setError("Please Enter A Value")
            return false
        }

        etxt_amount!!.setError(null)


        return true

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {


        //tenure
        if(spn_tenure!!.selectedItem.toString().equals("Day"))
        {
            edt_txt_tenure!!.setHint("Please Enter Day")
            peroidtype = "D"
        }
        else if(spn_tenure!!.selectedItem.toString().equals("Month"))
        {
            edt_txt_tenure!!.setHint("Please Enter Month")
            peroidtype = "M"
        }

        //deposit
        if(spn_deposit_type!!.selectedItem.toString().equals("Fixed Deposit"))
        {
            submodule = "TDFD"
        }
        else if(spn_deposit_type!!.selectedItem.toString().equals("Cumulative Deposit"))
        {
            submodule = "TDCC"
        }
        val beneflist: Beneflist = arrayList1!!.get(position)
        benefid = beneflist.getID_Benefit()

        //benefiuciary

        /*if(spn_beneficiary!!.selectedItem.toString().equals("Normal"))
        {
            beneficiary = "1"
        }
        else if(spn_beneficiary!!.selectedItem.toString().equals("Senior Citizen"))
        {
            beneficiary = "2"
        }*/


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}