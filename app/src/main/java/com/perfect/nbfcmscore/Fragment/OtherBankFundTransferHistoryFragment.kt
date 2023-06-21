package com.perfect.nbfcmscore.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Activity.OtherfundTransferHistory
import com.perfect.nbfcmscore.Adapter.ProductSummaryAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.AlertMessage
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class OtherBankFundTransferHistoryFragment:Fragment(), AdapterView.OnItemSelectedListener {

    private val mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var progressDialog: ProgressDialog? = null
    var rv_otherfund: RecyclerView? = null
    var tv_status: TextView? = null
    var txtv_status: TextView? = null

    var token: String? = null
    var submode: String?=null
    var cusid: kotlin.String? = null
    var loantype: kotlin.String? = null
    var status_spinner: Spinner? = null
    var status = arrayOfNulls<String>(0)
    private var jresult: JSONArray? = null
    var reqSubMode = "0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(
                R.layout.fragment_fundtransferhistory, container,
                false)

        rv_otherfund = v!!.findViewById(R.id.rv_otherfund)
        status_spinner = v!!.findViewById(R.id.status_spinner)
        tv_status = v!!.findViewById(R.id.tv_status)

        txtv_status= v!!.findViewById(R.id.txtv_status)
        val statsSP = context!!.getSharedPreferences(Config.SHARED_PREF340, 0)
        var stats =statsSP.getString("Status1", null)
        txtv_status!!.setText(stats)

        status_spinner!!.setOnItemSelectedListener(this)

        status = arrayOf<String?>("All", "SUCCESS", "WAITING", "RETURNED", "FAILED")

        val activity: OtherfundTransferHistory? = activity as OtherfundTransferHistory?
       submode = activity!!.sendData()
        getStatus()


        return v
    }

    private fun getStatus() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(context!!, android.R.layout.simple_spinner_item, status)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        status_spinner!!.adapter = aa
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        // Toast.makeText(getApplicationContext(),status[position] , Toast.LENGTH_LONG).show();
        //  String mySpinner = (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),mySpinner , Toast.LENGTH_LONG).show();
        val item_position = position.toString()
        val positonInt = Integer.valueOf(item_position)
        //  Toast.makeText(OrdersActivity.this, "value is "+ positonInt, Toast.LENGTH_SHORT).show();
        //  Toast.makeText(OrdersActivity.this, "value is "+ positonInt, Toast.LENGTH_SHORT).show();
        if (positonInt == 0) {
            reqSubMode = "0"
        } else if (positonInt == 1) {
            reqSubMode = "1"
        } else if (positonInt == 2) {
            reqSubMode = "2"
        } else if (positonInt == 3) {
            reqSubMode = "3"
        } else if (positonInt == 4) {
            reqSubMode = "4"
        }
        showOtherfundhistory(reqSubMode)
    }

    private fun showOtherfundhistory(reqSubMode: String) {

        val baseurlSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(activity!!)) {
            true -> {
                progressDialog = ProgressDialog(activity, R.style.Progress)
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
                        .sslSocketFactory(Config.getSSLSocketFactory(activity!!), trustManager)
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

                        val FK_CustomerSP = activity!!.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = activity!!.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)


                        val BankKeySP = activity!!.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = activity!!.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("32"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "SubMode",
                                MscoreApplication.encryptStart(submode)
                        )
                        requestObject1.put(
                                "Status",
                                MscoreApplication.encryptStart("1")
                        )
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val currentDateandTime = sdf.format(Date())
                        requestObject1.put(
                                "TrnsDate",
                                MscoreApplication.encryptStart(currentDateandTime)
                        )
                        requestObject1.put(
                                "TransType",
                                MscoreApplication.encryptStart("1")
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))


                        Log.e("TAG", "requestObject1  status   " + requestObject1+"\n"+"Submode : "+submode)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(context!!,
                            activity!!,"Alert","Some technical issues.",1);

                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        requestObject1.toString()
                    )
                    val call = apiService.getFundTransferStatus(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-fundtransfer", response.body().toString())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("OwnFundTransferHistory")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    jresult = jsonobj2.getJSONArray("OwnFundTransferHistoryList")
                                    if (jresult!!.length() != 0) {

                                        val lLayout =
                                                GridLayoutManager(activity, 1)
                                        rv_otherfund!!.layoutManager = lLayout
                                        rv_otherfund!!.setHasFixedSize(true)
                                        val adapter = ProductSummaryAdapter(activity!!, jresult!!)
                                        rv_otherfund!!.adapter = adapter
                                    } else {
                                        rv_otherfund!!.visibility = View.GONE

                                    }


                                } else {
                                    AlertMessage().alertMessage(context!!,activity!!,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(context!!,
                                    activity!!,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(context!!,
                                activity!!,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(context!!,
                        activity!!,"Alert","Some technical issues.",1);
                    e.printStackTrace()
                }
            }
            false -> {
                AlertMessage().alertMessage(context!!,activity!!,"Alert"," No Internet Connection. ",3);
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}