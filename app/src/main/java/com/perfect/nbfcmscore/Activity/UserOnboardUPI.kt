package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Activity.SplashActivity.Companion.BankKey
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.Splitupdetail
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


class UserOnboardUPI : AppCompatActivity(), View.OnClickListener {
    val TAG: String? = "UserOnboardUPI"
    var edtMobilenumber: EditText? = null
    var edtAccountNumber: TextView? = null
    var btn_submit: TextView? = null
    var btn_cancel: TextView? = null
    var edtDateofBirth: EditText? = null
    var edtAgentEmail: EditText? = null
    var checkBox: CheckBox? = null
    private var mAccountSpinner: Spinner? = null
    private var jresult: JSONArray? = null
    private var progressDialog: ProgressDialog? = null
    public var arrayList1: java.util.ArrayList<Splitupdetail>? = null
    var accountNumber: String = ""
    var strtypeShort: String = ""
    var strKitNumber: String = ""
    var strFK_Account: String = ""
    var strMobileNumber: String = ""

    //    var accountNumber:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_onboard_upi)
        getSupportActionBar()?.hide();
        setId()
        setListners()
        getAccountnumber()
        getSharedPrefdata()
        btn_submit?.setEnabled(false)
        btn_submit?.setAlpha(.4f)
    }

    private fun setListners() {
        edtAccountNumber?.setOnClickListener(this)
        edtDateofBirth?.setOnClickListener(this)
        checkBox?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                btn_submit?.setEnabled(true)
                btn_submit?.setAlpha(1f)
            } else {
                btn_submit?.setEnabled(false)
                btn_submit?.setAlpha(.4f)
            }
        })
    }

    private fun setId() {
        edtMobilenumber = findViewById<View>(R.id.edtMobilenumber) as EditText
        checkBox = findViewById<View>(R.id.checkBox) as CheckBox
        edtAccountNumber = findViewById<View>(R.id.edtAccountNumber) as TextView
        btn_submit = findViewById<View>(R.id.btn_submit) as TextView
        btn_cancel = findViewById<View>(R.id.btn_cancel) as TextView
        edtDateofBirth = findViewById<View>(R.id.edtDateofBirth) as EditText
        edtAgentEmail = findViewById<View>(R.id.edtAgentEmail) as EditText
        mAccountSpinner = findViewById<Spinner>(R.id.spn_account_num)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_cancel) {
            onBackPressed()
        } else if (v?.id == R.id.edtDateofBirth) {
            openBottomSheetDate()
        } else if (v?.id == R.id.btn_submit) {
            validateData()
        }
    }

    private fun getSharedPrefdata() {
        val AgentEmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF351, 0)
        val mobileNoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2, 0)
        val DateOfBirthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF7, 0)
        strMobileNumber = mobileNoSP.getString("mobileNo", "").toString();
        edtAgentEmail!!.setText(AgentEmailSP.getString("AgentEmail", ""))
        edtMobilenumber!!.setText(strMobileNumber)
        edtDateofBirth!!.setText(DateOfBirthSP.getString("DateOfBirth", null)?.replace("-", "/"))
    }

    private fun validateData() {
        val KItNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF349, 0)
        var strAgentEmail = edtAgentEmail!!.getText().toString()
        var strMobileNumber = edtMobilenumber!!.text.toString()
        var strDob = edtDateofBirth!!.text.toString()
        //  strKitNumber = "4210011416";
        strKitNumber = KItNumberSP.getString("KItNumber", "").toString()

//        Existing user Correct data

//        customer name : DIVIN
//        Customer number : 001001142958
//        demand deposit number : 001001016763
//        demand deposit number : 001001016909
//        phone number : 9037591858
//        8075115147

//        strAgentEmail = "kscbp373@gmail.com";
//        strMobileNumber = "9656292477";
//        strAccountNumber = "001001017268";
//        strDob = "31/08/2001";
//        strKitNumber = "4210011256";
//        strtypeShort = "SB";
        if (TextUtils.isEmpty(strAgentEmail)) {
            Toast.makeText(applicationContext, "Invalid Email", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(strMobileNumber)) {
            Toast.makeText(applicationContext, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(accountNumber)) {
            Toast.makeText(applicationContext, "Invalid A|c Number", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(strDob)) {
            Toast.makeText(applicationContext, "Invalid DOB", Toast.LENGTH_SHORT).show()
        } else {
            Log.e(
                TAG, """
     validateData   861  
     strAgentEmail    :   $strAgentEmail
     strMobileNumber  :   $strMobileNumber
     strAccountNumber :   $accountNumber
     strtypeShort     :   $strtypeShort
     strDob           :   $strDob
     strKitNumber     :   $strKitNumber
     """.trimIndent()
            )
            if (strKitNumber == "") {
                generateKitNo()
            } else {
                //     onBoardingUser()
            }
        }
    }

    private fun getAccountnumber() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@UserOnboardUPI, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@UserOnboardUPI))
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
                        val BankKeySP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                            "SubMode",
                            MscoreApplication.encryptStart("1")
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put(
                            "BankHeader",
                            MscoreApplication.encryptStart(BankHeaderPref)
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                    val call = apiService.getOwnbankownaccountdetail(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response2-AccountNumber", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("OwnAccountdetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("OwnAccountdetailsList")
                                    arrayList1 = ArrayList<Splitupdetail>()
                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            arrayList1!!.add(
                                                Splitupdetail(
                                                    json.getString("AccountNumber"),
                                                    json.getString(
                                                        "FK_Account"
                                                    ),
                                                    json.getString(
                                                        "SubModule"
                                                    ),
                                                    json.getString(
                                                        "BranchName"
                                                    )
                                                )
                                            )

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    mAccountSpinner!!.adapter = ArrayAdapter(
                                        this@UserOnboardUPI,
                                        android.R.layout.simple_spinner_dropdown_item, arrayList1!!
                                    )
                                    mAccountSpinner!!.setOnItemSelectedListener(object :
                                        OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parentView: AdapterView<*>?,
                                            selectedItemView: View,
                                            position: Int,
                                            id: Long
                                        ) {
                                            var accountNumberAll =
                                                arrayList1!!.get(position).accountno
                                            strFK_Account = arrayList1!!.get(position).fkaccount
                                            strtypeShort = arrayList1!!.get(position).submodule
                                            accountNumber = accountNumberAll.substring(
                                                0,
                                                Math.min(accountNumberAll.length, 12)
                                            )
                                            Log.v("dfdsfdsdddd", "accountNumber " + accountNumber)
                                            Log.v("dfdsfdsdddd", "strtypeShort " + strtypeShort)
                                        }

                                        override fun onNothingSelected(parentView: AdapterView<*>?) {
                                            // your code here
                                        }
                                    })

                                    //    spn_account_num!!.setSelection(arrayList1.indexOf("Select Account"));

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@UserOnboardUPI,
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
                                    this@UserOnboardUPI,
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
                                this@UserOnboardUPI,
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
                    val builder = AlertDialog.Builder(
                        this@UserOnboardUPI,
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
                    this@UserOnboardUPI,
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

    private fun generateKitNo() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@UserOnboardUPI, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@UserOnboardUPI))
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
                        requestObject1.put("ServiceType", MscoreApplication.encryptStart("1"))
                        requestObject1.put("ServiceProvider", MscoreApplication.encryptStart("2"))
                        requestObject1.put("CorpCode", MscoreApplication.encryptStart(BankKey))
                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                    val call = apiService.generateAceKitNumber(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response2-AccountNumber", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject = jObject.getJSONObject("KitNumber")
                                    strKitNumber = jsonObj1.getString("NextKitNumber")
                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@UserOnboardUPI,
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
                                    this@UserOnboardUPI,
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
                                this@UserOnboardUPI,
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
                    val builder = AlertDialog.Builder(
                        this@UserOnboardUPI,
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
                    this@UserOnboardUPI,
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

    private fun updateUserID(userid: String, vpa: String, dialogOld: Dialog) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when (ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@UserOnboardUPI, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@UserOnboardUPI))
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
                        val customerIdSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val cusid = customerIdSP.getString("customerId", null)
                        requestObject1.put("ServiceType", MscoreApplication.encryptStart("1"))
                        requestObject1.put("ServiceProvider", MscoreApplication.encryptStart("2"))
                        requestObject1.put("CorpCode", MscoreApplication.encryptStart(BankKey))

                        requestObject1.put("ID_Customer", MscoreApplication.encryptStart(cusid))
                        requestObject1.put(
                            "Fk_AccountCode",
                            MscoreApplication.encryptStart(strFK_Account)
                        )
                        requestObject1.put(
                            "SubModule",
                            MscoreApplication.encryptStart(strtypeShort)
                        )
                        requestObject1.put("KitID", MscoreApplication.encryptStart(strKitNumber))
                        requestObject1.put("MobNo", MscoreApplication.encryptStart(strMobileNumber))
                        requestObject1.put("ReqID", MscoreApplication.encryptStart("1"))
                        requestObject1.put("UPIUserID", MscoreApplication.encryptStart(userid))
                        requestObject1.put("UPIID", MscoreApplication.encryptStart(vpa))
                        requestObject1.put(
                            "MsgMode",
                            MscoreApplication.encryptStart("0")
                        ) //for forgot pin otp send

                        requestObject1.put("UPIPinOld", "")
                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                    val call = apiService.generateAceKitNumber(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response2-AccountNumber", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val AceMoneyUserIDSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF347,
                                        0
                                    )
                                    val AceMoneyUserIDEditer = AceMoneyUserIDSP.edit()
                                    AceMoneyUserIDEditer.putString("AceMoneyUserID", userid)
                                    AceMoneyUserIDEditer.commit()

                                    val AceMoneyUPIIDSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF348,
                                        0
                                    )
                                    val AceMoneyUPIIDEditer = AceMoneyUPIIDSP.edit()
                                    AceMoneyUPIIDEditer.putString("AceMoneyUPIID", vpa)
                                    AceMoneyUPIIDEditer.commit()


                                    val UPIAccNoSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF354,
                                        0
                                    )
                                    val UPIAccNoEditer = UPIAccNoSP.edit()
                                    UPIAccNoEditer.putString("UPIAccNo", accountNumber)
                                    UPIAccNoEditer.commit()
                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@UserOnboardUPI,
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
                                    this@UserOnboardUPI,
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
                                this@UserOnboardUPI,
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
                    val builder = AlertDialog.Builder(
                        this@UserOnboardUPI,
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
                    this@UserOnboardUPI,
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

    fun openBottomSheetDate() {
        val dialog = BottomSheetDialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.bottomsheet_date, null)
        val imgsave = view.findViewById<ImageView>(R.id.imgsave)
        val imgclose = view.findViewById<ImageView>(R.id.imgclose)
        val date_Picker1 = view.findViewById<DatePicker>(R.id.date_Picker1)
        date_Picker1.maxDate = System.currentTimeMillis()
        imgsave.setOnClickListener {
            val day = date_Picker1.dayOfMonth
            val month = date_Picker1.month + 1
            val year = date_Picker1.year
            val dob = "$day/$month/$year"
            edtDateofBirth!!.setText(dob)
            dialog.dismiss()
        }
        imgclose.setOnClickListener { dialog.dismiss() }
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
    }
}