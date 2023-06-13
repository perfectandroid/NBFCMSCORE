package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Helper.Config.getSSLSocketFactory
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
import java.security.KeyStore
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class OtherBankFundTransferActivity : AppCompatActivity() , View.OnClickListener,
    ItemClickListener {

    private var progressDialog: ProgressDialog? = null
 //   val TAG: String? = "OtherBankFundTransferActivity"
    private val PICK_BENEFICIARY = 1

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    //var tv_header: TextView? = null
    var submode: String? = null
    var tv_beneficiaryname: TextView? = null
    var txtTrans: TextView? = null

    var tv_beneficiarylist: TextView? = null

    var im_beneficiarylist: ImageView? = null

    var tie_accountnumber: EditText? = null
    var tie_beneficiary: EditText? = null
    var tie_beneficiary_aacno: EditText? = null
    var tie_Conf_beneficiary_aacno: EditText? = null
    var tie_ifsc_code: EditText? = null
    var tie_amount: EditText? = null


    var jArrayAccount: JSONArray? = null

    var  dialogAccount: BottomSheetDialog? = null
    var ll_chk_bene: LinearLayout? = null
    var llhist: LinearLayout? = null
    var imhist: ImageView? = null

    var chk_beneficiary: CheckBox? = null


    var FK_Account: String? = ""
    var AccountNumber: String? = ""
    var SubModule: String? = ""
    var BranchName: String? = ""

    var but_pay: TextView? = null
    var btn_clear: TextView? = null

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

    var txtv_accntno: TextView? = null
    var txtvbenf: TextView? = null
    var txtv_benfacno: TextView? = null
    var txtv_confrmbenfacno: TextView? = null
    var txtv_ifs: TextView? = null
    var txtv_amtpay: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_bank_fund_transfer)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        setInitialise()
        setRegister()

        if(intent.getStringExtra("TYPE")!!.equals("IMPS")){
            EftType = "3"
            tv_beneficiaryname!!.setText("  IMPS")
        }
        if(intent.getStringExtra("TYPE")!!.equals("NEFT")){
            EftType = "2"
            tv_beneficiaryname!!.setText("  NEFT")
        }
        if(intent.getStringExtra("TYPE")!!.equals("RTGS")){
            EftType = "1"
            tv_beneficiaryname!!.setText("  RTGS")
        }

       /* val ID_header = applicationContext.getSharedPreferences(Config.SHARED_PREF122,0)
        tv_header!!.setText(ID_header.getString("FundTransfer",null))*/

        val ID_Hist = applicationContext.getSharedPreferences(Config.SHARED_PREF72,0)
        txtTrans!!.setText(ID_Hist.getString("History",null))

        val ID_Benflist = applicationContext.getSharedPreferences(Config.SHARED_PREF157,0)
        tv_beneficiarylist!!.setText(ID_Benflist.getString("BeneficiaryList",null))

        val ID_Accno = applicationContext.getSharedPreferences(Config.SHARED_PREF158,0)
       // tie_accountnumber!!.setHint(ID_Accno.getString("AccountNumber",null))
        txtv_accntno!!.setText(ID_Accno.getString("AccountNumber",null))

        val ID_Benfname = applicationContext.getSharedPreferences(Config.SHARED_PREF159,0)
       // tie_beneficiary!!.setHint(ID_Benfname.getString("BeneficiaryName",null))
        txtvbenf!!.setText(ID_Benfname.getString("BeneficiaryName",null))

        val ID_Benfaccno = applicationContext.getSharedPreferences(Config.SHARED_PREF160,0)
        //tie_beneficiary_aacno!!.setHint(ID_Benfaccno.getString("BeneficiaryCNo",null))
        txtv_benfacno!!.setText(ID_Benfaccno.getString("BeneficiaryCNo",null))

        val ID_confBenfacc = applicationContext.getSharedPreferences(Config.SHARED_PREF161,0)
       // tie_Conf_beneficiary_aacno!!.setHint(ID_confBenfacc.getString("ConfirmBeneficiaryACNo",null))
        txtv_confrmbenfacno!!.setText(ID_confBenfacc.getString("ConfirmBeneficiaryACNo",null))

        val ID_ifsc = applicationContext.getSharedPreferences(Config.SHARED_PREF150,0)
       // tie_ifsc_code!!.setHint(ID_ifsc.getString("IFSCCode",null))
        txtv_ifs!!.setText(ID_ifsc.getString("IFSCCode",null))

        val ID_amtpyble = applicationContext.getSharedPreferences(Config.SHARED_PREF95,0)
      //  tie_amount!!.setHint(ID_amtpyble.getString("AmountPayable",null))
        txtv_amtpay!!.setText(ID_amtpyble.getString("AmountPayable",null))

        val ID_chkbenf = applicationContext.getSharedPreferences(Config.SHARED_PREF162,0)
        chk_beneficiary!!.setText(ID_chkbenf.getString("SaveBeneficiaryForFuture",null))

        val ID_Pay = applicationContext.getSharedPreferences(Config.SHARED_PREF97,0)
        but_pay!!.setText(ID_Pay.getString("PAY",null))

        val ID_clr = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        btn_clear!!.setText(ID_clr.getString("RESET",null))




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
       // tv_header = findViewById<TextView>(R.id.tv_header)
        tv_beneficiaryname = findViewById<TextView>(R.id.tv_beneficiaryname)
        txtTrans = findViewById<TextView>(R.id.txtTrans)

        txtv_accntno = findViewById<TextView>(R.id.txtv_accntno)
        txtvbenf = findViewById<TextView>(R.id.txtvbenf)
        txtv_benfacno = findViewById<TextView>(R.id.txtv_benfacno)
        txtv_confrmbenfacno = findViewById<TextView>(R.id.txtv_confrmbenfacno)
        txtv_ifs = findViewById<TextView>(R.id.txtv_ifs)
        txtv_amtpay = findViewById<TextView>(R.id.txtv_amtpay)


        im_beneficiarylist = findViewById<ImageView>(R.id.im_beneficiarylist)
        tv_beneficiarylist = findViewById<TextView>(R.id.tv_beneficiarylist)


        tie_accountnumber = findViewById<EditText>(R.id.tie_accountnumber)
        tie_beneficiary = findViewById<EditText>(R.id.tie_beneficiary)
        tie_beneficiary_aacno = findViewById<EditText>(R.id.tie_beneficiary_aacno)
        tie_Conf_beneficiary_aacno = findViewById<EditText>(R.id.tie_Conf_beneficiary_aacno)
        tie_ifsc_code = findViewById<EditText>(R.id.tie_ifsc_code)
        tie_amount = findViewById<EditText>(R.id.tie_amount)

        ll_chk_bene = findViewById<LinearLayout>(R.id.ll_chk_bene)
        chk_beneficiary = findViewById<CheckBox>(R.id.chk_beneficiary)
        but_pay = findViewById<TextView>(R.id.but_pay)
        btn_clear = findViewById<TextView>(R.id.btn_clear)
        llhist = findViewById(R.id.llhist)
        imhist = findViewById(R.id.imhist)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        imhist!!.setOnClickListener(this)
        llhist!!.setOnClickListener(this)
        tie_accountnumber!!.setOnClickListener(this)

        im_beneficiarylist!!.setOnClickListener(this)
        tv_beneficiarylist!!.setOnClickListener(this)
        but_pay!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)

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
            R.id.imhist ->{
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
            R.id.im_beneficiarylist ->{

//            startActivity(Intent(this@OtherBankFundTransferActivity, BeneficiaryListActivity::class.java))
            val i = Intent(this, BeneficiaryListActivity::class.java)
            startActivityForResult(i, PICK_BENEFICIARY)
            }
            R.id.tv_beneficiarylist ->{

//            startActivity(Intent(this@OtherBankFundTransferActivity, BeneficiaryListActivity::class.java))
            val i = Intent(this, BeneficiaryListActivity::class.java)
            startActivityForResult(i, PICK_BENEFICIARY)
            }
            R.id.but_pay ->{
                payValidation()
            }
            R.id.btn_clear ->{

                getOwnAccount()
                tie_beneficiary!!.setText("")
                tie_beneficiary_aacno!!.setText("")
                tie_Conf_beneficiary_aacno!!.setText("")
                tie_ifsc_code!!.setText("")
                tie_amount!!.setText("")
                chk_beneficiary!!.isChecked=false
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
                    val apiService = retrofit.create(ApiInterface::class.java)
                    val requestObject1 = JSONObject()
                    try {
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e("TAG","requestObject1  516   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e("TAG","Some  5161   "+e.toString())
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
                    val call = apiService.getOwnAccounDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e("TAG","response  5162   "+response.body())
//                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
//                                    Log.e(TAG,"jArrayAccount  5164   "+jArrayAccount)

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
//                                Log.e(TAG,"Some  2162   "+e.toString())
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
//                            Log.e(TAG,"Some  2163   "+t.message)
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
//                    Log.e(TAG,"Some  2165   "+e.toString())
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
        dialogAccount!!.window?.setBackgroundDrawableResource(R.color.transparent)

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
//        Log.e(TAG,"tempContact  698  "+requestCode+"  "+resultCode)
        if (requestCode == PICK_BENEFICIARY && resultCode == RESULT_OK && applicationContext != null) {
            try {

                ll_chk_bene!!.visibility = View.GONE
                chk_beneficiary!!.isChecked = false
//                Log.e(TAG,"BeneName  323   "+ data!!.getStringExtra("BeneName"))
//                Log.e(TAG,"BeneIFSC  323   "+ data!!.getStringExtra("BeneIFSC"))
//                Log.e(TAG,"BeneAccNo  323   "+ data!!.getStringExtra("BeneAccNo"))

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


//        Log.e(TAG,"CONFIRMS   446     "+BeneAccountNumber)
//        Log.e(TAG,"CONFIRMS   446     "+BeneAccountNumber_conf)

        if (AccountNo!!.length != 12){

            val ID_validacc = applicationContext.getSharedPreferences(Config.SHARED_PREF232,0)
            var validacc = ID_validacc.getString("SelectavalidAccountNumber",null)
            CustomBottomSheeet.Show(this,validacc!!,"0")
           // Toast.makeText(applicationContext,"Select valid  account number",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Select valid  account number","0")
        }
        else if(BeneName!!.equals("")){
            val ID_benfname = applicationContext.getSharedPreferences(Config.SHARED_PREF229,0)
            var benfname = ID_benfname.getString("PleaseEnterBeneficiaryName",null)
            CustomBottomSheeet.Show(this,benfname!!,"0")
            //Toast.makeText(applicationContext,"Please enter Beneficiary name",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please enter Beneficiary name","0")
        }
        else if(BeneAccountNumber!!.equals("") || BeneAccountNumber!!.length == 0){
            val ID_validbenfacc = applicationContext.getSharedPreferences(Config.SHARED_PREF230,0)
            var valdbenfacc = ID_validbenfacc.getString("PleaseEnterValidBeneficiaryAccountNumber",null)
            CustomBottomSheeet.Show(this,valdbenfacc!!,"0")
            //Toast.makeText(applicationContext,"Beneficiary account number is required",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Please enter valid Beneficiary account number ","0")
        }
        else if(BeneAccountNumber_conf!!.equals("")){
            val ID_confrmvalidbenfacc = applicationContext.getSharedPreferences(Config.SHARED_PREF231,0)
            var confrmvaldbenfacc = ID_confrmvalidbenfacc.getString("PleaseentervalidConfirmBeneficiaryaccountnumber",null)
            CustomBottomSheeet.Show(this,confrmvaldbenfacc!!,"0")
           // Toast.makeText(applicationContext,"Confirm Beneficiary account number is required",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Please enter valid Confirm Beneficiary account number","0")
        }
        else if(!BeneAccountNumber!!.equals(BeneAccountNumber_conf!!)){
            val ID_benfaccmtch = applicationContext.getSharedPreferences(Config.SHARED_PREF276,0)
            var benfaccmtch = ID_benfaccmtch.getString("BeneficiaryAccountNumberdidntmatch",null)
            CustomBottomSheeet.Show(this,benfaccmtch!!,"0")
           // Toast.makeText(applicationContext,"Beneficiary account numbers don't match",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Beneficiary account numbers don't match","0")
        }else if(BeneIFSC!!.equals("") || BeneIFSC!!.length == 0){
            val ID_validifs = applicationContext.getSharedPreferences(Config.SHARED_PREF275,0)
            var validifs = ID_validifs.getString("PleaseEnterValidIFSC",null)
            CustomBottomSheeet.Show(this,validifs!!,"0")
           // Toast.makeText(applicationContext,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please enter valid IFSC","0")
        }else if(BeneIFSC!!.equals("") || BeneIFSC!!.length == 0){
            val ID_validifs = applicationContext.getSharedPreferences(Config.SHARED_PREF275,0)
            var validifs = ID_validifs.getString("PleaseEnterValidIFSC",null)
            CustomBottomSheeet.Show(this,validifs!!,"0")
            //Toast.makeText(applicationContext,"Please enter valid IFSC",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Please enter valid IFSC","0")
        }
        else if(Amount!!.length == 0 ||Amount!!.toDouble() <= 0){
            val ID_entramt = applicationContext.getSharedPreferences(Config.SHARED_PREF223,0)
            var entram = ID_entramt.getString("EnterAmount",null)
            CustomBottomSheeet.Show(this,entram!!,"0")
          //  Toast.makeText(applicationContext,"Enter Amount",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Enter Amount","0")
        }else{


            Log.e("TAG","CONFIRMS   446"
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

        val butOk: TextView = dialog.findViewById<TextView>(R.id.btnOK)
        val butCan: TextView = dialog.findViewById<TextView>(R.id.btnCncl)

        txtvAcntno.text = ""+ accountNo
        txtvbranch!!.setText(""+BranchName)
        txtvAcntnoto!!.setText(""+BeneAccountNumber)
        tv_amount!!.setText("₹ "+Config.getDecimelFormate(amount!!.toDouble()))

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
//        Log.e(TAG,"imagepath  566   "+imagepath)
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
//        Log.e(TAG,"RetreiveOtp   666")
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OtherBankFundTransferActivity, R.style.Progress)
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
                    val apiService = retrofit.create(ApiInterface::class.java)
                    val requestObject1 = JSONObject()
                    try {
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        Log.e("TAG","amount  6661  "+amount)

//                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
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

                        Log.e("TAG","requestObject1  624   "+requestObject1)
//                        Log.e(TAG,"requestObject1  62411   "+requestObject1)

                    } catch (e: Exception) {
//                        Log.e(TAG,"Some  6662   "+e.toString())
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
                    val call = apiService.getFundTransferToOtherBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e("TAG","response  6243   "+response.body())
//                                Log.e(TAG,"response  6664   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("FundTransferToOtherBank")

//                                    Log.e(TAG,"response  6665   "+jobjt.getString("OtpRefNo"))
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
//                                Log.e(TAG,"Some  6666   "+e.toString())
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
//                            Log.e(TAG,"Some  6667   "+t.message)
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
//                    Log.e(TAG,"Some  6668   "+e.toString())
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
        val idImgV1: ImageView = dialog.findViewById<ImageView>(R.id.idImgV1)
        Glide.with(this).load(R.drawable.otpgif).into(idImgV1)
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


        Log.e("TAG","FundTransfer  961   "
        +"\n"+"accountNo     "+accountNo
                +"\n"+"subModule     "+subModule
                +"\n"+"beneName     "+beneName
                +"\n"+"beneIFSC     "+beneIFSC
                +"\n"+"beneAccountNumber     "+beneAccountNumber
                +"\n"+"amount     "+amount
                +"\n"+"eftType     "+eftType
                +"\n"+"beneAdd     "+beneAdd
                +"\n"+"otpRef     "+otpRef
                +"\n"+"otpCode     "+otpCode)

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)

//        Log.e(TAG,"RetreiveOtp   856  "+otpRef+ "   "+otpCode)


        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@OtherBankFundTransferActivity, R.style.Progress)
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

//                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("26"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(accountNo))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(subModule))
                        requestObject1.put("BeneName", MscoreApplication.encryptStart(beneName))
                        requestObject1.put("BeneIFSC", MscoreApplication.encryptStart(beneIFSC))
                        requestObject1.put("BeneAccountNumber", MscoreApplication.encryptStart(beneAccountNumber))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("EftType", MscoreApplication.encryptStart(eftType))
                        requestObject1.put("BeneAdd", MscoreApplication.encryptStart(beneAdd))
                        requestObject1.put("OTPRef", MscoreApplication.encryptStart(otpRef))
                        requestObject1.put("OTPCode", MscoreApplication.encryptStart(otpCode))

//                        requestObject1.put("OTPRef", MscoreApplication.encryptStart(""))
//                        requestObject1.put("OTPCode", MscoreApplication.encryptStart(""))

                        Log.e("TAG","requestObject1  9611   "+requestObject1)
//                        Log.e(TAG,"requestObject1  856   "+requestObject1)

                    } catch (e: Exception) {
//                        Log.e(TAG,"Some  8561   "+e.toString())
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
                    val call = apiService.getFundTransferToOtherBank(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e("TAG","response  9612   "+response.body())
//                                Log.e(TAG,"response  8564   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("FundTransferToOtherBank")

//                                    Log.e(TAG,"response  8565   "+jobjt.getString("OtpRefNo"))

                                    var result = jobjt.getString("ResponseMessage")
                                    var refid = jobjt.getString("OtpRefNo")
                                    var amt = jobjt.getString("Amount")
                                    var reacc = beneAccountNumber


                                    alertPopup(refid, amt, reacc, result)

//                                    OTPRef = jobjt.getString("OtpRefNo")
//                                    otpPopup()

//                                    val builder = AlertDialog.Builder(
//                                        this@OtherBankFundTransferActivity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jObject.getString("EXMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//
//                                        startActivity(Intent(this@OtherBankFundTransferActivity, HomeActivity::class.java))
//                                        finish()
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()



                                } else {
//                                    Log.e(TAG,"response  8566   "+jObject.getString("EXMessage"))
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
//                                Log.e(TAG,"Some  8567   "+e.toString())
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
//                            Log.e(TAG,"Some  8568   "+t.message)
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
//                    Log.e(TAG,"Some  8569   "+e.toString())
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


    private fun alertPopup(refid: String, amt: String, reacc: String, result: String) {


        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.activity_success_popup, null)
        dialogBuilder.setView(dialogView)

        val rltv_share = dialogView.findViewById<RelativeLayout>(R.id.rltv_share)
        val lay_share = dialogView.findViewById<RelativeLayout>(R.id.lay_share)
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
   //     tv_account_no = dialogView.findViewById<TextView>(R.id.tv_account_no)
        txtvbranchto!!.visibility = View.GONE

        tvrefe.text = "Ref.No : "+refid

        //current time

        //current time
//        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//        tvtime.text = "Time : $currentTime"

        val currentTime = Calendar.getInstance().time
        Log.e("TAG","currentTime  "+currentTime)
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

        val amnt: String = tie_amount!!.getText().toString().replace(",".toRegex(), "")
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



        txtvAcntno.text = "A/C :"+tie_accountnumber!!.text.toString()
        txtvbranch.text = "Branch :"+BranchName
     //   val num1 = Balance!!.toDouble() - stramnt!!.replace(",", "").toDouble()
        // double num1 = Double.parseDouble(Balance) - Double.parseDouble(stramnt);
        // double num1 = Double.parseDouble(Balance) - Double.parseDouble(stramnt);
        val fmt = DecimalFormat("#,##,###.00")

      //  txtvbalnce.text = "Available Bal: " + "\u20B9 " + Config.getDecimelFormate(num1)

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

            txtMessage.setText(result)
            txtTitle.text = title
            lay_share.setOnClickListener {
                Log.e("img_share", "img_share   1170   ")
//                val bitmap = Bitmap.createBitmap(rltv_share.width,
//                    rltv_share.height, Bitmap.Config.ARGB_8888)
//                val canvas = Canvas(bitmap)
//                rltv_share.draw(canvas)
//                try {
//                    val bmpUri: Uri = getLocalBitmapUri(bitmap)!!
//                    val shareIntent = Intent()
//                    shareIntent.action = Intent.ACTION_SEND
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
//                    shareIntent.type = "image/*"
//                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                    startActivity(Intent.createChooser(shareIntent, "Share"))
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                    Log.e("Exception", "Exception   117   $e")
//                }

                try{

                    val bitmap = Bitmap.createBitmap(rltv_share.width,
                        rltv_share.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    rltv_share.draw(canvas)

                    val file: File = saveBitmap(bitmap, System.currentTimeMillis().toString() + ".png")
                    Log.e("chase  2044   ", "filepath: " + file.absolutePath)
                    val bmpUri = Uri.fromFile(file)
                    Log.i("Uri", bmpUri.toString())


                    // Uri bmpUri = getLocalBitmapUri(bitmap);
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    shareIntent.type = "image/*"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    //    shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    //    shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share"))


                }catch (e : Exception){

                }
            }

        } catch (e: java.lang.Exception) {
            //Do nothing
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun saveBitmap(bm: Bitmap, fileName: String): File {

        val docsFolder =
            File(Environment.getExternalStorageDirectory().toString() + "/Download" + "/")
        val isPresent = true

        Log.e("photoURI", "StatementDownloadViewActivity   5682   ")
        if (!docsFolder.exists()) {
            // isPresent = docsFolder.mkdir();
            docsFolder.mkdir()
            Log.e("photoURI", "StatementDownloadViewActivity   5683   ")
        }
        val file = File(docsFolder, fileName)
        Log.i("Filess", file.toString())
        if (file.exists()) {
            file.delete()
        }
        try {
            val fOut = FileOutputStream(file)
            bm.setHasAlpha(true)
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
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