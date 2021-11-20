package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
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


class OtherBankFundTransferActivity : AppCompatActivity() , View.OnClickListener,
    ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "OtherBankActivity"
    private val PICK_BENEFICIARY = 1

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null
    var submode: String? = null
    var tv_beneficiaryname: TextView? = null
    var txtTrans: TextView? = null

    var tv_beneficiarylist: TextView? = null

    var tie_accountnumber: TextInputEditText? = null
    var tie_beneficiary: TextInputEditText? = null
    var tie_beneficiary_aacno: TextInputEditText? = null
    var tie_Conf_beneficiary_aacno: TextInputEditText? = null
    var tie_ifsc_code: TextInputEditText? = null
    var tie_amount: TextInputEditText? = null


    var jArrayAccount: JSONArray? = null

    var  dialogAccount: BottomSheetDialog? = null
    var ll_chk_bene: LinearLayout? = null
    var llhist: LinearLayout? = null

    var chk_beneficiary: CheckBox? = null


    var FK_Account: String? = ""
    var AccountNumber: String? = ""
    var SubModule: String? = ""
    var BranchName: String? = ""

    var but_pay: Button? = null

    // Save
    var AccountNo: String? = ""
    var BeneName: String? = ""
    var BeneIFSC: String? = ""
    var BeneAccountNumber: String? = ""
    var BeneAccountNumber_conf: String? = ""
    var Amount: String? = ""
    var EftType: String? = "0"
    var BeneAdd: String? = ""
    var OTPRef: String? = ""
    var OTPCode: String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_bank_fund_transfer)

        setInitialise()
        setRegister()

        if(intent.getStringExtra("TYPE")!!.equals("IMPS")){
            EftType = "3"
            tv_beneficiaryname!!.setText("IMPS")
        }
        if(intent.getStringExtra("TYPE")!!.equals("NEFT")){
            EftType = "2"
            tv_beneficiaryname!!.setText("NEFT")
        }
        if(intent.getStringExtra("TYPE")!!.equals("RTGS")){
            EftType = "1"
            tv_beneficiaryname!!.setText("RTGS")
        }

        val ID_header = applicationContext.getSharedPreferences(Config.SHARED_PREF122,0)
        tv_header!!.setText(ID_header.getString("FundTransfer",null))

        val ID_Hist = applicationContext.getSharedPreferences(Config.SHARED_PREF72,0)
        txtTrans!!.setText(ID_Hist.getString("History",null))

        val ID_Benflist = applicationContext.getSharedPreferences(Config.SHARED_PREF157,0)
        tv_beneficiarylist!!.setText(ID_Benflist.getString("BeneficiaryList",null))

        val ID_Accno = applicationContext.getSharedPreferences(Config.SHARED_PREF158,0)
        tie_accountnumber!!.setHint(ID_Accno.getString("AccountNumber",null))

        val ID_Benfname = applicationContext.getSharedPreferences(Config.SHARED_PREF159,0)
        tie_beneficiary!!.setHint(ID_Benfname.getString("BeneficiaryName",null))

        val ID_Benfaccno = applicationContext.getSharedPreferences(Config.SHARED_PREF160,0)
        tie_beneficiary_aacno!!.setHint(ID_Benfaccno.getString("BeneficiaryCNo",null))


        val ID_confBenfacc = applicationContext.getSharedPreferences(Config.SHARED_PREF161,0)
        tie_Conf_beneficiary_aacno!!.setHint(ID_confBenfacc.getString("ConfirmBeneficiaryACNo",null))


        val ID_ifsc = applicationContext.getSharedPreferences(Config.SHARED_PREF150,0)
        tie_ifsc_code!!.setHint(ID_ifsc.getString("IFSCCode",null))

        val ID_amtpyble = applicationContext.getSharedPreferences(Config.SHARED_PREF95,0)
        tie_amount!!.setHint(ID_amtpyble.getString("AmountPayable",null))

        val ID_chkbenf = applicationContext.getSharedPreferences(Config.SHARED_PREF162,0)
        chk_beneficiary!!.setText(ID_chkbenf.getString("SaveBeneficiaryForFuture",null))

        val ID_Pay = applicationContext.getSharedPreferences(Config.SHARED_PREF97,0)
        but_pay!!.setText(ID_Pay.getString("PAY",null))




        getOwnAccount()


//        tie_beneficiary!!.isEnabled = false



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
        tv_header = findViewById<TextView>(R.id.tv_header)
        tv_beneficiaryname = findViewById<TextView>(R.id.tv_beneficiaryname)
        txtTrans = findViewById<TextView>(R.id.txtTrans)

        tv_beneficiarylist = findViewById<TextView>(R.id.tv_beneficiarylist)


        tie_accountnumber = findViewById<TextInputEditText>(R.id.tie_accountnumber)
        tie_beneficiary = findViewById<TextInputEditText>(R.id.tie_beneficiary)
        tie_beneficiary_aacno = findViewById<TextInputEditText>(R.id.tie_beneficiary_aacno)
        tie_Conf_beneficiary_aacno = findViewById<TextInputEditText>(R.id.tie_Conf_beneficiary_aacno)
        tie_ifsc_code = findViewById<TextInputEditText>(R.id.tie_ifsc_code)
        tie_amount = findViewById<TextInputEditText>(R.id.tie_amount)

        ll_chk_bene = findViewById<LinearLayout>(R.id.ll_chk_bene)
        chk_beneficiary = findViewById<CheckBox>(R.id.chk_beneficiary)
        but_pay = findViewById<Button>(R.id.but_pay)
        llhist = findViewById(R.id.llhist)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        llhist!!.setOnClickListener(this)
        tie_accountnumber!!.setOnClickListener(this)

        tv_beneficiarylist!!.setOnClickListener(this)
        but_pay!!.setOnClickListener(this)


     //   tie_beneficiary!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()
//                startActivity(Intent(this@BeneficiaryListActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@OtherBankFundTransferActivity, HomeActivity::class.java))
                finish()
            }
            R.id.tie_accountnumber ->{
                AccountNobottomSheet(jArrayAccount!!)
            }
            R.id.llhist ->{
                var intent = Intent(this@OtherBankFundTransferActivity, OtherfundTransferHistory::class.java)
                if(tv_beneficiaryname!!.text.toString().equals("IMPS"))
                {
                       submode ="1"
                }
                else if(tv_beneficiaryname!!.text.toString().equals("NEFT"))
                {
                        submode ="2"
                }
                else if(tv_beneficiaryname!!.text.toString().equals("RTGS"))
                {
                    submode ="3"
                }
                intent.putExtra("submode", submode)
                startActivity(intent)
            }
            R.id.tv_beneficiarylist ->{

//            startActivity(Intent(this@OtherBankFundTransferActivity, BeneficiaryListActivity::class.java))
            val i = Intent(this, BeneficiaryListActivity::class.java)
            startActivityForResult(i, PICK_BENEFICIARY)
            } R.id.but_pay ->{
                payValidation()
            }
        }
    }



    private fun getOwnAccount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OtherBankFundTransferActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@OtherBankFundTransferActivity))
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

                                    for (i in 0 until jArrayAccount!!.length()) {
                                        var jsonObject = jArrayAccount!!.getJSONObject(i)
                                        if(i == 0){
                                            tie_accountnumber!!.setText(""+jsonObject.getString("AccountNumber"))
                                            FK_Account = jsonObject.getString("FK_Account")
                                            AccountNumber = jsonObject.getString("AccountNumber")
                                            SubModule= jsonObject.getString("SubModule")
                                            BranchName= jsonObject.getString("BranchName")
                                        }

                                    }

                                  //  AccountNobottomSheet(jArrayAccount!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@OtherBankFundTransferActivity,
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
                                    this@OtherBankFundTransferActivity,
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
                                this@OtherBankFundTransferActivity,
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
                    val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun AccountNobottomSheet(jArrayAccount: JSONArray) {

        dialogAccount = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.account_bottom_sheet, null)

        val rvAccount = view.findViewById<RecyclerView>(R.id.rvAccount)
//
        val lLayout = GridLayoutManager(this@OtherBankFundTransferActivity, 1)
        rvAccount.setLayoutManager(lLayout)
        rvAccount.setHasFixedSize(true)
        val obj_adapter2 = AccountAdapter(applicationContext!!, jArrayAccount!!)
        rvAccount!!.adapter = obj_adapter2
        obj_adapter2.setClickListener(this@OtherBankFundTransferActivity)

        dialogAccount!!.setCancelable(true)

        dialogAccount!!.setContentView(view)

        dialogAccount!!.show()


    }

    override fun onClick(position: Int, data: String) {

        if (data.equals("account")){
            dialogAccount!!.dismiss()
            var jsonObject1 = jArrayAccount!!.getJSONObject(position)
            FK_Account = jsonObject1.getString("FK_Account")
            AccountNumber = jsonObject1.getString("AccountNumber")
            SubModule= jsonObject1.getString("SubModule")
            BranchName= jsonObject1.getString("BranchName")
            tie_accountnumber!!.setText(""+jsonObject1.getString("AccountNumber"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG,"tempContact  698  "+requestCode+"  "+resultCode)
        if (requestCode == PICK_BENEFICIARY && resultCode == RESULT_OK && applicationContext != null) {
            try {

                ll_chk_bene!!.visibility = View.GONE
                chk_beneficiary!!.isChecked = false
                Log.e(TAG,"BeneName  323   "+ data!!.getStringExtra("BeneName"))
                Log.e(TAG,"BeneIFSC  323   "+ data!!.getStringExtra("BeneIFSC"))
                Log.e(TAG,"BeneAccNo  323   "+ data!!.getStringExtra("BeneAccNo"))

                tie_beneficiary!!.setText(data!!.getStringExtra("BeneName"))
                tie_beneficiary_aacno!!.setText(data!!.getStringExtra("BeneAccNo"))
                tie_Conf_beneficiary_aacno!!.setText(data!!.getStringExtra("BeneAccNo"))
                tie_ifsc_code!!.setText(data!!.getStringExtra("BeneIFSC"))

                tie_beneficiary!!.isEnabled = false
                tie_beneficiary_aacno!!.isEnabled = false
                tie_Conf_beneficiary_aacno!!.isEnabled = false
                tie_ifsc_code!!.isEnabled = false

            } catch (e: java.lang.Exception) {

            }
        }

    }


    private fun payValidation() {

        OTPRef = ""
        OTPCode = ""

        var AccountNos = AccountNumber!!.replace(AccountNumber!!.substring(AccountNumber!!.indexOf(" (") + 1, AccountNumber!!.indexOf(')') + 1), "")
        AccountNos = AccountNos.replace(" ", "")
        AccountNo = AccountNos

        if (chk_beneficiary!!.isChecked){
            BeneAdd = "1"
        }else{
            BeneAdd = "0"
        }

        BeneName = tie_beneficiary!!.text.toString();
        BeneAccountNumber = tie_beneficiary_aacno!!.text.toString();
        BeneAccountNumber_conf = tie_Conf_beneficiary_aacno!!.text.toString();
        BeneIFSC = tie_ifsc_code!!.text.toString();
        Amount = tie_amount!!.text.toString().replace(",", "");


        Log.e(TAG,"CONFIRMS   446     "+BeneAccountNumber)
        Log.e(TAG,"CONFIRMS   446     "+BeneAccountNumber_conf)

        if (AccountNo!!.length != 12){
           // Toast.makeText(applicationContext,"Select valid  account number",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Select valid  account number","0")
        }
        else if(BeneName!!.equals("")){
            //Toast.makeText(applicationContext,"Please enter Beneficiary name",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please enter Beneficiary name","0")
        }
        else if(BeneAccountNumber!!.equals("")){
            //Toast.makeText(applicationContext,"Beneficiary account number is required",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Beneficiary account number is required","0")
        }
        else if(BeneAccountNumber_conf!!.equals("")){
           // Toast.makeText(applicationContext,"Confirm Beneficiary account number is required",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Confirm Beneficiary account number is required","0")
        }
        else if(!BeneAccountNumber!!.equals(BeneAccountNumber_conf!!)){
           // Toast.makeText(applicationContext,"Beneficiary account numbers don't match",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Beneficiary account numbers don't match","0")
        }else if(BeneIFSC!!.equals("") || BeneIFSC!!.length != 11){
           // Toast.makeText(applicationContext,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please enter valid IFSC","0")
        }else if(BeneIFSC!!.equals("") || BeneIFSC!!.length != 11){
            //Toast.makeText(applicationContext,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Please enter valid IFSC","0")
        }
        else if(Amount!!.length == 0 ||Amount!!.toDouble() <= 0){
          //  Toast.makeText(applicationContext,"Enter Amount",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,"Enter Amount","0")
        }else{


            Log.e(TAG,"CONFIRMS   446"
            +"\n"+"AccountNo    "+AccountNo
                    +"\n"+"SubModule    "+SubModule
                    +"\n"+"BeneName    "+BeneName
                    +"\n"+"BeneIFSC    "+BeneIFSC
                    +"\n"+"BeneAccountNumber    "+BeneAccountNumber
                    +"\n"+"Amount    "+Amount
                    +"\n"+"EftType    "+EftType
                    +"\n"+"BeneAdd    "+BeneAdd
                    +"\n"+"OTPRef    "+OTPRef
                    +"\n"+"OTPCode    "+OTPCode)

            FundConfirmPopup(AccountNo!!,SubModule,BeneName!!,BeneIFSC!!,BeneAccountNumber!!,Amount!!,EftType,BeneAdd!!,OTPRef!!,OTPCode!!)
        }

    }

    private fun FundConfirmPopup(accountNo: String, subModule: String?, beneName: String, beneIFSC: String, beneAccountNumber: String,
        amount: String, eftType: String?, beneAdd: String, otpRef: String, otpCode: String) {


        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.confirmation_fund_popup)

        val amnt: String = tie_amount!!.text.toString().replace(",".toRegex(), "")
        val text_confirmationmsg: TextView = dialog.findViewById<TextView>(R.id.text_confirmationmsg)
        val tv_amount: TextView = dialog.findViewById<TextView>(R.id.tv_amount)
        val txtvAcntno: TextView = dialog.findViewById<TextView>(R.id.txtvAcntno)
        val txtvbranch: TextView = dialog.findViewById<TextView>(R.id.txtvbranch)
        val tv_amount_words: TextView = dialog.findViewById<TextView>(R.id.tv_amount_words)
        val img_aapicon: ImageView = dialog.findViewById<ImageView>(R.id.img_aapicon)
//        val txtvbalnce: TextView = dialog.findViewById<TextView>(R.id.txtvbalnce)

        val txtvAcntnoto: TextView = dialog.findViewById<TextView>(R.id.txtvAcntnoto)
//        val txtvbranchto: TextView = dialog.findViewById<TextView>(R.id.txtvbranchto)
//        val txtvbalnceto: TextView = dialog.findViewById<TextView>(R.id.txtvbalnceto)

        val butOk: Button = dialog.findViewById<Button>(R.id.btnOK)
        val butCan: Button = dialog.findViewById<Button>(R.id.btnCncl)

        txtvAcntno.text = ""+ accountNo
        txtvbranch!!.setText(""+BranchName)
        txtvAcntnoto!!.setText(""+BeneAccountNumber)
        tv_amount!!.setText(""+Config.getDecimelFormate(amount!!.toDouble()))

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
        Log.e(TAG,"imagepath  566   "+imagepath)
       // img_aapicon!!.setImageResource(R.drawable.applogo)
        PicassoTrustAll.getInstance(this@OtherBankFundTransferActivity)!!.load(imagepath).error(android.R.color.transparent).into(img_aapicon!!)

//        val fmt = DecimalFormat("#,##,###.00")
//
// Log.e(TAG,"517     "+Config.getDecimelFormate(amount!!.toDouble()))

        val netAmountArr = amount.split("\\.".toRegex()).toTypedArray()
        var amountInWordPop = ""
          if (netAmountArr.size > 0) {
              val integerValue = netAmountArr[0].toInt()
              amountInWordPop = "Rupees " + NumberToWord!!.convertNumberToWords(integerValue)
              if (netAmountArr.size > 1) {
                  val decimalValue = netAmountArr[1].toInt()
                  if (decimalValue != 0) {
                      amountInWordPop += " and " + NumberToWord.convertNumberToWords(decimalValue).toString() + " paise"
                  }
              }
              amountInWordPop += " only"
          }
        tv_amount_words!!.setText("" + amountInWordPop)


        butOk.setOnClickListener(View.OnClickListener { v: View? ->
            dialog.dismiss()
           // submit()
         //   OtpPopups(accountNo, subModule, beneName, beneIFSC, beneAccountNumber, amount, eftType, beneAdd, otpRef, otpCode)
            RetreiveOtp(accountNo, subModule, beneName, beneIFSC, beneAccountNumber, amount, eftType, beneAdd, otpRef, otpCode)
          //  otpPopup()

        })

        butCan.setOnClickListener(View.OnClickListener {
            dialog.dismiss() })


        dialog.show()

    }

    private fun RetreiveOtp(accountNo: String, subModule: String?, beneName: String, beneIFSC: String, beneAccountNumber: String,
                                 amount: String, eftType: String?, beneAdd: String, otpRef: String, otpCode: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        OTPRef = ""
        Log.e(TAG,"RetreiveOtp   575")
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OtherBankFundTransferActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@OtherBankFundTransferActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

//                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(accountNo))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(subModule))
                        requestObject1.put("BeneName", MscoreApplication.encryptStart(beneName))
                        requestObject1.put("BeneIFSC", MscoreApplication.encryptStart(beneIFSC))
                        requestObject1.put("BeneAccountNumber", MscoreApplication.encryptStart(beneAccountNumber))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("EftType", MscoreApplication.encryptStart(eftType))
                        requestObject1.put("BeneAdd", MscoreApplication.encryptStart(beneAdd))
                        requestObject1.put("OTPRef", MscoreApplication.encryptStart(""))
                        requestObject1.put("OTPCode", MscoreApplication.encryptStart(""))

                        Log.e(TAG,"requestObject1  624   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  6241   "+e.toString())
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
                    val call = apiService.getFundTransferToOtherBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  6243   "+response.body())
                                Log.e(TAG,"response  6244   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("FundTransferToOtherBank")

                                    Log.e(TAG,"response  6245   "+jobjt.getString("OtpRefNo"))
                                    OTPRef = jobjt.getString("OtpRefNo")
                                    otpPopup()

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@OtherBankFundTransferActivity,
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
                                    this@OtherBankFundTransferActivity,
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
                                this@OtherBankFundTransferActivity,
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
                    val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    private fun otpPopup(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_other_fundtransfer)


        val edt_txt_otp: EditText = dialog.findViewById<EditText>(R.id.edt_txt_otp)
        val btn_submit: Button = dialog.findViewById<Button>(R.id.btn_submit)
        val btn_resend: Button = dialog.findViewById<Button>(R.id.btn_resend)

        btn_submit.setOnClickListener {
            if(edt_txt_otp!!.text.toString(). length == 6){
                OTPCode = edt_txt_otp!!.text.toString()
                dialog.dismiss()
                FundTransfer(AccountNo!!, SubModule!!, BeneName!!, BeneIFSC!!, BeneAccountNumber!!, Amount!!, EftType!!, BeneAdd!!, OTPRef!!, OTPCode!!)
            }else{
                Toast.makeText(applicationContext,"Enter Valid OTP",Toast.LENGTH_LONG).show()
            }
        }

        dialog.show()


    }


    private fun FundTransfer(accountNo: String, subModule: String?, beneName: String, beneIFSC: String, beneAccountNumber: String,
                            amount: String, eftType: String?, beneAdd: String, otpRef: String, otpCode: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        OTPRef = ""
        Log.e(TAG,"RetreiveOtp   575")
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OtherBankFundTransferActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@OtherBankFundTransferActivity))
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

//                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(accountNo))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(subModule))
                        requestObject1.put("BeneName", MscoreApplication.encryptStart(beneName))
                        requestObject1.put("BeneIFSC", MscoreApplication.encryptStart(beneIFSC))
                        requestObject1.put("BeneAccountNumber", MscoreApplication.encryptStart(beneAccountNumber))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("EftType", MscoreApplication.encryptStart(eftType))
                        requestObject1.put("BeneAdd", MscoreApplication.encryptStart(beneAdd))
                        requestObject1.put("OTPRef", MscoreApplication.encryptStart(""))
                        requestObject1.put("OTPCode", MscoreApplication.encryptStart(""))

                        Log.e(TAG,"requestObject1  624   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  6241   "+e.toString())
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
                    val call = apiService.getFundTransferToOtherBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  6243   "+response.body())
                                Log.e(TAG,"response  6244   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("FundTransferToOtherBank")

                                    Log.e(TAG,"response  6245   "+jobjt.getString("OtpRefNo"))
//                                    OTPRef = jobjt.getString("OtpRefNo")
//                                    otpPopup()

                                    val builder = AlertDialog.Builder(
                                        this@OtherBankFundTransferActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->

                                        startActivity(Intent(this@OtherBankFundTransferActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@OtherBankFundTransferActivity,
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
                                    this@OtherBankFundTransferActivity,
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
                                this@OtherBankFundTransferActivity,
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
                    val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@OtherBankFundTransferActivity, R.style.MyDialogTheme)
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