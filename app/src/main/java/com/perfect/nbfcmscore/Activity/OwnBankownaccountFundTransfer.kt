package com.perfect.nbfcmscore.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.BalancesplitAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class OwnBankownaccountFundTransfer : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    val TAG: String? = "OwnBankownaccountFundTransfer"
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    public var arrayList1: ArrayList<Splitupdetail>? = null
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    private var txt_amtinword: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var textView: TextView? = null
    private var txtv_acno: TextView? = null
    private var txtv_payingto: TextView? = null
    private var txtv_acno1: TextView? = null
    private var txtamtpayable: TextView? = null
    private var txtvremark: TextView? = null
    private var tv_availbal: TextView? = null
    var status_spinner: Spinner? = null
    private var edt_txt_amount: EditText? = null
    private var edt_txt_remark: EditText? = null
    public var tv_balance: TextView? = null
    public var spn_account_num: Spinner? = null
    private var progressDialog: ProgressDialog? = null
    private var btn_submit: Button? = null
    private var btn_clear: Button? = null
    private var rv_split_details: FullLenghRecyclertview? = null
    private var card_split_details: CardView? = null
    private var ll_list: LinearLayout? = null
    private var ll_needTochange: LinearLayout? = null
    private var ll_needToPayAdvance: LinearLayout? = null
    private var ll_remittance: LinearLayout? = null
    var pendinginsa = arrayOfNulls<String>(0)
    public var BranchName: String? = null
    public var Balance: String? = null
    public var Submod: String? = null
    public var branchName1: String? = null
    public var Acnt: String? = null
    private var jresult: JSONArray? = null
    public var fkaccount:String?=null
    public var submodule:String?=null
    var compareValue = "Select Account"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ownbankfundtransfer)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        setRegViews()
        getAccountnumber()

    }



    private fun setRegViews() {
        status_spinner = findViewById(R.id.status_spinner)
        status_spinner!!.setOnItemSelectedListener(this)
        spn_account_num = findViewById<Spinner>(R.id.spn_account_type)
        btn_submit = findViewById<Button>(R.id.btn_submit)
        btn_clear = findViewById<Button>(R.id.btn_submit)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        ll_needToPayAdvance= findViewById(R.id.ll_needToPayAdvance)
        ll_needTochange= findViewById(R.id.ll_needTochange)
        ll_remittance= findViewById(R.id.ll_needTochange)
        txt_amtinword= findViewById(R.id.txt_amtinword)
        edt_txt_amount = findViewById(R.id.edt_txt_amount)

        textView= findViewById(R.id.textView)
        txtv_acno= findViewById(R.id.txtv_acno)
        txtv_payingto= findViewById(R.id.txtv_payingto)
        txtv_acno1= findViewById(R.id.txtv_acno1)
        txtamtpayable= findViewById(R.id.txtamtpayable)
        txtvremark= findViewById(R.id.txtvremark)


        val PayingFromSP = applicationContext.getSharedPreferences(Config.SHARED_PREF93, 0)
        val AccnoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF107, 0)
        val PayingToSP = applicationContext.getSharedPreferences(Config.SHARED_PREF94, 0)
        val AMtpaybleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF95, 0)
        val RemarkeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF96, 0)
        val PAYSP = applicationContext.getSharedPreferences(Config.SHARED_PREF97, 0)


        textView!!.setText(PayingFromSP.getString("PayingFrom", null))
        txtv_acno!!.setText(AccnoSP.getString("AccountNo", null))
        txtv_acno1!!.setText(AccnoSP.getString("AccountNo", null)+" : ")
        txtv_payingto!!.setText(PayingToSP.getString("PayingTo", null))
        txtamtpayable!!.setText(AMtpaybleSP.getString("AmountPayable", null))
        txtvremark!!.setText(RemarkeSP.getString("Remark", null))





        tv_availbal= findViewById(R.id.tv_availbal)
        edt_txt_remark= findViewById(R.id.edt_txt_remark)
        tv_account_no = findViewById(R.id.tv_account_no)
        tv_branch_name = findViewById(R.id.tv_branch_name)
        tv_balance = findViewById(R.id.tv_balance)

        Submod = intent.getStringExtra("SubModule")
        Log.i("Submod1", Submod!!)
        BranchName = intent.getStringExtra("Branch")
        Balance = intent.getStringExtra("Balance")
        Acnt = intent.getStringExtra("A/c")
        Log.i("Details", BranchName + Balance + Acnt)

        val amt1 =Balance!!.toDouble()
        tv_account_no!!.text=Acnt
        tv_balance!!.text="\u20B9 " + Config.getDecimelFormate(amt1)

        tv_branch_name!!.text=BranchName

        spn_account_num!!.onItemSelectedListener = this
        btn_submit!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)
        edt_txt_amount!!.setOnClickListener(this)
        edt_txt_remark!!.setOnClickListener(this)

        rv_split_details = findViewById(R.id.rv_split_details)
//        card_split_details = findViewById(R.id.card_split_details)
        ll_list = findViewById(R.id.ll_list)

        edt_txt_amount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                edt_txt_amount!!.removeTextChangedListener(this)
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
                        edt_txt_amount!!.setText(formattedString)
                        edt_txt_amount!!.setSelection(edt_txt_amount!!.getText().length)
                        val amnt: String = edt_txt_amount!!.getText().toString().replace(
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
                edt_txt_amount!!.addTextChangedListener(this)
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
                        // btn_submit!!.setText(PAYSP.getString("PAY", null))
                        btn_submit!!.setText(PAYSP.getString("PAY", null) + "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        btn_submit!!.setText(PAYSP.getString("PAY", null))
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
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankownaccountFundTransfer))
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
                                MscoreApplication.encryptStart("2")
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
                        Log.e("TAG", "Some  3091   " + e.toString())
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
                                Log.i("Response2", response.body())
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
                                    spn_account_num!!.adapter = ArrayAdapter(
                                            this@OwnBankownaccountFundTransfer,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1!!
                                    )

                                    //    spn_account_num!!.setSelection(arrayList1.indexOf("Select Account"));

                                } else {
                                    Log.e("TAG", "Some  3092   ")
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankownaccountFundTransfer,
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
                                Log.e("TAG", "Some  3093   " + e.toString())
                                val builder = AlertDialog.Builder(
                                        this@OwnBankownaccountFundTransfer,
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
                            Log.e("TAG", "Some  3094   " + t.message)
                            val builder = AlertDialog.Builder(
                                    this@OwnBankownaccountFundTransfer,
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
                    Log.e("TAG", "Some  3095   " + e.toString())
                    //  progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                            this@OwnBankownaccountFundTransfer,
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
                        this@OwnBankownaccountFundTransfer,
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
                startActivity(Intent(this@OwnBankownaccountFundTransfer, HomeActivity::class.java))
            }
            R.id.btn_submit -> {
                if (this == null) return
                // String recieverAccountNo = confirmAndSetRecieversAccountNo();
                val accountPayingTo: String = spn_account_num!!.getSelectedItem().toString()
                try {
                    if (accountPayingTo !== "Select Account") {
                        val recieverAccountNo = accountPayingTo.substring(0, 12)
                        Log.e("TAG", "recieverAccountNo   619   $recieverAccountNo")
                        if (isValid() && recieverAccountNo.length == 12) {
                            if (ConnectivityUtils.isConnected(this)) {
//                final String accountNumber = mAccountSpinner.getSelectedItem().toString();
                                var SourceAccountNumber = tv_account_no!!.text.toString()
                                val accountNumber: String = SourceAccountNumber
                                var amount: String = edt_txt_amount!!.getText().toString()
                                val remark = edt_txt_remark!!.text.toString()
                                val fromAccountNo = accountNumber.substring(0, 12)
                                val type = accountPayingTo.substring(accountPayingTo.indexOf("(") + 1, accountPayingTo.indexOf(")"))
                                Log.e("TAG", "type   763  $type")
                                amount = amount.replace(",", "")
                                if (amount.length != 0 && amount.toFloat() > 0) {
                                    if (accountNumber != accountPayingTo) {
                                        val dialogBuilder = AlertDialog.Builder(this)
                                        val inflater = this.layoutInflater
                                        val dialogView: View = inflater.inflate(R.layout.confirmation_fund_popup, null)
                                        dialogBuilder.setCancelable(false)
                                        dialogBuilder.setView(dialogView)
                                        val amnt: String = edt_txt_amount!!.getText().toString().replace(",".toRegex(), "")
                                        val text_confirmationmsg = dialogView.findViewById<TextView>(R.id.text_confirmationmsg)
                                        val tv_amount = dialogView.findViewById<TextView>(R.id.tv_amount)
                                        val txtvAcntno = dialogView.findViewById<TextView>(R.id.txtvAcntno)
                                        val txtvbranch = dialogView.findViewById<TextView>(R.id.txtvbranch)
                                        val txtvbalnce = dialogView.findViewById<TextView>(R.id.txtvbalnce)
                                        val txtvAcntnoto = dialogView.findViewById<TextView>(R.id.txtvAcntnoto)
                                        val txtvbranchto = dialogView.findViewById<TextView>(R.id.txtvbranchto)
                                        val img_aapicon: ImageView = dialogView.findViewById<ImageView>(R.id.img_aapicon)
                                        val txtvbalnceto = dialogView.findViewById<TextView>(R.id.txtvbalnceto)
                                        txtvAcntno.text = "A/C No : $SourceAccountNumber"
                                        txtvbranch.text = "Branch :$BranchName"
                                        val num1 = Balance!!.toDouble()
                                        val fmt = DecimalFormat("#,##,###.00")
                                        txtvbalnce.text = "Available Bal: " + "\u20B9 " + Config.getDecimelFormate(num1)
                                        val tv_amount_words = dialogView.findViewById<TextView>(R.id.tv_amount_words)
                                        val butOk = dialogView.findViewById<Button>(R.id.btnOK)
                                        val butCan = dialogView.findViewById<Button>(R.id.btnCncl)
                                        txtvAcntnoto.text = "A/C No: " + spn_account_num!!.getSelectedItem().toString()
                                        txtvbranchto.text = "Branch :$branchName1"
                                        val num = ("" + amnt).toDouble()
                                        var stramnt: String? = Config.getDecimelFormate(num)
                                        text_confirmationmsg.text = "Proceed Transaction with above receipt amount" + "..?"

                                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                                        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
                                        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
                                        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)

                                        PicassoTrustAll.getInstance(this@OwnBankownaccountFundTransfer)!!.load(imagepath).error(android.R.color.transparent).into(img_aapicon!!)

                                        // text_confirmationmsg.setText("Proceed Transaction with above receipt amount to A/C no " + accNumber + " ..?");
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
                                        tv_amount_words.text = "" + amountInWordPop
                                        tv_amount.text = "₹ $stramnt"
                                        val alertDialog = dialogBuilder.create()
                                        alertDialog.show()
                                        val finalAmount = amount
                                        butOk.setOnClickListener { v: View? ->
                                            alertDialog.dismiss()
                                            getOwnAccountFundTransfer(accountNumber, Submod, recieverAccountNo, submodule, finalAmount, remark)
                                            //startTransfer(accountNumber, type, recieverAccountNo, finalAmount, remark)
                                        }
                                        butCan.setOnClickListener { alertDialog.dismiss() }
                                    } else {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Account Numbers Are Same, Please Select Other Account For Transaction.")
                                                .setCancelable(false)
                                                .setPositiveButton("OK") { dialog, id -> }

                                        //Creating dialog box
                                        val alert = builder.create()
                                        alert.show()
                                    }
                                } else {
                                    Toast.makeText(this, "Please Enter Valid Amount.", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                alertMessage1("", "Network is currently unavailable. Please try again later.")

                                // DialogUtil.showAlert(this,
                                //  "Network is currently unavailable. Please try again later.");
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please Select Paying To Account Number.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }
            R.id.ll_needToPayAdvance -> {
                if (ll_remittance!!.visibility == View.GONE) {
                    ll_remittance!!.visibility = View.VISIBLE
                } else {
                    ll_remittance!!.visibility = View.GONE
                }

            }

            R.id.btn_clear -> {
                getAccountnumber()

                txt_amtinword!!.text = ""
//                setAccountType();
                edt_txt_amount!!.setText("")
            }
        }
    }

    private fun getOwnAccountFundTransfer(accountNumber: String, Submod: String?, recieverAccountNo: String, submodule: String?, finalAmount: String, remark: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(
                        this@OwnBankownaccountFundTransfer,
                        R.style.Progress
                )
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankownaccountFundTransfer))
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
                        var amount = edt_txt_amount!!.text.toString()
                        amount = amount.replace(",", "")
                        //requestObject1.put("Reqmode", MscoreApplication.encryptStart("27"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        /* requestObject1.put(
                                "AccountNo",
                                MscoreApplication.encryptStart(tv_account_no!!.text.toString())
                        )*/
                        val tokens1 = StringTokenizer(accountNumber, "(")
                        val acno = tokens1.nextToken() //
                        var accno = acno.replace(" ", "");
                        requestObject1.put(
                                "AccountNo",
                                MscoreApplication.encryptStart(accno)
                        )

                        requestObject1.put("SubModule", MscoreApplication.encryptStart(Submod))
                        //    requestObject1.put("SubModule", MscoreApplication.encryptStart(this.Submod))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(finalAmount))
                        val tokens = StringTokenizer(recieverAccountNo, "(")
                        val recacno = tokens.nextToken()

                        requestObject1.put(
                                "ReceiverAccountNo", MscoreApplication.encryptStart(
                                recacno
                        )
                        )
                        requestObject1.put(
                                "ReceiverModule", MscoreApplication.encryptStart(
                                submodule
                        )
                        )
                        requestObject1.put("QRCode", MscoreApplication.encryptStart(""))
                        requestObject1.put("Remark", MscoreApplication.encryptStart(remark))
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  650   " + requestObject1)
                    } catch (e: Exception) {

                        Log.e("TAG", "Some  6791   " + e.toString())
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
                    val call = apiService.getfundtransferownBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-ownfd", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("FundTransferToOwnBank")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())
                                    var result = jsonobj2.getString("ResponseMessage")
                                    var refid = jsonobj2.getString("RefID")
                                    var amt = jsonobj2.getString("Amount")
                                    var reacc = jsonobj2.getString("RecAccNumber")
                                    Log.e("Result","706  "+ result)
                                    Log.e("refid","706   "+  refid)
                                    alertPopup(refid, amt, reacc, result)
                                    //   alertMessage1("", result)
                                    // Toast.makeText(applicationContext, result, Toast.LENGTH_LONG)
                                    //  .show()


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankownaccountFundTransfer,
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
                                Log.e("TAG", "Some  6792   " + e.toString())
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@OwnBankownaccountFundTransfer,
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
                            Log.e("TAG", "Some  6793   " + t.message)
                            val builder = AlertDialog.Builder(
                                    this@OwnBankownaccountFundTransfer,
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
                    Log.e("TAG", "Some  6794   " + e.toString())
                    val builder = AlertDialog.Builder(
                            this@OwnBankownaccountFundTransfer,
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
                        this@OwnBankownaccountFundTransfer,
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

    @SuppressLint("LongLogTag")
    private fun alertPopup(refid: String, amt: String, reacc: String, result: String) {


        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        // ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.activity_success_popup, null)
        dialogBuilder.setView(dialogView)

        // EditText editText = (EditText) dialogView.findViewById(R.id.label_field);

        // EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
        val rltv_share = dialogView.findViewById<RelativeLayout>(R.id.rltv_share)
        val lay_share = dialogView.findViewById<RelativeLayout>(R.id.lay_share)
        mRecyclerView = dialogView.findViewById(R.id.recycler_message)
        val imgIcon = dialogView.findViewById<ImageView>(R.id.img_success)
        val img_share = dialogView.findViewById<ImageView>(R.id.img_share)
        val txtTitle = dialogView.findViewById<TextView>(R.id.txt_success)
        val txtMessage = dialogView.findViewById<TextView>(R.id.txt_message)
        val tvrefe = dialogView.findViewById<TextView>(R.id.tvrefe)

        val tvdate = dialogView.findViewById<TextView>(R.id.tvdate)
        val tvtime = dialogView.findViewById<TextView>(R.id.tvtime)
        val tv_amount_words = dialogView.findViewById<TextView>(R.id.tv_amount_words)

        val tv_amount = dialogView.findViewById<TextView>(R.id.tv_amount)
        val txtvAcntno = dialogView.findViewById<TextView>(R.id.txtvAcntno)
        val txtvbranch = dialogView.findViewById<TextView>(R.id.txtvbranch)
        val txtvbalnce = dialogView.findViewById<TextView>(R.id.txtvbalnce)

        val txtvAcntnoto = dialogView.findViewById<TextView>(R.id.txtvAcntnoto)
        val txtvbranchto = dialogView.findViewById<TextView>(R.id.txtvbranchto)
        val txtvbalnceto = dialogView.findViewById<TextView>(R.id.txtvbalnceto)
        tvrefe.text = "Ref.No : "+refid

        //current time

        //current time
      //  val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//        tvtime.text = " $currentTime"
        val currentTime = Calendar.getInstance().time
        Log.e(TAG,"currentTime  "+currentTime)
        val date: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val time: DateFormat = SimpleDateFormat("HH:mm")
//                                    val date: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm a")
        val localDate = date.format(currentTime)
        val localtime = time.format(currentTime)
        val timeParts = localtime.split(":").toTypedArray()

        var hour = timeParts[0].toInt()
        val min = timeParts[1].toInt()

        var suffix: String =""
        if(hour>11) {
            suffix = "PM";
            if(hour>12)
                hour -= 12;
        } else {
            suffix = "AM";
            if(hour==0)
                hour = 12;
        }
        var hours : String =""
        var mins : String =""
        if (hour.toString().length==1){
            hours = "0"+hour.toString()
        }else{
            hours = hour.toString()
        }

        if (min.toString().length==1){
            mins = "0"+hour.toString()
        }else{
            mins = min.toString()
        }

        val localDateTime = hours+" : "+mins +" "+suffix

        tvtime.text = "$localDateTime"


        //current date


        //current date
        val c = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = df.format(c)
        tvdate.text = "$formattedDate"

        val amnt: String = edt_txt_amount!!.getText().toString().replace(",".toRegex(), "")
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
        tv_amount_words.text = "" + amountInWordPop

        val num = ("" + amnt).toDouble()
        Log.e("TAG", "CommonUtilities  945   " + Config.getDecimelFormate(num))
        val stramnt: String? = Config.getDecimelFormate(num)


        tv_amount.text = "₹ $stramnt"



        txtvAcntno.text = "A/C :"+tv_account_no!!.text.toString()
        txtvbranch.text = "Branch :"+BranchName
        val num1 = Balance!!.toDouble() - stramnt!!.replace(",", "").toDouble()
        // double num1 = Double.parseDouble(Balance) - Double.parseDouble(stramnt);
        // double num1 = Double.parseDouble(Balance) - Double.parseDouble(stramnt);
        val fmt = DecimalFormat("#,##,###.00")

        txtvbalnce.text = "Available Bal: " + "\u20B9 " + Config.getDecimelFormate(num1)

        txtvAcntnoto.text = "A/C : " + spn_account_num!!.getSelectedItem().toString()
        txtvbranchto.text = "Branch :$BranchName"

        dialogView.findViewById<View>(R.id.rltv_footer).setOnClickListener { view1: View? ->
            try {
//                getFragmentManager().beginTransaction().replace( R.id.container, FragmentMenuCard.newInstance("EMPTY","EMPTY") )
//                        .commit();
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()
            } catch (e: NullPointerException) {
                //Do nothing
            }
        }

        try {
//            Bundle bundle = getArguments();
//            boolean isHappy = bundle.getBoolean( HAPPY );
//            String title = bundle.getString( TITLE );
//            String message = bundle.getString( MESSAGE );
        Log.e("TAG","result   884   "+result)
            txtMessage.setText(result)
            txtTitle.text = title
            /*if (!isHappy) {
                imgIcon.setImageResource(R.mipmap.ic_failed)
            }*/
            //  ArrayList<KeyValuePair> keyValuePairs = bundle.getParcelableArrayList( KEY_VALUE );
            /*      ArrayList<KeyValuePair> keyValuePairs = keyValueList;
           SuccessAdapter successAdapter = new SuccessAdapter( keyValuePairs );
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
           mRecyclerView.setLayoutManager( layoutManager );
           mRecyclerView.setAdapter( successAdapter );*/lay_share.setOnClickListener {
                Log.e("img_share", "img_share   1170   ")
                val bitmap = Bitmap.createBitmap(rltv_share.width,
                        rltv_share.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                rltv_share.draw(canvas)
                try {
                    val bmpUri: Uri = getLocalBitmapUri(bitmap)!!
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    shareIntent.type = "image/*"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(Intent.createChooser(shareIntent, "Share"))
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e("Exception", "Exception   117   $e")
                }
            }


//            img_share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Log.e("img_share","img_share   1170   ");
//                    Bitmap bitmap = Bitmap.createBitmap(rltv_share.getWidth(),
//                            rltv_share.getHeight(), Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(bitmap);
//                    rltv_share.draw(canvas);
//
//                    try {
//
//
//                        Uri bmpUri = getLocalBitmapUri(bitmap);
//
//                        Intent shareIntent = new Intent();
//                        shareIntent.setAction(Intent.ACTION_SEND);
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//                        shareIntent.setType("image/*");
//                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        startActivity(Intent.createChooser(shareIntent, "Share"));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("Exception","Exception   117   "+e.toString());
//                    }
//
//                }
//            });
        } catch (e: java.lang.Exception) {
            //Do nothing
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val splitupdetail: Splitupdetail = arrayList1!!.get(position)
        fkaccount = splitupdetail.getFkaccount()
        submodule = splitupdetail.getSubmodule()
        branchName1 =splitupdetail.getBranchname()
        //  Toast.makeText(getApplicationContext(),AccountAdapter.getItem(position).getBranchName(),Toast.LENGTH_LONG).show();
//                                                tv_as_on_date.setVisibility(View.VISIBLE);
        ll_needTochange!!.visibility = View.GONE
        ll_needToPayAdvance!!.visibility = View.GONE
        ll_remittance!!.visibility = View.GONE
      //  Toast.makeText(this, "FKAccount: " + fkaccount ,
      //   Toast.LENGTH_SHORT).show();
         balanceSplitUpDetails(fkaccount, submodule)
    }

    private fun balanceSplitUpDetails(fkaccount: String?, submodule: String?) {
//        card_split_details!!.setVisibility(View.GONE)
        ll_list!!.setVisibility(View.GONE)
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OwnBankownaccountFundTransfer, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankownaccountFundTransfer))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("27"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "FK_Account",
                                MscoreApplication.encryptStart(fkaccount)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(submodule)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  1091   " + requestObject1)
                    } catch (e: Exception) {
                        Log.e("TAG", "Some  6795   " + e.toString())
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
                    val call = apiService.getbalancesplitupdetail(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-balancesplitup", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("BalanceSplitUpDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("Data")

                                    if (jresult!!.length() != 0) {
                                        val jsonObject: JSONObject = jresult!!.getJSONObject(0)
                                        val jsonObjectjarray = jsonObject.getJSONArray("Details")
                                        if (jsonObjectjarray.length() != 0) {
                                            var Slimit = "0"
                                            var Advancelimit = "0"
                                            rv_split_details!!.setVisibility(View.VISIBLE)
//                                            card_split_details!!.setVisibility(View.VISIBLE)
                                            ll_list!!.setVisibility(View.VISIBLE)
                                            val lLayout = GridLayoutManager(this@OwnBankownaccountFundTransfer, 1)
                                            rv_split_details!!.setLayoutManager(lLayout)
                                            rv_split_details!!.setHasFixedSize(true)
                                            val adapter = BalancesplitAdapter(this@OwnBankownaccountFundTransfer, jsonObjectjarray)
                                            rv_split_details!!.setAdapter(adapter)
                                            for (k in 0 until jsonObjectjarray.length()) {
                                                val DetailsjsonObject = jsonObjectjarray.getJSONObject(k)
                                                val NetAmount = DetailsjsonObject.getString("Key")
                                                if (NetAmount == "NetAmount") {
                                                    Log.e("TAG", "onResponse   1142   ")
                                                    edt_txt_amount!!.setText(DetailsjsonObject.getString("Value").replace("-", ""))
                                                } else {
                                                    edt_txt_amount!!.setText("")
                                                }
                                                val BalanceInstallment = DetailsjsonObject.getString("Key")
                                                if (BalanceInstallment == "BalanceInstallment") {
                                                    Advancelimit = DetailsjsonObject.getString("Value")
                                                }
                                            }
                                            var IsAdvance = "0"
                                            if (submodule == "PDRD" || submodule == "ODGD") {
                                                val DetailsjsonObject = jsonObjectjarray.getJSONObject(0)
                                                Slimit = DetailsjsonObject.getString("Value")
                                                val limit = Slimit.toInt()
                                                if (limit > 0) {
                                                    ll_needTochange!!.visibility = View.VISIBLE
                                                    edt_txt_amount!!.setKeyListener(null)
                                                    edt_txt_amount!!.setEnabled(false)
                                                    edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                    edt_txt_amount!!.setFocusable(false)
                                                    pendinginsa = arrayOfNulls(limit)
                                                    IsAdvance = "0"
                                                    var i: Int
                                                    i = 0
                                                    while (i < limit) {
                                                        pendinginsa[i] = (i + 1).toString()
                                                        i++
                                                    }
                                                    getpendinginsa(submodule, fkaccount!!, IsAdvance)
                                                } else if (limit == 0 && Advancelimit != "0") {
                                                    ll_needToPayAdvance!!.visibility = View.VISIBLE
                                                    edt_txt_amount!!.setKeyListener(null)
                                                    edt_txt_amount!!.setEnabled(false)
                                                    edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                    edt_txt_amount!!.setFocusable(false)
                                                    val Advancelimitlimit = Advancelimit.toInt()
                                                    pendinginsa = arrayOfNulls(Advancelimitlimit)
                                                    IsAdvance = "1"
                                                    var i: Int
                                                    i = 0
                                                    while (i < Advancelimitlimit) {
                                                        pendinginsa[i] = (i + 1).toString()
                                                        i++
                                                    }
                                                    getpendinginsa(submodule, fkaccount!!, IsAdvance)
                                                } else {
                                                    edt_txt_amount!!.setKeyListener(null)
                                                    edt_txt_amount!!.setEnabled(false)
                                                    edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                    edt_txt_amount!!.setFocusable(false)
                                                }
                                            } else {
                                                ll_needTochange!!.visibility = View.GONE
                                                ll_needToPayAdvance!!.visibility = View.GONE
                                                ll_remittance!!.visibility = View.GONE
                                                edt_txt_amount!!.setEnabled(true)
                                                edt_txt_amount!!.setInputType(InputType.TYPE_CLASS_NUMBER)
                                                edt_txt_amount!!.setFocusable(true)
                                                edt_txt_amount!!.setFocusableInTouchMode(true)
                                            }
                                        } else {
                                            tv_availbal!!.visibility = View.GONE
                                        }
                                    } else {
                                        tv_availbal!!.visibility = View.GONE
                                    }


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankownaccountFundTransfer,
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
                                Log.e("TAG", "Some  6796   " + e.toString())
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@OwnBankownaccountFundTransfer,
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
                            Log.e("TAG", "Some  6797   " + t.message)
                            val builder = AlertDialog.Builder(
                                    this@OwnBankownaccountFundTransfer,
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
                    Log.e("TAG", "Some  6798   " + e.toString())
                    val builder = AlertDialog.Builder(this@OwnBankownaccountFundTransfer, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@OwnBankownaccountFundTransfer, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun getpendinginsa(toSubModule: String, tofkAccount: String, isAdvance: String) {

        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, pendinginsa)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        status_spinner!!.adapter = aa
        status_spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position)
                edt_txt_amount!!.setText("" + position)
                val remittanance = position + 1
                Log.e("TAG", "onResponse   355   ")
                remittanceDetails("" + remittanance, toSubModule, tofkAccount, isAdvance)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }
        }
    }

    private fun remittanceDetails(s: String, toSubModule: String, tofkAccount: String, isAdvance: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OwnBankownaccountFundTransfer, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankownaccountFundTransfer))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("28"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "FK_Account",
                                MscoreApplication.encryptStart(tofkAccount)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(toSubModule)
                        )
                        requestObject1.put(
                                "InstalmentCount",
                                MscoreApplication.encryptStart(s)
                        )
                        requestObject1.put(
                                "IsAdvance",
                                MscoreApplication.encryptStart(isAdvance)
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
                        progressDialog!!.dismiss()
                        Log.e("TAG", "Some  6799   " + e.toString())
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
                    val call = apiService.getinstanceremittanceamt(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("GetInstalmmentRemittanceAmount")
                                    val RemittanceAmount: String = jsonObj1.getString("RemittanceAmount")
                                    edt_txt_amount!!.setText(RemittanceAmount)
                                } else {
                                    edt_txt_amount!!.setText("")
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankownaccountFundTransfer,
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
                                Log.e("TAG", "Some  67910   " + e.toString())
                                val builder = AlertDialog.Builder(
                                        this@OwnBankownaccountFundTransfer,
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
                            Log.e("TAG", "Some  67911   " + t.message)
                            val builder = AlertDialog.Builder(
                                    this@OwnBankownaccountFundTransfer,
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
                    Log.e("TAG", "Some  67911   " + e.toString())
                    val builder = AlertDialog.Builder(this@OwnBankownaccountFundTransfer, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@OwnBankownaccountFundTransfer, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun alertMessage1(msg1: String, msg2: String) {
        val dialogBuilder = AlertDialog.Builder(this@OwnBankownaccountFundTransfer)
        val inflater: LayoutInflater = this@OwnBankownaccountFundTransfer.getLayoutInflater()
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
    private fun isValid(): Boolean {
        val amount: String = edt_txt_amount!!.getText().toString()
        if (amount.length < 1) return false
        //        final double amt = Double.parseDouble(amount);

//        if(amt < 1 || amt > 50000) {
//
//            edtTxtAmount.setError("Please enter amount between 0 and 50000");
//            return false;
//        }
        edt_txt_amount!!.setError(null)
        return true
    }

    private fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        //  final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        val file: File = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".png")
        Log.e("File  ", "File   142   $file")
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            try {
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            bmpUri = Uri.fromFile(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}