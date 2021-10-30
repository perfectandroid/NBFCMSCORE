package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
import java.util.*

class OwnBankownaccountFundTransfer : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    public var arrayList1: ArrayList<Splitupdetail>? = null
    private var tv_account_no: TextView? = null
    private var tv_branch_name: TextView? = null
    private var txt_amtinword: TextView? = null
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
    private var ll_needTochange: LinearLayout? = null
    private var ll_needToPayAdvance: LinearLayout? = null
    private var ll_remittance: LinearLayout? = null

    public var BranchName: String? = null
    public var Balance: String? = null
    public var Submod: String? = null
    public var Acnt: String? = null
    private var jresult: JSONArray? = null
    public var fkaccount:String?=null
    public var submodule:String?=null
    var pendinginsa = arrayOfNulls<String>(0)
    var compareValue = "Select Account"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ownbankfundtransfer)

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
        rv_split_details = findViewById(R.id.rv_split_details)
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
                        btn_submit!!.setText("PAY  " + "\u20B9 " + Config.getDecimelFormate(num))
                    } else {
                        btn_submit!!.setText("PAY")
                    }
                } catch (e: NumberFormatException) {
                }
            }
        })
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
                            .sslSocketFactory(Config.getSSLSocketFactory(this@OwnBankownaccountFundTransfer))
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
                getOwnAccountFundTransfer()

            }
            R.id.btn_clear -> {
                getAccountnumber()

                txt_amtinword!!.text = ""
//                setAccountType();
                edt_txt_amount!!.setText("")
            }
        }
    }

    private fun getOwnAccountFundTransfer() {
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
                        var amount = edt_txt_amount!!.text.toString()
                        amount = amount.replace(",", "")
                        //requestObject1.put("Reqmode", MscoreApplication.encryptStart("27"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "AccountNo",
                                MscoreApplication.encryptStart(tv_account_no!!.text.toString())
                        )
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(Submod))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put(
                                "ReceiverAccountNo", MscoreApplication.encryptStart(
                                spn_account_num!!.selectedItem.toString()
                        )
                        )
                        requestObject1.put(
                                "ReceiverModule", MscoreApplication.encryptStart(
                                submodule
                        )
                        )
                        requestObject1.put("QRCode", MscoreApplication.encryptStart(""))
                        requestObject1.put("Remark", MscoreApplication.encryptStart(""))
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  650   " + requestObject1)
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
                                Log.i("Response-ownfd", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("FundTransferToOwnBank")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())
                                    var result = jsonobj2.getString("ResponseMessage")
                                    Log.i("Result", result)
                                    alertMessage1("", result)
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val splitupdetail: Splitupdetail = arrayList1!!.get(position)
        fkaccount = splitupdetail.getFkaccount()
        submodule = splitupdetail.getSubmodule()
        //  Toast.makeText(getApplicationContext(),AccountAdapter.getItem(position).getBranchName(),Toast.LENGTH_LONG).show();
//                                                tv_as_on_date.setVisibility(View.VISIBLE);
        ll_needTochange!!.visibility = View.GONE
        ll_needToPayAdvance!!.visibility = View.GONE
        ll_remittance!!.visibility = View.GONE
        //Toast.makeText(this, "FKAccount: " + fkaccount ,
        // Toast.LENGTH_SHORT).show();
         balanceSplitUpDetails(fkaccount, submodule)
    }

      private fun balanceSplitUpDetails(fkaccount: String?, submodule: String?) {
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
                              .baseUrl(Config.BASE_URL)
                              .addConverterFactory(ScalarsConverterFactory.create())
                              .addConverterFactory(GsonConverterFactory.create(gson))
                              .client(client)
                              .build()
                      val apiService = retrofit.create(ApiInterface::class.java!!)
                      val requestObject1 = JSONObject()

                      val accountType: String = spn_account_num!!.getSelectedItem().toString()
                      val type: String
                      var accountNo = accountType.replace(accountType.substring(accountType.indexOf(" (") + 1, accountType.indexOf(")") + 1), "")
                      accountNo = accountNo.replace(" ", "")


                      //Log.i("Type", type)
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
                          requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
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


                          Log.e("TAG", "requestObject1  balance   " + requestObject1)
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
                      val call = apiService.getbalancesplitupdetail(body)
                      call.enqueue(object : retrofit2.Callback<String> {
                          override fun onResponse(
                                  call: retrofit2.Call<String>, response:
                                  Response<String>
                          ) {
                              try {
                                  //  progressDialog!!.dismiss()
                                  val jObject = JSONObject(response.body())
                                  Log.i("Response-balancesplit", response.body())
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
                                              val lLayout = GridLayoutManager(this@OwnBankownaccountFundTransfer, 1)
                                              rv_split_details!!.setLayoutManager(lLayout)
                                              rv_split_details!!.setHasFixedSize(true)
                                              val adapter = BalancesplitAdapter(this@OwnBankownaccountFundTransfer, jsonObjectjarray)
                                              rv_split_details!!.setAdapter(adapter)
                                              for (k in 0 until jsonObjectjarray.length()) {
                                                  val DetailsjsonObject = jsonObjectjarray.getJSONObject(k)
                                                  val NetAmount = DetailsjsonObject.getString("Key")
                                                  if (NetAmount == "NetAmount") {
                                                      Log.e("T", "onResponse   1142   ")
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
                                                      ll_needTochange!!.setVisibility(View.VISIBLE)
                                                      edt_txt_amount!!.setKeyListener(null)
                                                      edt_txt_amount!!.setEnabled(false)
                                                      edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                      edt_txt_amount!!.setFocusable(false)
                                                      pendinginsa = arrayOfNulls<String>(limit)

                                                      IsAdvance = "0"
                                                      var i: Int
                                                      i = 0
                                                      while (i < limit) {
                                                          pendinginsa[i] = (i + 1).toString()
                                                          i++
                                                      }
                                                      fkaccount?.let { getpendinginsa(submodule, it, IsAdvance) }
                                                  } else if (limit == 0 && Advancelimit != "0") {
                                                      ll_needToPayAdvance!!.setVisibility(View.VISIBLE)
                                                      edt_txt_amount!!.setKeyListener(null)
                                                      edt_txt_amount!!.setEnabled(false)
                                                      edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                      edt_txt_amount!!.setFocusable(false)
                                                      val Advancelimitlimit = Advancelimit.toInt()
                                                      pendinginsa = arrayOfNulls<String>(Advancelimitlimit)
                                                      IsAdvance = "1"
                                                      var i: Int
                                                      i = 0
                                                      while (i < Advancelimitlimit) {
                                                          pendinginsa[i] = (i + 1).toString()
                                                          i++
                                                      }
                                                      fkaccount?.let { getpendinginsa(submodule, it, IsAdvance) }
                                                  } else {
                                                      edt_txt_amount!!.setKeyListener(null)
                                                      edt_txt_amount!!.setEnabled(false)
                                                      edt_txt_amount!!.setInputType(InputType.TYPE_NULL)
                                                      edt_txt_amount!!.setFocusable(false)
                                                  }
                                              } else {
                                                  ll_needTochange!!.setVisibility(View.GONE)
                                                  ll_needToPayAdvance!!.setVisibility(View.GONE)
                                                  ll_remittance!!.setVisibility(View.GONE)
                                                  edt_txt_amount!!.setEnabled(true)
                                                  edt_txt_amount!!.setInputType(InputType.TYPE_CLASS_NUMBER)
                                                  edt_txt_amount!!.setFocusable(true)
                                                  edt_txt_amount!!.setFocusableInTouchMode(true)
                                              }
                                          } else {
                                              tv_availbal!!.setVisibility(View.GONE)
                                          }
                                      } else {
                                          tv_availbal!!.setVisibility(View.GONE)
                                      }

                                  } else {
                                      tv_availbal!!.setVisibility(View.GONE)
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
                      //  progressDialog!!.dismiss()
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


    private fun getpendinginsa(ToSubModule: String, ToFK_Account: String, IsAdvance: String) {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_spinner_item,
                pendinginsa
        )
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        status_spinner!!.setAdapter(aa)
        status_spinner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                edt_txt_amount!!.setText("" + position)
                val remittanance = position + 1
                Log.e("TAG", "onResponse   355   ")
                remittanceDetails("" + remittanance, ToSubModule, ToFK_Account, IsAdvance)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }
        })
    }

    private fun remittanceDetails(s: String, toSubModule: String, tofkAccount: String, isAdvance: String) {

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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("28"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Account",
                                MscoreApplication.encryptStart(fkaccount)
                        )
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
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


                        Log.e("TAG", "requestObject1 remittance   " + requestObject1)
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
                    val call = apiService.getinstanceremittanceamt(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-remittance", response.body())
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
}