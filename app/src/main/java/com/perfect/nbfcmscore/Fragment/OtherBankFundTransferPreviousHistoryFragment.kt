package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
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


class OtherBankFundTransferPreviousHistoryFragment : Fragment() , OnItemSelectedListener,OnClickListener{

    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private var progressDialog: ProgressDialog? = null
    var rv_otherfund: RecyclerView? = null
    var tv_status: TextView? = null
    var token: String? = null
    var cusid:kotlin.String? = null
    var loantype:kotlin.String? = null
    var status_spinner: Spinner? = null
    var week_spinner:Spinner? = null
    var status = arrayOfNulls<String>(0)
    var weeks = arrayOfNulls<String>(0)
    private var jresult: JSONArray? = null
    var reqSubMode = "0"
    var SelectedWeek:kotlin.String? = "2"
    var etFromdate: EditText? = null
    var check = 0
    var submode: String?=null
    var txtv_status: TextView? = null
    var txtv_weeks: TextView? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(
                R.layout.fragment_fundtransfer_previoushistory, container,
                false
        )

        rv_otherfund = v!!.findViewById(R.id.rv_otherfund)
        status_spinner = v!!.findViewById(R.id.status_spinner)
        week_spinner = v!!.findViewById(R.id.week_spinner)
        tv_status = v!!.findViewById(R.id.tv_status)

        txtv_status = v!!.findViewById(R.id.txtv_status)
        txtv_weeks = v!!.findViewById(R.id.txtv_weeks)


        val statsSP = context!!.getSharedPreferences(Config.SHARED_PREF340, 0)
        var stats =statsSP.getString("Status1", null)
        txtv_status!!.setText(stats)

        val weeksSP = context!!.getSharedPreferences(Config.SHARED_PREF341, 0)
        var wks =weeksSP.getString("Weeks", null)
        txtv_weeks!!.setText(wks)



        etFromdate = v!!.findViewById(R.id.etFromdate)
        etFromdate!!.setKeyListener(null)

        etFromdate!!.setOnClickListener(this)

        status_spinner!!.setOnItemSelectedListener(this)

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        etFromdate!!.setText(currentDateandTime)
        status = arrayOf("All", "SUCCESS", "WAITING", "RETURNED", "FAILED")
        weeks = arrayOf<String?>("2", "4", "6", "8", "10")!!
        getStatus()
        getWeek()
        val activity: OtherfundTransferHistory? = activity as OtherfundTransferHistory?
        submode = activity!!.sendData()
        return v

    }

    private fun getWeek() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(context!!, android.R.layout.simple_spinner_item, weeks)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        week_spinner!!.adapter = aa

        week_spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                if (check > 0) {
                    val item = parent.getItemAtPosition(pos)
                    val item_position = pos.toString()
                    val positonInt = Integer.valueOf(item_position)
                    if (positonInt == 0) {
                        SelectedWeek = "2"
                    } else if (positonInt == 1) {
                        SelectedWeek = "4"
                    } else if (positonInt == 2) {
                        SelectedWeek = "6"
                    } else if (positonInt == 3) {
                        SelectedWeek = "8"
                    } else if (positonInt == 4) {
                        SelectedWeek = "10"
                    }
                    showOtherfundhistory(reqSubMode)
                }
                check = check + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

                        val FK_CustomerSP =activity!!.getSharedPreferences(
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
                        requestObject1.put(
                                "TrnsDate",
                                MscoreApplication.encryptStart(SelectedWeek)
                        )
                        requestObject1.put(
                                "TransType",
                                MscoreApplication.encryptStart("1")
                        )
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))


                        Log.e("TAG", "requestObject1  status   " + requestObject1+"\n"+"Submode"+submode)
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

    private fun getStatus() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(context!!, android.R.layout.simple_spinner_item, status)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        status_spinner!!.adapter = aa
    }




    override fun onClick(v: View) {
        when (v.id) {
            R.id.etFromdate ->
                dateSelectorfrom()
        }
    }

    private fun dateSelectorfrom() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(context!!,
                { view, year, monthOfYear, dayOfMonth ->
                    etFromdate!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    showOtherfundhistory(reqSubMode)
                }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        val minDate = Calendar.getInstance()
        minDate[Calendar.DAY_OF_MONTH] = mDay - 365
        minDate[Calendar.MONTH] = mMonth
        minDate[Calendar.YEAR] = mYear
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item_position: String = position.toString()
        val positonInt = Integer.valueOf(item_position)
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

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}