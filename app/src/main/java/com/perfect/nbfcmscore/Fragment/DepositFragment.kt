package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Activity.MpinActivity
import com.perfect.nbfcmscore.Adapter.AccountLsitAdaptor
import com.perfect.nbfcmscore.Adapter.LanguageLsitAdaptor
import com.perfect.nbfcmscore.Helper.AlertMessage
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class DepositFragment : Fragment(){

    private var switch1: Switch? = null
    val TAG: String = "DepositFragment"

    private var progressDialog: ProgressDialog? = null
    var rv_Accountlist: RecyclerView? = null
    var tvdate: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.fragment_deposit, container,
            false
        )

        tvdate = v.findViewById<View>(R.id.tvdate) as TextView?
        val sdf = SimpleDateFormat("dd-M-yyyy")

        val ID_list = context!!.getSharedPreferences(Config.SHARED_PREF209,0)
        var listason =  ID_list.getString("ListasonDate", null)


        tvdate!!.text="** "+listason+" "+sdf.format(Date())+"."
        rv_Accountlist = v.findViewById<View>(R.id.rv_Accountlist) as RecyclerView?
        getAccountlist("1")
        switch1 = v.findViewById<View>(R.id.switch1) as Switch?

        val ID_Active = activity!!.getSharedPreferences(Config.SHARED_PREF87,0)
        switch1!!.setText(ID_Active.getString("Active",null))

        val ID_Closed = activity!!.getSharedPreferences(Config.SHARED_PREF199,0)


        /*val ID_Active = activity!!.getSharedPreferences(Config.SHARED_PREF87,0)
        switch1!!.setText(ID_Active.getString("Active",null))*/

        switch1?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                switch1!!.setText(ID_Closed.getString("Closed",null))
               // switch1!!.setText("Closed")
                switch1!!.setTextColor(resources.getColor(R.color.redDark))

                getAccountlist("0")
            }else{
              //  switch1!!.setText("Active")
                switch1!!.setText(ID_Active.getString("Active",null))
                switch1!!.setTextColor(resources.getColor(R.color.green))
                getAccountlist("1")
            }
        })
        return v
    }

    private fun getAccountlist(strStatus: String) {
        val baseurlSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(context!!)) {
            true -> {
                progressDialog = ProgressDialog(context, R.style.Progress)
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
                        .sslSocketFactory(Config.getSSLSocketFactory(context!!), trustManager)
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

                        val FK_CustomerSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        val BankKeySP = context!!.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = context!!.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("9"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("1"))
                        requestObject1.put("AccountStatus", MscoreApplication.encryptStart(strStatus))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))

                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        Log.e(TAG,"requestObject1   1291    "+requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(context!!,activity!!,"Alert","Some technical issues.",1);
                        e.printStackTrace()
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getCustomerLoanAndDepositDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"response   1292    "+response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jObject = JSONObject(response.body())
                                    if (jObject.getString("StatusCode") == "0") {
                                        //   val jobjt = jObject.getJSONObject("VarificationMaintenance")
                                        rv_Accountlist!!.visibility=View.VISIBLE

                                        val jobjt =
                                            jObject.getJSONObject("CustomerLoanAndDepositDetails")
                                        val jarray =
                                            jobjt.getJSONArray("CustomerLoanAndDepositDetailsList")

                                        val obj_adapter = AccountLsitAdaptor(context!!.applicationContext!!, jarray)
                                        rv_Accountlist!!.layoutManager = LinearLayoutManager(context!!.applicationContext, LinearLayoutManager.VERTICAL, false)
                                        rv_Accountlist!!.adapter = obj_adapter

                                    } else {
                                        rv_Accountlist!!.visibility=View.GONE
                                        AlertMessage().alertMessage(context!!,activity!!,"Alert",jObject.getString("EXMessage"),1);
                                    }
                                } else {
                                    rv_Accountlist!!.visibility=View.GONE
                                    AlertMessage().alertMessage(context!!,activity!!,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(context!!,activity!!,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(context!!,activity!!,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(context!!,activity!!,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(context!!,activity!!,"Alert"," No Internet Connection. ",3);
            }
        }

    }

}