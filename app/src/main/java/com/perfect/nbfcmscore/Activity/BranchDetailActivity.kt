package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.bizcorelite.Api.ApiInterface
import com.perfect.nbfcmscore.Adapter.BranchListAdapter
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


class BranchDetailActivity : AppCompatActivity() , OnMapReadyCallback , View.OnClickListener,
    ItemClickListener {

    private var progressDialog: ProgressDialog? = null
    var mapFragment: SupportMapFragment? = null
    var tv_branch: TextView? = null
    var tv_bankdetails: TextView? = null
    var strBranches ="";
    var mMap : GoogleMap?=null
    val TAG: String = "BranchDetailActivity"
    var jsonArray: JSONArray? = null
    var rvBranchList: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch_detail)



        setInitialise()
        setRegister()

        getBranchList();



    }

    private fun getBranchList() {

     //   strBranches =getString(R.string.strBranches)
        Log.e("TAG","strBranches  78  "+strBranches)


        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)




        mapFragment!!.getMapAsync { googleMap ->
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(LatLng(37.4233438, -122.0728817))
//                    .title("LinkedIn")
////                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            )
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(LatLng(37.4629101, -122.2449094))
//                    .title("Facebook")
//                    .snippet("Facebook HQ: Menlo Park")
//            )
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(LatLng(37.3092293, -122.1136845))
//                    .title("Apple")
//            )
//            googleMap.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    LatLng(
//                        11.2406,
//                        75.7909
//                    ), 10f
//                )
//            )
        }
    }

    private fun setRegister() {
        tv_branch!!.setOnClickListener(this)
        tv_bankdetails!!.setOnClickListener(this)
    }

    private fun setInitialise() {
        tv_branch = findViewById<TextView>(R.id.tv_branch)
        tv_bankdetails = findViewById<TextView>(R.id.tv_bankdetails)
        rvBranchList = findViewById<RecyclerView>(R.id.rvBranchList)
    }

    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap = googleMap
        getBranchList1(mMap!!);
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(11.2406, 75.7909))
//                .title("LinkedIn")
//               // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//        )
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(37.4629101, -122.2449094))
//                .title("Facebook")
//                .snippet("Facebook HQ: Menlo Park")
//        )
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(37.3092293, -122.1136845))
//                .title("Apple")
//        )
//        googleMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(11.2406, 75.7909),
//                10f
//            )
//        )
    }

    private fun getBranchList1(mMap: GoogleMap) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@BranchDetailActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@BranchDetailActivity))
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
                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("8"))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  171   "+requestObject1)
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
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestObject1.toString()
                    )
                    val call = apiService.getBankBranchDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    Log.e(TAG,"response  195   "+response.body())

                                    val jobjt = jObject.getJSONObject("BankBranchDetails")
                                    jsonArray = jobjt.getJSONArray("BankBranchDetailsListInfo")

                                    Log.e(TAG,"jarray   200  "+jsonArray)

                                    val obj_adapter = BranchListAdapter(applicationContext!!, jsonArray!!)
                                    rvBranchList!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                                    rvBranchList!!.adapter = obj_adapter
                                    obj_adapter.setClickListener(this@BranchDetailActivity)

                                    setMarker(jsonArray!!,mMap)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@BranchDetailActivity,
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
                                    this@BranchDetailActivity,
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
                                this@BranchDetailActivity,
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
                    val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }

    private fun setMarker(jsonArray: JSONArray, mMap: GoogleMap) {

        for (i in 0 until jsonArray.length()) {
            var jsonObject = jsonArray.getJSONObject(i)

            Log.e(TAG,"jsonObject  286   "+jsonObject)
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(jsonObject.getDouble("LocationLatitude"), jsonObject.getDouble("LocationLongitude")))
                    .title(""+jsonObject.getString("Address"))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    11.2406,
//                    75.7909
//                ), 10f
//            )
//        )


        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
            try {
                Log.e("Exception", " Occured 1   "+marker)

            } catch (e: Exception) {
                Log.e("Exception", " Occured    "+e.toString())
            }
        })


    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_branch ->{
                tv_branch!!.setBackgroundResource(R.drawable.bottom_line);
                tv_bankdetails!!.setBackgroundResource(R.drawable.bg_white);
            }

            R.id.tv_bankdetails ->{
                tv_branch!!.setBackgroundResource(R.drawable.bg_white);
                tv_bankdetails!!.setBackgroundResource(R.drawable.bottom_line);
            }
        }
    }

    override fun onClick(position: Int) {

        Log.e(TAG,"position  331  "+position)
        var jsonObject1 = jsonArray!!.getJSONObject(position)
        setMarker1(jsonObject1,mMap)
    }

    private fun setMarker1(jsonObject1: JSONObject, mMap: GoogleMap?) {

        mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(jsonObject1.getDouble("LocationLatitude"), jsonObject1.getDouble("LocationLongitude")))
                .title(""+jsonObject1.getString("Address"))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    11.2406,
                    75.7909
                ), 10f
            )
        )
    }
}