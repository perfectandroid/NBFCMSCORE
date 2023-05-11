package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountAdapter
import com.perfect.nbfcmscore.Adapter.CircleAdapter
import com.perfect.nbfcmscore.Adapter.OperatorAdapter
import com.perfect.nbfcmscore.Adapter.RecentRechargeAdapter
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
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class RechargeActivity : AppCompatActivity() , View.OnClickListener, ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "RechargeActivity"
    private val PICK_CONTACT = 1
    private val REACHARGE_OFFER = 10
    val PERMISSIONS_REQUEST_READ_CONTACTS = 2

    var im_back: ImageView? = null
    var im_home: ImageView? = null
    var im_contact: ImageView? = null
    var im_offers: ImageView? = null

    var tv_header: TextView? = null

    var tie_mobilenumber: TextInputEditText? = null
    var tie_subscriber: TextInputEditText? = null
    var tie_operator: EditText? = null
    var tie_circle: TextInputEditText? = null
    var tie_account: TextInputEditText? = null
    var tie_amount: TextInputEditText? = null
    var tie_circleAccNo: TextInputEditText? = null

    var card_circleAccNo: LinearLayout? = null

    var jArrayOperator: JSONArray? = null
    var jArrayCircle: JSONArray? = null
    var jArrayAccount: JSONArray? = null


    var rltv_mobile: LinearLayout? = null
    var rltv_subscriber: LinearLayout? = null
    var ll_recentrecharge: LinearLayout? = null
    var ll_contact: LinearLayout? = null

    var txtmobilenumber: TextView? = null
    var txtsubscriber: TextView? = null
    var txtoperator: TextView? = null
    var txtcircle: TextView? = null
    var txtamount: TextView? = null
    var txtcircleAccNo: TextView? = null
    var txtaccount: TextView? = null

    var  dialogOperator: BottomSheetDialog? = null
    var  dialogCircle: BottomSheetDialog? = null
    var  dialogAccount: BottomSheetDialog? = null

    var mobileNumber: String? = ""
    var subscriberId: String? = ""
    var Amount: String? = ""
    var ID_Providers: String? = ""
    var ID_RechargeCircle: String? = ""
    var ProvidersMode: String? = ""
    var CircleMode: String? = ""
    var AccountNo: String? = ""
    var SubModule: String? = ""
    var FK_Account: String? = ""
    var ProvidersCode: String? = ""
    var BranchName: String? = ""
    var Circleaccount: String? = ""


    var rltv_recharge: RelativeLayout? = null
    var but_recharge: Button? = null
    var but_clear: Button? = null
    var rvrecentRecharge: FullLenghRecyclertview? = null
    var jArrayRecent: JSONArray? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)

        setInitialise()
        setRegister()

        card_circleAccNo!!.visibility = View.GONE
        tie_circleAccNo!!.visibility = View.GONE


        if(intent.getStringExtra("from")!!.equals("prepaid")){
            ProvidersMode = "1"
          //  tv_header!!.text = "Prepaid Mobile"
            val PrepaidSP = applicationContext.getSharedPreferences(Config.SHARED_PREF66, 0)
            tv_header!!.setText(PrepaidSP.getString("PrepaidMobile", null))
            rltv_subscriber!!.visibility = View.GONE
        }
        if(intent.getStringExtra("from")!!.equals("postpaid")){
            val PostpaidSP = applicationContext.getSharedPreferences(Config.SHARED_PREF67, 0)
            tv_header!!.setText(PostpaidSP.getString("PostpaidMobile", null))
          //  tv_header!!.text = "Postpaid Mobile"
            ProvidersMode = "4"
            rltv_subscriber!!.visibility = View.GONE
        }
        if(intent.getStringExtra("from")!!.equals("landline")){
         //   tv_header!!.text = "Landline"
            val LandlineSP = applicationContext.getSharedPreferences(Config.SHARED_PREF68, 0)
            tv_header!!.setText(LandlineSP.getString("Landline", null))
            ProvidersMode = "3"
            rltv_subscriber!!.visibility = View.GONE
            ll_contact!!.visibility = View.GONE
        }
        if(intent.getStringExtra("from")!!.equals("dth")){
            val dthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF69, 0)
            tv_header!!.setText(dthSP.getString("DTH", null))
          //  tv_header!!.text = "DTH"
            ProvidersMode = "2"
            rltv_mobile!!.visibility = View.GONE
        }
        if(intent.getStringExtra("from")!!.equals("datacard")){
           // val dthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF69, 0)
          //  tv_header!!.setText(dthSP.getString("DTH", null))
            tv_header!!.setText("Data Card")
            //  tv_header!!.text = "DTH"
            ProvidersMode = "5"
            rltv_mobile!!.visibility = View.GONE
        }

        getRecentRecharges()


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
        im_contact = findViewById<ImageView>(R.id.im_contact)
        im_offers = findViewById<ImageView>(R.id.im_offers)

        tv_header = findViewById<TextView>(R.id.tv_header)

        txtmobilenumber = findViewById<TextView>(R.id.txtmobilenumber)
        txtsubscriber = findViewById<TextView>(R.id.txtsubscriber)
        txtoperator = findViewById<TextView>(R.id.txtoperator)
        txtcircle = findViewById<TextView>(R.id.txtcircle)
        txtamount = findViewById<TextView>(R.id.txtamount)
        txtcircleAccNo = findViewById<TextView>(R.id.txtcircleAccNo)
        txtaccount = findViewById<TextView>(R.id.txtaccount)


        tie_mobilenumber = findViewById<TextInputEditText>(R.id.tie_mobilenumber)
        tie_subscriber = findViewById<TextInputEditText>(R.id.tie_subscriber)
        tie_operator = findViewById<EditText>(R.id.tie_operator)
        tie_circle = findViewById<TextInputEditText>(R.id.tie_circle)
        tie_account = findViewById<TextInputEditText>(R.id.tie_account)
        tie_circleAccNo = findViewById<TextInputEditText>(R.id.tie_circleAccNo)

        card_circleAccNo = findViewById<LinearLayout>(R.id.card_circleAccNo)




        tie_amount = findViewById<TextInputEditText>(R.id.tie_amount)

        val MobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF110, 0)
        val SubscriberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF116, 0)
        val OperatorSP = applicationContext.getSharedPreferences(Config.SHARED_PREF111, 0)
        val CircleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF112, 0)
        val AmountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF113, 0)
        val RechrgSP = applicationContext.getSharedPreferences(Config.SHARED_PREF114, 0)
        val SelctaccSP = applicationContext.getSharedPreferences(Config.SHARED_PREF135, 0)
        val AccnoSP = applicationContext.getSharedPreferences(Config.SHARED_PREF107, 0)


       /* tie_mobilenumber!!.setHint(MobileSP.getString("MobileNumber", null))
        tie_subscriber!!.setHint(SubscriberSP.getString("SubscriberID", null))
        tie_operator!!.setHint(OperatorSP.getString("Operator", null))
        tie_circle!!.setHint(CircleSP.getString("Circle", null))
        tie_amount!!.setHint(AmountSP.getString("Amount", null))
        tie_account!!.setHint(SelctaccSP.getString("SelectAccount", null))
        tie_circleAccNo!!.setHint("Acoount no ")*/

        txtmobilenumber!!.setText(MobileSP.getString("MobileNumber", null))
        txtsubscriber!!.setText(SubscriberSP.getString("SubscriberID", null))
        txtoperator!!.setText(OperatorSP.getString("Operator", null))
        txtcircle!!.setText(CircleSP.getString("Circle", null))
        txtamount!!.setText(AmountSP.getString("Amount", null))
        txtcircleAccNo!!.setText(SelctaccSP.getString("SelectAccount", null))
        txtaccount!!.setText(AccnoSP.getString("AccountNo", null))



        but_recharge = findViewById<Button>(R.id.but_recharge)
        but_clear = findViewById<Button>(R.id.but_clear)
        but_recharge!!.setText(RechrgSP.getString("RECHARGE", null))

        val ID_clr = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        but_clear!!.setText(ID_clr.getString("RESET",null))


        rvrecentRecharge = findViewById<FullLenghRecyclertview>(R.id.rvrecentRecharge)

        rltv_mobile = findViewById<LinearLayout>(R.id.rltv_mobile)
        rltv_subscriber = findViewById<LinearLayout>(R.id.rltv_subscriber)
        ll_recentrecharge = findViewById<LinearLayout>(R.id.ll_recentrecharge)
        ll_contact = findViewById<LinearLayout>(R.id.ll_contact)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        im_contact!!.setOnClickListener(this)
        im_offers!!.setOnClickListener(this)
        tie_operator!!.setOnClickListener(this)
        tie_circle!!.setOnClickListener(this)
        tie_account!!.setOnClickListener(this)
        but_recharge!!.setOnClickListener(this)
        but_clear!!.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
               // onBackPressed()
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }
            R.id.tie_operator ->{
              //  tie_operator!!.setText("Airtel")
                Log.e(TAG,"tie_operator")
                getOperator()
            }

            R.id.tie_circle ->{
                Log.e(TAG,"tie_circle")
                getCircle()
            }
            R.id.tie_account ->{
                Log.e(TAG,"tie_account")
                getOwnAccount()
            }
            R.id.im_contact ->{
                Log.e(TAG,"tie_account")
                contactSelect()
            }

            R.id.but_recharge ->{
                Log.e(TAG,"ll_recharge")
                if (ProvidersMode.equals("1") || ProvidersMode.equals("3") || ProvidersMode.equals("4")){
                    rechargeValidation()
                }
                else if(ProvidersMode.equals("2") || ProvidersMode.equals("5")){
                    dthValidation()
                }

            }
            R.id.but_clear ->{

                tie_mobilenumber!!.setText("")
                tie_subscriber!!.setText("")
                tie_operator!!.setText("")
                tie_circle!!.setText("")
                tie_amount!!.setText("")
                tie_account!!.setText("")
                tie_circleAccNo!!.setText("")

                ID_Providers = ""
                ProvidersCode = ""
                ID_RechargeCircle = ""
                CircleMode =  ""
                FK_Account = ""
                AccountNo = ""
                SubModule= ""
                BranchName= ""
                Circleaccount = ""


            }
            R.id.im_offers ->{
                Log.e(TAG,"im_offers")
                val operatorIdPref: SharedPreferences = applicationContext.getSharedPreferences(
                    Config.SHARED_PREF20,
                    0
                )
                val operatorIdEdit = operatorIdPref.edit()
                operatorIdEdit.putString("ID_Providers", "")
                operatorIdEdit.commit()
                if (!ID_Providers.equals("")) {

                    val operatorIdPref: SharedPreferences = applicationContext.getSharedPreferences(
                        Config.SHARED_PREF20,
                        0
                    )
                    val operatorIdEdit = operatorIdPref.edit()
                    operatorIdEdit.putString("ID_Providers", ID_Providers)
                    operatorIdEdit.commit()

                    val intent = Intent(this@RechargeActivity, RechargeOfferActivity::class.java)
                    intent.putExtra("ID_Providers", ID_Providers)
                    startActivityForResult(
                        intent,
                        REACHARGE_OFFER
                    ) // Activity is started with requestCode 2

                } else {

                    val slctopSP = applicationContext.getSharedPreferences(Config.SHARED_PREF115, 0)
                   var slctop = slctopSP.getString("SelectOperator", null)
                    CustomBottomSheeet.Show(this,slctop!!,"0")
                   // Toast.makeText(applicationContext, "Select Operator", Toast.LENGTH_LONG).show()
                  //  CustomBottomSheeet.Show(this,"Select Operator","0")
                }

            }
        }
    }




    private fun getOperator() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("17"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("ProvidersMode", MscoreApplication.encryptStart(ProvidersMode))

                        Log.e(TAG,"requestObject1  434   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  2161   "+e.toString())
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
                    val call = apiService.getProvidersList(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  4342   "+response.body())
                                Log.e(TAG,"response  4343   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
                                    OperatorbottomSheet(jArrayOperator!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
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
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun getCircle() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("25"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  315   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  3151   "+e.toString())
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
                    val call = apiService.getRechargeCircleDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  3152   "+response.body())
                                Log.e(TAG,"response  3153   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("RechargeCircleDetails")
                                    jArrayCircle = jobjt.getJSONArray("RechargeCircleDetailsList")
                                    Log.e(TAG,"jArrayCircle  3154   "+jArrayCircle)
                                    CirclebottomSheet(jArrayCircle!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
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
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun getOwnAccount() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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
                                Log.e(TAG,"response  5162   "+response.body())
                                Log.e(TAG,"response  5163   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    val jobjt = jObject.getJSONObject("OwnAccountdetails")
                                    jArrayAccount = jobjt.getJSONArray("OwnAccountdetailsList")
                                    Log.e(TAG,"jArrayAccount  5164   "+jArrayAccount)
                                    AccountNobottomSheet(jArrayAccount!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
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
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun CirclebottomSheet(jArrayCircle: JSONArray) {

        dialogCircle = BottomSheetDialog(this,R.style.BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.circle_bottom_sheet, null)

        val rvCircle = view.findViewById<RecyclerView>(R.id.rvCircle)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 1)
        rvCircle.setLayoutManager(lLayout)
        rvCircle.setHasFixedSize(true)
        val obj_adapter1 = CircleAdapter(applicationContext!!, jArrayCircle!!)
        rvCircle!!.adapter = obj_adapter1
        obj_adapter1.setClickListener(this@RechargeActivity)

        dialogCircle!!.setCancelable(true)

        dialogCircle!!.setContentView(view)

        dialogCircle!!.show()

    }

    private fun OperatorbottomSheet(jArrayOperator: JSONArray) {
        Log.e(TAG,"jArrayOperator  272     "+jArrayOperator)

        dialogOperator = BottomSheetDialog(this,R.style.BottomSheetDialog)
       // val view = layoutInflater.inflate(R.layout.operator_bottom_sheet, null)
        dialogOperator!!.setContentView(R.layout.operator_bottom_sheet)
//
//     //   val tv_Close = view.findViewById<TextView>(R.id.tv_Close)
        val rvOperator = dialogOperator!!.findViewById<RecyclerView>(R.id.rvOperator)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 3)
        rvOperator!!.setLayoutManager(lLayout)
        rvOperator!!.setHasFixedSize(true)
        val obj_adapter = OperatorAdapter(applicationContext!!, jArrayOperator!!)
        rvOperator!!.adapter = obj_adapter
        obj_adapter.setClickListener(this@RechargeActivity)

        dialogOperator!!.setCancelable(true)

       // dialogOperator!!.setContentView(view)

        dialogOperator!!.show()
    }




    private fun AccountNobottomSheet(jArrayAccount: JSONArray) {

        dialogAccount = BottomSheetDialog(this,R.style.BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.account_bottom_sheet, null)

        val rvAccount = view.findViewById<RecyclerView>(R.id.rvAccount)
//
        val lLayout = GridLayoutManager(this@RechargeActivity, 1)
        rvAccount.setLayoutManager(lLayout)
        rvAccount.setHasFixedSize(true)
        val obj_adapter2 = AccountAdapter(applicationContext!!, jArrayAccount!!)
        rvAccount!!.adapter = obj_adapter2
        obj_adapter2.setClickListener(this@RechargeActivity)

        dialogAccount!!.setCancelable(true)

        dialogAccount!!.setContentView(view)

        dialogAccount!!.show()


    }

    override fun onClick(position: Int, data: String) {
        Log.e(TAG,"position  315  "+position+"  "+data+"   "+ProvidersMode)
        if (data.equals("operator")){
            dialogOperator!!.dismiss()
            var jsonObject1 = jArrayOperator!!.getJSONObject(position)
            ID_Providers = jsonObject1.getString("ID_Providers")
            ProvidersCode = jsonObject1.getString("ProvidersCode")
            tie_operator!!.setText(""+jsonObject1.getString("ProvidersName"))

            card_circleAccNo!!.visibility= View.GONE
            tie_circleAccNo!!.visibility = View.GONE
            tie_circleAccNo!!.setText("")
            Circleaccount = ""

            if ((ProvidersMode.equals("3") || ProvidersMode.equals("4")) && isCircleAccountNumberMandatory()){

                card_circleAccNo!!.visibility= View.VISIBLE
                tie_circleAccNo!!.visibility = View.VISIBLE
            }


        }

        if (data.equals("circle")){
            dialogCircle!!.dismiss()
            var jsonObject1 = jArrayCircle!!.getJSONObject(position)
            ID_RechargeCircle = jsonObject1.getString("ID_RechargeCircle")
            tie_circle!!.setText(""+jsonObject1.getString("CircleName"))
            CircleMode = jsonObject1.getString("CircleMode")

            Circleaccount = ""
            tie_circleAccNo!!.setText("")
            if (ProvidersMode.equals("4")) {
                if (isCircleAccountNumberMandatory()) {
                    card_circleAccNo!!.visibility= View.VISIBLE
                    tie_circleAccNo!!.visibility = View.VISIBLE
                } else {
                    card_circleAccNo!!.visibility= View.GONE
                    tie_circleAccNo!!.visibility = View.GONE
                }
            }
        }

        if (data.equals("account")){
            dialogAccount!!.dismiss()
            var jsonObject1 = jArrayAccount!!.getJSONObject(position)
            FK_Account = jsonObject1.getString("FK_Account")
            AccountNo = jsonObject1.getString("AccountNumber")
            SubModule= jsonObject1.getString("SubModule")
            BranchName= jsonObject1.getString("BranchName")
            tie_account!!.setText(""+jsonObject1.getString("AccountNumber"))
        }

        if (data.equals("recent")){
//            dialogAccount!!.dismiss()
            var jsonObject2 = jArrayRecent!!.getJSONObject(position)

            tie_mobilenumber!!.setText(""+jsonObject2.getString("MobileNo"))
            tie_operator!!.setText(""+jsonObject2.getString("ProvidersName"))
            tie_circle!!.setText(""+jsonObject2.getString("CircleName"))
            tie_amount!!.setText(""+jsonObject2.getString("RechargeRs"))

//            ProvidersCode = "1"
//            CircleMode = "1"

            ID_Providers = jsonObject2.getString("FK_Providers")
            ProvidersCode = jsonObject2.getString("ProvidersCode")

            ID_RechargeCircle = jsonObject2.getString("FK_RechargeCircle")
            CircleMode = jsonObject2.getString("CircleMode")



        }


    }



    private fun contactSelect() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read Contacts permission")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
              //  getContacts()
                val intent = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI
                )
                intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(intent, PICK_CONTACT)
            }
        } else {
         //   getContacts()
            val intent = Intent(
                Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI
            )
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, PICK_CONTACT)
        }
//        val intent = Intent(
//            Intent.ACTION_PICK,
//            ContactsContract.Contacts.CONTENT_URI
//        )
//        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
//        startActivityForResult(intent, PICK_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG,"tempContact  698  "+requestCode+"  "+resultCode)
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val uriContact = data!!.data!!
                val cursor: Cursor = applicationContext.getContentResolver().query(
                    uriContact, null, null, null, null
                )!!
                cursor.moveToFirst()
                val tempContact = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                Log.e(TAG,"tempContact  6981  "+tempContact)
                tie_mobilenumber!!.setText(extractPhoneNumber(tempContact))
                closeCursor(cursor)
            } catch (e: java.lang.Exception) {

            }
        }
        if (requestCode == REACHARGE_OFFER && resultCode == RESULT_OK && applicationContext != null) {
            try {
                val value = data?.getStringExtra("data")
                Log.e(TAG, "AMOUNT   932   " + value)
                tie_amount!!.setText(value)
            } catch (e: Exception) {
                Log.e(TAG, "AMOUNT   932   " + e.toString())
            }
        }

    }
    private fun extractPhoneNumber(resultPhoneNumber: String): String? {
        var result: String
        try {
            result = resultPhoneNumber.replace("\\D+".toRegex(), "")
            if (result.length > 10) {
                result = result.substring(result.length - 10, result.length)
            }
        } catch (e: java.lang.Exception) {
            result = ""
        }
        return result
    }
    private fun closeCursor(cursor: Cursor) {
        try {
            cursor.close()
        } catch (e: java.lang.Exception) {
//            if (IScoreApplication.DEBUG) Log.e("Null pointer ex", e.toString())
        }
    }

    private fun rechargeValidation() {

        mobileNumber = tie_mobilenumber!!.text.toString();
        Amount = tie_amount!!.text.toString().replace(",", "")
        var mAccountNumber = AccountNo!!.replace(AccountNo!!.substring(AccountNo!!.indexOf(" (") + 1, AccountNo!!.indexOf(')') + 1), "")
        mAccountNumber = mAccountNumber.replace(" ", "")
        Circleaccount = tie_circleAccNo!!.text.toString()

        if (mobileNumber!!.length!= 10||mobileNumber.equals("")){
           // Toast.makeText(applicationContext,"Please enter valid  mobile number",Toast.LENGTH_LONG).show()
            val ID_validmob = applicationContext.getSharedPreferences(Config.SHARED_PREF281,0)
            var validmob = ID_validmob.getString("PleaseEnterMobileNumber", null)

            CustomBottomSheeet.Show(this,validmob!!,"0")

          //  CustomBottomSheeet.Show(this,"Please enter valid  mobile number","0")
//            showToast("Please enter valid  mobile number")
        }else if(ProvidersCode!!.equals("")){
            val ID_slctop = applicationContext.getSharedPreferences(Config.SHARED_PREF301,0)
            var slctop = ID_slctop.getString("PleaseSelectOperator", null)
           // Toast.makeText(applicationContext,"Please Select Operator",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,slctop!!,"0")
        }
        else if(CircleMode!!.equals("")){
            val ID_slctcrcle = applicationContext.getSharedPreferences(Config.SHARED_PREF302,0)
            var slctcircle = ID_slctcrcle.getString("PleaseSelectCircle", null)
          //  Toast.makeText(applicationContext,"Please Select Circle",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,slctcircle!!,"0")
        }
        else if(Amount!!.equals("")){
            val ID_plstslctamt = applicationContext.getSharedPreferences(Config.SHARED_PREF259,0)
            var entramt = ID_plstslctamt.getString("Pleaseenteramount", null)
            CustomBottomSheeet.Show(this,entramt!!,"0")
           // Toast.makeText(applicationContext,"Please Enter Amount",Toast.LENGTH_LONG).show()
           // CustomBottomSheeet.Show(this,"Please Enter Amount","0")
        }
        else if(mAccountNumber!!.length != 12){
            val ID_plstslctacc = applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plsslctacc = ID_plstslctacc.getString("PleaseSelectAccount", null)
            CustomBottomSheeet.Show(this,plsslctacc!!,"0")
           // Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Select Account","0")
        }
        else if(SubModule!!.equals("")){
            val ID_plstslctacc = applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plsslctacc = ID_plstslctacc.getString("PleaseSelectAccount", null)
            CustomBottomSheeet.Show(this,plsslctacc!!,"0")
          //  Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
        //    CustomBottomSheeet.Show(this,"Please Select Account","0")
        }else{

            Log.e(TAG,"MobileNumer      785   "+mobileNumber)
            Log.e(TAG,"ProvidersCode    785   "+ProvidersCode)
            Log.e(TAG,"CircleMode       785   "+CircleMode)
            Log.e(TAG,"Amount           785   "+Amount)
            Log.e(TAG,"AccountNo        785   "+mAccountNumber)
            Log.e(TAG,"FK_Providers     785   "+ID_Providers)
            Log.e(TAG,"FK_RechargeCircle    785   "+ID_RechargeCircle)
            Log.e(TAG,"FK_Account         785   "+FK_Account)

            RechargeConfirmationPop(mobileNumber!!,ProvidersCode!!,CircleMode!!,Amount!!,mAccountNumber,ID_Providers,ID_RechargeCircle,FK_Account,AccountNo!!,Circleaccount!!)

        }

    }

    private fun dthValidation() {

//        ID_Providers = "1"
//        ProvidersCode ="2"
//        tie_operator!!.setText("Airtel DTH")


        subscriberId = tie_subscriber!!.text.toString();
        Amount = tie_amount!!.text.toString().replace(",", "")
        var mAccountNumber = AccountNo!!.replace(AccountNo!!.substring(AccountNo!!.indexOf(" (") + 1, AccountNo!!.indexOf(')') + 1), "")
        mAccountNumber = mAccountNumber.replace(" ", "")

        if (subscriberId!!.length <= 0){
            // Toast.makeText(applicationContext,"Please enter valid  mobile number",Toast.LENGTH_LONG).show()
            val ID_validsubsid = applicationContext.getSharedPreferences(Config.SHARED_PREF332,0)
            var validsubsid = ID_validsubsid.getString("Pleaseentervalidsubscriberid", null)
            CustomBottomSheeet.Show(this,validsubsid!!,"0")

           // CustomBottomSheeet.Show(this,"Please enter valid  subscriber id","0")
//            showToast("Please enter valid  mobile number")
        }else if(ProvidersCode!!.equals("")){
            val ID_validslctop = applicationContext.getSharedPreferences(Config.SHARED_PREF301,0)
            var validslctop = ID_validslctop.getString("PleaseSelectOperator", null)
            CustomBottomSheeet.Show(this,validslctop!!,"0")
            // Toast.makeText(applicationContext,"Please Select Operator",Toast.LENGTH_LONG).show()
            //CustomBottomSheeet.Show(this,"Please Select Operator","0")
        }
        else if(CircleMode!!.equals("")){
            val ID_slctcrcle = applicationContext.getSharedPreferences(Config.SHARED_PREF302,0)
            var slctcircle = ID_slctcrcle.getString("PleaseSelectCircle", null)
            //  Toast.makeText(applicationContext,"Please Select Circle",Toast.LENGTH_LONG).show()
            CustomBottomSheeet.Show(this,slctcircle!!,"0")
            //  Toast.makeText(applicationContext,"Please Select Circle",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Select Circle","0")
        }
        else if(Amount!!.equals("")){
            val ID_plstslctamt = applicationContext.getSharedPreferences(Config.SHARED_PREF259,0)
            var entramt = ID_plstslctamt.getString("Pleaseenteramount", null)
            CustomBottomSheeet.Show(this,entramt!!,"0")
            // Toast.makeText(applicationContext,"Please Enter Amount",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Enter Amount","0")
        }
        else if(mAccountNumber!!.length != 12){
            val ID_plstslctacc = applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plsslctacc = ID_plstslctacc.getString("PleaseSelectAccount", null)
            CustomBottomSheeet.Show(this,plsslctacc!!,"0")
            // Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Select Account","0")
        }
        else if(SubModule!!.equals("")){
            val ID_plstslctacc = applicationContext.getSharedPreferences(Config.SHARED_PREF284,0)
            var plsslctacc = ID_plstslctacc.getString("PleaseSelectAccount", null)
            CustomBottomSheeet.Show(this,plsslctacc!!,"0")
            //  Toast.makeText(applicationContext,"Please Select Account",Toast.LENGTH_LONG).show()
          //  CustomBottomSheeet.Show(this,"Please Select Account","0")
        }else{

            Log.e(TAG,"subscriberId      785   "+subscriberId)
            Log.e(TAG,"ProvidersCode    785   "+ProvidersCode)
            Log.e(TAG,"CircleMode       785   "+CircleMode)
            Log.e(TAG,"Amount           785   "+Amount)
            Log.e(TAG,"AccountNo        785   "+mAccountNumber)
            Log.e(TAG,"FK_Providers     785   "+ID_Providers)
            Log.e(TAG,"FK_RechargeCircle    785   "+ID_RechargeCircle)
            Log.e(TAG,"FK_Account         785   "+FK_Account)

            RechargeConfirmationPop(subscriberId!!,ProvidersCode!!,CircleMode!!,Amount!!,mAccountNumber,ID_Providers,ID_RechargeCircle,FK_Account,AccountNo!!,Circleaccount!!)

        }
    }

    private fun RechargeConfirmationPop(mobileNumber: String, providersCode: String, circleMode: String, amount: String, mAccountNumber: String?,
        idProviders: String?, idRechargecircle: String?, fkAccount: String?, accountNo: String , Circleaccount: String?) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.recharge_confirmation)

        val tvAcntno = dialog.findViewById(R.id.tvAcntno) as TextView
        val tvbranch = dialog.findViewById(R.id.tvbranch) as TextView
        val tv_mob = dialog.findViewById(R.id.tv_mob) as TextView
        val tv_oper = dialog.findViewById(R.id.tv_oper) as TextView
        val tv_cir = dialog.findViewById(R.id.tv_cir) as TextView
        val text_confirmationmsg = dialog.findViewById(R.id.text_confirmationmsg) as TextView
        val tv_amount = dialog.findViewById(R.id.tv_amount) as TextView
        val tv_amount_words = dialog.findViewById(R.id.tv_amount_words) as TextView
        val img_aapicon = dialog.findViewById(R.id.img_aapicon) as ImageView

        val bt_cancel = dialog.findViewById(R.id.bt_cancel) as Button
        val bt_ok = dialog.findViewById(R.id.bt_ok) as Button



        tvAcntno!!.setText(""+accountNo)
        tvbranch!!.setText(""+BranchName)
        tv_mob!!.setText(""+mobileNumber)
        tv_oper!!.setText(""+tie_operator!!.text.toString())
        tv_cir!!.setText(""+tie_circle!!.text.toString())

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
        PicassoTrustAll.getInstance(this@RechargeActivity)!!.load(imagepath).error(android.R.color.transparent).into(img_aapicon)

        text_confirmationmsg!!.setText("Proceed Recharge With Above Amount ..?")

//            double num =Double.parseDouble(""+mAmount);
//            String stramnt = CommonUtilities.getDecimelFormate(num);
        val stramnt: String = amount.replace(",", "")
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

            if (ProvidersMode.equals("1")){
                recharge(mobileNumber, providersCode, CircleMode, mAccountNumber, SubModule, ID_Providers, ID_RechargeCircle, FK_Account,amount)
            }
            else if (ProvidersMode.equals("2") || ProvidersMode.equals("5")){
                Log.e(TAG,"1067   subscriberId  "+mobileNumber)
                rechargedth(mobileNumber, providersCode, CircleMode, mAccountNumber, SubModule, ID_Providers, ID_RechargeCircle, FK_Account,amount)
            }
//            else if (ProvidersMode.equals("3") || ProvidersMode.equals("4")){
//                //landLine
//               // rechargepostpaid(mobileNumber, providersCode, CircleMode, mAccountNumber, SubModule, ID_Providers, ID_RechargeCircle, FK_Account,amount)
//            }
            else if (ProvidersMode.equals("3") || ProvidersMode.equals("4")){
                rechargepostpaid(mobileNumber, providersCode, CircleMode, mAccountNumber, SubModule, ID_Providers, ID_RechargeCircle, FK_Account,amount)
            }


            dialog.dismiss()
        }

        bt_cancel!!.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()




    }


    private fun rechargepostpaid(mobileNumber: String, providersCode: String, circleMode: String?, mAccountNumber: String?, subModule: String?,
                                 idProviders: String?, idRechargecircle: String?, fkAccount: String?, amount: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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

                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("MobileNumer", MscoreApplication.encryptStart(mobileNumber))
                        requestObject1.put("ProvidersCode", MscoreApplication.encryptStart(ProvidersCode))
                        requestObject1.put("CircleMode", MscoreApplication.encryptStart(circleMode))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("Circleaccount", MscoreApplication.encryptStart(Circleaccount))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(mAccountNumber))

                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FK_Providers", MscoreApplication.encryptStart(idProviders))
                        requestObject1.put("FK_RechargeCircle", MscoreApplication.encryptStart(idRechargecircle))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(FK_Account))

                        Log.e(TAG,"requestObject1  901   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  9011   "+e.toString())
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
                    val call = apiService.getPOSTPaidBilling(body)
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
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()



                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    private fun rechargedth(subscriberId: String, providersCode: String, circleMode: String?, mAccountNumber: String?, subModule: String?, idProviders: String?,
        idRechargecircle: String?, fkAccount: String?, amount: String) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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

                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("SUBSCRIBER_ID", MscoreApplication.encryptStart(subscriberId))
                        requestObject1.put("ProvidersCode", MscoreApplication.encryptStart(ProvidersCode))
                        requestObject1.put("CircleMode", MscoreApplication.encryptStart(circleMode))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(mAccountNumber))

                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FK_Providers", MscoreApplication.encryptStart(idProviders))
                        requestObject1.put("FK_RechargeCircle", MscoreApplication.encryptStart(idRechargecircle))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(FK_Account))

                        Log.e(TAG,"requestObject1  1130   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  11301   "+e.toString())
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
                    val call = apiService.getDTHRecharge(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  11302   "+response.body())
                                Log.e(TAG,"response  11303   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

//                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
//                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
//                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
//                                    OperatorbottomSheet(jArrayOperator!!)

                                    // Toast.makeText(applicationContext,"Not Complete",Toast.LENGTH_LONG).show()
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()



                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    private fun recharge(mobileNumber: String, providersCode: String, circleMode: String?, mAccountNumber: String?, subModule: String?,
                         idProviders: String?, idRechargecircle: String?, fkAccount: String?, amount: String?) {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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


                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("MobileNumer", MscoreApplication.encryptStart(mobileNumber))
                        requestObject1.put("ProvidersCode", MscoreApplication.encryptStart(ProvidersCode))
                        requestObject1.put("CircleMode", MscoreApplication.encryptStart(circleMode))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(amount))
                        requestObject1.put("AccountNo", MscoreApplication.encryptStart(mAccountNumber))

                        requestObject1.put("SubModule", MscoreApplication.encryptStart(SubModule))
                        requestObject1.put("FK_Providers", MscoreApplication.encryptStart(idProviders))
                        requestObject1.put("FK_RechargeCircle", MscoreApplication.encryptStart(idRechargecircle))
                        requestObject1.put("FK_Account", MscoreApplication.encryptStart(FK_Account))

                        Log.e(TAG,"requestObject1  1549   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  15491   "+e.toString())
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
                    val call = apiService.getMobileRecharge(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  15492   "+response.body())
                                Log.e(TAG,"response  15493   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

//                                    val jobjt = jObject.getJSONObject("ProvidersDetailsIfo")
//                                    jArrayOperator = jobjt.getJSONArray("ProvidersDetails")
//                                    Log.e(TAG,"jArrayOperator  4344   "+jArrayOperator)
//                                    OperatorbottomSheet(jArrayOperator!!)

                                   // Toast.makeText(applicationContext,"Not Complete",Toast.LENGTH_LONG).show()
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()



                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                                        finish()
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  15494   "+e.toString())
                                val builder = AlertDialog.Builder(
                                    this@RechargeActivity,
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
                            Log.e(TAG,"Some  15495   "+t.message)
                            val builder = AlertDialog.Builder(
                                this@RechargeActivity,
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
                    Log.e(TAG,"Some  15496   "+e.toString())
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }


    private fun getRecentRecharges() {

        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        ll_recentrecharge!!.visibility = View.GONE
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@RechargeActivity, R.style.Progress)
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

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("36"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))
                        requestObject1.put("ProvidersMode", MscoreApplication.encryptStart(ProvidersMode))

                        Log.e(TAG,"requestObject1  1085   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  10851   "+e.toString())
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
                    val call = apiService.getRechargeHistory(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()

                                val jObject = JSONObject(response.body())
                                Log.e(TAG,"response  10852   "+response.body())
                                Log.e(TAG,"response  10853   "+jObject.getString("StatusCode"))
                                if (jObject.getString("StatusCode") == "0") {

                                    ll_recentrecharge!!.visibility = View.VISIBLE

                                    val jobjt = jObject.getJSONObject("RechargeHistory")
                                    jArrayRecent = jobjt.getJSONArray("RechargeHistoryList")
                                    Log.e(TAG,"jArrayRecent  10854   "+jArrayRecent)

                                    val lLayout = GridLayoutManager(this@RechargeActivity, 1)
                                    rvrecentRecharge!!.setLayoutManager(lLayout)
                                    rvrecentRecharge!!.setHasFixedSize(true)
                                    val recent_adapter = RecentRechargeAdapter(applicationContext!!, jArrayRecent!!)
                                    rvrecentRecharge!!.adapter = recent_adapter
                                    recent_adapter.setClickListener(this@RechargeActivity)

                                    //  AccountNobottomSheet(jArrayAccount!!)

                                }
                                else if(jObject.getString("StatusCode") == "-2"){

                                }
                                else {
                                    val builder = AlertDialog.Builder(
                                        this@RechargeActivity,
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
                                    this@RechargeActivity,
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
                                this@RechargeActivity,
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
                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }


    private fun isCircleAccountNumberMandatory(): Boolean {

        return (ProvidersMode.equals("3") || ProvidersMode.equals("4")) && (tie_operator!!.text.toString().contains("MTNL") && tie_circle!!.text.toString()
            .contains("Delhi") || tie_operator!!.text.toString().contains("BSNL"))
    }


}