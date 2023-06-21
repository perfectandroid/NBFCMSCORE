package com.perfect.nbfcmscore.Activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
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
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class KSEBActivity : AppCompatActivity(), View.OnClickListener, ItemClickListener {

    val TAG: String = "KSEBActivity"
    private var progressDialog: ProgressDialog? = null

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var tv_header: TextView? = null

    var but_recharge: TextView? = null
    var but_clear: TextView? = null

    var tie_consumername: EditText? = null
    var tie_mobilenumber: EditText? = null
    var tie_consumerno: EditText? = null
    var tie_sectionname: EditText? = null
    var tie_billno: EditText? = null
    var tie_amount: EditText? = null
    var tie_account: EditText? = null

    var im_sectionname: ImageView? = null

    private val KSEB_SECTION = 10
    var jArrayAccount: JSONArray? = null
    var  dialogAccount: BottomSheetDialog? = null
    var AccountNo: String? = ""
    var SubModule: String? = ""
    var FK_Account: String? = ""
    var BranchName: String? = ""


    var consumername: String? = ""
    var mobilenumber: String? = ""
    var consumernumber: String? = ""
    var sectionname: String? = ""
    var billnumber: String? = ""
    var amount: String? = ""
    var SectionList: String? = ""

    var txtconsumername: TextView? = null
    var txtmobilenumber: TextView? = null
    var txtconsumerno: TextView? = null
    var txtsectionname: TextView? = null
    var txtbillno: TextView? = null
    var txtamount: TextView? = null
    var txtaccount: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksebactivity)

        setInitialise()
        setRegister()

        val ID_pay = applicationContext.getSharedPreferences(Config.SHARED_PREF97,0)
        but_recharge!!.setText(ID_pay.getString("PAY",null))

        val ID_clr = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        but_clear!!.setText(ID_clr.getString("RESET",null))

        val ID_cnsmrnme = applicationContext.getSharedPreferences(Config.SHARED_PREF277,0)
        txtconsumername!!.setText(ID_cnsmrnme.getString("ConsumerName",null))
        tie_consumername!!.setHint(ID_cnsmrnme.getString("ConsumerName",null))

        val ID_mob = applicationContext.getSharedPreferences(Config.SHARED_PREF110,0)
        txtmobilenumber!!.setText(ID_mob.getString("MobileNumber",null))
        tie_mobilenumber!!.setHint(ID_mob.getString("MobileNumber",null))

        val ID_consumrno = applicationContext.getSharedPreferences(Config.SHARED_PREF278,0)
        txtconsumerno!!.setText(ID_consumrno.getString("ConsumerNumber",null))
        tie_consumerno!!.setHint(ID_consumrno.getString("ConsumerNumber",null))

        val ID_sectnname = applicationContext.getSharedPreferences(Config.SHARED_PREF279,0)
        txtsectionname!!.setText(ID_sectnname.getString("SectionName",null))
        tie_sectionname!!.setHint(ID_sectnname.getString("SectionName",null))

        val ID_billno= applicationContext.getSharedPreferences(Config.SHARED_PREF280,0)
        txtbillno!!.setText(ID_billno.getString("BillNumber",null))
        tie_billno!!.setHint(ID_billno.getString("BillNumber",null))

        val ID_amt= applicationContext.getSharedPreferences(Config.SHARED_PREF113,0)
        txtamount!!.setText(ID_amt.getString("Amount",null))
        tie_amount!!.setHint(ID_amt.getString("Amount",null))

        val ID_accno= applicationContext.getSharedPreferences(Config.SHARED_PREF158,0)
        txtaccount!!.setText(ID_accno.getString("AccountNumber",null))
        tie_account!!.setHint(ID_accno.getString("AccountNumber",null))

        val ID_header= applicationContext.getSharedPreferences(Config.SHARED_PREF71,0)
        tv_header!!.setText(ID_header.getString("KSEB",null))


       // tv_header!!.setText(Html.fromHtml(billnumber))


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


        txtconsumername = findViewById<TextView>(R.id.txtconsumername)
        txtmobilenumber = findViewById<TextView>(R.id.txtmobilenumber)
        txtconsumerno = findViewById<TextView>(R.id.txtconsumerno)
        txtsectionname = findViewById<TextView>(R.id.txtsectionname)
        txtbillno = findViewById<TextView>(R.id.txtbillno)
        txtamount = findViewById<TextView>(R.id.txtamount)
        txtaccount = findViewById<TextView>(R.id.txtaccount)

        tv_header = findViewById<TextView>(R.id.tv_header)
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tie_consumername = findViewById<EditText>(R.id.tie_consumername)
        tie_mobilenumber = findViewById<EditText>(R.id.tie_mobilenumber)
        tie_consumerno = findViewById<EditText>(R.id.tie_consumerno)
        tie_sectionname = findViewById<EditText>(R.id.tie_sectionname)
        tie_billno = findViewById<EditText>(R.id.tie_billno)
        tie_amount = findViewById<EditText>(R.id.tie_amount)
        tie_account = findViewById<EditText>(R.id.tie_account)

        im_sectionname = findViewById<ImageView>(R.id.im_sectionname)

        but_recharge = findViewById<TextView>(R.id.but_recharge)
        but_clear = findViewById<TextView>(R.id.but_clear)
    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        tie_account!!.setOnClickListener(this)
        im_sectionname!!.setOnClickListener(this)

        but_recharge!!.setOnClickListener(this)
        but_clear!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.im_back ->{
                onBackPressed()
//                startActivity(Intent(this@StatementActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@KSEBActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_sectionname ->{

                val intent = Intent(this@KSEBActivity, KsebSectionActivity::class.java)
                startActivityForResult(intent, KSEB_SECTION) // Activity is started with requestCode 2
            }
            R.id.tie_account ->{
                Log.e(TAG,"tie_account")
                getOwnAccount()
            }

            R.id.but_recharge ->{
                Config.hideSoftKeyBoards(applicationContext)
                Log.e(TAG,"Recharge")
                validations()
            }
            R.id.but_clear ->{
                Log.e(TAG,"but_clear")
               clears()
            }
        }
    }

    private fun clears() {
        tie_consumername!!.setText("")
        tie_mobilenumber!!.setText("")
        tie_consumerno!!.setText("")
        tie_sectionname!!.setText("")
        tie_billno!!.setText("")
        tie_amount!!.setText("")
        tie_account!!.setText("")

        AccountNo = ""
        SubModule = ""
        FK_Account = ""
        BranchName = ""

        consumername = ""
        mobilenumber = ""
        consumernumber = ""
        sectionname = ""
        SectionList = ""
        billnumber = ""
        amount = ""
    }

    private fun validations() {

        consumername = tie_consumername!!.text.toString()
        mobilenumber = tie_mobilenumber!!.text.toString()
        consumernumber = tie_consumerno!!.text.toString()
        sectionname = tie_sectionname!!.text.toString()
        billnumber = tie_billno!!.text.toString()
        amount = tie_amount!!.text.toString().replace(",", "")
        var mAccountNumber = AccountNo!!.replace(AccountNo!!.substring(AccountNo!!.indexOf(" (") + 1, AccountNo!!.indexOf(')') + 1), "")
        mAccountNumber = mAccountNumber.replace(" ", "")

        if (consumername!!.equals("")){
            val ID_plsentconsnme = applicationContext.getSharedPreferences(Config.SHARED_PREF285,0)
            var plsentconsnme = ID_plsentconsnme.getString("PleaseEnterConsumerName",null)
            CustomBottomSheeet.Show(this,plsentconsnme!!,"0")

          //  CustomBottomSheeet.Show(this,"Please Enter Consumer name ","0")
        }
        else if (mobilenumber!!.length!=10){
            val ID_validmob = applicationContext.getSharedPreferences(Config.SHARED_PREF287,0)
            var validmob = ID_validmob.getString("PleaseEnterValidMobileNumber",null)
            CustomBottomSheeet.Show(this,validmob!!,"0")
          //  CustomBottomSheeet.Show(this,"Please enter valid  mobile number","0")
        }
        else if (consumernumber!!.equals("")){
            val ID_plstentconsno = applicationContext.getSharedPreferences(Config.SHARED_PREF286,0)
            var consno = ID_plstentconsno.getString("PleaseEnterConsumerNumber",null)
            CustomBottomSheeet.Show(this,consno!!,"0")

            //CustomBottomSheeet.Show(this,"Please Enter Consumer number ","0")
        }
        else if (sectionname!!.equals("")){
            val ID_plsentsectname = applicationContext.getSharedPreferences(Config.SHARED_PREF282,0)
            var plsentsctname= ID_plsentsectname.getString("PleaseEnterSectionName",null)
            CustomBottomSheeet.Show(this,plsentsctname!!,"0")

          //  CustomBottomSheeet.Show(this,"Please Enter Section name ","0")
        }
        else if (billnumber!!.equals("")){
            val ID_plsentbill= applicationContext.getSharedPreferences(Config.SHARED_PREF283,0)
            var plsentbill= ID_plsentbill.getString("PleaseEnterBillnumber",null)
            CustomBottomSheeet.Show(this,plsentbill!!,"0")
          //  CustomBottomSheeet.Show(this,"Please Enter Bill number ","0")
        }
        else if (amount!!.equals("")){
            val ID_plsentamt= applicationContext.getSharedPreferences(Config.SHARED_PREF259,0)
            var plsentamt= ID_plsentamt.getString("Pleaseenteramount",null)
            CustomBottomSheeet.Show(this,plsentamt!!,"0")

          //  CustomBottomSheeet.Show(this,"Please Enter Amount","0")
        }
        else if(mAccountNumber!!.length != 12){
            val ID_plselctacc= applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plslctacc= ID_plselctacc.getString("PleaseSelectAccount",null)
            CustomBottomSheeet.Show(this,plslctacc!!,"0")
            // Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Please Select Account","0")
        }
        else if(SubModule!!.equals("")){
            val ID_plselctacc= applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plslctacc= ID_plselctacc.getString("PleaseSelectAccount",null)
            CustomBottomSheeet.Show(this,plslctacc!!,"0")
            //  Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Select Account","0")
        }else{

          //  mAccountNumber = AccountNo!!.replace(" ", "")
            Log.e(TAG,"MobileNumer      785   "+mobilenumber)
            Log.e(TAG,"ConsumerName    785   "+consumername)
            Log.e(TAG,"ConsumerNo       785   "+consumernumber)
            Log.e(TAG,"Amount           785   "+amount)
            Log.e(TAG,"AccountNo        785   "+mAccountNumber)
            Log.e(TAG,"SubModule     785   "+SubModule)
            Log.e(TAG,"SectionList    785   "+SectionList)
            Log.e(TAG,"FK_Account         785   "+FK_Account)
            Log.e(TAG,"BillNo         785   "+billnumber)

            RechargeConfirmationPop(mobilenumber!!,mAccountNumber!!,SubModule!!,FK_Account!!,consumername!!,consumernumber!!,SectionList,sectionname!!)

          //



        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sectionname = ""
        Config.hideSoftKeyBoards(applicationContext)
        Log.e("TAG","tempContact  698  "+requestCode+"  "+resultCode)
        if (requestCode == KSEB_SECTION && resultCode == RESULT_OK && applicationContext != null) {
            try {

                val SecName = data?.getStringExtra("SecName")
                val SecCode = data?.getStringExtra("SecCode")
                sectionname = SecCode
                SectionList = data?.getStringExtra("SecCode")

                Log.e("TAG", "SecName   270   " + SecName+"  "+SecCode)
                tie_sectionname!!.setText(SecName+"("+SecCode+")")
            } catch (e: Exception) {
                Log.e("TAG", "SecName   270   " + e.toString())
            }
        }

    }



    private fun getOwnAccount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@KSEBActivity, R.style.Progress)
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




                        Log.e(TAG,"requestObject1  516   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  5161   "+e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
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
                                Log.e(TAG,"response  5162   "+response.body())
                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    Log.e(TAG,"jArrayAccount  5164   "+jArrayAccount)
                                    AccountNobottomSheet(jArrayAccount!!)

                                } else {
                                    AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
                            AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
                    AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {

                AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert"," No Internet Connection. ",3);
            }
        }
    }

    private fun AccountNobottomSheet(jArrayAccount: JSONArray) {

        dialogAccount = BottomSheetDialog(this,R.style.BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.account_bottom_sheet, null)

        val rvAccount = view.findViewById<RecyclerView>(R.id.rvAccount)
//
        val lLayout = GridLayoutManager(this@KSEBActivity, 1)
        rvAccount.setLayoutManager(lLayout)
        rvAccount.setHasFixedSize(true)
        val obj_adapter2 = AccountAdapter(applicationContext!!, jArrayAccount!!)
        rvAccount!!.adapter = obj_adapter2
        obj_adapter2.setClickListener(this@KSEBActivity)

        dialogAccount!!.setCancelable(true)

        dialogAccount!!.setContentView(view)

        dialogAccount!!.show()


    }

    override fun onClick(position: Int, data: String) {

        if (data.equals("account")){
            dialogAccount!!.dismiss()
            var jsonObject1 = jArrayAccount!!.getJSONObject(position)
            FK_Account = jsonObject1.getString("FK_Account")
            AccountNo = jsonObject1.getString("AccountNumber")
            SubModule= jsonObject1.getString("SubModule")
            BranchName= jsonObject1.getString("BranchName")
            tie_account!!.setText(""+jsonObject1.getString("AccountNumber"))
        }


    }


    private fun RechargeConfirmationPop(mobilenumber: String, mAccountNumber: String, subModule: String, fkAccount: String,
                                        consumername: String, consumernumber: String, sectionList: String?, sectionname: String) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.kseb_confirmation)

        val tvAcntno = dialog.findViewById(R.id.tvAcntno) as TextView
        val tvbranch = dialog.findViewById(R.id.tvbranch) as TextView
        val tv_name = dialog.findViewById(R.id.tv_name) as TextView
        val tv_mob = dialog.findViewById(R.id.tv_mob) as TextView
        val tv_consumerno = dialog.findViewById(R.id.tv_consumerno) as TextView
        val tv_section = dialog.findViewById(R.id.tv_section) as TextView
        val tv_billnumber = dialog.findViewById(R.id.tv_billnumber) as TextView

        val tv_amount = dialog.findViewById(R.id.tv_amount) as TextView
        val tv_amount_words = dialog.findViewById(R.id.tv_amount_words) as TextView
        val img_aapicon = dialog.findViewById(R.id.img_aapicon) as ImageView

        val bt_cancel = dialog.findViewById(R.id.bt_cancel) as Button
        val bt_ok = dialog.findViewById(R.id.bt_ok) as Button




        tvAcntno!!.setText(""+AccountNo)
        tvbranch!!.setText(""+BranchName)
        tv_name!!.setText(""+consumername)
        tv_mob!!.setText(""+mobilenumber)
        tv_consumerno!!.setText("Consumer No : "+consumernumber)
        tv_section!!.setText("Section : "+sectionname)
        tv_billnumber!!.setText("Bill No : "+billnumber)

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
        PicassoTrustAll.getInstance(this@KSEBActivity)!!.load(imagepath).error(android.R.color.transparent).into(img_aapicon)

      //  text_confirmationmsg!!.setText("Proceed Recharge With Above Amount ..?")

//            double num =Double.parseDouble(""+mAmount);
//            String stramnt = CommonUtilities.getDecimelFormate(num);
        val stramnt: String = amount!!.replace(",", "")
        val netAmountArr: Array<String> = stramnt.split("\\.").toTypedArray()
        var amountInWordPop = ""
        //  tv_amount.setText(""+stramnt)
        tv_amount!!.setText("₹ "+Config.getDecimelFormate(amount!!.toDouble()))


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

        bt_ok!!.setOnClickListener {

            ksebRecharge(mobilenumber!!,mAccountNumber!!,SubModule!!,FK_Account!!,consumername!!,consumernumber!!,SectionList,sectionname!!)
            dialog.dismiss()
        }

        bt_cancel!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }



    private fun ksebRecharge(mobilenumber: String, mAccountNumber: String, subModule: String, fkAccount: String,
                             consumername: String, consumernumber: String, sectionList: String?, sectionname: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@KSEBActivity, R.style.Progress)
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
                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("MobileNumer", MscoreApplication.encryptStart(mobilenumber))

                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(mAccountNumber))
                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(FK_Account))

                        requestObject1.put("ConsumerName", MscoreApplication.encryptStart(consumername))
                        requestObject1.put("ConsumerNo", MscoreApplication.encryptStart(consumernumber))
                        requestObject1.put("SectionList", MscoreApplication.encryptStart(sectionList))
                        requestObject1.put("BillNo", MscoreApplication.encryptStart(billnumber))


                        Log.e(TAG,"requestObject1  901   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  9011   "+e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getKSEBBilling(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  9013   "+response.body())
                                Log.e(TAG,"response  9014   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

//                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
//                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
//                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
//                                    OperatorbottomSheet(jArrayOperator!!)

                                    // Toast.makeText(applicationContext,"Not Complete",Toast.LENGTH_LONG).show()
//                                    val builder = AlertDialog.Builder(
//                                        this@KSEBActivity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jObject.getString("EXMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                        startActivity(Intent(this@KSEBActivity, HomeActivity::class.java))
//                                        finish()
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()

                                    alertMessage("Success",jObject.getString("EXMessage"),2)

                                } else {
                                    AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
                            AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
                    AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {

                AlertMessage().alertMessage(this@KSEBActivity,this@KSEBActivity,"Alert"," No Internet Connection. ",3);
            }
        }

    }

    fun alertMessage(header:String, message:String, type:Int) {
        val bottomSheetDialog = BottomSheetDialog(this@KSEBActivity)
        bottomSheetDialog.setContentView(R.layout.alert_message)
        val txt_ok = bottomSheetDialog.findViewById<TextView>(R.id.txt_ok)
        val img = bottomSheetDialog.findViewById<ImageView>(R.id.img)
        val txt_cancel = bottomSheetDialog.findViewById<TextView>(R.id.txt_cancel)
        val txtheader = bottomSheetDialog.findViewById<TextView>(R.id.header)
        val txtmessage = bottomSheetDialog.findViewById<TextView>(R.id.message)
        txtmessage!!.setText(message)
        txtheader!!.setText(header)
        txt_cancel!!.setText("OK")
        txt_cancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this@KSEBActivity, HomeActivity::class.java))
            finish()
        }
        if(type==1)
        {
            txt_ok!!.visibility=View.GONE
            txt_cancel!!.visibility=View.VISIBLE
            img!!.setImageResource(R.drawable.new_alert)
        }
        else if(type==2)
        {
            txt_ok!!.visibility=View.GONE
            txt_cancel!!.visibility=View.VISIBLE
            img!!.setImageResource(R.drawable.new_success)
        }
        else if(type==3)
        {
            txt_ok!!.visibility=View.GONE
            txt_cancel!!.visibility=View.VISIBLE
            img!!.setImageResource(R.drawable.new_nonetwork)
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
    }



}