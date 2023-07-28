package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.aceware.cobrandprepaidkit.CobrandPrepaidSdkkit
import com.aceware.cobrandprepaidkit.CobrandPrepaidSdkkit.ResponseListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.perfect.nbfcmscore.Activity.SplashActivity.Companion.BankKey
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.Config.getSSLSocketFactory
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.Splitupdetail
import com.perfect.nbfcmscore.R
import `in`.aabhasjindal.otptextview.OtpTextView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class UserOnboardUPI : AppCompatActivity(), View.OnClickListener {
    val TAG: String? = "UserOnboardUPI"
    var edtMobilenumber: EditText? = null
    var edtAccountNumber: TextView? = null
    var btn_submit: TextView? = null
    var btn_cancel: TextView? = null
    var textDialogue: TextView? = null
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
    var strAgentEmail: String = ""
    var strDob: String = ""
    var cusid: String = ""
    var AceMoneyUser: String = ""
    var upiid: String = ""
    lateinit var cobrandPrepaidSdkkit : CobrandPrepaidSdkkit

    //    var accountNumber:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_onboard_upi)
        getSupportActionBar()?.hide();
        setId()
        setListners()
        getAccountnumber()
        getSharedPrefdata()
        cobrandPrepaidSdkkit = CobrandPrepaidSdkkit(this)
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
        val customerIdSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
        val AceMoneyUserIDSP = applicationContext.getSharedPreferences(Config.SHARED_PREF347, 0)
        val AceMoneyUPIIDSP = applicationContext.getSharedPreferences(Config.SHARED_PREF348, 0)
        upiid = AceMoneyUPIIDSP.getString("AceMoneyUPIID", "").toString()
        AceMoneyUser = AceMoneyUserIDSP.getString("AceMoneyUserID", "").toString()
        cusid = customerIdSP.getString("FK_Customer", "").toString()
        strMobileNumber = mobileNoSP.getString("mobileNo", "").toString();
        edtAgentEmail!!.setText(AgentEmailSP.getString("AgentEmail", ""))
        edtMobilenumber!!.setText(strMobileNumber)
        edtDateofBirth!!.setText(DateOfBirthSP.getString("DateOfBirth", null)?.replace("-", "/"))
    }

    private fun validateData() {
        val KItNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF349, 0)
         strAgentEmail = edtAgentEmail!!.getText().toString()
         strMobileNumber = edtMobilenumber!!.text.toString()
         strDob = edtDateofBirth!!.text.toString()
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
                     onBoardingUser()
            }
        }
    }

    private fun onBoardingUser() {
        val AceMoneyUpiBankCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF74, 0)
        val upiBankCode = AceMoneyUpiBankCodeSP.getString("AceMoneyUpiBankCode", "")
        val dialog1: Dialog = showLoadingDialog("Registering user")
        val map = java.util.ArrayList<String>()
        map.add(strAgentEmail)
        map.add(strMobileNumber)
        map.add(accountNumber)
        map.add(strDob)
        map.add(strKitNumber)
        map.add(strtypeShort)
        Log.e(TAG, "map  414   $map")
        cobrandPrepaidSdkkit.initService(upiBankCode, "onboardRegisteruser", map)
        cobrandPrepaidSdkkit.setResponseCall(object : ResponseListener {
            override fun onSuccess(s: String, jsonObject: JsonObject) {
                try {

//              {"success":true,"message":"Successfully Registered","userid":879,"vpa":"ACE11NZO3410@yesbank"}
                    Log.e("TAG", "3301  :  $s")
                    Log.e("TAG", "3302  :  $jsonObject")
                    if (jsonObject["success"].asBoolean == true) {
                        val userId = jsonObject["userid"].toString()
                        val vpa = ""
                        Log.v("dfsdfsddd", "vpa_" + vpa + "_")
                        val KItNumberSP =
                            applicationContext.getSharedPreferences(Config.SHARED_PREF349, 0)
                        val KItNumberEditer = KItNumberSP.edit()
                        KItNumberEditer.putString("KItNumber", strKitNumber)
                        KItNumberEditer.commit()
                        updateUserID(userId, vpa, dialog1)
                        //                    }
                    } else {
                        dialog1.dismiss()
                        val builder = AlertDialog.Builder(this@UserOnboardUPI)
                        builder.setMessage("" + jsonObject["message"])
                            .setPositiveButton(
                                "Ok"
                            ) { dialog, which -> dialog.dismiss() }
                        val alert = builder.create()
                        alert.show()
                    }
                } catch (e: java.lang.Exception) {
                }
            }

            override fun onFailure(s: String, jsonObject: JsonObject) {
                dialog1.dismiss()
                Log.e("TAG", "3303  :  $s")
                Log.e("TAG", "3304  :  $jsonObject")
                val builder = AlertDialog.Builder(this@UserOnboardUPI)
                builder.setMessage("" + jsonObject["message"])
                    .setPositiveButton(
                        "Ok"
                    ) { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }
        })
    }

    fun showLoadingDialog(msg: String?): Dialog {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_loading)
        textDialogue = dialog.findViewById<TextView>(R.id.text)
        textDialogue?.setText(msg)
        dialog.show()
        return dialog
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
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client:OkHttpClient = okhttp3 . OkHttpClient . Builder ()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(Config.getSSLSocketFactory(this), trustManager)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response2-AccountNumber", response.body().toString())
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
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client:OkHttpClient = okhttp3 . OkHttpClient . Builder ()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response2-AccountNumber", response.body().toString())
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
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client:OkHttpClient = okhttp3 . OkHttpClient . Builder ()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response2-AccountNumber", response.body().toString())
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
                                    showSuccessDialog2("User registration success", userid, vpa)
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
    private fun verifyOTPNewPin(bottomSheetDialog: BottomSheetDialog, otp: String, pin: String) {
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
                    val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm()
                    )
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        ("Unexpected default trust managers:"
                                + Arrays.toString(trustManagers))
                    }
                    val trustManager = trustManagers[0] as X509TrustManager
                    val client:OkHttpClient = okhttp3 . OkHttpClient . Builder ()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .sslSocketFactory(getSSLSocketFactory(this), trustManager)
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
                        requestObject1.put("CorpCode", MscoreApplication.encryptStart(BankKey))
                        requestObject1.put("ServiceType", MscoreApplication.encryptStart("1"))
                        requestObject1.put("ServiceProvider", MscoreApplication.encryptStart("2"))
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
                        requestObject1.put("ReqID", MscoreApplication.encryptStart("4"))
                        requestObject1.put(
                            "UPIUserID",
                            MscoreApplication.encryptStart(AceMoneyUser)
                        )
                        requestObject1.put("UPIID", MscoreApplication.encryptStart(upiid))
                        requestObject1.put("JsonLogParameter", "")
                        requestObject1.put("UPIOtp", MscoreApplication.encryptStart(otp))
                        requestObject1.put("UPIPin", MscoreApplication.encryptStart(pin))
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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.sendOTPforUPIPinGeneration(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response2-AccountNumber", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    bottomSheetDialog.dismiss()
                                    showSuccessDialog("Your UPI pin updated")
                                    val UPIPinSETSP = applicationContext.getSharedPreferences(
                                        Config.SHARED_PREF77,
                                        0
                                    )
                                    val UPIPinSETEditer = UPIPinSETSP.edit()
                                    UPIPinSETEditer.putString("UPIPinSET", "true")
                                    UPIPinSETEditer.commit()
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

    fun showSuccessDialog(message: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.upi_success)
        val submit = dialog.findViewById<TextView>(R.id.submit)
        val msg = dialog.findViewById<TextView>(R.id.msg)
        msg.text = message
        submit.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
        dialog.show()
    }
    fun showSuccessDialog2(message: String?, userid: String?, vpa: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.upi_success2)
        val submit = dialog.findViewById<TextView>(R.id.submit)
        val msg = dialog.findViewById<TextView>(R.id.msg)
        msg.text = message
        submit.setOnClickListener {
            dialog.dismiss()
            alertSetupPin()
        }
        dialog.show()
    }

    private fun alertSetupPin() {
        val dialog = Dialog(this)
        val view: View = layoutInflater.inflate(R.layout.alert_vpa_warning, null)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        val tv_cancel = view.findViewById<TextView>(R.id.tv_cancel)
        val tv_register = view.findViewById<TextView>(R.id.tv_register)
        val txt_message = view.findViewById<TextView>(R.id.txt_message)
        txt_message.text = "Pin not set.Do you want to create pin now ?"
        tv_cancel.setOnClickListener {
            dialog.dismiss()
            onBackPressed()
        }
        tv_register.setOnClickListener {
            dialog.dismiss()
            showBottomPin()
        }
        dialog.window!!.findViewById<View>(R.id.rl_main)
            .setBackgroundResource(android.R.color.transparent)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun showBottomPin() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_upi_pin)
        val lin_no_pin = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_no_pin)
        val lin_otp = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.lin_otp)
        val otp_view: OtpTextView? = bottomSheetDialog.findViewById<OtpTextView>(R.id.otp_view)
        val lin_pin = bottomSheetDialog.findViewById<LinearLayout>(R.id.lin_pin)
        val pinclick = bottomSheetDialog.findViewById<ImageView>(R.id.pinclick)
        val txtsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtsave)
        val txtotpsave = bottomSheetDialog.findViewById<ImageView>(R.id.txtotpsave)
        val imgclose = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose)
        val imgclose2 = bottomSheetDialog.findViewById<ImageView>(R.id.imgclose2)
        val txtpin = bottomSheetDialog.findViewById<EditText>(R.id.txtpin)
        val txtpin2 = bottomSheetDialog.findViewById<EditText>(R.id.txtpin2)
        imgclose!!.visibility = View.VISIBLE
        imgclose2!!.visibility = View.VISIBLE
        val pin = arrayOf("")
        lin_no_pin!!.visibility = View.GONE
        lin_pin!!.visibility = View.VISIBLE
        //        pinclick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        txtsave!!.setOnClickListener {
            val pass1 = txtpin!!.text.toString()
            val pass2 = txtpin2!!.text.toString()
            if (pass1.equals("", ignoreCase = true) || pass2.equals("", ignoreCase = true)) {
                Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else if (pass1.length < 6 || pass2.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Please check the length of pin",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (pass1 != pass2) {
                Toast.makeText(
                    applicationContext,
                    "Please check provided pin.",
                    Toast.LENGTH_SHORT
                )
            } else {
                pin[0] = pass1
                //sendOTPNewPin(lin_otp, lin_pin, pass1);
                verifyOTPNewPin(bottomSheetDialog, "121454", pin[0])
                bottomSheetDialog.setCancelable(false)
            }
        }
        txtotpsave!!.setOnClickListener {
            val otp: String = otp_view!!.getOTP()
            if (otp.length != 6) {
                otp_view!!.showError()
            } else {
                verifyOTPNewPin(bottomSheetDialog, otp, pin[0])
            }
        }
        imgclose.setOnClickListener {
            bottomSheetDialog.dismiss()
            onBackPressed()
        }
        imgclose2.setOnClickListener {
            bottomSheetDialog.dismiss()
            onBackPressed()
        }
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
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
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}