package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Adapter.AccountLsitAdaptor
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class LoanlistFragment : Fragment(){

    private var switch1: Switch? = null
    var tvdate: TextView? = null

    private var progressDialog: ProgressDialog? = null
    var rv_Accountlist: RecyclerView? = null

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
        tvdate!!.text="** List as on "+sdf.format(Date())+"."
        rv_Accountlist = v.findViewById<View>(R.id.rv_Accountlist) as RecyclerView?
        getAccountlist("1")
        switch1 = v.findViewById<View>(R.id.switch1) as Switch?

        val ID_Active = activity!!.getSharedPreferences(Config.SHARED_PREF87,0)
        switch1!!.setText(ID_Active.getString("Active",null))

        switch1?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                switch1!!.setText("Closed")
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
        when(ConnectivityUtils.isConnected(context!!)) {
            true -> {
                progressDialog = ProgressDialog(context, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(context!!))
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

                        val FK_CustomerSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("9"))
                        requestObject1.put("FK_Customer",  MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("SubMode", MscoreApplication.encryptStart("0"))
                        requestObject1.put("AccountStatus", MscoreApplication.encryptStart(strStatus))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                            "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankKey
                                )
                            )
                        )
                        requestObject1.put(
                            "BankHeader", MscoreApplication.encryptStart(
                                getResources().getString(
                                    R.string.BankHeader
                                )
                            )
                        )
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        val builder = AlertDialog.Builder(
                            context!!,
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
                    val body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
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
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jObject = JSONObject(response.body())
                                    if (jObject.getString("StatusCode") == "0") {
                                        //   val jobjt = jObject.getJSONObject("VarificationMaintenance")

                                        val jobjt =
                                            jObject.getJSONObject("CustomerLoanAndDepositDetails")
                                        val jarray =
                                            jobjt.getJSONArray("CustomerLoanAndDepositDetailsList")

                                        val obj_adapter = AccountLsitAdaptor(context!!.applicationContext!!, jarray)
                                        rv_Accountlist!!.layoutManager = LinearLayoutManager(context!!.applicationContext, LinearLayoutManager.VERTICAL, false)
                                        rv_Accountlist!!.adapter = obj_adapter

                                    } else {
                                        val builder = AlertDialog.Builder(
                                            context!!,
                                            R.style.MyDialogTheme
                                        )
                                        builder.setMessage("" + jObject.getString("EXMessage"))
                                        builder.setPositiveButton("Ok") { dialogInterface, which ->
                                        }
                                        val alertDialog: AlertDialog = builder.create()
                                        alertDialog.setCancelable(false)
                                        alertDialog.show()
                                    }
                                } else {
                                    val builder = AlertDialog.Builder(
                                        context!!,
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
                                    context!!,
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
                                context!!,
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
                    val builder = AlertDialog.Builder(context!!, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(context!!, R.style.MyDialogTheme)
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