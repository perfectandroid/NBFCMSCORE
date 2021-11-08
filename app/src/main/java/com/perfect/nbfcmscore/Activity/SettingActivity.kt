package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.CustomBottomSheeet
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
import java.util.ArrayList

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    val TAG: String = "SettingActivity"

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var act_UpdateDays: AutoCompleteTextView? = null
    var act_hours: AutoCompleteTextView? = null
    var act_minutes: AutoCompleteTextView? = null
    var act_DefAcc: AutoCompleteTextView? = null

    var btn_apply: Button? = null



    val daysItems : Array<String> = arrayOf("7", "10", "14", "30", "60", "120", "150", "180")
    val hourItems : Array<String> = arrayOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
    val minutesItems : Array<String> = arrayOf("00", "15", "30", "45")

    var jArrayAccount: JSONArray? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setInitialise()
        setRegister()


        setDropdowns()


//        val adapterday = ArrayAdapter(this@SettingActivity,
//            android.R.layout.simple_list_item_1, daysItems)
//        act_UpdateDays!!.setAdapter(adapterday)



    }

    private fun setDropdowns() {



        val updateDaysSP = applicationContext.getSharedPreferences(Config.SHARED_PREF21,0)
        val updateHourSP = applicationContext.getSharedPreferences(Config.SHARED_PREF22,0)
        val updateMinuteSP = applicationContext.getSharedPreferences(Config.SHARED_PREF23,0)
        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24,0)

        Log.e(TAG,"updateDays  69   "+updateDaysSP.getString("updateDays",null))

        if (updateDaysSP.getString("updateDays",null) == null && updateHourSP.getString("updateHour",null) == null &&
            updateMinuteSP.getString("updateMinute",null)== null && DefaultAccountSP.getString("DefaultAccount",null)==null){
            Log.e(TAG,"updateDays  691   "+updateDaysSP.getString("updateDays",null))

            act_UpdateDays!!.setText("7")
            act_hours!!.setText("00")
            act_minutes!!.setText("00")

        }else{
            Log.e(TAG,"updateDays  692   "+updateDaysSP.getString("updateDays",null))
            act_UpdateDays!!.setText(updateDaysSP.getString("updateDays",null))
            act_hours!!.setText(updateHourSP.getString("updateHour",null))
            act_minutes!!.setText(updateMinuteSP.getString("updateMinute",null))
            act_DefAcc!!.setText(DefaultAccountSP.getString("DefaultAccount",null))

        }

        val adapterday = ArrayAdapter(this@SettingActivity,
            android.R.layout.simple_list_item_1, daysItems)
        act_UpdateDays!!.setAdapter(adapterday)

        val adapterhour = ArrayAdapter(this@SettingActivity,
            android.R.layout.simple_list_item_1, hourItems)
        act_hours!!.setAdapter(adapterhour)

        val adapterminute = ArrayAdapter(this@SettingActivity,
            android.R.layout.simple_list_item_1, minutesItems)
        act_minutes!!.setAdapter(adapterminute)


        getOwnAccount()


    }

    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)


        act_UpdateDays = findViewById<AutoCompleteTextView>(R.id.act_UpdateDays)
        act_hours = findViewById<AutoCompleteTextView>(R.id.act_hours)
        act_minutes = findViewById<AutoCompleteTextView>(R.id.act_minutes)
        act_DefAcc = findViewById<AutoCompleteTextView>(R.id.act_DefAcc)

        btn_apply = findViewById<Button>(R.id.btn_apply)

    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)

        act_UpdateDays!!.setOnClickListener(this)
        act_hours!!.setOnClickListener(this)
        act_minutes!!.setOnClickListener(this)
        act_DefAcc!!.setOnClickListener(this)

        btn_apply!!.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
                onBackPressed()
//                startActivity(Intent(this@StatementActivity, HomeActivity::class.java))
//                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@SettingActivity, HomeActivity::class.java))
                finish()
            }
            R.id.act_UpdateDays ->{
                act_UpdateDays!!.showDropDown()
            }
            R.id.act_hours ->{
                act_hours!!.showDropDown()
            }
            R.id.act_minutes ->{
                act_minutes!!.showDropDown()
            }
            R.id.act_DefAcc ->{
                act_DefAcc!!.showDropDown()
            }
            R.id.btn_apply ->{
                validation()
            }

        }
    }

    private fun validation() {
        val updays = act_UpdateDays!!.text.toString();
        val uphour = act_hours!!.text.toString();
        val upmin = act_minutes!!.text.toString();
        val upacc = act_DefAcc!!.text.toString();

        if (updays.equals("")){
            CustomBottomSheeet.Show(this,"Select Update (Days)","0")
        }
        else if (uphour.equals("")){
            CustomBottomSheeet.Show(this,"Select Update Intervel Hour","0")

        }
        else if (upmin.equals("")){
            CustomBottomSheeet.Show(this,"Select Update Intervel Minutes","0")

        }
        else if (upacc.equals("")){
            CustomBottomSheeet.Show(this,"Select Account Number","0")

        }
        else{

            try {
                val updateDaysSP = applicationContext.getSharedPreferences(Config.SHARED_PREF21,0)
                val updateDaysEditer = updateDaysSP.edit()
                updateDaysEditer.putString("updateDays", updays)
                updateDaysEditer.commit()

                val updateHourSP = applicationContext.getSharedPreferences(Config.SHARED_PREF22,0)
                val updateHourEditer = updateHourSP.edit()
                updateHourEditer.putString("updateHour", uphour)
                updateHourEditer.commit()

                val updateMinuteSP = applicationContext.getSharedPreferences(Config.SHARED_PREF23,0)
                val updateMinuteEditer = updateMinuteSP.edit()
                updateMinuteEditer.putString("updateMinute", upmin)
                updateMinuteEditer.commit()

                val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24,0)
                val DefaultAccountEditer = DefaultAccountSP.edit()
                DefaultAccountEditer.putString("DefaultAccount", upacc)
                DefaultAccountEditer.commit()

                for (i in 0 until jArrayAccount!!.length()) {
                    val obj: JSONObject = jArrayAccount!!.getJSONObject(i)
                    if (obj.getString("AccountNumber").equals(upacc)){

                        val DefaultFK_AccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF25,0)
                        val DefaultFK_AccountEditer = DefaultFK_AccountSP.edit()
                        DefaultFK_AccountEditer.putString("DefaultFK_Account", obj.getString("FK_Account"))
                        DefaultFK_AccountEditer.commit()

                        val DefaultSubModuleSP = applicationContext.getSharedPreferences(Config.SHARED_PREF26,0)
                        val DefaultSubModuleEditer = DefaultSubModuleSP.edit()
                        DefaultSubModuleEditer.putString("DefaultSubModule", obj.getString("SubModule"))
                        DefaultSubModuleEditer.commit()

                        val DefaultBalanceSP = applicationContext.getSharedPreferences(Config.SHARED_PREF27,0)
                        val DefaultBalanceEditer = DefaultBalanceSP.edit()
                        DefaultBalanceEditer.putString("DefaultBalance", obj.getString("Balance"))
                        DefaultBalanceEditer.commit()

                        val DefaultBranchNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF28,0)
                        val DefaultBranchNameEditer = DefaultBranchNameSP.edit()
                        DefaultBranchNameEditer.putString("DefaultBranchName", obj.getString("BranchName"))
                        DefaultBranchNameEditer.commit()


                    }
                }


                val builder = AlertDialog.Builder(
                    this@SettingActivity,
                    R.style.MyDialogTheme
                )
                builder.setMessage("Updated successfully")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                    startActivity(Intent(this@SettingActivity, HomeActivity::class.java))
                    finish()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

            }catch (e : Exception){


                val builder = AlertDialog.Builder(
                    this@SettingActivity,
                    R.style.MyDialogTheme
                )
                builder.setMessage("Some technical issues.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }


        }

    }





    private fun getOwnAccount() {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@SettingActivity, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                        .sslSocketFactory(Config.getSSLSocketFactory(this@SettingActivity))
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

                                    val accountItems: ArrayList<String> = ArrayList()
                                    for (i in 0 until jArrayAccount!!.length()) {
                                        val obj: JSONObject = jArrayAccount!!.getJSONObject(i)
                                        accountItems.add(obj.getString("AccountNumber"));
                                        val DefaultAccountSP = applicationContext.getSharedPreferences(Config.SHARED_PREF24,0)
                                        if (DefaultAccountSP.getString("DefaultAccount",null) == null){
                                            if (i == 0){
                                                act_DefAcc!!.setText(obj.getString("AccountNumber"))
                                            }
                                        }
                                    }

                                    val adapterAccount = ArrayAdapter(this@SettingActivity,
                                        android.R.layout.simple_list_item_1, accountItems)
                                    act_DefAcc!!.setAdapter(adapterAccount)

                                } else {
                                    val builder = AlertDialog.Builder(
                                        this@SettingActivity,
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
                                    this@SettingActivity,
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
                                this@SettingActivity,
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
                    val builder = AlertDialog.Builder(this@SettingActivity, R.style.MyDialogTheme)
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

                val builder = AlertDialog.Builder(this@SettingActivity, R.style.MyDialogTheme)
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