package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.CustomListAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Model.BarcodeAgainstCustomerAccountList
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
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

class OwnBankotheraccountFundTransfer : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, OnEditorActionListener, OnFocusChangeListener, TextWatcher {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    private var progressDialog: ProgressDialog? = null
    var arrayList1 = ArrayList<String>()
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    private var txt_amtinword: TextView? = null
    var tv_maxamount: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    public var tv_balance: TextView? = null
    public var spn_account_type: Spinner? = null
    public var BranchName: String? = null
    public var Balance: String? = null
    public var dataItem: String? = null
    private var btnScanAccounttNo: Button? = null
    private val ZXING_CAMERA_PERMISSION = 1
    public var Acnt: String? = null
    public var Submod: String? = null
    private var jresult1: JSONArray? = null
    var MaximumAmount = "0"
    var list_view: ListView? = null
    private var jresult: JSONArray? = null
    private var mScannedValue: String? = null
    private var edtTxtAccountNoFirstBlock: EditText? = null
    private var edtTxtAccountNoSecondBlock: EditText? = null
    private var edtTxtAccountNoThirdBlock: EditText? = null
    private var mAccountNumberEt: AppCompatEditText? = null
    private var mConfirmAccountNumberEt: AppCompatEditText? = null
    private var edtTxtConfirmAccountNoFirstBlock: EditText? = null
    private var edtTxtConfirmAccountNoSecondBlock: EditText? = null
    private var edtTxtConfirmAccountNoThirdBlock: EditText? = null
    private var btn_submit: Button? = null
    private  var btn_clear:android.widget.Button? = null
    private var edtTxtAmount: EditText? = null
    private var edt_txt_remark: EditText? = null

    private var txtvPayingfrom: TextView? = null
    private var txtvac_no: TextView? = null
    private var txtv_recivactype: TextView? = null
    private var txtv_acno2: TextView? = null
    private var txtv_confrmacc: TextView? = null
    private var txtv_amtpayable: TextView? = null
    private var txtv_remark: TextView? = null
    private var txtv_header: TextView? = null



    var compareValue = "Select Account"
    var tv_popuptitle: TextView? = null
    private val CustomerList = ArrayList<BarcodeAgainstCustomerAccountList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherbankfundtransfer)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        setRegViews()
        setAccountType()

        getminTransAmount()
    }



    private fun setRegViews() {


        txtv_header= findViewById(R.id.tv_header)
        mAccountNumberEt = findViewById(R.id.account_number)
        mConfirmAccountNumberEt = findViewById(R.id.confirm_account_number)

        tv_account_no = findViewById<TextView>(R.id.tv_account_no)
        tv_branch_name = findViewById<TextView>(R.id.tv_branch_name)
        spn_account_type = findViewById<Spinner>(R.id.spn_account_type)
        tv_balance = findViewById<TextView>(R.id.tv_balance)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)

        tv_maxamount = findViewById<TextView>(R.id.tv_maxamount)

        txt_amtinword = findViewById(R.id.txt_amtinword)

        spn_account_type!!.onItemSelectedListener = this

        BranchName = intent.getStringExtra("Branch")
        Balance = intent.getStringExtra("Balance")
        Acnt = intent.getStringExtra("A/c")
        Submod = intent.getStringExtra("SubModule")
        Log.i("Details", BranchName + Balance + Acnt)

        val amt1 =Balance!!.toDouble()
        tv_account_no!!.text=Acnt
        tv_balance!!.text="\u20B9 " + Config.getDecimelFormate(amt1)

        tv_branch_name!!.text=BranchName

        edtTxtAccountNoFirstBlock = findViewById(R.id.acc_no_block_one)
        edtTxtAccountNoSecondBlock = findViewById(R.id.acc_no_block_two)
        edtTxtAccountNoThirdBlock = findViewById(R.id.acc_no_block_three)

        edtTxtAccountNoFirstBlock!!.setOnEditorActionListener(this)
        edtTxtAccountNoSecondBlock!!.setOnEditorActionListener(this)
        edtTxtAccountNoThirdBlock!!.setOnEditorActionListener(this)

         edtTxtAccountNoFirstBlock!!.setOnFocusChangeListener(this)
         edtTxtAccountNoSecondBlock!!.setOnFocusChangeListener(this)
         edtTxtAccountNoThirdBlock!!.setOnFocusChangeListener(this)

         edtTxtAccountNoFirstBlock!!.addTextChangedListener(this)
         edtTxtAccountNoSecondBlock!!.addTextChangedListener(this)
         edtTxtAccountNoThirdBlock!!.addTextChangedListener(this)

        edtTxtConfirmAccountNoFirstBlock = findViewById(R.id.confirm_acc_no_block_one)
        edtTxtConfirmAccountNoSecondBlock = findViewById(R.id.confirm_acc_no_block_two)
        edtTxtConfirmAccountNoThirdBlock = findViewById(R.id.confirm_acc_no_block_three)



       edtTxtConfirmAccountNoFirstBlock!!.setOnEditorActionListener(this)
        edtTxtConfirmAccountNoSecondBlock!!.setOnEditorActionListener(this)
        edtTxtConfirmAccountNoThirdBlock!!.setOnEditorActionListener(this)

       edtTxtConfirmAccountNoFirstBlock!!.setOnFocusChangeListener(this)
         edtTxtConfirmAccountNoSecondBlock!!.setOnFocusChangeListener(this)
         edtTxtConfirmAccountNoThirdBlock!!.setOnFocusChangeListener(this)

        edtTxtConfirmAccountNoFirstBlock!!.addTextChangedListener(this)
      edtTxtConfirmAccountNoSecondBlock!!.addTextChangedListener(this)
      edtTxtConfirmAccountNoThirdBlock!!.addTextChangedListener(this)

        edtTxtAmount = findViewById(R.id.edt_txt_amount)
        edt_txt_remark = findViewById(R.id.edt_txt_remark)

        txtvPayingfrom = findViewById(R.id.txtvPayingfrom)
        txtvac_no = findViewById(R.id.txtv_accno)
        txtv_recivactype = findViewById(R.id.textView5)
        txtv_acno2 = findViewById(R.id.txtv_acno2)
        txtv_confrmacc = findViewById(R.id.txtv_confrmacc)
        txtv_amtpayable = findViewById(R.id.txtv_amtpayable)
        txtv_remark = findViewById(R.id.txtv_remark)
        btnScanAccounttNo = findViewById<Button>(R.id.btn_scan_acnt_no)



        btn_submit = findViewById<Button>(R.id.btn_submit)
        btn_clear = findViewById(R.id.btn_clear)



        val PayingFromSP = applicationContext.getSharedPreferences(Config.SHARED_PREF93, 0)
        val AccnoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF107, 0)
        val PayingToSP = applicationContext.getSharedPreferences(Config.SHARED_PREF98, 0)
        val ConfirmaccSP = applicationContext.getSharedPreferences(Config.SHARED_PREF99, 0)
        val AMtpaybleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF95, 0)
        val RemarkeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF96, 0)
        val PAYSP = applicationContext.getSharedPreferences(Config.SHARED_PREF97, 0)
        val ScanSP = applicationContext.getSharedPreferences(Config.SHARED_PREF100, 0)



        txtvPayingfrom!!.setText(PayingFromSP.getString("PayingFrom", null))
        txtvac_no!!.setText(AccnoSP.getString("AccountNo", null))
        txtv_recivactype!!.setText(PayingToSP.getString("ReceiverAccountType", null))
        txtv_acno2!!.setText(AccnoSP.getString("AccountNo", null))
        txtv_confrmacc!!.setText(ConfirmaccSP.getString("ConfirmAccountNo", null))
        txtv_amtpayable!!.setText(AMtpaybleSP.getString("AmountPayable", null))
        txtv_remark!!.setText(RemarkeSP.getString("Remark", null))
        btnScanAccounttNo!!.setText(ScanSP.getString("Scan", null))

        btn_submit!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)


        btnScanAccounttNo!!.setOnClickListener(this)

        mAccountNumberEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 14) {
                    mScannedValue = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                //Do nothing
            }
        })




        edtTxtAmount!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                edtTxtAmount!!.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    val longval: Double
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = originalString.toDouble()
                    val formattedString: String = Config.getDecimelFormateForEditText(longval)!!
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
                    edtTxtAmount!!.setText(formattedString)
                    edtTxtAmount!!.setSelection(edtTxtAmount!!.getText().length)
                    val amnt = edtTxtAmount!!.getText().toString().replace(",".toRegex(), "")
                    val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()
                    var amountInWordPop = ""
                    if (netAmountArr.size > 0) {
                        val integerValue = netAmountArr[0].toInt()
                        amountInWordPop =
                                "Rupees " + NumberToWord.convertNumberToWords(integerValue)
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
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                edtTxtAmount!!.addTextChangedListener(this)
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
                        //txtv_remark!!.setText(RemarkeSP.getString("Remark", null))
                        btn_submit!!.setText(PAYSP.getString("PAY", null) + "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        btn_submit!!.setText(PAYSP.getString("PAY", null))
                    }
                } catch (e: NumberFormatException) {
                }
            }
        })
    }

    @SuppressLint("NewApi")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.btn_submit -> {
                val recieverAccountNo: String = confirmAndSetRecieversAccountNo()
                if (isValid() && recieverAccountNo.length == 12) {
                    if (ConnectivityUtils.isConnected(this)) {
                        var SourceAccountNumber = tv_account_no!!.text.toString()
                        val accountNumber: String = SourceAccountNumber
                        val amount = edtTxtAmount!!.text.toString()
                        val remark = edt_txt_remark!!.text.toString()
                        val fromAccountNo = accountNumber.substring(0, 12)
                        val type = arrayOf("")
                        val dialogBuilder = AlertDialog.Builder(this)
                        val inflater = this.layoutInflater
                        val dialogView: View = inflater.inflate(R.layout.confirmation_fund_popup, null)
                        dialogBuilder.setCancelable(false)
                        dialogBuilder.setView(dialogView)
                        val txtvAcntno = dialogView.findViewById<TextView>(R.id.txtvAcntno)
                        val txtvbranch = dialogView.findViewById<TextView>(R.id.txtvbranch)
                        val txtvbalnce = dialogView.findViewById<TextView>(R.id.txtvbalnce)
                        val txtvAcntnoto = dialogView.findViewById<TextView>(R.id.txtvAcntnoto)
                        val txtvbranchto = dialogView.findViewById<TextView>(R.id.txtvbranchto)
                        val txtvbalnceto = dialogView.findViewById<TextView>(R.id.txtvbalnceto)
                        val img_aapicon: ImageView = dialogView.findViewById<ImageView>(R.id.img_aapicon)
                        txtvAcntno.text = "A/C No : $SourceAccountNumber"
                        txtvbranch.text = "Branch :$BranchName"
                        val num1 = Balance!!.toDouble()
                        val fmt = DecimalFormat("#,##,###.00")
                        txtvbalnce.text = "Available Bal: " + "\u20B9 " + Config.getDecimelFormate(num1)
                        val amnt = edtTxtAmount!!.text.toString().replace(",".toRegex(), "")
                        val text_confirmationmsg = dialogView.findViewById<TextView>(R.id.text_confirmationmsg)
                        txtvAcntnoto.text = "A/C No: " + edtTxtConfirmAccountNoFirstBlock!!.text.toString() + edtTxtConfirmAccountNoSecondBlock!!.text.toString() + edtTxtConfirmAccountNoThirdBlock!!.text.toString()
                        txtvbranchto.text = "A/C Type :" + spn_account_type!!.getSelectedItem().toString()
                        val tv_amount = dialogView.findViewById<TextView>(R.id.tv_amount)
                        val tv_amount_words = dialogView.findViewById<TextView>(R.id.tv_amount_words)
                        val butOk = dialogView.findViewById<Button>(R.id.btnOK)
                        val butCan = dialogView.findViewById<Button>(R.id.btnCncl)
                        val stramnt: String? = Config.getDecimelFormate(amnt.toDouble())
                        text_confirmationmsg.text = "Proceed Transaction with above receipt amount" + "..?"
                        //   text_confirmationmsg.setText("Proceed Transaction with above receipt amount to A/C no " + accNumber + " ..?");
                        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
                        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
                        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
                        val imagepath = IMAGE_URL + AppIconImageCodeSP!!.getString("AppIconImageCode", null)

                        PicassoTrustAll.getInstance(this@OwnBankotheraccountFundTransfer)!!.load(imagepath).error(android.R.color.transparent).into(img_aapicon!!)
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
                        butOk.setOnClickListener { v: View? ->
                            alertDialog.dismiss()
                            var type: String? = null
                            val accountType: String = spn_account_type!!.getSelectedItem().toString()
                            if (accountType.equals("Savings bank")) {
                                type = "DDSB"
                            }
                            val Finalamount = amount.replace(",", "")
                            getOwnAccountFundTransfer(accountNumber, type, recieverAccountNo, Finalamount, remark)
                        }
                        butCan.setOnClickListener { alertDialog.dismiss() }
                    } else {
                        alertMessage1("", "Network is currently unavailable. Please try again later.")

                        /*  DialogUtil.showAlert(this,
                        "Network is currently unavailable. Please try again later.");*/
                    }
                }

            }
            R.id.btn_scan_acnt_no -> {


                if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                            arrayOf(Manifest.permission.CAMERA),
                            this@OwnBankotheraccountFundTransfer!!.ZXING_CAMERA_PERMISSION
                    )
                } else {
                    val intent = Intent(this, ScannerActivity::class.java)
                    startActivityForResult(intent, 100)
                }

            }
            R.id.imgHome -> {
                startActivity(
                        Intent(
                                this@OwnBankotheraccountFundTransfer,
                                HomeActivity::class.java
                        )
                )
            }
            R.id.btn_clear -> {
                edtTxtAccountNoFirstBlock!!.setText("")
                edtTxtAccountNoSecondBlock!!.setText("")
                edtTxtAccountNoThirdBlock!!.setText("")
                edtTxtConfirmAccountNoFirstBlock!!.setText("")
                edtTxtConfirmAccountNoSecondBlock!!.setText("")
                edtTxtConfirmAccountNoThirdBlock!!.setText("")
                edtTxtAmount!!.setText("")
                edt_txt_remark!!.setText("")
                tv_maxamount!!.setText("")
                txt_amtinword!!.text = ""

                setAccountType()

                getminTransAmount()

            }
        }
    }

    private fun isValid(): Boolean {

        var amount = edtTxtAmount!!.text.toString()
        if (MaximumAmount !== "") {
            val MaximumAmountD = MaximumAmount.toDouble()
            amount = amount.replace(",", "")
            if (amount.length < 1 && MaximumAmountD > 0) {
                edtTxtAmount!!.error = "Please enter amount between 1 and $MaximumAmount."
                return false
            } else if (amount.length < 1 && MaximumAmountD == 0.0) {
                edtTxtAmount!!.error = "Please enter valid amount."
                return false
            }
            val amt = amount.toDouble()
            if (MaximumAmountD > 0) {
                if (amt < 1 || amt > MaximumAmountD) {
                    edtTxtAmount!!.error = "Please enter amount between 1 and $MaximumAmountD."
                    return false
                }
            }
        }


        edtTxtAmount!!.error = null
        return true
    }

    private fun confirmAndSetRecieversAccountNo(): String {

        val recieverAccountNo = edtTxtAccountNoFirstBlock!!.text.toString() +
                edtTxtAccountNoSecondBlock!!.text.toString() +
                edtTxtAccountNoThirdBlock!!.text.toString()
        val confirmRecieverAccountNo = edtTxtConfirmAccountNoFirstBlock!!.text.toString() +
                edtTxtConfirmAccountNoSecondBlock!!.text.toString() +
                edtTxtConfirmAccountNoThirdBlock!!.text.toString()
        if (recieverAccountNo == confirmRecieverAccountNo && recieverAccountNo.length == 12 && confirmRecieverAccountNo.length == 12) {
            return recieverAccountNo
        } else if (recieverAccountNo != confirmRecieverAccountNo && recieverAccountNo.length == 12 && confirmRecieverAccountNo.length == 12) {
            showAlert()
        } else {
            if (edtTxtAccountNoFirstBlock!!.text.toString().length < 3) edtTxtAccountNoFirstBlock!!.error = "Atleast 3 digit are required"
            if (edtTxtAccountNoSecondBlock!!.text.toString().length < 3) edtTxtAccountNoSecondBlock!!.error = "Atleast 3 digit are required"
            if (edtTxtAccountNoThirdBlock!!.text.toString().length < 6) edtTxtAccountNoThirdBlock!!.error = "Atleast 6 digits are required"
            if (edtTxtConfirmAccountNoFirstBlock!!.text.toString().length < 3) edtTxtConfirmAccountNoFirstBlock!!.error = "Atleast 3 digits are required"
            if (edtTxtConfirmAccountNoSecondBlock!!.text.toString().length < 3) edtTxtConfirmAccountNoSecondBlock!!.error = "Atleast 3 digits are required"
            if (edtTxtConfirmAccountNoThirdBlock!!.text.toString().length < 6) edtTxtConfirmAccountNoThirdBlock!!.error = "Atleast 6 digits are required"
        }
        return ""

    }

    private fun showAlert() {
        if (this == null) return
        alertMessage1("Oops...", "Both account number and confirm account number are not matching")
    }

    private fun getminTransAmount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@PassbookTransactionDetailsActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankotheraccountFundTransfer))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("30"))
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


                        Log.e("TAG", "requestObject1  700   " + requestObject1)
                    } catch (e: Exception) {
                        //progressDialog!!.dismiss()
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
                    val call = apiService.getfundtransferlimit(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //   progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-fundtransfer", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("FundTransferLimit")
                                    MaximumAmount = jsonObj1.getString("MaximumAmount")
                                    //                                tv_maxamount.setText("Transfer upto ₹ " + MaximumAmount + " instantly.");
                                    val num = MaximumAmount.toDouble()
                                    if (num > 0) {
                                        tv_maxamount!!.visibility = View.VISIBLE
                                        tv_maxamount!!.text =
                                                "Transfer upto ₹ " + Config.getDecimelFormate(
                                                        num
                                                ).toString() + " instantly."
                                    } else {
                                        tv_maxamount!!.visibility = View.GONE
                                    }
                                } else {
                                    tv_maxamount!!.visibility = View.GONE
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankotheraccountFundTransfer,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                                /* if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("PassBookAccountDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            arrayList1!!.add(json.getString("AccountNumber"))
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spnAccountNum!!.adapter = ArrayAdapter(
                                            this@PassbookTransactionDetailsActivity,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1
                                    )


                                }*/
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@OwnBankotheraccountFundTransfer,
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
                            //   progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    this@OwnBankotheraccountFundTransfer,
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
                    // progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                            this@OwnBankotheraccountFundTransfer,
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
                        this@OwnBankotheraccountFundTransfer,
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

    private fun setAccountType() {
        val items = ArrayList<String>()
        items.add(getString(R.string.savings_bank))
        items.add("")
        items.add("")

        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item_dark, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_account_type!!.setAdapter(spinnerAdapter)
        spn_account_type!!.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


    }



    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onEditorAction(v: TextView?, actionId: Int, i: KeyEvent?): Boolean {
        val editorAction = "editor_action"
        assert(v != null)
        val id = v!!.id
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_NONE) {
            when (id) {
                R.id.acc_no_block_one -> findValueForEditText(edtTxtAccountNoFirstBlock!!.text.toString(), id, editorAction)
                R.id.acc_no_block_two -> findValueForEditText(edtTxtAccountNoSecondBlock!!.text.toString(), id, editorAction)
                R.id.acc_no_block_three -> findValueForEditText(edtTxtAccountNoThirdBlock!!.text.toString(), id, editorAction)
                R.id.confirm_acc_no_block_one -> findValueForEditText(edtTxtConfirmAccountNoFirstBlock!!.text.toString(), id, editorAction)
                R.id.confirm_acc_no_block_two -> findValueForEditText(edtTxtConfirmAccountNoSecondBlock!!.text.toString(), id, editorAction)
                R.id.confirm_acc_no_block_three -> findValueForEditText(edtTxtConfirmAccountNoThirdBlock!!.text.toString(), id, editorAction)
                else -> {
                }
            }
        }
        return false
    }

    private fun findValueForEditText(value: String, id: Int, from: String) {

        val stringLength: Int = value.length
        var valueS = value

        if ((id == R.id.acc_no_block_one || id == R.id.acc_no_block_two || id == R.id.confirm_acc_no_block_one || id == R.id.confirm_acc_no_block_two)
                && stringLength > 0 && stringLength < 4) {
            when (stringLength) {
                        1 ->
                            valueS = "00$value"
                        2 ->
                            valueS = "0$value"

            }
        } else if ((id == R.id.acc_no_block_three || id == R.id.confirm_acc_no_block_three) && stringLength > 0 && stringLength < 7) {
            when (stringLength) {
                1 ->
                    valueS = "00000$value"
                2 ->
                    valueS = "0000$value"
                3 ->
                    valueS = "000$value"
                4 ->
                    valueS = "00$value"
                5 ->
                    valueS = "0$value"

            }
        }
        if (from == "editor_action")
            assignTextToEdtTextOnKeyBoardImeAction(valueS, id)
        else assignTextToEdtTextOnFocusChange(valueS, id)

    }
    private fun assignTextToEdtTextOnFocusChange(value: String, id: Int) {
        when (id) {
            R.id.acc_no_block_one -> edtTxtAccountNoFirstBlock!!.setText(value)
            R.id.acc_no_block_two -> edtTxtAccountNoSecondBlock!!.setText(value)
            R.id.acc_no_block_three -> edtTxtAccountNoThirdBlock!!.setText(value)
            R.id.confirm_acc_no_block_one -> edtTxtConfirmAccountNoFirstBlock!!.setText(value)
            R.id.confirm_acc_no_block_two -> edtTxtConfirmAccountNoSecondBlock!!.setText(value)
            R.id.confirm_acc_no_block_three -> edtTxtConfirmAccountNoThirdBlock!!.setText(value)
            else -> {
            }
        }
    }
    private fun assignTextToEdtTextOnKeyBoardImeAction(value: String, id: Int) {
        when (id) {
            R.id.acc_no_block_one -> {
                edtTxtAccountNoFirstBlock!!.setText(value)
                edtTxtAccountNoSecondBlock!!.requestFocus()
            }
            R.id.acc_no_block_two -> {
                edtTxtAccountNoSecondBlock!!.setText(value)
                edtTxtAccountNoThirdBlock!!.requestFocus()
            }
            R.id.acc_no_block_three -> edtTxtAccountNoThirdBlock!!.setText(value)
            R.id.confirm_acc_no_block_one -> {
                edtTxtConfirmAccountNoFirstBlock!!.setText(value)
                edtTxtConfirmAccountNoSecondBlock!!.requestFocus()
            }
            R.id.confirm_acc_no_block_two -> {
                edtTxtConfirmAccountNoSecondBlock!!.setText(value)
                edtTxtConfirmAccountNoSecondBlock!!.requestFocus()
            }
            R.id.confirm_acc_no_block_three -> edtTxtConfirmAccountNoThirdBlock!!.setText(value)
            else -> {
            }
        }
    }
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        val focusChange = "focus_change"
        if (!hasFocus) {
            val id: Int = view!!.getId()
            when (id) {
                R.id.acc_no_block_one ->
                    findValueForEditText(edtTxtAccountNoFirstBlock!!.text.toString(), R.id.acc_no_block_one, focusChange)
                R.id.acc_no_block_two -> {
                    findValueForEditText(edtTxtAccountNoFirstBlock!!.text.toString(), R.id.acc_no_block_one, focusChange)
                    findValueForEditText(edtTxtAccountNoSecondBlock!!.text.toString(), R.id.acc_no_block_two, focusChange)
                }
                R.id.acc_no_block_three -> {
                    findValueForEditText(edtTxtAccountNoFirstBlock!!.text.toString(), R.id.acc_no_block_one, focusChange)
                    findValueForEditText(edtTxtAccountNoSecondBlock!!.text.toString(), R.id.acc_no_block_two, focusChange)
                    findValueForEditText(edtTxtAccountNoThirdBlock!!.text.toString(), R.id.acc_no_block_three, focusChange)
                }
                R.id.confirm_acc_no_block_one -> findValueForEditText(edtTxtConfirmAccountNoFirstBlock!!.text.toString(), R.id.confirm_acc_no_block_one, focusChange)
                R.id.confirm_acc_no_block_two -> {
                    findValueForEditText(edtTxtConfirmAccountNoFirstBlock!!.text.toString(), R.id.confirm_acc_no_block_one, focusChange)
                    findValueForEditText(edtTxtConfirmAccountNoSecondBlock!!.text.toString(), R.id.confirm_acc_no_block_two, focusChange)
                }
                R.id.confirm_acc_no_block_three -> {
                    findValueForEditText(edtTxtConfirmAccountNoFirstBlock!!.text.toString(), R.id.confirm_acc_no_block_one, focusChange)
                    findValueForEditText(edtTxtConfirmAccountNoSecondBlock!!.text.toString(), R.id.confirm_acc_no_block_two, focusChange)
                    findValueForEditText(edtTxtConfirmAccountNoThirdBlock!!.text.toString(), R.id.confirm_acc_no_block_three, focusChange)
                }
                else -> {
                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        try {
            val id: Int
            id = currentFocus!!.id
            changeFocusOnTextFill(id)
        } catch (e: java.lang.NullPointerException) {
           // if (IScoreApplication.DEBUG) Log.d("Error", e.toString())
        }
    }

    private fun changeFocusOnTextFill(id: Int) {

        when (id) {
            R.id.acc_no_block_one -> if (edtTxtAccountNoFirstBlock!!.text.toString().length == 3) edtTxtAccountNoSecondBlock!!.requestFocus()
            R.id.acc_no_block_two -> if (edtTxtAccountNoSecondBlock!!.text.toString().length == 3) edtTxtAccountNoThirdBlock!!.requestFocus()
            R.id.acc_no_block_three -> if (edtTxtAccountNoThirdBlock!!.text.toString().length == 6) edtTxtConfirmAccountNoFirstBlock!!.requestFocus()
            R.id.confirm_acc_no_block_one -> if (edtTxtConfirmAccountNoFirstBlock!!.text.toString().length == 3) edtTxtConfirmAccountNoSecondBlock!!.requestFocus()
            R.id.confirm_acc_no_block_two -> if (edtTxtConfirmAccountNoSecondBlock!!.text.toString().length == 3) edtTxtConfirmAccountNoThirdBlock!!.requestFocus()
            else -> {
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String?>,
            grantResults: IntArray
    ) {
        if (requestCode == this@OwnBankotheraccountFundTransfer!!.ZXING_CAMERA_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivityForResult(intent, 100)
            } else {
                Toast.makeText(
                        this,
                        "App need permission for use camera to scan account number",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun getOwnAccountFundTransfer(accountNumber: String, type: String?, recieverAccountNo: String, Finalamount: String, remark: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(
                        this@OwnBankotheraccountFundTransfer,
                        R.style.Progress
                )
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankotheraccountFundTransfer))
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
                        //  var amount = edtTxtAmount!!.text.toString()
                        // amount = amount.replace(",", "")
                        //requestObject1.put("Reqmode", MscoreApplication.encryptStart("27"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        val tokens1 = StringTokenizer(accountNumber, "(")
                        val acno = tokens1.nextToken() //
                        var accno = acno.replace(" ", "");
                        requestObject1.put(
                                "AccountNo",
                                MscoreApplication.encryptStart(accno)
                        )
                        /* requestObject1.put(
                                "AccountNo",
                                //MscoreApplication.encryptStart(tv_account_no!!.text.toString())
                                MscoreApplication.encryptStart(accountNumber)
                        )*/
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(Submod))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(Finalamount))
                        val tokens = StringTokenizer(recieverAccountNo, "(")
                        val recacno = tokens.nextToken()

                        requestObject1.put(
                                "ReceiverAccountNo", MscoreApplication.encryptStart(
                                recacno
                        )
                        )
                        /*var accno =
                                requestObject1.put(
                                        "ReceiverAccountNo", MscoreApplication.encryptStart(recieverAccountNo))*/
                        val rtype: String = spn_account_type!!.getSelectedItem().toString()
                        /* var type = ""

                        if (rtype == "Savings bank") {
                            type = "DDSB"
                        }*/
                        requestObject1.put(
                                "ReceiverModule", MscoreApplication.encryptStart(type)
                        )
                        requestObject1.put("QRCode", MscoreApplication.encryptStart(""))
                        //  var remark = edt_txt_remark!!.text.toString()
                        requestObject1.put("Remark", MscoreApplication.encryptStart(remark))
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  ownfund   " + requestObject1)
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
                    val call = apiService.getfundtransferownBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-ownother", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("FundTransferToOwnBank")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    var result = jsonobj2.getString("ResponseMessage")
                                    var refid = jsonobj2.getString("RefID")
                                    var amt = jsonobj2.getString("Amount")
                                    var reacc = jsonobj2.getString("RecAccNumber")
                                    Log.i("Result", result)
                                    alertPopup(refid, amt, reacc, result)
                                    // alertMessage1("", result)
                                    // Toast.makeText(applicationContext, result, Toast.LENGTH_LONG)
                                    //  .show()


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankotheraccountFundTransfer,
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
                                        this@OwnBankotheraccountFundTransfer,
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
                                    this@OwnBankotheraccountFundTransfer,
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
                            this@OwnBankotheraccountFundTransfer,
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
                        this@OwnBankotheraccountFundTransfer,
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
    private fun alertMessage1(msg1: String, msg2: String) {
        val dialogBuilder = AlertDialog.Builder(this@OwnBankotheraccountFundTransfer)
        val inflater: LayoutInflater = this@OwnBankotheraccountFundTransfer.getLayoutInflater()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 200) {
            var value = data!!.getStringExtra("Value")
            if (TextUtils.isEmpty(value)) {
                return
            }
            if (value!!.trim { it <= ' ' }.length >= 14) {
                value = value!!.substring(0, 14)
            }
            val customerNumber = value!!.substring(0, 12)
            val Submodule = value!!.substring(12, 14)
            CustomerList.clear()
            try {
                val builder = AlertDialog.Builder(this)
                val inflater1 = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout: View = inflater1.inflate(R.layout.cusmodule_popup, null)
                list_view = layout.findViewById<View>(R.id.list_view) as ListView
                tv_popuptitle = layout.findViewById<View>(R.id.tv_popuptitle) as TextView
                tv_popuptitle!!.setText("Select Account")
                builder.setView(layout)
                val alertDialog = builder.create()
                getCustomerAccount(alertDialog, customerNumber, Submodule)
                alertDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCustomerAccount(alertDialog: AlertDialog?, customerNumber: String, submodule: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                /*   progressDialog = ProgressDialog(this@PassbookTransactionDetailsActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()*/
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankotheraccountFundTransfer))
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("31"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )

                        requestObject1.put(
                                "CustomerNumber",
                                MscoreApplication.encryptStart(customerNumber)
                        )
                        requestObject1.put(
                                "SubModule",
                                MscoreApplication.encryptStart(submodule)
                        )
                        requestObject1.put(
                                "ModuleCode",
                                MscoreApplication.encryptStart(submodule)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  getcustomer   " + requestObject1)
                    } catch (e: Exception) {
                        //progressDialog!!.dismiss()
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
                    val call = apiService.getfundtransferlimit(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                //   progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-customeracnt", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("BarcodeAgainstCustomerAccountDets")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult1 = jsonobj2.getJSONArray("BarcodeAgainstCustomerAccountList")

                                    if (jresult1!!.length() != 0) {
                                        for (k in 0 until jresult1!!.length()) {
                                            val jsonObject: JSONObject = jresult1!!.getJSONObject(k)
                                            CustomerList.add(BarcodeAgainstCustomerAccountList(jsonObject.getString("FK_Customer"), jsonObject.getString("CustomerName"), jsonObject.getString("AccountName"), jsonObject.getString("AccountNumber")))
                                        }
                                        if (jresult1!!.length() == 1) {
                                            dataItem = CustomerList[0].accountNumber
                                            mScannedValue = dataItem
                                            displayAccountNumber(dataItem, alertDialog)
                                        } else {

                                            var hashSet = HashSet<BarcodeAgainstCustomerAccountList>(CustomerList)

                                            CustomerList.clear()
                                            CustomerList.addAll(hashSet)
                                            val adapter = CustomListAdapter(this@OwnBankotheraccountFundTransfer, CustomerList)
                                            list_view!!.adapter = adapter
                                            list_view!!.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                                                dataItem = CustomerList[position].accountNumber
                                                mScannedValue = dataItem
                                                displayAccountNumber(dataItem, alertDialog)
                                            }
                                        }
                                    }

                                } else {
                                    //   tv_maxamount!!.visibility = View.GONE
                                    val builder = AlertDialog.Builder(
                                            this@OwnBankotheraccountFundTransfer,
                                            R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                                /* if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("PassBookAccountDetails")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("PassBookAccountDetailsList")

                                    for (i in 0 until jresult!!.length()) {
                                        try {
                                            val json = jresult!!.getJSONObject(i)
                                            arrayList1!!.add(json.getString("AccountNumber"))
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    spnAccountNum!!.adapter = ArrayAdapter(
                                            this@PassbookTransactionDetailsActivity,
                                            android.R.layout.simple_spinner_dropdown_item, arrayList1
                                    )


                                }*/
                            } catch (e: Exception) {
                                //   progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                        this@OwnBankotheraccountFundTransfer,
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
                            //   progressDialog!!.dismiss()

                            val builder = AlertDialog.Builder(
                                    this@OwnBankotheraccountFundTransfer,
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
                    // progressDialog!!.dismiss()
                    val builder = AlertDialog.Builder(
                            this@OwnBankotheraccountFundTransfer,
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
                        this@OwnBankotheraccountFundTransfer,
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

    private fun displayAccountNumber(dataItem: String?, alertDialog: AlertDialog?) {

              if(dataItem!!.trim().length>=14)
              {

                  dataItem== dataItem.substring(0, 14)
              }



        if (dataItem!!.startsWith("01")) {
            spn_account_type!!.setSelection(0)
        } else if (dataItem.startsWith("02")) {
            spn_account_type!!.setSelection(1)
        }
        val firstValueQrScanned: String = dataItem.substring(0, 3)
        val secondValueQrScanned: String = dataItem.substring(3, 6)
        val thirdValueQrScanned: String = dataItem.substring(6, 12)

        edtTxtAccountNoFirstBlock!!.setText(firstValueQrScanned)
        edtTxtAccountNoSecondBlock!!.setText(secondValueQrScanned)
        edtTxtAccountNoThirdBlock!!.setText(thirdValueQrScanned)

        edtTxtConfirmAccountNoFirstBlock!!.setText(firstValueQrScanned)
        edtTxtConfirmAccountNoSecondBlock!!.setText(secondValueQrScanned)
        edtTxtConfirmAccountNoThirdBlock!!.setText(thirdValueQrScanned)

        mAccountNumberEt!!.setText(dataItem)
        mConfirmAccountNumberEt!!.setText(dataItem)

        edtTxtAmount!!.requestFocus()
        edtTxtAmount!!.setSelection(edtTxtAmount!!.getText().length)
        alertDialog!!.dismiss()

    }
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
//        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//        tvtime.text = "Time : $currentTime"

        val currentTime = Calendar.getInstance().time
        Log.e("TAG", "currentTime  " + currentTime)
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

        val amnt: String = edtTxtAmount!!.getText().toString().replace(",".toRegex(), "")
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

        txtvAcntnoto.text = "A/C : " + reacc
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