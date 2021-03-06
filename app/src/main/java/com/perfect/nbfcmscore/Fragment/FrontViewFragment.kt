package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
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

class FrontViewFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "FrontViewFragment"
    var cusid: String? = null
    var token: String? = null
    var vritualcardCombination: String? = null
    var txt_custname: TextView? = null
    var txt_custid: TextView? = null
    var tv_vritualcard: TextView? = null

    var txtv_purpose: TextView? = null
    var txtv_points1: TextView? = null
    var txtv_points2: TextView? = null
    var txtv_points3: TextView? = null
    var txtv_points4: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(
            R.layout.fragment_front_view, container,
            false
        )

        txt_custname = v.findViewById<View>(R.id.txt_custname) as TextView?
        txt_custid = v.findViewById<View>(R.id.txt_custid) as TextView?
        tv_vritualcard = v.findViewById<View>(R.id.tv_vritualcard) as TextView?

        txtv_purpose = v.findViewById<View>(R.id.txtv_purpose) as TextView?
        txtv_points1 = v.findViewById<View>(R.id.txtv_points1) as TextView?
        txtv_points2 = v.findViewById<View>(R.id.txtv_points2) as TextView?
        txtv_points3 = v.findViewById<View>(R.id.txtv_points3) as TextView?
        txtv_points4 = v.findViewById<View>(R.id.txtv_points4) as TextView?

        txtv_purpose!!.setText(R.string.purpose)
        txtv_purpose!!.visibility = View.VISIBLE
        txtv_points1!!.visibility = View.VISIBLE
        txtv_points2!!.visibility = View.VISIBLE
        txtv_points3!!.visibility = View.VISIBLE
        txtv_points4!!.visibility = View.VISIBLE

        val FK_CustomerSP = activity!!.getSharedPreferences(Config.SHARED_PREF1, 0)
        cusid = FK_CustomerSP.getString("FK_Customer", null)

        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
        val token = TokenSP.getString("Token", null)

        Log.e(TAG,"Start  44   ")
        val CustomerNameSP = activity!!.getSharedPreferences(Config.SHARED_PREF3,0)
        txt_custname!!.text = CustomerNameSP.getString("CustomerName", null)

        val CustomerNumberSP = activity!!.getSharedPreferences(Config.SHARED_PREF19,0)
        txt_custid!!.text = CustomerNumberSP.getString("CustomerNumber",null)

        getVritualcardCombination()

        return v

    }

    private fun getVritualcardCombination() {

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
                        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("15"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(cusid))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  434   "+requestObject1)

                    } catch (e: Exception) {
                        Log.e(TAG,"Some  2161   "+e.toString())
                        progressDialog!!.dismiss()
                        e.printStackTrace()
//                        val mySnackbar = Snackbar.make(
//                            findViewById(R.id.rl_main),
//                            " Some technical issues.", Snackbar.LENGTH_SHORT
//                        )
//                        mySnackbar.show()
                    }
                    val body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestObject1.toString()
                    )
                    val call = apiService.getBardCodeData(body)
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
                                    val jobj = jObject.getJSONObject("BarcodeFormatDet")
                                    vritualcardCombination = jobj.getString("BarcodeFormat")
                                    tv_vritualcard!!.text = vritualcardCombination


                                } else {
//                                    val builder = AlertDialog.Builder(
//                                        activity,
//                                        R.style.MyDialogTheme
//                                    )
//                                    builder.setMessage("" + jObject.getString("EXMessage"))
//                                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                    }
//                                    val alertDialog: AlertDialog = builder.create()
//                                    alertDialog.setCancelable(false)
//                                    alertDialog.show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                Log.e(TAG,"Some  2162   "+e.toString())
//                                val builder = AlertDialog.Builder(
//                                    this@RechargeActivity,
//                                    R.style.MyDialogTheme
//                                )
//                                builder.setMessage("Some technical issues.")
//                                builder.setPositiveButton("Ok") { dialogInterface, which ->
//                                }
//                                val alertDialog: AlertDialog = builder.create()
//                                alertDialog.setCancelable(false)
//                                alertDialog.show()
//                                e.printStackTrace()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()
                            Log.e(TAG,"Some  2163   "+t.message)
//                            val builder = AlertDialog.Builder(
//                                this@RechargeActivity,
//                                R.style.MyDialogTheme
//                            )
//                            builder.setMessage("Some technical issues.")
//                            builder.setPositiveButton("Ok") { dialogInterface, which ->
//                            }
//                            val alertDialog: AlertDialog = builder.create()
//                            alertDialog.setCancelable(false)
//                            alertDialog.show()
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    Log.e(TAG,"Some  2165   "+e.toString())
//                    val builder = AlertDialog.Builder(this@RechargeActivity, R.style.MyDialogTheme)
//                    builder.setMessage("Some technical issues.")
//                    builder.setPositiveButton("Ok") { dialogInterface, which ->
//                    }
//                    val alertDialog: AlertDialog = builder.create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.show()
//                    e.printStackTrace()
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


}