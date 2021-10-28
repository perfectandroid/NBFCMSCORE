package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.AccountAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.Helper.MscoreApplication
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


    var tv_beneficiarylist: TextView? = null

    var tie_accountnumber: TextInputEditText? = null
    var tie_beneficiary: TextInputEditText? = null

    var jArrayAccount: JSONArray? = null

    var  dialogAccount: BottomSheetDialog? = null

    var FK_Account: String? = ""
    var AccountNumber: String? = ""
    var SubModule: String? = ""
    var BranchName: String? = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_bank_fund_transfer)

        setInitialise()
        setRegister()

        getOwnAccount()

//        tie_beneficiary!!.isEnabled = false

    }

    private fun setInitialise() {
        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)
        tv_header = findViewById<TextView>(R.id.tv_header)

        tv_beneficiarylist = findViewById<TextView>(R.id.tv_beneficiarylist)


        tie_accountnumber = findViewById<TextInputEditText>(R.id.tie_accountnumber)
        tie_beneficiary = findViewById<TextInputEditText>(R.id.tie_beneficiary)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

        tie_accountnumber!!.setOnClickListener(this)

        tv_beneficiarylist!!.setOnClickListener(this)
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
            } R.id.tv_beneficiarylist ->{

//            startActivity(Intent(this@OtherBankFundTransferActivity, BeneficiaryListActivity::class.java))
            val i = Intent(this, BeneficiaryListActivity::class.java)
            startActivityForResult(i, PICK_BENEFICIARY)
            }
        }
    }


    private fun getOwnAccount() {
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
                        .baseUrl(Config.BASE_URL)
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
}