package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.NumberToWord
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class OwnBankotheraccountFundTransfer : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, OnEditorActionListener, OnFocusChangeListener, TextWatcher {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var arrayList1 = ArrayList<String>()
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    private var txt_amtinword: TextView? = null
    var tv_maxamount: TextView? = null
    public var tv_balance: TextView? = null
    public var spn_account_type: Spinner? = null
    public var BranchName: String? = null
    public var Balance: String? = null
    private var btnScanAccounttNo: Button? = null
    private val ZXING_CAMERA_PERMISSION = 1
    public var Acnt: String? = null
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
    var compareValue = "Select Account"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherbankfundtransfer)

        setRegViews()
        setAccountType()

    }



    private fun setRegViews() {


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


        btn_submit = findViewById<Button>(R.id.btn_submit)
        btn_clear = findViewById(R.id.btn_clear)

        btn_submit!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)

         btnScanAccounttNo = findViewById<Button>(R.id.btn_scan_acnt_no)
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
                        amountInWordPop = "Rupees " + NumberToWord.convertNumberToWords(integerValue)
                        if (netAmountArr.size > 1) {
                            val decimalValue = netAmountArr[1].toInt()
                            if (decimalValue != 0) {
                                amountInWordPop += " and " + NumberToWord.convertNumberToWords(decimalValue).toString() + " paise"
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
                        btn_submit!!.setText("PAY  " + "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        btn_submit!!.setText("PAY")
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
            R.id.btn_scan_acnt_no -> {


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    /*ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            OwnBankotheraccountFundTransfer!!.ZXING_CAMERA_PERMISSION
                    )*/
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

    private fun getminTransAmount() {
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("30"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  171   " + requestObject1)
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
                                /* if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject = jObject.getJSONObject("PassBookAccountTransactionList")
                                    val jresult = jsonObj1.getJSONArray("Data")
                                    val jsonArrayKey = jresult.getJSONObject(0).getJSONArray("Details")
                                    val lLayout = GridLayoutManager(this@PassbookTransactionDetailsActivity, 1)
                                    rv_statementDetails!!.layoutManager = lLayout
                                    rv_statementDetails!!.setHasFixedSize(true)
                                    val adapter = PassbookTransactionDetailsAdapter(this@PassbookTransactionDetailsActivity, jsonArrayKey)
                                    rv_statementDetails!!.adapter = adapter
                                }
                                else {
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
                                }*/
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
                    val builder = AlertDialog.Builder(this@OwnBankotheraccountFundTransfer, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@OwnBankotheraccountFundTransfer, R.style.MyDialogTheme)
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
        items.add(getString(R.string.current_account))
        items.add(getString(R.string.cash_credit))

        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item_dark, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_account_type!!.setAdapter(spinnerAdapter)
        spn_account_type!!.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        TODO("Not yet implemented")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(p0: Editable?) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        /*if (requestCode == OwnBankotheraccountFundTransfer!!.ZXING_CAMERA_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivityForResult(intent, 100)
            } else {
                Toast.makeText(this, "App need permission for use camera to scan account number", Toast.LENGTH_LONG).show()
            }
        }*/
    }

}