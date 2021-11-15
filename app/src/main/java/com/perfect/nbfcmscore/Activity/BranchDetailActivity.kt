package com.perfect.nbfcmscore.Activity

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Adapter.BranchListAdapter
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
import java.util.*


class BranchDetailActivity : AppCompatActivity() , OnMapReadyCallback , View.OnClickListener,
    ItemClickListener, LocationListener, ConnectionCallbacks,
    OnConnectionFailedListener {
//    , GoogleMap.OnMarkerClickListener
    private var progressDialog: ProgressDialog? = null
    var mapFragment: SupportMapFragment? = null
    var tv_branch: TextView? = null
    var tv_bankdetails: TextView? = null
    var tv_header: TextView? = null
    var strBranches ="";
    var mMap : GoogleMap?=null
    val TAG: String = "BranchDetailActivity"
    var LandPhoneNumber: String? =""
    var ContactPersonMobile: String? =""
    var BranchPhoneNumber: String? =""
    var jsonArray: JSONArray? = null
    var jsonArrayDist: JSONArray? = null
    var rvBranchList: RecyclerView? = null
    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null

    var act_district: AutoCompleteTextView? = null
    var card_branches: CardView? = null
    var ll_branches: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch_detail)


        setInitialise()
        setRegister()
        getBranchList();
        getDistricts()

    }

    private fun getDistricts() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
//                progressDialog = ProgressDialog(this@BranchDetailActivity, R.style.Progress)
//                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
//                progressDialog!!.setCancelable(false)
//                progressDialog!!.setIndeterminate(true)
//                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
//                progressDialog!!.show()
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

                        val TokenSP = applicationContext.getSharedPreferences(Config.SHARED_PREF8, 0)
                        val Token = TokenSP.getString("Token", null)
                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("19"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(getResources().getString(R.string.BankKey)))
                        // requestObject1.put("BankHeader", MscoreApplication.encryptStart(getResources().getString(R.string.BankHeader)))

                        Log.e(TAG,"requestObject1  171   "+requestObject1)
                    } catch (e: Exception) {
                        // progressDialog!!.dismiss()
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
                    val call = apiService.getDistrictDetails(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                            call: retrofit2.Call<String>, response:
                            Response<String>
                        ) {
                            try {
                                //  progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    Log.e(TAG,"response  153   "+response.body())



                                    val jobjt = jObject.getJSONObject("DistrictDetails")
                                    jsonArrayDist = jobjt.getJSONArray("DistrictDetailsList")

                                    val distnames: ArrayList<String> = ArrayList()
                                    for (i in 0 until jsonArrayDist!!.length()) {
                                        val obj: JSONObject = jsonArrayDist!!.getJSONObject(i)
                                        distnames.add(obj.getString("DistrictName"));
                                    }

                                    val adapter = ArrayAdapter(this@BranchDetailActivity,
                                        android.R.layout.simple_list_item_1, distnames)
                                    act_district!!.setAdapter(adapter)
//                                    act_district!!.showDropDown()
                                    act_district!!.threshold =1

                                    act_district!!.addTextChangedListener(object : TextWatcher {
                                        override fun onTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            before: Int,
                                            count: Int) {
                                            mMap!!.clear()
                                            rvBranchList!!.adapter = null
                                            card_branches!!.visibility = View.GONE
                                            ll_branches!!.visibility = View.GONE

                                          //  val str: String? = "Hello"



                                            for (i in 0 until jsonArrayDist!!.length()) {
                                                val obj1: JSONObject = jsonArrayDist!!.getJSONObject(i)
                                                var FK_District: String = ""
                                                if (obj1.getString("DistrictName")==(""+ act_district!!.text.toString())){

                                                    FK_District = obj1.getString("FK_District")
                                                    Log.e("DistrictName","DistrictName  192   "+obj1.getString("DistrictName")+"  "+FK_District)
                                                    getBranchList1(mMap!!,FK_District)
                                                }

                                                //
                                            }

                                        }

                                        override fun beforeTextChanged(
                                            s: CharSequence,
                                            start: Int,
                                            count: Int,
                                            after: Int
                                        ) {
                                        }

                                        override fun afterTextChanged(s: Editable) {}
                                    })
//
//                                    Log.e(TAG,"jarray   200  "+jsonArray)

//                                    val obj_adapter = BranchListAdapter(applicationContext!!, jsonArray!!)
//                                    rvBranchList!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
//                                    rvBranchList!!.adapter = obj_adapter
//                                    obj_adapter.setClickListener(this@BranchDetailActivity)


                                } else {
//                                    val builder = AlertDialog.Builder(
//                                        this@BranchDetailActivity,
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
                                // progressDialog!!.dismiss()

//                                val builder = AlertDialog.Builder(
//                                    this@BranchDetailActivity,
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
                            //  progressDialog!!.dismiss()

//                            val builder = AlertDialog.Builder(
//                                this@BranchDetailActivity,
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
                    //   progressDialog!!.dismiss()
//                    val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
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
//                val builder = AlertDialog.Builder(this@BranchDetailActivity, R.style.MyDialogTheme)
//                builder.setMessage("No Internet Connection.")
//                builder.setPositiveButton("Ok") { dialogInterface, which ->
//                }
//                val alertDialog: AlertDialog = builder.create()
//                alertDialog.setCancelable(false)
//                alertDialog.show()
            }
        }
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
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
    }

    private fun setInitialise() {
        tv_branch = findViewById<TextView>(R.id.tv_branch)
        tv_bankdetails = findViewById<TextView>(R.id.tv_bankdetails)
        rvBranchList = findViewById<RecyclerView>(R.id.rvBranchList)

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        act_district = findViewById<AutoCompleteTextView>(R.id.act_district)
        tv_header= findViewById<AutoCompleteTextView>(R.id.tv_header)
        card_branches= findViewById<CardView>(R.id.card_branches)
        ll_branches= findViewById<LinearLayout>(R.id.ll_branches)

        val EnterDistSP = applicationContext.getSharedPreferences(Config.SHARED_PREF108, 0)
        val BranchdetailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF75, 0)

     //   act_district!!.setText(EnterDistSP.getString("EnterDistrict", null))
        tv_header!!.setText(BranchdetailSP.getString("BranchDetails", null))
    }

    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap = googleMap
        getBranchList1(mMap!!,"0");
       // mMap!!.setOnMarkerClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap!!.setMyLocationEnabled(true)
        }
       // mMap!!.getUiSettings().setMapToolbarEnabled(false);
//        mMap!!.uiSettings.isMapToolbarEnabled = true
//        mMap!!.uiSettings.isZoomControlsEnabled = true

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

    private fun getBranchList1(mMap: GoogleMap,FK_District: String) {
        closeKeyBoard()
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(Config.SHARED_PREF1, 0)
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        requestObject1.put("Reqmode", MscoreApplication.encryptStart("8"))
                        requestObject1.put("FK_District", MscoreApplication.encryptStart(FK_District))
                        requestObject1.put("FK_Customer", MscoreApplication.encryptStart(FK_Customer))
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
                                Log.e(TAG,"response  195   "+response.body())
                                val jObject = JSONObject(response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    card_branches!!.visibility = View.VISIBLE
                                    ll_branches!!.visibility = View.VISIBLE


                                    val jobjt = jObject.getJSONObject("BankBranchDetails")
                                    jsonArray = jobjt.getJSONArray("BankBranchDetailsListInfo")

                                    Log.e(TAG,"jarray   200  "+jsonArray)

                                    val obj_adapter = BranchListAdapter(applicationContext!!, jsonArray!!)
                                    rvBranchList!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                                    rvBranchList!!.adapter = obj_adapter
                                    obj_adapter.setClickListener(this@BranchDetailActivity)

                                    setMarker(jsonArray!!,mMap)

                                } else {
                                    card_branches!!.visibility = View.GONE
                                    ll_branches!!.visibility = View.GONE
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
                                card_branches!!.visibility = View.GONE
                                ll_branches!!.visibility = View.GONE

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
                            card_branches!!.visibility = View.GONE
                            ll_branches!!.visibility = View.GONE

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
                    card_branches!!.visibility = View.GONE
                    ll_branches!!.visibility = View.GONE
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
                card_branches!!.visibility = View.GONE
                ll_branches!!.visibility = View.GONE
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

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setMarker(jsonArray: JSONArray, mMap: GoogleMap) {
        mMap.clear()
        for (i in 0 until jsonArray.length()) {
            var jsonObject = jsonArray.getJSONObject(i)

            Log.e(TAG,"jsonObject  286   "+jsonObject)

            val id = jsonObject.getString("ID_Branch")
            val bank = jsonObject.getString("BankName")
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(jsonObject.getDouble("LocationLatitude"), jsonObject.getDouble("LocationLongitude")))
                    .title(id + ") " + bank)
//                    .title(jsonObject.toString())
//                    .title(jsonObject.getString("BranchName")+","+jsonObject.getString("BankName")+""+jsonObject.getString("Address"))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )

            mMap!!.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker1 ->
                val title1 = marker1.title
                val st = StringTokenizer(title1, ") ")
//                val separate1 = str.split(" ")[0]
//                val namesList: Array<String> = title1.split("\\s".toRegex())[0]
//                val id = title1.split("\\s".toRegex())[0]
//                val id = namesList[0]
//                val bank = namesList[1]
//                //  Toast.makeText(getActivity(), "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
//                showBranchDetails(id)
                // return false;
                val ids = st.nextToken()
                val banks = st.nextToken()

                Log.e(TAG,"title  3691   "+title+"  "+ids+"   "+banks)
//                var obj = JSONObject(title)
//                popupBankDetails(obj)
                popupBankDetails(ids)

            })
            mMap!!.setOnMarkerClickListener(OnMarkerClickListener { marker1 ->
                val title1 = marker1.title
//                val namesList: Array<String> = title.split("\\)").toTypedArray()
//                val id = namesList[0]
//                val bank = namesList[1]
//                // Toast.makeText(MapsActivity.this, "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
//                showBranchDetails(id)

                val st = StringTokenizer(title1, ") ")

                val ids = st.nextToken()
                val banks = st.nextToken()

                Log.e(TAG,"title  3691   "+title+"  "+ids+"   "+banks)

//                Log.e(TAG,"title  3692   "+title)
//                var obj = JSONObject(title)
                popupBankDetails(ids)
                false
            })
        }
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    11.2406,
//                    75.7909
//                ), 10f
//            )
//        )


//        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
//            try {
//                Log.e("Exception", " Occured 1   "+marker)
//
//            } catch (e: Exception) {
//                Log.e("Exception", " Occured    "+e.toString())
//            }
//        })

//        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener { marker ->
//            val latLon = marker.position
//
//            //Cycle through places array
////            for (place in places) {
////                if (latLon == place.latlng) {
////                    //match found!  Do something....
////                }
////            }
//        })



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

            R.id.im_back ->{
                onBackPressed()
            }

            R.id.im_home ->{
                startActivity(Intent(this@BranchDetailActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onClick(position: Int,data: String) {
        Log.e(TAG,"position  331  "+position+"  "+jsonArray)
        if (data.equals("map")){

            var jsonObject1 = jsonArray!!.getJSONObject(position)
            setMarker1(jsonObject1,mMap)
//            val ids = jsonObject1.getString("ID_Branch")
//            popupBankDetails(ids)
        }

        if (data.equals("call")){
            Log.e(TAG,"position  Call  331  "+position)
            var jsonObject1 = jsonArray!!.getJSONObject(position)
            LandPhoneNumber = jsonObject1.getString("LandPhoneNumber")
            ContactPersonMobile = jsonObject1.getString("ContactPersonMobile")
            if (!LandPhoneNumber!!.equals("")){
             //   LandPhoneNumber = "8075283549"
                 BranchPhoneNumber = LandPhoneNumber
                checkPermissions()
            }
            else if (!ContactPersonMobile!!.equals("")){
                BranchPhoneNumber = ContactPersonMobile
                checkPermissions()
            }

        }
        if (data.equals("branch")){
            var jsonObject1 = jsonArray!!.getJSONObject(position)
            val ids = jsonObject1.getString("ID_Branch")
            popupBankDetails(ids)
        }

    }

    private fun setMarker1(jsonObject1: JSONObject, mMap: GoogleMap?) {
        val id = jsonObject1.getString("ID_Branch")
        val bank = jsonObject1.getString("BankName")
        mMap!!.clear()
        mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(jsonObject1.getDouble("LocationLatitude"), jsonObject1.getDouble("LocationLongitude")))
                .title(id + ") " + bank)
//                .title(""+jsonObject1.getString("Address"))
//                .title(jsonObject1.toString())
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    jsonObject1.getDouble("LocationLatitude"),
                    jsonObject1.getDouble("LocationLongitude")
                ), 10f
            )
        )

//        val ids = jsonObject1.getString("ID_Branch")
//        popupBankDetails(ids)

    }

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            }
            else {
                Log.e(TAG,"permission denied  417")
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + BranchPhoneNumber))
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun popupBankDetails(ids: String) {

        Log.e(TAG,"name    4642    "+ids)

//        mMap!!.getUiSettings().setMapToolbarEnabled(true);
        mMap!!.uiSettings.isMapToolbarEnabled = true
        mMap!!.uiSettings.isZoomControlsEnabled = true


        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_bank_details)
        val tva_branch = dialog.findViewById(R.id.tva_branch) as TextView
        val tva_bank = dialog.findViewById(R.id.tva_bank) as TextView
        val tva_address = dialog.findViewById(R.id.tva_address) as TextView
        val tva_place = dialog.findViewById(R.id.tva_place) as TextView
        val tva_post = dialog.findViewById(R.id.tva_post) as TextView
        val tva_district = dialog.findViewById(R.id.tva_district) as TextView



        for (i in 0 until jsonArray!!.length()) {
            var obj = jsonArray!!.getJSONObject(i)
            if (obj.getString("ID_Branch").equals(ids)){

                tva_branch.text = obj.getString("BranchName")
                tva_bank.text = obj.getString("BankName")
                tva_address.text = obj.getString("Address")
                tva_place.text = obj.getString("Place")
                tva_post.text = obj.getString("Post")
                tva_district.text = obj.getString("District")
            }
        }


//
//        tva_branch.text = obj.getString("BranchName")
//        tva_bank.text = obj.getString("BankName")
//        tva_address.text = obj.getString("Address")
//        tva_place.text = obj.getString("Place")
//        tva_post.text = obj.getString("Post")
//        tva_district.text = obj.getString("District")


        dialog.show()

    }

//    override fun onMarkerClick(marker: Marker): Boolean {
//        val name = marker.title
//
//        Log.e(TAG,"name    4641    "+name)
//        var obj = JSONObject(marker.title)
//        Log.e(TAG,"name    4642    "+obj)
//
////        mMap!!.getUiSettings().setMapToolbarEnabled(true);
//        mMap!!.uiSettings.isMapToolbarEnabled = true
//        mMap!!.uiSettings.isZoomControlsEnabled = true
//
//
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(true)
//        dialog.setContentView(R.layout.alert_bank_details)
//        val tva_branch = dialog.findViewById(R.id.tva_branch) as TextView
//        val tva_bank = dialog.findViewById(R.id.tva_bank) as TextView
//        val tva_address = dialog.findViewById(R.id.tva_address) as TextView
//        val tva_place = dialog.findViewById(R.id.tva_place) as TextView
//        val tva_post = dialog.findViewById(R.id.tva_post) as TextView
//        val tva_district = dialog.findViewById(R.id.tva_district) as TextView
//
//
//        tva_branch.text = obj.getString("BranchName")
//        tva_bank.text = obj.getString("BankName")
//        tva_address.text = obj.getString("Address")
//        tva_place.text = obj.getString("Place")
//        tva_post.text = obj.getString("Post")
//        tva_district.text = obj.getString("District")
//
//
//        dialog.show()
//
//        return true
//    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(1000)
        mLocationRequest!!.setFastestInterval(1000)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
               applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("You are here")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

        //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(CHALAPPURAM, 15));

        // Zoom in, animating the camera.
        // map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(applicationContext)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }


}