package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.PassbookTranscationListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.NumberToWord
import com.perfect.nbfcmscore.Model.SenderReceiver
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
import java.util.*

class QuickPayActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener {
    private var mAccountSpinner: Spinner? = null
    private val mAmountEt: AppCompatEditText? = null
    private val mMessageEt: AppCompatEditText? = null
    private var add_new_sender: TextView?=null
    private var add_new_receiver: TextView?=null
    public var arrayList1: ArrayList<Splitupdetail>? = null
    private val mPin: AppCompatEditText? = null
    private val mProgressDialog: ProgressDialog? = null
    private val mSenderSpinner: Spinner? = null
    private val mReceiverSpinner: Spinner? = null
    private val mOtpResendLink: String? = null
    private val mCanLoadSenderReceiver = false
    var reference: String? = null
    private val mLnrAnimatorContainer: LinearLayout? = null
    private val mRltvError: RelativeLayout? = null
    private val mTxtError: TextView? = null
    private  var txt_amtinword:TextView? = null
    var BranchName: String? = null
    private var mBtnSubmit: Button? = null
    private var jresult: JSONArray? = null
    private val accountlist = ArrayList<String>()
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    public var branchname:String?=null
    var btn_forgot_mpin: Button? = null
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quickpay)
        setRegviews()
        getAccountnumber()

    }

    private fun setRegviews() {

        btn_forgot_mpin= findViewById<Button>(R.id.btn_forgot_mpin)

        add_new_sender = findViewById<TextView>(R.id.add_new_sender)
        add_new_receiver = findViewById<TextView>(R.id.add_new_receiver)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        mAccountSpinner = findViewById<Spinner>(R.id.spn_account_num)
        mBtnSubmit = findViewById<Button>(R.id.btn_submit)
        mBtnSubmit!!.setOnClickListener(this)
        add_new_sender!!.setOnClickListener(this)
        add_new_receiver!!.setOnClickListener(this)
        btn_forgot_mpin!!.setOnClickListener(this)

        mAccountSpinner!!.onItemSelectedListener = this

    }

    private fun getAccountnumber() {
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
                        .sslSocketFactory(Config.getSSLSocketFactory(this@QuickPayActivity))
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
                    val call = apiService.getOwnbankownaccountdetail(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
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
                                        this@QuickPayActivity,
                                        android.R.layout.simple_spinner_dropdown_item, arrayList1!!
                                    )

                                    //    spn_account_num!!.setSelection(arrayList1.indexOf("Select Account"));

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@QuickPayActivity,
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
                                    this@QuickPayActivity,
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
                                this@QuickPayActivity,
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
                        this@QuickPayActivity,
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
                    this@QuickPayActivity,
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

   

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@QuickPayActivity, HomeActivity::class.java))
            }
            R.id.btn_submit -> {
              //  QuickConfirmation()
                getQuickPay()
            }
            R.id.add_new_receiver -> {
                startActivity(Intent(this@QuickPayActivity, AddReceiver::class.java))

            }
            R.id.add_new_sender -> {
                startActivity(Intent(this@QuickPayActivity, AddSender::class.java))

            }
            R.id.btn_forgot_mpin -> {
                ForgotMpin()

            }

        }
    }

    private fun ForgotMpin() {

        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@QuickPayActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@QuickPayActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("41"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SenderID", MscoreApplication.encryptStart("9539036341"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))


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
                    val call = apiService.getResendMpin(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-forgotmpin", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    /*val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountStatement")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())*/


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@QuickPayActivity,
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
                                        this@QuickPayActivity,
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
                                    this@QuickPayActivity,
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
                    val builder = AlertDialog.Builder(this@QuickPayActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@QuickPayActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    private fun QuickConfirmation() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {

                try {
                    val amount = mAmountEt!!.text.toString().replace(",".toRegex(), "")
                    val receiverObj: SenderReceiver =
                        mReceiverSpinner!!.selectedItem as SenderReceiver
                    val accountNumber = mAccountSpinner!!.selectedItem.toString()


                    val message = mMessageEt!!.text.toString()

                    val senderObj: SenderReceiver = mSenderSpinner!!.selectedItem as SenderReceiver

                    val sender = java.lang.String.valueOf(senderObj.userId)
                    val senderName = java.lang.String.valueOf(senderObj.senderName)
                    val senderAccountno = java.lang.String.valueOf(senderObj.receiverAccountno)
                    val senderMobile = java.lang.String.valueOf(senderObj.senderMobile)


                    val recievererName = java.lang.String.valueOf(receiverObj.senderName)
                    val receiverAccountno = java.lang.String.valueOf(receiverObj.receiverAccountno)
                    val recieverMobile = java.lang.String.valueOf(receiverObj.senderMobile)

                    val receiver = java.lang.String.valueOf(receiverObj.userId)

                    val mPinString = mPin!!.text.toString().trim { it <= ' ' }
                    val branch = BranchName!!

                    val builder = AlertDialog.Builder(applicationContext)
                    val layout = LayoutInflater.from(applicationContext).inflate(
                        R.layout.quick_pay_confirmation_popup,
                        null
                    )
                    val tvbranch = layout.findViewById<TextView>(R.id.tvbranch)
                    val tv_sender_name = layout.findViewById<TextView>(R.id.tv_sender_name)
                    val tv_sender_acc_no = layout.findViewById<TextView>(R.id.tv_sender_acc_no)
                    val tv_sender_mob_no = layout.findViewById<TextView>(R.id.tv_sender_mob_no)
                    val tv_reciever_name = layout.findViewById<TextView>(R.id.tv_reciever_name)
                    val tv_reciever_acc_no = layout.findViewById<TextView>(R.id.tv_reciever_acc_no)
                    val tv_reciever_mob_no = layout.findViewById<TextView>(R.id.tv_reciever_mob_no)

                    val tv_amount = layout.findViewById<TextView>(R.id.tv_amount)
                    val tv_amount_words = layout.findViewById<TextView>(R.id.tv_amount_words)
                    val text_confirmationmsg =
                        layout.findViewById<TextView>(R.id.text_confirmationmsg)
                    val bt_ok = layout.findViewById<TextView>(R.id.bt_ok)
                    val bt_cancel = layout.findViewById<TextView>(R.id.bt_cancel)
                    builder.setView(layout)
                    val alertDialog = builder.create()

                    tvbranch.text = BranchName
                    tv_sender_name.text = senderName
                    tv_sender_acc_no.text = accountNumber
                    tv_sender_mob_no.text = senderMobile
                    tv_reciever_name.text = recievererName
                    tv_reciever_acc_no.text = receiverAccountno
                    tv_reciever_mob_no.text = recieverMobile

                    val num = ("" + amount).toDouble()
                    val stramnt: String? = Config.getDecimelFormate(num)
                    text_confirmationmsg.text = "Proceed Payment With Above Amount" + "..?"
                    val netAmountArr = amount.split("\\.".toRegex()).toTypedArray()
                    var amountInWordPop = ""
                    if (netAmountArr.size > 0) {
                        val integerValue = netAmountArr[0].toInt()
                        amountInWordPop =
                            "Rupees " + NumberToWord.convertNumberToWords(integerValue)
                        if (netAmountArr.size > 1) {
                            val decimalValue = netAmountArr[1].toInt()
                            if (decimalValue != 0) {
                                amountInWordPop += " And " + NumberToWord.convertNumberToWords(
                                    decimalValue
                                ).toString() + " Paise"
                            }
                        }
                        amountInWordPop += " Only"
                    }
                    tv_amount_words.text = "" + amountInWordPop
                    tv_amount.text = "₹ $stramnt"
                    bt_cancel.setOnClickListener { alertDialog.dismiss() }
                    bt_cancel.setOnClickListener {
                       // getQuickPay()
                        alertDialog.dismiss() }

                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }

            }

    }

    private fun getQuickPay() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@QuickPayActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@QuickPayActivity))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("41"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "FK_Sender",
                            MscoreApplication.encryptStart("1")
                        )
                        requestObject1.put(
                            "FK_Customer",
                            MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                            "FK_Receiver",
                            MscoreApplication.encryptStart("2")
                        )
                        requestObject1.put(
                            "Amount",
                            MscoreApplication.encryptStart("500")
                        )
                        requestObject1.put(
                            "AccountNo",
                            MscoreApplication.encryptStart("001001001002")
                        )
                        requestObject1.put(
                            "SubModule",
                            MscoreApplication.encryptStart("ddsb")
                        )
                        requestObject1.put(
                            "MPIN",
                            MscoreApplication.encryptStart("123456")
                        )
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )


                        Log.e("TAG", "requestObject1  QUICKPAY   " + requestObject1)
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
                    val call = apiService.getQuickPay(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-quickpay", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                        jObject.getJSONObject("PassBookAccountStatement")





                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@QuickPayActivity,
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
                                    this@QuickPayActivity,
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
                                this@QuickPayActivity,
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
                    val builder = AlertDialog.Builder(this@QuickPayActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@QuickPayActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val splitupdetail: Splitupdetail = arrayList1!!.get(position)
        branchname = splitupdetail.getBranchname()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}


