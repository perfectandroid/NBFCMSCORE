package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.LoanTypeAdapter
import com.perfect.nbfcmscore.Adapter.EmiListAdapter
import com.perfect.nbfcmscore.Adapter.LoanPurposeAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Model.EMIModel
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class LoanApplicationActivity : AppCompatActivity()   , View.OnClickListener, ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "EMIActivity"
    private var rv_loanslab: RecyclerView? = null
    var list_view: ListView? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var principal: EditText? = null
  //  var interest: TextInputEditText? = null
    var years: EditText? = null
    var tie_emi: TextInputEditText? = null
    var tie_porpose: TextInputEditText? = null
    //  var llemitype: LinearLayout? = null
    var sadapter: EmiListAdapter? = null
    // var txt_emi: TextView? = null


    var EMIArrayList: ArrayList<EMIModel> = ArrayList<EMIModel>()
    var array_sort: ArrayList<EMIModel> = ArrayList<EMIModel>()

    var jArrayEmi: JSONArray? = null
    var  dialogEmii: BottomSheetDialog? = null

    var  btn_calculate: Button? = null
    var  btn_clear: Button? = null

    var LoanAmount: String? = ""
    var RateOfInterset: String? = ""
    var TenureValue: String? = ""
    var LoanDate: String? = ""
    var ID_EmiMethod: String? = ""
    var ID_PurposeMethod: String? = ""

    var tv_instalment: TextView? = null
    var tv_interest_total: TextView? = null
    var tv_interest_principal: TextView? = null
    var llOutput: LinearLayout? = null
    var tv_header: TextView? = null

    var txtv_princamt: TextView? = null
    var txtv_mnth: TextView? = null
    var txtv_type: TextView? = null
    var txtv_loanpur: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loanapplication)


        setInitialise()
        setRegister()

        val ID_header = applicationContext.getSharedPreferences(Config.SHARED_PREF76,0)
        tv_header!!.setText(ID_header.getString("LoanApplication",null))

        val ID_princamt = applicationContext.getSharedPreferences(Config.SHARED_PREF191,0)
        principal!!.setHint(ID_princamt.getString("PRINCIPALAMOUNT",null))

        val ID_month = applicationContext.getSharedPreferences(Config.SHARED_PREF193,0)
        years!!.setHint(ID_month.getString("MONTH",null))

        /*val ID_loantype = applicationContext.getSharedPreferences(Config.SHARED_PREF197,0)
        tie_emi!!.setHint(ID_loantype.getString("SelectLoantype",null))*/

       /* val ID_loanpurpse = applicationContext.getSharedPreferences(Config.SHARED_PREF198,0)
        tie_porpose!!.setHint(ID_loanpurpse.getString("Selectloanpurpose",null))
*/
        val ID_reset = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        btn_clear!!.setText(ID_reset.getString("RESET",null))

        val ID_calc = applicationContext.getSharedPreferences(Config.SHARED_PREF190,0)
        btn_calculate!!.setText(ID_calc.getString("CALCULATE",null))


        val ID_princplamt = applicationContext.getSharedPreferences(Config.SHARED_PREF191,0)
        txtv_princamt!!.setText(ID_princplamt.getString("PRINCIPALAMOUNT",null))

        val ID_mnth = applicationContext.getSharedPreferences(Config.SHARED_PREF193,0)
        txtv_mnth!!.setText(ID_mnth.getString("MONTH",null))

        val ID_loantyp = applicationContext.getSharedPreferences(Config.SHARED_PREF264,0)
        txtv_type!!.setText(ID_loantyp.getString("LoanType",null))

        val ID_loanpurps = applicationContext.getSharedPreferences(Config.SHARED_PREF263,0)
        txtv_loanpur!!.setText(ID_loanpurps.getString("LoanPurpose",null))



        val imlogo: ImageView = findViewById(R.id.imlogo)
        Glide.with(this).load(R.drawable.loanapplicationgif).into(imlogo)
        principal!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                principal!!.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString != "") {
                        val longval: Double
                        if (originalString.contains(",")) {
                            originalString = originalString.replace(",".toRegex(), "")
                        }
                        longval = originalString.toDouble()

                        val formattedString: String? = Config!!.getDecimelFormateForEditText(longval)
                        principal!!.setText(formattedString)
                        val selection: Int = principal!!.length()
                        principal!!.setSelection(selection)
                        // tie_amount!!.setSelection(tie_amount!!.getText().length)
                        val amnt: String = principal!!.getText().toString().replace(",".toRegex(), "")
                        val netAmountArr = amnt.split("\\.".toRegex()).toTypedArray()

                    } else {
                        principal!!.setText("")
                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                principal!!.addTextChangedListener(this)
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

    private fun setRegister() {

        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
        tie_porpose!!.setOnClickListener(this)
        tie_emi!!.setOnClickListener(this)
        btn_calculate!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)
    }

    private fun setInitialise() {
//        txt_emi  = findViewById<View>(R.id.txt_emi) as TextView?
//        llemitype  = findViewById<View>(R.id.llemitype) as LinearLayout?
        principal  = findViewById<View>(R.id.tie_principal) as EditText?
       // interest  = findViewById<View>(R.id.tie_interest) as TextInputEditText?
        years  = findViewById<View>(R.id.tie_years) as EditText?
        tie_emi  = findViewById<View>(R.id.tie_emi) as TextInputEditText?
        tie_porpose  = findViewById<View>(R.id.tie_porpose) as TextInputEditText?

        rv_loanslab  = findViewById<View>(R.id.rv_loanslab) as RecyclerView?
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgHome = findViewById<ImageView>(R.id.imgHome)

        txtv_princamt = findViewById<TextView>(R.id.txtv_princamt)
        txtv_mnth = findViewById<TextView>(R.id.txtv_mnth)
        txtv_type = findViewById<TextView>(R.id.txtv_type)
        txtv_loanpur = findViewById<TextView>(R.id.txtv_loanpur)

        btn_calculate = findViewById<Button>(R.id.btn_calculate)
        btn_clear = findViewById<Button>(R.id.btn_clear)

        tv_header = findViewById<TextView>(R.id.tv_header)

        tv_instalment = findViewById<TextView>(R.id.tv_instalment)
        tv_interest_total = findViewById<TextView>(R.id.tv_interest_total)
        tv_interest_principal = findViewById<TextView>(R.id.tv_interest_principal)
        llOutput = findViewById<LinearLayout>(R.id.llOutput)



    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@LoanApplicationActivity, HomeActivity::class.java))
            }
            R.id.tie_porpose ->{
                getLoanPurpose()
            }
            R.id.tie_emi ->{
                getLoanTypes()
            }
            R.id.btn_calculate ->{

                validations()
            }

            R.id.btn_clear ->{

                clearFields()
            }
        }
    }

    private fun clearFields() {
        principal!!.setText("")
       // interest!!.setText("")
        years!!.setText("")
        tie_emi!!.setText("")
        tie_porpose!!.setText("")
        ID_EmiMethod = ""
        ID_PurposeMethod = ""
        llOutput!!.visibility = View.GONE

    }


    private fun getLoanTypes() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LoanApplicationActivity, R.style.Progress)
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

                        val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("38"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  558  "+requestObject1)

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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getLoanTypeDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {


                                    val jobjt = jObject.getJSONObject("LoanTypeDetails")
                                    jArrayEmi = jobjt.getJSONArray("LoanTypeDetailsList")
//                                    val jarray =
//                                        jobjt.getJSONArray("EMIMethodDateilsList")
//                                    array_sort =
//                                        ArrayList<EMIModel>()
//                                    for (k in 0 until jarray.length()) {
//                                        val jsonObject = jarray.getJSONObject(k)
//                                        EMIArrayList.add(
//                                            EMIModel(
//                                                jsonObject.getString("ID_EMIMethod"),
//                                                jsonObject.getString("MethodName")
//                                            )
//                                        )
//                                        array_sort.add(
//                                            EMIModel(
//                                                jsonObject.getString("ID_EMIMethod"),
//                                                jsonObject.getString("MethodName")
//                                            )
//                                        )
//                                    }
//                                    sadapter = EmiListAdapter(
//                                        this@EMIActivity,array_sort
//                                    )
//                                    list_view!!.adapter = sadapter
//                                    list_view!!.onItemClickListener =
//                                        OnItemClickListener { parent, view, position, id ->
//                                            txt_emi!!.setText(array_sort.get(
//                                                position
//                                            ).getProductName()
//                                            )
//                                            ID_EmiMethod =array_sort.get(
//                                                position
//                                            ).getID_Product()
//                                            alertDialog.dismiss()
//                                        }

                                    Log.e(TAG,"jArrayEmi  558  "+jArrayEmi)
                                    jArrayEmi = jobjt.getJSONArray("LoanTypeDetailsList")
                                    emiBottomSheet(jArrayEmi!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@LoanApplicationActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@LoanApplicationActivity,
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
                                this@LoanApplicationActivity,
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
                    val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }


    private fun getLoanPurpose() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LoanApplicationActivity, R.style.Progress)
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

                        val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("39"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  558  "+requestObject1)

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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getLoanPurposeDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {


                                    val jobjt = jObject.getJSONObject("LoanPurposeDetails")
                                    jArrayEmi = jobjt.getJSONArray("LoanPurposeDetailsList")
//                                    val jarray =
//                                        jobjt.getJSONArray("EMIMethodDateilsList")
//                                    array_sort =
//                                        ArrayList<EMIModel>()
//                                    for (k in 0 until jarray.length()) {
//                                        val jsonObject = jarray.getJSONObject(k)
//                                        EMIArrayList.add(
//                                            EMIModel(
//                                                jsonObject.getString("ID_EMIMethod"),
//                                                jsonObject.getString("MethodName")
//                                            )
//                                        )
//                                        array_sort.add(
//                                            EMIModel(
//                                                jsonObject.getString("ID_EMIMethod"),
//                                                jsonObject.getString("MethodName")
//                                            )
//                                        )
//                                    }
//                                    sadapter = EmiListAdapter(
//                                        this@EMIActivity,array_sort
//                                    )
//                                    list_view!!.adapter = sadapter
//                                    list_view!!.onItemClickListener =
//                                        OnItemClickListener { parent, view, position, id ->
//                                            txt_emi!!.setText(array_sort.get(
//                                                position
//                                            ).getProductName()
//                                            )
//                                            ID_EmiMethod =array_sort.get(
//                                                position
//                                            ).getID_Product()
//                                            alertDialog.dismiss()
//                                        }

                                    Log.e(TAG,"jArrayEmi  558  "+jArrayEmi)
                                    jArrayEmi = jobjt.getJSONArray("LoanPurposeDetailsList")
                                    loanpurposeBottomSheet(jArrayEmi!!)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@LoanApplicationActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@LoanApplicationActivity,
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
                                this@LoanApplicationActivity,
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
                    val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun emiBottomSheet(jArrayEmi: JSONArray) {

        Log.e(TAG,"jArrayEmi  272     "+jArrayEmi)

        dialogEmii = BottomSheetDialog(this,R.style.BottomSheetDialog)
        dialogEmii!!.setContentView(R.layout.loantype_bottom_sheet)

        val rvEmiType = dialogEmii!!.findViewById<RecyclerView>(R.id.rvEmiType)

        val lLayout = GridLayoutManager(this@LoanApplicationActivity, 1)
        rvEmiType!!.setLayoutManager(lLayout)
        rvEmiType!!.setHasFixedSize(true)
        val obj_adapter = LoanTypeAdapter(applicationContext!!, jArrayEmi!!)
        rvEmiType!!.adapter = obj_adapter
        obj_adapter.setClickListener(this@LoanApplicationActivity)

        dialogEmii!!.setCancelable(true)
        dialogEmii!!.show()
    }

    private fun loanpurposeBottomSheet(jArrayEmi: JSONArray) {

        Log.e(TAG,"jArrayEmi  272     "+jArrayEmi)

        dialogEmii = BottomSheetDialog(this,R.style.BottomSheetDialog)
        dialogEmii!!.setContentView(R.layout.loanpurpose_bottom_sheet)

        val rvEmiType = dialogEmii!!.findViewById<RecyclerView>(R.id.rvEmiType)

        val lLayout = GridLayoutManager(this@LoanApplicationActivity, 1)
        rvEmiType!!.setLayoutManager(lLayout)
        rvEmiType!!.setHasFixedSize(true)
        val obj_adapter = LoanPurposeAdapter(applicationContext!!, jArrayEmi!!)
        rvEmiType!!.adapter = obj_adapter
        obj_adapter.setClickListener(this@LoanApplicationActivity)

        dialogEmii!!.setCancelable(true)
        dialogEmii!!.show()
    }

    override fun onClick(position: Int, data: String) {
        if (data.equals("emi")){
            dialogEmii!!.dismiss()
            var jsonObject1 = jArrayEmi!!.getJSONObject(position)

            ID_EmiMethod = jsonObject1.getString("FK_TermLoanType")
            tie_emi!!.setText(""+jsonObject1.getString("LoanType"))

        }
        if (data.equals("purpose")){
            dialogEmii!!.dismiss()
            var jsonObject1 = jArrayEmi!!.getJSONObject(position)

            ID_PurposeMethod = jsonObject1.getString("FK_LoanPurpose")
            tie_porpose!!.setText(""+jsonObject1.getString("LoanPurpose"))

        }
    }

    private fun validations() {

        LoanAmount = principal!!.text.toString().replace(",", "")
      //  RateOfInterset = interest!!.text.toString()
        TenureValue = years!!.text.toString()

        if (LoanAmount.equals("")){
            val ID_princamt = applicationContext.getSharedPreferences(Config.SHARED_PREF260,0)
            var princamt = ID_princamt.getString("EnterPrincipalAmount",null)

            CustomBottomSheeet.Show(this,princamt!!,"0")

            //CustomBottomSheeet.Show(this,"Enter Principal Amount ","0")
        }
//        else if (RateOfInterset.equals("")){
//            CustomBottomSheeet.Show(this,"Enter Interest Rate ","0")
//        }
        else if (TenureValue.equals("")){
            val ID_mnth = applicationContext.getSharedPreferences(Config.SHARED_PREF261,0)
            var mnth = ID_mnth.getString("EnterMonth",null)
            CustomBottomSheeet.Show(this,mnth!!,"0")
           // CustomBottomSheeet.Show(this,"Enter Month ","0")
        }
        else if (ID_EmiMethod.equals("")){
            val ID_loant = applicationContext.getSharedPreferences(Config.SHARED_PREF197,0)
            var loanty = ID_loant.getString("SelectLoantype",null)
            CustomBottomSheeet.Show(this,loanty!!,"0")

           // CustomBottomSheeet.Show(this,"Select Loan type ","0")
        }
        else if (ID_PurposeMethod.equals("")){
            val ID_loanpur = applicationContext.getSharedPreferences(Config.SHARED_PREF198,0)
            var loanpur = ID_loanpur.getString("Selectloanpurpose",null)
            CustomBottomSheeet.Show(this,loanpur!!,"0")
            //CustomBottomSheeet.Show(this,"Select Loan Purpose ","0")
        }
        else{

            val currentTime = Calendar.getInstance().time
            Log.e(TAG,"currentTime  "+currentTime)
            val date: DateFormat = SimpleDateFormat("yyyy_MM-dd")
            LoanDate = date.format(currentTime)
            Log.e(TAG,"LoanDate  "+LoanDate)

            getLoanApplication()

        }

    }

    private fun getLoanApplication() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        llOutput!!.visibility = View.GONE
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@LoanApplicationActivity, R.style.Progress)
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

                        val FK_CustomerSP = applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)


                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("Amount", MscoreApplication.encryptStart(LoanAmount))
                     //   requestObject1.put("RateOfInterset", MscoreApplication.encryptStart(RateOfInterset))
                        requestObject1.put("FK_LoanPurpose",  MscoreApplication.encryptStart(ID_PurposeMethod))
                        requestObject1.put("LoanPeriod", MscoreApplication.encryptStart(TenureValue))
                        requestObject1.put("FK_LoanType", MscoreApplication.encryptStart(ID_EmiMethod))
                       // requestObject1.put("LoanDate", MscoreApplication.encryptStart(LoanDate))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  212  "+requestObject1)

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
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getLoanApplicationRequset(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"response   2122   "+response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    llOutput!!.visibility = View.VISIBLE
                                    val jobjt = jObject.getJSONObject("LoanApplicationRequset")


                                    tv_instalment!!.setText("Application number : "+jobjt.getString("ApplicationNumber"))
                                 //   tv_interest_total!!.setText(""+jobjt.getString("TotalInterest"))
                                 //   tv_interest_principal!!.setText(""+jobjt.getString("PrincipalInterest"))

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@LoanApplicationActivity,
                                        R.style.MyDialogTheme
                                    )
                                    builder.setMessage("" + jObject.getString("EXMessage"))
                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                val builder = AlertDialog.Builder(
                                    this@LoanApplicationActivity,
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
                                this@LoanApplicationActivity,
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
                    val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@LoanApplicationActivity, R.style.MyDialogTheme)
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