package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.EmiAdapter
import com.perfect.nbfcmscore.Adapter.EmiListAdapter
import com.perfect.nbfcmscore.Adapter.LoanSlabAdaptor
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

class EMIActivity : AppCompatActivity()  , View.OnClickListener, ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "EMIActivity"
    private var rv_loanslab: RecyclerView? = null
    var list_view: ListView? = null
    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var principal: EditText? = null
    var interest: EditText? = null
    var years: EditText? = null
    var tie_emi: TextInputEditText? = null
  //  var llemitype: LinearLayout? = null
    var sadapter: EmiListAdapter? = null
   // var txt_emi: TextView? = null


    var EMIArrayList: ArrayList<EMIModel> = ArrayList<EMIModel>()
    var array_sort: ArrayList<EMIModel> = ArrayList<EMIModel>()

    var jArrayEmi: JSONArray? = null
    var  dialogEmii: BottomSheetDialog? = null

    var  btn_calculate: TextView? = null
    var  btn_clear: TextView? = null

    var LoanAmount: String? = ""
    var RateOfInterset: String? = ""
    var TenureValue: String? = ""
    var LoanDate: String? = ""
    var ID_EmiMethod: String? = ""

    var tv_instalment: TextView? = null
    var tv_interest_total: TextView? = null
    var tv_interest_principal: TextView? = null
    var llOutput: LinearLayout? = null
    var tv_header: TextView? = null
    var txtv_emi: TextView? = null
    var txtv_intrstrate: TextView? = null
    var txtv_month: TextView? = null
    var txtv_type: TextView? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emi_new)


        setInitialise()
        setRegister()

        val imemilogo: ImageView = findViewById(R.id.imemilogo)
        //Glide.with(this).load(R.drawable.emigif).into(imemilogo)

        val ID_header = applicationContext.getSharedPreferences(Config.SHARED_PREF79,0)
        tv_header!!.setText(ID_header.getString("EMICalculator",null))

        val ID_princamt = applicationContext.getSharedPreferences(Config.SHARED_PREF191,0)
        principal!!.setHint(ID_princamt.getString("PRINCIPALAMOUNT",null))

        val ID_rate = applicationContext.getSharedPreferences(Config.SHARED_PREF192,0)
        interest!!.setHint(ID_rate.getString("INTERESTRATE",null))

        val ID_mnth = applicationContext.getSharedPreferences(Config.SHARED_PREF193,0)
        years!!.setHint(ID_mnth.getString("MONTH",null))

        val ID_emityp = applicationContext.getSharedPreferences(Config.SHARED_PREF194,0)
        tie_emi!!.setHint(ID_emityp.getString("Selectemitype",null))

        val ID_reset = applicationContext.getSharedPreferences(Config.SHARED_PREF189,0)
        btn_clear!!.setText(ID_reset.getString("RESET",null))

        val ID_calc = applicationContext.getSharedPreferences(Config.SHARED_PREF190,0)
        btn_calculate!!.setText(ID_calc.getString("CALCULATE",null))


        txtv_emi!!.setText(ID_princamt.getString("PRINCIPALAMOUNT",null))

        val ID_intrstrate = applicationContext.getSharedPreferences(Config.SHARED_PREF192,0)
        txtv_intrstrate!!.setText(ID_intrstrate.getString("INTERESTRATE",null))


        txtv_month!!.setText(ID_mnth.getString("MONTH",null))

        val ID_type = applicationContext.getSharedPreferences(Config.SHARED_PREF256,0)
        txtv_type!!.setText(ID_type.getString("EMITYPE",null))



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

//        getStandingInstruction()
    }

    private fun setRegister() {

        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
      //  llemitype!!.setOnClickListener(this)
        tie_emi!!.setOnClickListener(this)
        btn_calculate!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)
    }

    private fun setInitialise() {
//        txt_emi  = findViewById<View>(R.id.txt_emi) as TextView?
//        llemitype  = findViewById<View>(R.id.llemitype) as LinearLayout?
        principal  = findViewById<View>(R.id.tie_principal) as EditText?
        interest  = findViewById<View>(R.id.tie_interest) as EditText?
        years  = findViewById<View>(R.id.tie_years) as EditText?
        tie_emi  = findViewById<View>(R.id.tie_emi) as TextInputEditText?

        tv_header= findViewById<View>(R.id.tv_header) as TextView?

        txtv_emi= findViewById<View>(R.id.txtv_emi) as TextView?
        txtv_intrstrate= findViewById<View>(R.id.txtv_intrstrate) as TextView?
        txtv_month= findViewById<View>(R.id.txtv_month) as TextView?
        txtv_type= findViewById<View>(R.id.txtv_type) as TextView?


        rv_loanslab  = findViewById<View>(R.id.rv_loanslab) as RecyclerView?
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgHome = findViewById<ImageView>(R.id.imgHome)

        btn_calculate = findViewById<TextView>(R.id.btn_calculate)
        btn_clear = findViewById<TextView>(R.id.btn_clear)

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
                startActivity(Intent(this@EMIActivity, HomeActivity::class.java))
            }
//            R.id.llemitype ->{
//                getEMIType()
//            }
            R.id.tie_emi ->{
                getEMITypes()
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
        interest!!.setText("")
        years!!.setText("")
        tie_emi!!.setText("")
        ID_EmiMethod = ""
        llOutput!!.visibility = View.GONE

    }


    private fun getEMITypes() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@EMIActivity, R.style.Progress)
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


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("21"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  558  "+requestObject1)

                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getEMIMethodDateils(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {


                                    val jobjt = jObject.getJSONObject("EMIMethodDateils")
                                    jArrayEmi = jobjt.getJSONArray("EMIMethodDateilsList")
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
                                    jArrayEmi = jobjt.getJSONArray("EMIMethodDateilsList")
                                    emiBottomSheet(jArrayEmi!!)

                                } else {
                                    AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert",jObject.getString("EXMessage"),1);
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                }
            }
            false -> {
                AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert"," No Internet Connection. ",3);
            }
        }
    }

    private fun emiBottomSheet(jArrayEmi: JSONArray) {

        Log.e(TAG,"jArrayEmi  272     "+jArrayEmi)

        dialogEmii = BottomSheetDialog(this,R.style.BottomSheetDialog)
        dialogEmii!!.setContentView(R.layout.emi_bottom_sheet)

        val rvEmiType = dialogEmii!!.findViewById<RecyclerView>(R.id.rvEmiType)

        val lLayout = GridLayoutManager(this@EMIActivity, 1)
        rvEmiType!!.setLayoutManager(lLayout)
        rvEmiType!!.setHasFixedSize(true)
        val obj_adapter = EmiAdapter(applicationContext!!, jArrayEmi!!)
        rvEmiType!!.adapter = obj_adapter
        obj_adapter.setClickListener(this@EMIActivity)

        dialogEmii!!.setCancelable(true)
        dialogEmii!!.show()
    }

    override fun onClick(position: Int, data: String) {
        if (data.equals("emi")){
            dialogEmii!!.dismiss()
            var jsonObject1 = jArrayEmi!!.getJSONObject(position)

            ID_EmiMethod = jsonObject1.getString("ID_EMIMethod")
            tie_emi!!.setText(""+jsonObject1.getString("MethodName"))

        }
    }

    private fun validations() {

        LoanAmount = principal!!.text.toString().replace(",", "")
        RateOfInterset = interest!!.text.toString()
        TenureValue = years!!.text.toString()

        if (LoanAmount.equals("")){
            val ID_princamt = applicationContext.getSharedPreferences(Config.SHARED_PREF260,0)
            var princamt = ID_princamt.getString("EnterPrincipalAmount",null)


            CustomBottomSheeet.Show(this,princamt!!,"0")
          //  CustomBottomSheeet.Show(this,"Enter Principal Amount ","0")
        }
        else if (RateOfInterset.equals("")){
            val ID_intrstrate = applicationContext.getSharedPreferences(Config.SHARED_PREF262,0)
            var intrst = ID_intrstrate.getString("EnterInterestRate",null)
            CustomBottomSheeet.Show(this,intrst!!,"0")
           // CustomBottomSheeet.Show(this,"Enter Interest Rate ","0")
        }
        else if (TenureValue.equals("")){
            val ID_mnth = applicationContext.getSharedPreferences(Config.SHARED_PREF261,0)
            var mnth = ID_mnth.getString("EnterMonth",null)
            CustomBottomSheeet.Show(this,mnth!!,"0")
            //CustomBottomSheeet.Show(this,"Enter Month ","0")
        }
        else if (ID_EmiMethod.equals("")){
            val ID_emityp = applicationContext.getSharedPreferences(Config.SHARED_PREF194,0)
            var emity = ID_emityp.getString("Selectemitype",null)
            CustomBottomSheeet.Show(this,emity!!,"0")
          //  CustomBottomSheeet.Show(this,"Select emi type ","0")
        }
        else{

            val currentTime = Calendar.getInstance().time
            Log.e(TAG,"currentTime  "+currentTime)
            val date: DateFormat = SimpleDateFormat("yyyy_MM-dd")
            LoanDate = date.format(currentTime)
            Log.e(TAG,"LoanDate  "+LoanDate)

            getStandingInstruction()

        }

    }

    private fun getStandingInstruction() {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        llOutput!!.visibility = View.GONE
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@EMIActivity, R.style.Progress)
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


                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("22"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("LoanAmount", MscoreApplication.encryptStart(LoanAmount))
                        requestObject1.put("RateOfInterset", MscoreApplication.encryptStart(RateOfInterset))
                        requestObject1.put("MonthOrYear",  MscoreApplication.encryptStart("M"))
                        requestObject1.put("TenureValue", MscoreApplication.encryptStart(TenureValue))
                        requestObject1.put("Id_Method", MscoreApplication.encryptStart(ID_EmiMethod))
                        requestObject1.put("LoanDate", MscoreApplication.encryptStart(LoanDate))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1  212  "+requestObject1)

                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getEMICalculatorDateils(body)
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
                                    val jobjt = jObject.getJSONObject("EMICalculatorDateils")
//                                    val jarray =
//                                        jobjt.getJSONArray("LoanSlabDetailsList")
//
//                                    val obj_adapter = LoanSlabAdaptor(applicationContext!!, jarray)
//                                    rv_loanslab!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
//                                    rv_loanslab!!.adapter = obj_adapter

                                    tv_instalment!!.setText("\u20B9 " + Config.getDecimelFormate(jobjt.getDouble("InstallmentAmount")))
                                    tv_interest_total!!.setText("\u20B9 " + Config.getDecimelFormate(jobjt.getDouble("TotalInterest")))
                                    tv_interest_principal!!.setText("\u20B9 " + Config.getDecimelFormate(jobjt.getDouble("PrincipalInterest")))
                                } else {
                                    AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert",jObject.getString("EXMessage"),1);
                                }                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(this@EMIActivity,this@EMIActivity,"Alert"," No Internet Connection. ",3);
            }
        }

    }


}