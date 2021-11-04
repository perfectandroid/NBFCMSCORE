package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Activity.OtherfundTransferHistory
import com.perfect.nbfcmscore.Adapter.ProductSummaryAdapter
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
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
import java.text.SimpleDateFormat
import java.util.*

class OtherBankFundTransferHistoryFragment:Fragment(), AdapterView.OnItemSelectedListener {

    private val mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var progressDialog: ProgressDialog? = null
    var rv_otherfund: RecyclerView? = null
    var tv_status: TextView? = null
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

        when(ConnectivityUtils.isConnected(activity!!)) {
            true -> {
                progressDialog = ProgressDialog(activity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(activity!!))
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
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  status   " + requestObject1+"\n"+"Submode : "+submode)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        Snackbar.make(
                                requireActivity().findViewById(R.id.rl_main),
                                ("Some technical issues."),
                                Snackbar.LENGTH_LONG
                        ).show()

                    }
                    val body = RequestBody.create(
                            okhttp3.MediaType.parse("application/json; charset=utf-8"),
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
                                Log.i("Response-fundtransfer", response.body())
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
                                    val builder = AlertDialog.Builder(
                                            activity,
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
                                        activity,
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
                                    activity,
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
                    val builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}