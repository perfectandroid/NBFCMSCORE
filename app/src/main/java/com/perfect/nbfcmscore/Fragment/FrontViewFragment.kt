package com.perfect.nbfcmscore.Fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
    var txtv_header: TextView? = null
    var txtv_cusid: TextView? = null
    var txtv_cusname: TextView? = null
    var txtv_elctrnc: TextView? = null

    var ll_points1: LinearLayout? = null
    var ll_points2: LinearLayout? = null
    var ll_points3: LinearLayout? = null
    var ll_points4: LinearLayout? = null

    var img_applogo: ImageView? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(
            R.layout.fragment_front_view, container,
            false
        )

        txt_custname = v.findViewById<View>(R.id.txt_custname) as TextView?
        txt_custid = v.findViewById<View>(R.id.txt_custid) as TextView?
        tv_vritualcard = v.findViewById<View>(R.id.tv_vritualcard) as TextView?
        txtv_header = v.findViewById<View>(R.id.txtv_header) as TextView?

        txtv_cusid = v.findViewById<View>(R.id.txtv_cusid) as TextView?
        txtv_cusname = v.findViewById<View>(R.id.txtv_cusname) as TextView?
        txtv_elctrnc = v.findViewById<View>(R.id.txtv_elctrnc) as TextView?

        ll_points1 = v.findViewById<View>(R.id.ll_points1) as LinearLayout?
        ll_points2 = v.findViewById<View>(R.id.ll_points2) as LinearLayout?
        ll_points3 = v.findViewById<View>(R.id.ll_points3) as LinearLayout?
        ll_points4 = v.findViewById<View>(R.id.ll_points4) as LinearLayout?


        val FK_CustomernameSP = activity!!.getSharedPreferences(Config.SHARED_PREF268, 0)
        txtv_cusname!!.setText(FK_CustomernameSP.getString("Customer Name",null))

        val FK_CusidSP = activity!!.getSharedPreferences(Config.SHARED_PREF269, 0)
        txtv_cusid!!.setText(FK_CusidSP.getString("Customer Id",null))

        val FK_electrncSP = activity!!.getSharedPreferences(Config.SHARED_PREF270, 0)
        txtv_elctrnc!!.setText(FK_electrncSP.getString("Electronicuseonly",null))



        txtv_purpose = v.findViewById<View>(R.id.txtv_purpose) as TextView?

        txtv_points1 = v.findViewById<View>(R.id.txtv_points1) as TextView?
        txtv_points2 = v.findViewById<View>(R.id.txtv_points2) as TextView?
        txtv_points3 = v.findViewById<View>(R.id.txtv_points3) as TextView?
        txtv_points4 = v.findViewById<View>(R.id.txtv_points4) as TextView?

        img_applogo = v.findViewById<View>(R.id.img_applogo) as ImageView?

      //  txtv_purpose!!.setText(R.string.purpose)
        txtv_purpose!!.visibility = View.VISIBLE
        txtv_points1!!.visibility = View.VISIBLE
        txtv_points2!!.visibility = View.VISIBLE
        txtv_points3!!.visibility = View.VISIBLE
        txtv_points4!!.visibility = View.VISIBLE

        ll_points1!!.visibility = View.VISIBLE
        ll_points2!!.visibility = View.VISIBLE
        ll_points3!!.visibility = View.VISIBLE
        ll_points4!!.visibility = View.VISIBLE


        val ID_Virtual = activity!!.getSharedPreferences(Config.SHARED_PREF74,0)
        val ID_Purpose = activity!!.getSharedPreferences(Config.SHARED_PREF105,0)
        txtv_header!!.setText(ID_Virtual.getString("VirtualCard",null))
        txtv_purpose!!.setText(ID_Purpose.getString("PurposeofVirtualCard",null))


        val FK_p1SP = activity!!.getSharedPreferences(Config.SHARED_PREF271, 0)
        txtv_points1!!.setText(FK_p1SP.getString("Streamlinetransactions",null))

        val FK_p2SP = activity!!.getSharedPreferences(Config.SHARED_PREF272, 0)
        txtv_points2!!.setText(FK_p2SP.getString("Enableasinglepointofcontactforcreditanddebit",null))

        val FK_p3SP = activity!!.getSharedPreferences(Config.SHARED_PREF273, 0)
        txtv_points3!!.setText(FK_p3SP.getString("Strengthenyourloanportfolio",null))

        val FK_p4SP = activity!!.getSharedPreferences(Config.SHARED_PREF274, 0)
        txtv_points4!!.setText(FK_p4SP.getString("Eliminatethelongqueues",null))

        val FK_CustomerSP = activity!!.getSharedPreferences(Config.SHARED_PREF1, 0)
        cusid = FK_CustomerSP.getString("FK_Customer", null)

        val TokenSP = activity!!.getSharedPreferences(Config.SHARED_PREF8, 0)
        val token = TokenSP.getString("Token", null)

        Log.e(TAG,"Start  44   ")
        val CustomerNameSP = activity!!.getSharedPreferences(Config.SHARED_PREF3,0)
        txt_custname!!.text = CustomerNameSP.getString("CustomerName", null)

        val CustomerNumberSP = activity!!.getSharedPreferences(Config.SHARED_PREF19,0)
        txt_custid!!.text = CustomerNumberSP.getString("CustomerNumber1",null)

        try {
            val ImageURLSP = context!!.applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
            val IMAGE_URL = ImageURLSP.getString("ImageURL", null)
            val AppIconImageCodeSP = activity!!.getSharedPreferences(Config.SHARED_PREF14, 0)
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            PicassoTrustAll.getInstance(activity!!)!!.load(imagepath).error(android.R.color.transparent).into(img_applogo!!)
        }catch (e : java.lang.Exception){

        }

        val currentTime = Calendar.getInstance().time
        Log.e(TAG,"currentTime  "+currentTime)
        val date: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val localTime = date.format(currentTime)

        Log.e(TAG,"localTime   110    "+localTime)
        val dateParts = localTime.split("-").toTypedArray()
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val yr = dateParts[2].toInt()

        Log.e(TAG,"Monthndat  1101    "+month+"   "+day)

        tv_vritualcard!!.text = day.toString()+CustomerNumberSP.getString("CustomerNumber",null)+month.toString()

      //  getVritualcardCombination()

        return v

    }

    private fun getVritualcardCombination() {

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
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(activity!!))
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