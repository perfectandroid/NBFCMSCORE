package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Model.Receivers
import com.perfect.nbfcmscore.Model.SenderReceiverlist
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
    private var mMessageEt: EditText? = null
    private var add_new_sender: TextView?=null
    private var receiver: String?=null
    val newSenders: ArrayList<SenderReceiverlist> = ArrayList<SenderReceiverlist>()
    private var add_new_receiver: TextView?=null
    public var arrayList1: ArrayList<Splitupdetail>? = null
    public var arrayList2: ArrayList<SenderReceiverlist>? = null
    public var arrayList3: ArrayList<SenderReceiverlist>? = null
    public var arrayList4: ArrayList<Receivers>? = null
    private val mPin: AppCompatEditText? = null
    private val mProgressDialog: ProgressDialog? = null
    private var mSenderSpinner: Spinner? = null
    private var mReceiverSpinner: Spinner? = null
    private val mOtpResendLink: String? = null
    private val mCanLoadSenderReceiver = false
    var reference: String? = null
    var submodule: String? = null
    var fkSenderId: String? = null
    var senderName: String? = null
    var senderMobile: String? = null
    var receiverAccountno: String? = null
    var receiverid: String? = null
    var mode: String? = null


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
    var etxt_amount: EditText? = null
    var etxt_mpin: EditText? = null
    var tv_header: TextView? = null
    var txtv_slctacnt: TextView? = null
    var txtv_slctsndr: TextView? = null
    var txtv_slctrecvr: TextView? = null
    var sendername: String? = null
    var senderid: String? = null
    public var branchname:String?=null
    var btn_forgot_mpin: Button? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quickpay)
        setRegviews()
        getAccountnumber()
        getSenderReceiver()

        val slctacntSP = applicationContext.getSharedPreferences(Config.SHARED_PREF135, 0)
        txtv_slctacnt!!.setText(slctacntSP.getString("SelectAccount", null))

        val slctsndrSP = applicationContext.getSharedPreferences(Config.SHARED_PREF136, 0)
        txtv_slctsndr!!.setText(slctsndrSP.getString("SelectSender", null))

        val slctrcvrSP = applicationContext.getSharedPreferences(Config.SHARED_PREF137, 0)
        txtv_slctrecvr!!.setText(slctrcvrSP.getString("SelectReceiver", null))

        val slctaddnwsndSP = applicationContext.getSharedPreferences(Config.SHARED_PREF138, 0)
        add_new_sender!!.setText(slctaddnwsndSP.getString("AddNewSender", null))

        val slctaddnwrecvrSP = applicationContext.getSharedPreferences(Config.SHARED_PREF139, 0)
        add_new_receiver!!.setText(slctaddnwrecvrSP.getString("AddNewReceiver", null))

        val amtpaybleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF95, 0)
        etxt_amount!!.setHint(amtpaybleSP.getString("AmountPayable", null))

        val MpinSP = applicationContext.getSharedPreferences(Config.SHARED_PREF140, 0)
        etxt_mpin!!.setHint(MpinSP.getString("MPIN", null))

        val frgtMpinSP = applicationContext.getSharedPreferences(Config.SHARED_PREF141, 0)
        btn_forgot_mpin!!.setText(frgtMpinSP.getString("ForgotMPIN", null))

        val Makepaymntsp = applicationContext.getSharedPreferences(Config.SHARED_PREF142, 0)
        mBtnSubmit!!.setText(Makepaymntsp.getString("MAKEPAYMENT", null))

        val Fundtransfrsp = applicationContext.getSharedPreferences(Config.SHARED_PREF65, 0)
        tv_header!!.setText(Fundtransfrsp.getString("QuickPay", null))




    }





    private fun getSenderReceiver() {
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
                            .sslSocketFactory(Config.getSSLSocketFactory(this@QuickPayActivity))
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

                                    val jresult = jsonobj2.getJSONArray("QuickPaySenderReciverlist")


                                    arrayList2 = ArrayList<SenderReceiverlist>()
                                    arrayList3 = ArrayList<SenderReceiverlist>()

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            if (json.getString("Mode").equals("1")) {
                                                arrayList2!!.add(
                                                        SenderReceiverlist(
                                                                json.getString("UserID"),
                                                                json.getString(
                                                                        "FK_SenderID"
                                                                ),
                                                                json.getString(
                                                                        "SenderName"
                                                                ),
                                                                json.getString(
                                                                        "SenderMobile"
                                                                ), json.getString(
                                                                "ReceiverAccountno"
                                                        ), json.getString(
                                                                "Mode"
                                                        )
                                                        )
                                                )

                                            } else if (json.getString("Mode").equals("2")) {
                                                arrayList3!!.add(SenderReceiverlist(
                                                        json.getString("UserID"),
                                                        json.getString(
                                                                "FK_SenderID"
                                                        ),
                                                        json.getString(
                                                                "SenderName"
                                                        ),
                                                        json.getString(
                                                                "SenderMobile"
                                                        ), json.getString(
                                                        "ReceiverAccountno"
                                                ), json.getString(
                                                        "Mode"
                                                )
                                                )
                                                )
                                            }

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    mSenderSpinner!!.adapter = ArrayAdapter(
                                            this@QuickPayActivity,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList2!!
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

    private fun setRegviews() {

        btn_forgot_mpin= findViewById<Button>(R.id.btn_forgot_mpin)
        txt_amtinword= findViewById<TextView>(R.id.txt_amtinword)
        mMessageEt= findViewById<EditText>(R.id.message)

        txtv_slctacnt= findViewById<TextView>(R.id.txtv_slctacnt)
        txtv_slctsndr= findViewById<TextView>(R.id.txtv_slctsndr)
        txtv_slctrecvr= findViewById<TextView>(R.id.txtv_slctrecvr)
        tv_header= findViewById<TextView>(R.id.tv_header)


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

        mSenderSpinner= findViewById<Spinner>(R.id.sender_spinner)
        mReceiverSpinner= findViewById<Spinner>(R.id.receiver_spinner)

        mSenderSpinner!!.onItemSelectedListener = this
        mReceiverSpinner!!.onItemSelectedListener = this

        etxt_mpin= findViewById<EditText>(R.id.etxt_mpin)

        etxt_amount= findViewById<EditText>(R.id.etxt_amount)
        etxt_amount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                etxt_amount!!.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    val longval: Double
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = originalString.toDouble()
                    val formattedString: String? = Config.getDecimelFormateForEditText(longval)
                    //                    Long longval;
//                    if (originalString.contains(",")) {
//                        originalString = originalString.replaceAll(",", "");
//                    }
//                    longval = Long.parseLong(originalString);
//
//                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//                    formatter.applyPattern("#,###,###,###");
//                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etxt_amount!!.setText(formattedString)
                    etxt_amount!!.setSelection(etxt_amount!!.text!!.length)
                    val amnt = etxt_amount!!.text.toString().replace(",".toRegex(), "")
                    val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()
                    var amountInWordPop = ""
                    if (netAmountArr.size > 0) {
                        val integerValue = netAmountArr[0].toInt()
                        amountInWordPop = "Rupees " + NumberToWord.convertNumberToWords(integerValue)
                        if (netAmountArr.size > 1) {
                            val decimalValue = netAmountArr[1].toInt()
                            if (decimalValue != 0) {
                                amountInWordPop += " and " + NumberToWord.convertNumberToWords(decimalValue).toString() + " paise"
                            }
                        }
                        amountInWordPop += " only"
                    }
                    txt_amtinword!!.text = "" + amountInWordPop
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
                        mBtnSubmit!!.text = "MAKE PAYMENT OF  " + "\u20B9 " + Config.getDecimelFormate(num)
                    } else {
                        mBtnSubmit!!.text = "MAKE PAYMENT"
                    }
                } catch (e: NumberFormatException) {
                }

            }
        })
    }

    private fun getAccountnumber() {
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
                            .sslSocketFactory(Config.getSSLSocketFactory(this@QuickPayActivity))
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
                QuickConfirmation()


            }
            R.id.add_new_receiver -> {
                startActivity(Intent(this@QuickPayActivity, AddReceiver::class.java))

            }
            R.id.add_new_sender -> {
                startActivity(Intent(this@QuickPayActivity, AddSender::class.java))

            }
            R.id.btn_forgot_mpin -> {
                if (senderid.equals("-100")) {
                    Toast.makeText(this, "Please select sender", Toast.LENGTH_LONG).show()
                } else {
                    ForgotMpin(senderid)
                }


            }

        }
    }

    private fun QuickConfirmation() {

        if (isValid()) {
                 if(ConnectivityUtils.isConnected(this))
                 {

                     try {
                         val amount =etxt_amount!!.text.toString().replace(",".toRegex(), "")
                         val receiverObj = mReceiverSpinner!!.selectedItem.toString()
                         val accountNumber = mAccountSpinner!!.selectedItem.toString()
                         val tokens = StringTokenizer(accountNumber, "(")
                         val first = tokens.nextToken()
                         val message = mMessageEt!!.text.toString()
                         val senderObj = mSenderSpinner!!.selectedItem.toString()
                         val sender: String = senderid!!
                         val senderName: String = sendername!!
                         val senderAccountno: String = receiverAccountno!!
                         val senderMobile: String = senderMobile!!
                         val recievererName: String = senderName
                         val receiverAccountno: String = receiverAccountno!!
                         val recieverMobile: String = senderMobile
                         val receiver: String = receiverid!!
                         val mPinString = etxt_mpin!!.text.toString()
                         var branch = branchname
                         var submodle =submodule
                         val builder = AlertDialog.Builder(this)
                         val inflater1 = this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                         val layout = inflater1.inflate(R.layout.quick_pay_confirmation_popup, null)
                         val tvbranch = layout.findViewById<TextView>(R.id.tvbranch)
                         val tv_sender_name = layout.findViewById<TextView>(R.id.tv_sender_name)
                         val tv_sender_acc_no = layout.findViewById<TextView>(R.id.tv_sender_acc_no)
                         val tv_sender_mob_no = layout.findViewById<TextView>(R.id.tv_sender_mob_no)
                         val tv_reciever_name = layout.findViewById<TextView>(R.id.tv_reciever_name)
                         val tv_reciever_acc_no = layout.findViewById<TextView>(R.id.tv_reciever_acc_no)
                         val tv_reciever_mob_no = layout.findViewById<TextView>(R.id.tv_reciever_mob_no)
                         val tv_amount = layout.findViewById<TextView>(R.id.tv_amount)
                         val tv_amount_words = layout.findViewById<TextView>(R.id.tv_amount_words)
                         val text_confirmationmsg = layout.findViewById<TextView>(R.id.text_confirmationmsg)
                         val bt_ok = layout.findViewById<TextView>(R.id.bt_ok)
                         val bt_cancel = layout.findViewById<TextView>(R.id.bt_cancel)
                         builder.setView(layout)
                         val alertDialog = builder.create()
                         tvbranch.text = branch
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
                             amountInWordPop = "Rupees " + NumberToWord.convertNumberToWords(integerValue)
                             if (netAmountArr.size > 1) {
                                 val decimalValue = netAmountArr[1].toInt()
                                 if (decimalValue != 0) {
                                     amountInWordPop += " And " + NumberToWord.convertNumberToWords(decimalValue).toString() + " Paise"
                                 }
                             }
                             amountInWordPop += " Only"
                         }
                         tv_amount_words.text = "" + amountInWordPop
                         tv_amount.text = "₹ $stramnt"
                         bt_cancel.setOnClickListener { alertDialog.dismiss() }
                         bt_ok.setOnClickListener {
                             if (branch != null) {
                                 getQuickPay(first, fkSenderId!!, receiver, amount, message, mPinString, senderName, senderAccountno, senderMobile, recievererName, receiverAccountno, recieverMobile, branch, submodle)
                             }
                            // com.creativethoughts.iscore.money_transfer.QuickPayMoneyTransferFragment.MoneyTransferAsyncTask(accountNumber, sender, receiver, amount, message, mPinString, senderName, senderAccountno, senderMobile, recievererName, receiverAccountno, recieverMobile, branch).execute()
                             alertDialog.dismiss()
                         }
                         alertDialog.show()
                     } catch (e: java.lang.Exception) {
                         e.printStackTrace()
                     }
                 }
            else {
                     alertMessage1("", "Network is currently unavailable. Please try again later.")


            }

        }

    }

    private fun isValid(): Boolean {

        val amount = etxt_amount!!.text.toString()


        if (TextUtils.isEmpty(amount)) {
            CustomBottomSheeet.Show(this,"Please enter the amount","0")
           // etxt_amount!!.error = "Please enter the amount"
            return false
        }
        val amt: Double
        try {
            amt = amount.replace(",".toRegex(), "").toDouble()
        } catch (e: java.lang.Exception) {
            CustomBottomSheeet.Show(this,"Invalid format","0")
          //  etxt_amount!!.error = "Invalid format"
            return false
        }

        if (amt < 1) {
            CustomBottomSheeet.Show(this,"Please enter the amount","0")
           // etxt_amount!!.error = "Please enter the amount"
            return false
        }

       // etxt_amount!!.error = null

        val mPinString = etxt_mpin!!.text.toString()
        if (mPinString.equals("0")) {
            CustomBottomSheeet.Show(this,"Please enter the M-PIN","0")
            //etxt_mpin!!.error = "Please enter the M-PIN"
            return false
        }
        else if (mPinString.equals("")) {
           // etxt_mpin!!.error = "Please enter the M-PIN"
            CustomBottomSheeet.Show(this,"Please enter the M-PIN","0")
            return false
        }
       // etxt_mpin!!.error = null

        return true

    }

    private fun ForgotMpin(senderid: String?) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("41"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SenderID", MscoreApplication.encryptStart(senderid))
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
                                if (response.toString()== "1") {
                                    Toast.makeText(applicationContext, "M-Pin is resend to your mobile", Toast.LENGTH_LONG).show()
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

    /*private fun QuickConfirmation() {
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
                        alertDialog.dismiss()
                    }

                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }

            }

    }*/

    private fun getQuickPay(accountNumber: String, sender: String, receiver: String, amount: String, message: String, mPinString: String, senderName: String, senderAccountno: String, senderMobile: String, recievererName: String, receiverAccountno: String, recieverMobile: String, branch: String, submodle: String?) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("41"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Sender",
                                MscoreApplication.encryptStart(sender)
                        )
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "FK_Receiver",
                                MscoreApplication.encryptStart(receiver)
                        )
                        requestObject1.put(
                                "Amount",
                                MscoreApplication.encryptStart(amount)
                        )
                        requestObject1.put(
                                "AccountNo",
                                MscoreApplication.encryptStart(accountNumber)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(submodle)
                        )
                        requestObject1.put(
                                "MPIN",
                                MscoreApplication.encryptStart(mPinString)
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

                                if (jObject.getString("StatusCode")!=null &&
                                        jObject.getString("StatusCode").equals("200") && jObject.getString("otpRefNo").equals("0")) {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("QuickPayMoneyTransferPayment")


                                } else if (jObject.getString("StatusCode")!=null &&
                                        jObject.getString("StatusCode").equals("200") && !jObject.getString("otpRefNo").equals("0")) {
                                    // otprefno!=0
                                    startActivity(Intent(this@QuickPayActivity, TransactionOTPActivity::class.java))


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

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

        if (parent!!.id == R.id.spn_account_num) {
            val splitupdetail: Splitupdetail = arrayList1!!.get(position)
            branchname = splitupdetail.getBranchname()
            submodule = splitupdetail.getSubmodule()
        } else if (parent!!.id == R.id.sender_spinner) {
            val senderreceiver: SenderReceiverlist = arrayList2!!.get(position)
            senderid = senderreceiver.getFkSenderId()
            sendername =senderreceiver.getSenderName()
            fkSenderId=senderreceiver.getUserId()
            senderName=senderreceiver.getSenderName()
            senderMobile=senderreceiver.getSenderMobile()
            receiverAccountno=senderreceiver.getReceiverAccountno()
            receiverid = senderreceiver.getUserId()
            mode =senderreceiver.getMode()

            arrayList4 = ArrayList<Receivers>()
            for (i in 0 until arrayList3!!.size) {

                val senderReceiver: SenderReceiverlist = arrayList3!!.get(i) ?: continue
                var mod =senderReceiver.getMode()
                var user =senderreceiver.getUserId()
                var fkuser =senderReceiver.getFkSenderId()
                if (senderreceiver.getUserId().equals(senderReceiver.getFkSenderId())) {
                 //  arrayList4!!.add(senderReceiver)
                    arrayList4!!.add(
                            Receivers(

                                    senderReceiver.getSenderName(),
                                    senderReceiver.getReceiverAccountno()
                            ))
                }
            }
            mReceiverSpinner!!.adapter = ArrayAdapter(
                    this@QuickPayActivity,
                    android.R.layout.simple_spinner_dropdown_item, arrayList4!!
            )

        //    mReceiverSpinner!!.setAdapter(newSenders);
           // arrayList3!!.addItems(newSenders)
        }




    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun alertMessage1(msg1: String, msg2: String) {
        val dialogBuilder = AlertDialog.Builder(this@QuickPayActivity)
        val inflater: LayoutInflater = this@QuickPayActivity.getLayoutInflater()
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


