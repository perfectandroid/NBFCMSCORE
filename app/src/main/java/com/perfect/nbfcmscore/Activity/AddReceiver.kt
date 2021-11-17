package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.SenderReceiverlist
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class AddReceiver : AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener {
    private var progressDialog: ProgressDialog? = null
    public var senderid:String?=null
    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var sender_name_spinner: Spinner? = null
    var receiver_name: AppCompatEditText? = null
    var mobile_number: AppCompatEditText? = null
    var ifsc_code: AppCompatEditText? = null
    var account_number: AppCompatEditText? = null
    var confirm_account_number: AppCompatEditText? = null
    var btn_registr: Button? = null
    public var arrayList2: ArrayList<SenderReceiverlist>? = null
    var imgHome: ImageView? = null
    var tv_title: TextView? = null
    var txtv_sndrname: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

         setRegviews()
         getSenderReceiver()

        val Fundtransfrsp = applicationContext.getSharedPreferences(Config.SHARED_PREF122, 0)
        tv_title!!.setText(Fundtransfrsp.getString("FundTransfer", null))

        val Sndrnamesp = applicationContext.getSharedPreferences(Config.SHARED_PREF147, 0)
        txtv_sndrname!!.setHint(Sndrnamesp.getString("SenderName", null))

        val ifscsp = applicationContext.getSharedPreferences(Config.SHARED_PREF150, 0)
        ifsc_code!!.setHint(ifscsp.getString("IFSCCode", null))

        val accnosp = applicationContext.getSharedPreferences(Config.SHARED_PREF107, 0)
        account_number!!.setHint(accnosp.getString("AccountNo", null))

        val confrmaccnosp = applicationContext.getSharedPreferences(Config.SHARED_PREF149, 0)
        confirm_account_number!!.setHint(confrmaccnosp.getString("ConfirmAccountNumber", null))

        val Registerp = applicationContext.getSharedPreferences(Config.SHARED_PREF146, 0)
        btn_registr!!.setText(Registerp.getString("REGISTER", null))


    }

    private fun getSenderReceiver() {
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
                            .sslSocketFactory(Config.getSSLSocketFactory(this@AddReceiver))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("40"))
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


                        Log.e("TAG", "requestObject1  senderreceiver   " + requestObject1)
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
                    val call = apiService.getSenderReceiver(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-senderreceiver", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("QuickPaySenderReciver")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    val jresult = jsonobj2.getJSONObject("QuickPaySenderReciverlist")
                                    arrayList2 = ArrayList<SenderReceiverlist>()
                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i.toString())
                                            arrayList2!!.add(
                                                    SenderReceiverlist(
                                                            json.getString("userId"),
                                                            json.getString(
                                                                    "fkSenderId"
                                                            ),
                                                            json.getString(
                                                                    "senderName"
                                                            ),
                                                            json.getString(
                                                                    "senderMobile"
                                                            ), json.getString(
                                                            "receiverAccountno"
                                                    ), json.getString(
                                                            "receiverAccountno"
                                                    )
                                                    )
                                            )

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    sender_name_spinner!!.adapter = ArrayAdapter(
                                            this@AddReceiver,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList2!!
                                    )

                                    //    spn_account_num!!.setSelection(arrayList1.indexOf("Select Account"));

                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@AddReceiver,
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
                                        this@AddReceiver,
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
                                    this@AddReceiver,
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
                            this@AddReceiver,
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
                        this@AddReceiver,
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

    private fun setRegviews() {
        imgBack= findViewById<ImageView>(R.id.imgBack)
        imgHome= findViewById<ImageView>(R.id.imgHome)
        sender_name_spinner = findViewById<Spinner>(R.id.sender_name_spinner)
        receiver_name = findViewById<AppCompatEditText>(R.id.receiver_name)
        mobile_number = findViewById<AppCompatEditText>(R.id.mobile_number)
        ifsc_code = findViewById<AppCompatEditText>(R.id.ifsc_code)
        account_number = findViewById<AppCompatEditText>(R.id.account_number)
        confirm_account_number = findViewById<AppCompatEditText>(R.id.confirm_account_number)
        btn_registr = findViewById<Button>(R.id.btn_registr)

        tv_title= findViewById<TextView>(R.id.tv_title)
        txtv_sndrname= findViewById<TextView>(R.id.txtv_sndrname)

        sender_name_spinner!!.setOnItemSelectedListener(this)
        btn_registr!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)

    }

    private fun getReceiver(receiverName: String, mobileNumber: String, ifscCode: String, accNumber: String, confirmAccNumber: String) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@AddReceiver, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@AddReceiver))
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

                      //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("40"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "ReceiverName",
                                MscoreApplication.encryptStart(receiverName)
                        )
                        requestObject1.put(
                                "SenderID",
                                MscoreApplication.encryptStart(senderid)
                        )
                        requestObject1.put(
                                "ReceiverMobile",
                                MscoreApplication.encryptStart(mobileNumber)
                        )
                        requestObject1.put(
                                "ReceiverAccountNo",
                                MscoreApplication.encryptStart(accNumber)
                        )
                        requestObject1.put(
                                "ReceiverIFSCcode",
                                MscoreApplication.encryptStart(ifscCode)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  receiver   " + requestObject1)
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
                    val call = apiService.getaddReceiver(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-receiver", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("Addnewreceiver")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    var message = jsonobj2.getString("message")
                                    var status = jsonobj2.getString("Status")
                                    alertMessage1(status, message)




                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@AddReceiver,
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
                                        this@AddReceiver,
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
                                    this@AddReceiver,
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
                    val builder = AlertDialog.Builder(this@AddReceiver, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@AddReceiver, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {


            R.id.btn_registr -> {
                if (isValid()) {
                    val receiverName: String = receiver_name!!.getText().toString()
                    val mobileNumber: String = mobile_number!!.getText().toString()
                    val ifscCode: String = ifsc_code!!.getText().toString()
                    val accNumber: String = account_number!!.getText().toString()
                    val confirmAccNumber: String = confirm_account_number!!.getText().toString()
                    getReceiver(receiverName,mobileNumber,ifscCode,accNumber,confirmAccNumber)
                }
            }
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@AddReceiver, HomeActivity::class.java))
            }
        }
    }

    private fun isValid(): Boolean {

     //   val senderReceiver: SenderReceiverlist = sender_name_spinner!!.getSelectedItem() as SenderReceiverlist

        /*if (senderReceiver == null) {
            if (senderReceiver=="") {
                Toast.makeText(applicationContext,"Please add minimum one sender, and then add receiver",Toast.LENGTH_LONG).show()

            }
            return false
        }

        if (senderReceiver.fkSenderId == -100) {
            showToast("Please select sender")
            return false
        }
*/



        val receiverName: String = receiver_name!!.getText().toString()
        val mobileNumber: String = mobile_number!!.getText().toString()
        val ifscCode: String = ifsc_code!!.getText().toString()
        val accNumber: String = account_number!!.getText().toString()
        val confirmAccNumber: String = confirm_account_number!!.getText().toString()

        if (TextUtils.isEmpty(receiverName)) {
            receiver_name!!.setError("Please enter receiver name")
            return false
        }
        receiver_name!!.setError(null)

        if (TextUtils.isEmpty(mobileNumber)) {
            mobile_number!!.setError("Please enter mobile number")
            return false
        }

        if (mobileNumber.length > 10 || mobileNumber.length < 10) {
            mobile_number!!.setError("Please enter valid 10 digit mobile number")
            return false
        }

        mobile_number!!.setError(null)

        try {
            mobile_number!!.getText().toString().toLong()
        } catch (e: java.lang.Exception) {
            return false
        }

        if (TextUtils.isEmpty(ifscCode)) {
            ifsc_code!!.setError("Please enter IFSC code")
            return false
        } else {
            if (!isIfscIsValid(ifscCode)) {
                ifsc_code!!.setError("Invalid ifsc")
                return false
            }
        }
        ifsc_code!!.setError(null)

        if (TextUtils.isEmpty(accNumber)) {
            account_number!!.setError("Please enter account number")
            return false
        }
        account_number!!.setError(null)

        if (TextUtils.isEmpty(confirmAccNumber)) {
            confirm_account_number!!.setError("Please enter confirm account number")
            return false
        }

        if (!accNumber.equals(confirmAccNumber, ignoreCase = true)) {
            confirm_account_number!!.setError("Account number and Confirm Account number not matching")
            return false
        }
        confirm_account_number!!.setError(null)

        return true

    }

    private fun isIfscIsValid(ifscCode: String): Boolean{

        return ifscCode.length > 0
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val senderreceiverlist: SenderReceiverlist = arrayList2!!.get(position)
        senderid = senderreceiverlist.getUserId()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
    private fun alertMessage1(msg1: String, msg2: String) {
        val dialogBuilder = AlertDialog.Builder(this@AddReceiver)
        val inflater: LayoutInflater = this@AddReceiver.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.alert_layout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
        val tv_msg = dialogView.findViewById<TextView>(R.id.txt1)
        val tv_msg2 = dialogView.findViewById<TextView>(R.id.txt2)
        tv_msg.text = msg1
        tv_msg2.text = msg2
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.setOnClickListener { alertDialog.dismiss() }
        tv_share.setOnClickListener { //  finishAffinity();
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}