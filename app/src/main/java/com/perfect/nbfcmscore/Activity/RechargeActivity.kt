package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountAdapter
import com.perfect.nbfcmscore.Adapter.CircleAdapter
import com.perfect.nbfcmscore.Adapter.OperatorAdapter
import com.perfect.nbfcmscore.Adapter.RecentRechargeAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RechargeActivity : AppCompatActivity() , View.OnClickListener, ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "RechargeActivity"
    private val PICK_CONTACT = 1
    private val REACHARGE_OFFER = 10

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var im_contact: ImageView? = null
    var im_offers: ImageView? = null

    var tv_header: TextView? = null

    var tie_mobilenumber: TextInputEditText? = null
    var tie_operator: TextInputEditText? = null
    var tie_circle: TextInputEditText? = null
    var tie_account: TextInputEditText? = null
    var tie_amount: TextInputEditText? = null

    var jArrayOperator: JSONArray? = null
    var jArrayCircle: JSONArray? = null
    var jArrayAccount: JSONArray? = null


    var  dialogOperator: BottomSheetDialog? = null
    var  dialogCircle: BottomSheetDialog? = null
    var  dialogAccount: BottomSheetDialog? = null

    var mobileNumber: String? = ""
    var Amount: String? = ""
    var ID_Providers: String? = ""
    var ID_RechargeCircle: String? = ""
    var ProvidersMode: String? = ""
    var CircleMode: String? = ""
    var AccountNo: String? = ""
    var SubModule: String? = ""
    var FK_Account: String? = ""
    var ProvidersCode: String? = ""
    var BranchName: String? = ""


    var rltv_recharge: RelativeLayout? = null
    var but_recharge: Button? = null
    var rvrecentRecharge: FullLenghRecyclertview? = null
    var jArrayRecent: JSONArray? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)

        setInitialise()
        setRegister()

        if(intent.getStringExtra("from")!!.equals("prepaid")){
            ProvidersMode = "1"
            tv_header!!.text = "Prepaid Mobile"
        }
        if(intent.getStringExtra("from")!!.equals("postpaid")){
            tv_header!!.text = "Postpaid Mobile"
            ProvidersMode = "4"
        }
        if(intent.getStringExtra("from")!!.equals("landline")){
            tv_header!!.text = "Landline"
            ProvidersMode = "3"
        }
        if(intent.getStringExtra("from")!!.equals("dth")){
            tv_header!!.text = "DTH"
            ProvidersMode = "2"
        }

        getRecentRecharges()


        tie_amount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tie_amount!!.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString != "") {
                        val longval: Double
                        if (originalString.contains(",")) {
                            originalString = originalString.replace(",".toRegex(), "")
                        }
                        longval = originalString.toDouble()

                        val formattedString: String? = Config!!.getDecimelFormateForEditText(longval)
                        tie_amount!!.setText(formattedString)
                        val selection: Int = tie_amount!!.length()
                        tie_amount!!.setSelection(selection)
                        // tie_amount!!.setSelection(tie_amount!!.getText().length)
                        val amnt: String = tie_amount!!.getText().toString().replace(",".toRegex(), "")
                        val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()

                    } else {
                        tie_amount!!.setText("")
                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                tie_amount!!.addTextChangedListener(this)
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
                        // btn_submit!!.setText("PAY  " + "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        //  btn_submit!!.setText("PAY")
                    }
                } catch (e: NumberFormatException) {
                }
            }
        })


    }



    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        im_contact = findViewById<ImageView>(R.id.im_contact)
        im_offers = findViewById<ImageView>(R.id.im_offers)

        tv_header = findViewById<TextView>(R.id.tv_header)

        tie_mobilenumber = findViewById<TextInputEditText>(R.id.tie_mobilenumber)
        tie_operator = findViewById<TextInputEditText>(R.id.tie_operator)
        tie_circle = findViewById<TextInputEditText>(R.id.tie_circle)
        tie_account = findViewById<TextInputEditText>(R.id.tie_account)

        tie_amount = findViewById<TextInputEditText>(R.id.tie_amount)
        but_recharge = findViewById<Button>(R.id.but_recharge)

        rvrecentRecharge = findViewById<FullLenghRecyclertview>(R.id.rvrecentRecharge)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        im_contact!!.setOnClickListener(this)
        im_offers!!.setOnClickListener(this)
        tie_operator!!.setOnClickListener(this)
        tie_circle!!.setOnClickListener(this)
        tie_account!!.setOnClickListener(this)
        but_recharge!!.setOnClickListener(this)

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
            }

            R.id.tie_circle ->{
                Log.e(TAG,"tie_circle")
                getCircle()
            }
            R.id.tie_account ->{
                Log.e(TAG,"tie_account")
                getOwnAccount()
            }
            R.id.im_contact ->{
                Log.e(TAG,"tie_account")
                contactSelect()
            }

            R.id.but_recharge ->{
                Log.e(TAG,"ll_recharge")
                rechargeValidation()
            }
            R.id.im_offers ->{
                Log.e(TAG,"im_offers")
                val operatorIdPref: SharedPreferences = applicationContext.getSharedPreferences(
                    Config.SHARED_PREF20,
                    0
                )
                val operatorIdEdit = operatorIdPref.edit()
                operatorIdEdit.putString("ID_Providers", "")
                operatorIdEdit.commit()
                if (!ID_Providers.equals("")) {

                    val operatorIdPref: SharedPreferences = applicationContext.getSharedPreferences(
                        Config.SHARED_PREF20,
                        0
                    )
                    val operatorIdEdit = operatorIdPref.edit()
                    operatorIdEdit.putString("ID_Providers", ID_Providers)
                    operatorIdEdit.commit()

                    val intent = Intent(this@RechargeActivity, RechargeOfferActivity::class.java)
                    intent.putExtra("ID_Providers", ID_Providers)
                    startActivityForResult(
                        intent,
                        REACHARGE_OFFER
                    ) // Activity is started with requestCode 2

                } else {
                    Toast.makeText(applicationContext, "Select Operator", Toast.LENGTH_LONG).show()
                }

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
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("17"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("ProvidersMode", MscoreApplication.encryptStart(ProvidersMode))

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
                                    OperatorbottomSheet(jArrayOperator!!)

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

    private fun getCircle() {
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
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("25"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  315   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  3151   "+e.toString())
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
                    val call = apiService.getRechargeCircleDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  3152   "+response.body())
                                Log.e(TAG,"response  3153   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("RechargeCircleDetails")
                                    jArrayCircle = jobjt.getJSONArray("RechargeCircleDetailsList")
                                    Log.e(TAG,"jArrayCircle  3154   "+jArrayCircle)
                                    CirclebottomSheet(jArrayCircle!!)

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

    private fun getOwnAccount() {
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))


                        Log.e(TAG,"requestObject1  516   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  5161   "+e.toString())
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
                    val call = apiService.getOwnAccounDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  5162   "+response.body())
                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    Log.e(TAG,"jArrayAccount  5164   "+jArrayAccount)
                                    AccountNobottomSheet(jArrayAccount!!)

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

    private fun CirclebottomSheet(jArrayCircle: JSONArray) {

        dialogCircle = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.circle_bottom_sheet, null)

        val rvCircle = view.findViewById<RecyclerView>(R.id.rvCircle)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 1)
        rvCircle.setLayoutManager(lLayout)
        rvCircle.setHasFixedSize(true)
        val obj_adapter1 = CircleAdapter(applicationContext!!, jArrayCircle!!)
        rvCircle!!.adapter = obj_adapter1
        obj_adapter1.setClickListener(this@RechargeActivity)

        dialogCircle!!.setCancelable(true)

        dialogCircle!!.setContentView(view)

        dialogCircle!!.show()

    }

    private fun OperatorbottomSheet(jArrayOperator: JSONArray) {
        Log.e(TAG,"jArrayOperator  272     "+jArrayOperator)

        dialogOperator = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.operator_bottom_sheet, null)
//
//     //   val tv_Close = view.findViewById<TextView>(R.id.tv_Close)
        val rvOperator = view.findViewById<RecyclerView>(R.id.rvOperator)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 3)
        rvOperator.setLayoutManager(lLayout)
        rvOperator.setHasFixedSize(true)
        val obj_adapter = OperatorAdapter(applicationContext!!, jArrayOperator!!)
        rvOperator!!.adapter = obj_adapter
        obj_adapter.setClickListener(this@RechargeActivity)

        dialogOperator!!.setCancelable(true)

        dialogOperator!!.setContentView(view)

        dialogOperator!!.show()
    }




    private fun AccountNobottomSheet(jArrayAccount: JSONArray) {

        dialogAccount = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.account_bottom_sheet, null)

        val rvAccount = view.findViewById<RecyclerView>(R.id.rvAccount)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 1)
        rvAccount.setLayoutManager(lLayout)
        rvAccount.setHasFixedSize(true)
        val obj_adapter2 = AccountAdapter(applicationContext!!, jArrayAccount!!)
        rvAccount!!.adapter = obj_adapter2
        obj_adapter2.setClickListener(this@RechargeActivity)

        dialogAccount!!.setCancelable(true)

        dialogAccount!!.setContentView(view)

        dialogAccount!!.show()


    }

    override fun onClick(position: Int, data: String) {
        Log.e(TAG,"position  315  "+position+"  "+data)
        if (data.equals("operator")){
            dialogOperator!!.dismiss()
            var jsonObject1 = jArrayOperator!!.getJSONObject(position)
            ID_Providers = jsonObject1.getString("ID_Providers")
            ProvidersCode = jsonObject1.getString("ProvidersCode")
            tie_operator!!.setText(""+jsonObject1.getString("ProvidersName"))
        }

        if (data.equals("circle")){
            dialogCircle!!.dismiss()
            var jsonObject1 = jArrayCircle!!.getJSONObject(position)
            ID_RechargeCircle = jsonObject1.getString("ID_RechargeCircle")
            tie_circle!!.setText(""+jsonObject1.getString("CircleName"))
            CircleMode = jsonObject1.getString("CircleMode")
        }

        if (data.equals("account")){
            dialogAccount!!.dismiss()
            var jsonObject1 = jArrayAccount!!.getJSONObject(position)
            FK_Account = jsonObject1.getString("FK_Account")
            AccountNo = jsonObject1.getString("AccountNumber")
            SubModule= jsonObject1.getString("SubModule")
            BranchName= jsonObject1.getString("BranchName")
            tie_account!!.setText(""+jsonObject1.getString("AccountNumber"))
        }

        if (data.equals("recent")){
//            dialogAccount!!.dismiss()
            var jsonObject2 = jArrayRecent!!.getJSONObject(position)

            tie_mobilenumber!!.setText(""+jsonObject2.getString("MobileNo"))
            tie_operator!!.setText(""+jsonObject2.getString("ProvidersName"))
            tie_circle!!.setText(""+jsonObject2.getString("CircleName"))
            tie_amount!!.setText(""+jsonObject2.getString("RechargeRs"))

//            ProvidersCode = "1"
//            CircleMode = "1"

            ID_Providers = jsonObject2.getString("FK_Providers")
            ProvidersCode = jsonObject2.getString("ProvidersCode")

            ID_RechargeCircle = jsonObject2.getString("FK_RechargeCircle")
            CircleMode = jsonObject2.getString("CircleMode")



        }


    }

    private fun contactSelect() {
        val intent = Intent(
            Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI
        )
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, PICK_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG,"tempContact  698  "+requestCode+"  "+resultCode)
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val uriContact = data!!.data!!
                val cursor: Cursor = applicationContext.getContentResolver().query(
                    uriContact, null, null, null, null
                )!!
                cursor.moveToFirst()
                val tempContact = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                Log.e(TAG,"tempContact  6981  "+tempContact)
                tie_mobilenumber!!.setText(extractPhoneNumber(tempContact))
                closeCursor(cursor)
            } catch (e: java.lang.Exception) {

            }
        }
        if (requestCode == REACHARGE_OFFER && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val value = data?.getStringExtra("data")
                Log.e(TAG, "AMOUNT   932   " + value)
                tie_amount!!.setText(value)
            } catch (e: Exception) {
                Log.e(TAG, "AMOUNT   932   " + e.toString())
            }
        }

    }
    private fun extractPhoneNumber(resultPhoneNumber: String): String? {
        var result: String
        try {
            result = resultPhoneNumber.replace("\\D+".toRegex(), "")
            if (result.length > 10) {
                result = result.substring(result.length - 10, result.length)
            }
        } catch (e: java.lang.Exception) {
            result = ""
        }
        return result
    }
    private fun closeCursor(cursor: Cursor) {
        try {
            cursor.close()
        } catch (e: java.lang.Exception) {
//            if (IScoreApplication.DEBUG) Log.e("Null pointer ex", e.toString())
        }
    }

    private fun rechargeValidation() {

        mobileNumber = tie_mobilenumber!!.text.toString();
        Amount = tie_amount!!.text.toString().replace(",", "")
        var mAccountNumber = AccountNo!!.replace(AccountNo!!.substring(AccountNo!!.indexOf(" (") + 1, AccountNo!!.indexOf(')') + 1), "")
        mAccountNumber = mAccountNumber.replace(" ", "")

        if (mobileNumber!!.length!= 10){
           // Toast.makeText(applicationContext,"Please enter valid  mobile number",Toast.LENGTH_LONG).show()

            CustomBottomSheeet.Show(this,"Please enter valid  mobile number","0")
//            showToast("Please enter valid  mobile number")
        }else if(ProvidersCode!!.equals("")){
           // Toast.makeText(applicationContext,"Please Select Operator",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please Select Operator","0")
        }
        else if(CircleMode!!.equals("")){
          //  Toast.makeText(applicationContext,"Please Select Circle",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please Select Circle","0")
        }
        else if(Amount!!.equals("")){
           // Toast.makeText(applicationContext,"Please Enter Amount",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please Enter Amount","0")
        }
        else if(mAccountNumber!!.length != 12){
           // Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please Select Account","0")
        }
        else if(SubModule!!.equals("")){
          //  Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please Select Account","0")
        }else{

            Log.e(TAG,"MobileNumer      785   "+mobileNumber)
            Log.e(TAG,"ProvidersCode    785   "+ProvidersCode)
            Log.e(TAG,"CircleMode       785   "+CircleMode)
            Log.e(TAG,"Amount           785   "+Amount)
            Log.e(TAG,"AccountNo        785   "+mAccountNumber)
            Log.e(TAG,"FK_Providers     785   "+ID_Providers)
            Log.e(TAG,"FK_RechargeCircle    785   "+ID_RechargeCircle)
            Log.e(TAG,"FK_Account         785   "+FK_Account)

            RechargeConfirmationPop(mobileNumber!!,ProvidersCode!!,CircleMode!!,Amount!!,mAccountNumber,ID_Providers,ID_RechargeCircle,FK_Account,AccountNo!!)

        }

    }

    private fun RechargeConfirmationPop(mobileNumber: String, providersCode: String, circleMode: String, amount: String, mAccountNumber: String?,
        idProviders: String?, idRechargecircle: String?, fkAccount: String?, accountNo: String) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.recharge_confirmation)

        val tvAcntno = dialog.findViewById(R.id.tvAcntno) as TextView
        val tvbranch = dialog.findViewById(R.id.tvbranch) as TextView
        val tv_mob = dialog.findViewById(R.id.tv_mob) as TextView
        val tv_oper = dialog.findViewById(R.id.tv_oper) as TextView
        val tv_cir = dialog.findViewById(R.id.tv_cir) as TextView
        val text_confirmationmsg = dialog.findViewById(R.id.text_confirmationmsg) as TextView
        val tv_amount = dialog.findViewById(R.id.tv_amount) as TextView
        val tv_amount_words = dialog.findViewById(R.id.tv_amount_words) as TextView

        val bt_cancel = dialog.findViewById(R.id.bt_cancel) as Button
        val bt_ok = dialog.findViewById(R.id.bt_ok) as Button



        tvAcntno!!.setText(""+accountNo)
        tvbranch!!.setText(""+BranchName)
        tv_mob!!.setText(""+mobileNumber)
        tv_oper!!.setText(""+tie_operator!!.text.toString())
        tv_cir!!.setText(""+tie_circle!!.text.toString())

        text_confirmationmsg!!.setText("Proceed Recharge With Above Amount ..?")

//            double num =Double.parseDouble(""+mAmount);
//            String stramnt = CommonUtilities.getDecimelFormate(num);
        val stramnt: String = amount.replace(",", "")
        val netAmountArr: Array<String> = stramnt.split("\\.").toTypedArray()
        val amountInWordPop = ""
      //  tv_amount.setText(""+stramnt)
        tv_amount!!.setText(""+Config.getDecimelFormate(amount!!.toDouble()))

//        val netAmountArr = amount.split("\\.".toRegex()).toTypedArray()
//        var amountInWordPop = ""
//        if (netAmountArr.size > 0) {
//            val integerValue = netAmountArr[0].toInt()
//            amountInWordPop = "Rupees " + NumberToWord!!.convertNumberToWords(integerValue)
//            if (netAmountArr.size > 1) {
//                val decimalValue = netAmountArr[1].toInt()
//                if (decimalValue != 0) {
//                    amountInWordPop += " and " + NumberToWord.convertNumberToWords(decimalValue).toString() + " paise"
//                }
//            }
//            amountInWordPop += " only"
//        }
//        tv_amount_words!!.setText("" + amountInWordPop)

        bt_ok!!.setOnClickListener {
            recharge(mobileNumber, providersCode, CircleMode, mAccountNumber, SubModule, ID_Providers, ID_RechargeCircle, FK_Account,amount)

            dialog.dismiss()
        }

        bt_cancel!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()




    }

    private fun recharge(mobileNumber: String, providersCode: String, circleMode: String?, mAccountNumber: String?, subModule: String?,
                         idProviders: String?, idRechargecircle: String?, fkAccount: String?, amount: String?) {

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
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("MobileNumer", MscoreApplication.encryptStart(mobileNumber))
                        requestObject1.put("ProvidersCode", MscoreApplication.encryptStart(ProvidersCode))
                        requestObject1.put("CircleMode", MscoreApplication.encryptStart(circleMode))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(mAccountNumber))

                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FK_Providers", MscoreApplication.encryptStart(idProviders))
                        requestObject1.put("FK_RechargeCircle", MscoreApplication.encryptStart(idRechargecircle))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(FK_Account))

                        Log.e(TAG,"requestObject1  901   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  9011   "+e.toString())
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
                    val call = apiService.getMobileRecharge(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  9013   "+response.body())
                                Log.e(TAG,"response  9014   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

//                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
//                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
//                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
//                                    OperatorbottomSheet(jArrayOperator!!)

                                   // Toast.makeText(applicationContext,"Not Complete",Toast.LENGTH_LONG).show()
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()



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


    private fun getRecentRecharges() {

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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("36"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("ProvidersMode", MscoreApplication.encryptStart(ProvidersMode))

                        Log.e(TAG,"requestObject1  1085   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  10851   "+e.toString())
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
                    val call = apiService.getRechargeHistory(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  10852   "+response.body())
                                Log.e(TAG,"response  10853   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("RechargeHistory")
                                    jArrayRecent = jobjt.getJSONArray("RechargeHistoryList")
                                    Log.e(TAG,"jArrayRecent  10854   "+jArrayRecent)

                                    val lLayout = GridLayoutManager(this@RechargeActivity, 1)
                                    rvrecentRecharge!!.setLayoutManager(lLayout)
                                    rvrecentRecharge!!.setHasFixedSize(true)
                                    val recent_adapter = RecentRechargeAdapter(applicationContext!!, jArrayRecent!!)
                                    rvrecentRecharge!!.adapter = recent_adapter
                                    recent_adapter.setClickListener(this@RechargeActivity)

                                    //  AccountNobottomSheet(jArrayAccount!!)

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


}