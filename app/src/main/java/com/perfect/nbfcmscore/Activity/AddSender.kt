package com.perfect.nbfcmscore.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ConnectivityUtils
import com.perfect.nbfcmscore.Helper.MscoreApplication
import com.perfect.nbfcmscore.Model.SenderReceiver
import com.perfect.nbfcmscore.Model.SenderReceiverlist
import com.perfect.nbfcmscore.R
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class AddSender : AppCompatActivity() , View.OnClickListener{
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imgv_datepick: ImageView? = null
    var imgHome: ImageView? = null
    var txtv_first_name: TextInputEditText? = null
    var txtv_last_name: TextInputEditText? = null
    var txtv_mobile_number: TextInputEditText? = null
    var txtv_dob: TextInputEditText? = null
    var btn_register: Button? = null
    var tv_title: TextView? = null

    public var arrayList2: ArrayList<SenderReceiver>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)

        setRegViews()

        val Fundtransfrsp = applicationContext.getSharedPreferences(Config.SHARED_PREF122, 0)
        tv_title!!.setText(Fundtransfrsp.getString("FundTransfer", null))

        val Firsttimesp = applicationContext.getSharedPreferences(Config.SHARED_PREF143, 0)
        txtv_first_name!!.setHint(Firsttimesp.getString("FirstName", null))

        val Lastnamesp = applicationContext.getSharedPreferences(Config.SHARED_PREF144, 0)
        txtv_last_name!!.setHint(Lastnamesp.getString("LastName", null))

        val Registerp = applicationContext.getSharedPreferences(Config.SHARED_PREF146, 0)
        btn_register!!.setText(Registerp.getString("REGISTER", null))

        val defaultDate = "01-01-1990"
        txtv_dob!!.setText(defaultDate)
       /* val mobilesp = applicationContext.getSharedPreferences(Config.SHARED_PREF145, 0)
        txtv_mobile_number!!.setText(mobilesp.getString("DOB", null))
*/

    }

    private fun setRegViews() {
        tv_title = findViewById<TextView>(R.id.tv_title)
        imgv_datepick = findViewById<ImageView>(R.id.imgv_datepick)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        txtv_first_name = findViewById<TextInputEditText>(R.id.txtv_first_name)
        txtv_last_name = findViewById<TextInputEditText>(R.id.txtv_last_name)
        txtv_mobile_number = findViewById<TextInputEditText>(R.id.txtv_mobile_number)
        txtv_dob = findViewById<TextInputEditText>(R.id.txtv_dob)
        btn_register = findViewById<Button>(R.id.btn_register)

        imgv_datepick!!.setOnClickListener(this)
        btn_register!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
    }

    private fun getSender(firstName: String, lastName: String, mobileNumber: String, dob: String) {
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@AddSender, R.style.Progress)
                progressDialog!!.setProgressStyle(android.R.style.Widget_ProgressBar)
                progressDialog!!.setCancelable(false)
                progressDialog!!.setIndeterminate(true)
                progressDialog!!.setIndeterminateDrawable(this.resources.getDrawable(R.drawable.progress))
                progressDialog!!.show()
                try {
                    val client = OkHttpClient.Builder()
                            .sslSocketFactory(Config.getSSLSocketFactory(this@AddSender))
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

                        val FK_CustomerSP = this.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF1,
                                0
                        )
                        val FK_Customer = FK_CustomerSP.getString("FK_Customer", null)

                        val TokenSP = this!!.applicationContext.getSharedPreferences(
                                Config.SHARED_PREF8,
                                0
                        )
                        val Token = TokenSP.getString("Token", null)

                        //  requestObject1.put("Reqmode", MscoreApplication.encryptStart("40"))
                        requestObject1.put("Token", MscoreApplication.encryptStart(Token))
                        requestObject1.put(
                                "FK_Customer",
                                MscoreApplication.encryptStart(FK_Customer)
                        )
                        requestObject1.put(
                                "Sender_fname",
                                MscoreApplication.encryptStart(firstName)
                        )
                        requestObject1.put(
                                "Sender_lname",
                                MscoreApplication.encryptStart(lastName)
                        )
                        requestObject1.put(
                                "Sender_dob",
                                MscoreApplication.encryptStart(dob)
                        )
                        requestObject1.put(
                                "Sender_mobile",
                                MscoreApplication.encryptStart(mobileNumber)
                        )
                        requestObject1.put(
                                "BankKey", MscoreApplication.encryptStart(
                                getResources().getString(
                                        R.string.BankKey
                                )
                        )
                        )


                        Log.e("TAG", "requestObject1  sender   " + requestObject1)
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
                    val call = apiService.getaddSender(body)
                    call.enqueue(object : retrofit2.Callback<String> {
                        override fun onResponse(
                                call: retrofit2.Call<String>, response:
                                Response<String>
                        ) {
                            try {
                                progressDialog!!.dismiss()
                                val jObject = JSONObject(response.body())
                                Log.i("Response-sender", response.body())
                                if (jObject.getString("StatusCode") == "0") {
                                    val jsonObj1: JSONObject =
                                            jObject.getJSONObject("Addnewsender")
                                    val jsonobj2 = JSONObject(jsonObj1.toString())

                                    var message = jsonobj2.getString("message")
                                    var status = jsonobj2.getString("Status")
                                    var senderid = jsonobj2.getString("ID_Sender")
                                    var receiverid = jsonobj2.getString("ID_Receiver")
                                    var otpRefNo = jsonobj2.getString("otpRefNo")
                                    var statuscode = jsonobj2.getString("StatusCode")

                                    arrayList2 = ArrayList<SenderReceiver>()
                                    arrayList2!!.add(SenderReceiver(
                                           message, status, senderid, receiverid,otpRefNo,statuscode
                                    ))

                                    alertMessage1(status, message)


                                } else {
                                    val builder = AlertDialog.Builder(
                                            this@AddSender,
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
                                        this@AddSender,
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
                                    this@AddSender,
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
                    val builder = AlertDialog.Builder(this@AddSender, R.style.MyDialogTheme)
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
                val builder = AlertDialog.Builder(this@AddSender, R.style.MyDialogTheme)
                builder.setMessage("No Internet Connection.")
                builder.setPositiveButton("Ok") { dialogInterface, which ->
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgv_datepick -> {
                getDatepicker()

            }
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@AddSender, HomeActivity::class.java))
            }
            R.id.btn_register -> {
                if (isValid()) {
                    val firstName: String = txtv_first_name!!.getText().toString()
                    val lastName: String = txtv_last_name!!.getText().toString()
                    val mobileNumber: String = txtv_mobile_number!!.getText().toString()
                    val dob: String = txtv_dob!!.getText().toString()
                    getSender(firstName, lastName, mobileNumber, dob)
                }
            }
        }
    }

    private fun isValid(): Boolean {

        val firstName: String = txtv_first_name!!.getText().toString()
        val lastName: String = txtv_last_name!!.getText().toString()
        val mobileNumber: String = txtv_mobile_number!!.getText().toString()

        if (TextUtils.isEmpty(firstName)) {
            txtv_first_name!!.setError("Please enter first name")
            return false
        }
        txtv_first_name!!.setError(null)

        if (TextUtils.isEmpty(lastName)) {
            txtv_last_name!!.setError("Please enter last name")
            return false
        }
        txtv_last_name!!.setError(null)

        if (TextUtils.isEmpty(mobileNumber)) {
            txtv_mobile_number!!.setError("Please enter mobile number")
            return false
        }

        if (mobileNumber.length > 10 || mobileNumber.length < 10) {
            txtv_mobile_number!!.setError("Please enter valid 10 digit mobile number")
            return false
        }

        txtv_mobile_number!!.setError(null)

        return true

    }

    private fun getDatepicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            txtv_dob!!.setText("" + dayOfMonth + "-" + month + "-" + year)
        }, year, month, day)
        dpd.show()
    }

    private fun alertMessage1(msg1: String, msg2: String) {
        val dialogBuilder = AlertDialog.Builder(this@AddSender)
        val inflater: LayoutInflater = this@AddSender.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.alert_layout, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val tv_share = dialogView.findViewById<TextView>(R.id.tv_share)
        val tv_msg = dialogView.findViewById<TextView>(R.id.txt1)
        val tv_msg2 = dialogView.findViewById<TextView>(R.id.txt2)
        tv_msg.text = msg1
        tv_msg2.text = msg2
        val tv_cancel = dialogView.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.setOnClickListener { alertDialog.dismiss() }
        tv_share.setOnClickListener {
            //  finishAffinity();
            alertDialog.dismiss()

        }
        alertDialog.show()
    }
}