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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.perfect.nbfcmscore.Api.ApiInterface
import com.perfect.nbfcmscore.Helper.*
import com.perfect.nbfcmscore.Model.SenderReceiver
import com.perfect.nbfcmscore.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class AddSender : AppCompatActivity() , View.OnClickListener{
    private var progressDialog: ProgressDialog? = null
    var imgBack: ImageView? = null
    var applogo: ImageView? = null
   // var imgv_datepick: ImageView? = null
    var imgHome: ImageView? = null
    var txtv_first_name: EditText? = null
    var txtv_last_name: EditText? = null
    var txtv_mobile_number: EditText? = null
    var txtv_dob: EditText? = null
    var btn_register: TextView? = null
    var btn_clear: TextView? = null
    var tv_title: TextView? = null

    var receiver_name_inputlayout: TextView? = null
    var txtv_lastname: TextView? = null
    var txtvmob: TextView? = null
    var txtvdate: TextView? = null


    public var arrayList2: ArrayList<SenderReceiver>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)

        setRegViews()

        val Fundtransfrsp = applicationContext.getSharedPreferences(Config.SHARED_PREF138, 0)
        tv_title!!.setText(Fundtransfrsp.getString("AddNewSender", null))


        val Registerp = applicationContext.getSharedPreferences(Config.SHARED_PREF146, 0)
        btn_register!!.setText(Registerp.getString("REGISTER", null))

        val Resetsp = applicationContext.getSharedPreferences(Config.SHARED_PREF189, 0)
        btn_clear!!.setText(Resetsp.getString("RESET", null))

        val firstnamesp = applicationContext.getSharedPreferences(Config.SHARED_PREF143, 0)
        receiver_name_inputlayout!!.setText(firstnamesp.getString("FirstName", null))
        txtv_first_name!!.setHint(firstnamesp.getString("FirstName", null))

        val lastnamesp = applicationContext.getSharedPreferences(Config.SHARED_PREF144, 0)
        txtv_lastname!!.setText(lastnamesp.getString("LastName", null))
        txtv_last_name!!.setHint(lastnamesp.getString("LastName", null))

        val mobsp = applicationContext.getSharedPreferences(Config.SHARED_PREF110, 0)
        txtvmob!!.setText(mobsp.getString("MobileNumber", null))
        txtv_mobile_number!!.setHint(mobsp.getString("MobileNumber", null))

        val datesp = applicationContext.getSharedPreferences(Config.SHARED_PREF173, 0)
        txtvdate!!.setText(datesp.getString("Date", null))




        val defaultDate = "01-01-1990"
        txtv_dob!!.setText(defaultDate)
       /* val mobilesp = applicationContext.getSharedPreferences(Config.SHARED_PREF145, 0)
        txtv_mobile_number!!.setText(mobilesp.getString("DOB", null))
*/

    }

    private fun setRegViews() {
        tv_title = findViewById<TextView>(R.id.tv_title)
       // imgv_datepick = findViewById<ImageView>(R.id.imgv_datepick)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        txtv_first_name = findViewById<EditText>(R.id.txtv_first_name)
        txtv_last_name = findViewById<EditText>(R.id.txtv_last_name)
        txtv_mobile_number = findViewById<EditText>(R.id.txtv_mobile_number)
        txtv_dob = findViewById<EditText>(R.id.txtv_dob)
        btn_register = findViewById<TextView>(R.id.btn_register)
        btn_clear= findViewById<TextView>(R.id.btn_clear)

        receiver_name_inputlayout = findViewById<TextView>(R.id.receiver_name_inputlayout)
        txtv_lastname = findViewById<TextView>(R.id.txtv_lastname)
        txtvmob = findViewById<TextView>(R.id.txtvmob)
        txtvdate = findViewById<TextView>(R.id.txtvdate)



       // imgv_datepick!!.setOnClickListener(this)
        btn_register!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
        txtv_dob!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)
    }

    private fun getSender(firstName: String, lastName: String, mobileNumber: String, dob: String) {
        val baseurlSP = applicationContext.getSharedPreferences(Config.SHARED_PREF163, 0)
        val baseurl = baseurlSP.getString("baseurl", null)
        when(ConnectivityUtils.isConnected(this)) {
            true -> {
                progressDialog = ProgressDialog(this@AddSender, R.style.Progress)
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
                        .sslSocketFactory(Config.getSSLSocketFactory(this), trustManager)
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
                        val BankKeySP = applicationContext.getSharedPreferences(Config.SHARED_PREF312, 0)
                        val BankKeyPref = BankKeySP.getString("BankKey", null)
                        val BankHeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF313, 0)
                        val BankHeaderPref = BankHeaderSP.getString("BankHeader", null)

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
                        requestObject1.put("BankKey", MscoreApplication.encryptStart(BankKeyPref))
                        requestObject1.put("BankHeader", MscoreApplication.encryptStart(BankHeaderPref))

                        //   val nidhiSP = applicationContext.getSharedPreferences(Config.SHARED_PREF346, 0)
                        //   val nidhicode = BankHeaderSP.getString("nidhicode", "")

                        //  requestObject1.put("nidhicode", MscoreApplication.encryptStart(nidhicode))


                        Log.e("TAG", "requestObject1  sender   " + requestObject1)
                    } catch (e: Exception) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert","Some technical issues.",1);
                    }
                    val body = RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
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
                                Log.i("Response-sender", response.body().toString())
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

                                    AlertMessage().alertMessage(this@AddSender,this@AddSender,status,message,2);

                                }
                                else if ( jObject.getString("StatusCode").equals("-1") ){
                                  //  alertMessage1("", jObject.getString("EXMessage"))
                                    AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert",jObject.getString("EXMessage"),1);
                                }
                                else if ( ! jObject.getString("otpRefNo").equals("0") &&  jObject.getString("Status").equals("200") ){
                                   // startActivity(Intent(this@AddSender, TransactionOTPActivity::class.java))
                                    var intent = Intent(this@AddSender, TransactionOTPActivity::class.java)
                                    intent.putExtra("from", "sender")
                                    startActivity(intent)

                                }
                                else {
                                    AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert",jObject.getString("EXMessage"),1);
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()

                                AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert","Some technical issues.",1);
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                            progressDialog!!.dismiss()

                            AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert","Some technical issues.",1);
                        }
                    })
                } catch (e: Exception) {
                    progressDialog!!.dismiss()
                    AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert","Some technical issues.",1);
                }
            }
            false -> {
                AlertMessage().alertMessage(this@AddSender,this@AddSender,"Alert"," No Internet Connection. ",3);
            }
        }
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.txtv_dob -> {
                getDatepicker()

            }
            R.id.btn_clear -> {
                val defaultDate = "01-01-1990"
                txtv_dob!!.setText(defaultDate)
                txtv_first_name!!.setText("")
                txtv_last_name!!.setText("")
                txtv_mobile_number!!.setText("")

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
           val plsentfrstSP = applicationContext.getSharedPreferences(Config.SHARED_PREF303, 0)
            var plsentfst =plsentfrstSP.getString("PleaseEnterFirstName", null)

            CustomBottomSheeet.Show(this, plsentfst!!,"0")
         //   txtv_first_name!!.setError("Please enter first name")
            return false
        }
       // txtv_first_name!!.setError(null)

        if (TextUtils.isEmpty(lastName)) {
            val plsentlstSP = applicationContext.getSharedPreferences(Config.SHARED_PREF304, 0)
            var plsentlst =plsentlstSP.getString("PleaseEnterLastName", null)

            CustomBottomSheeet.Show(this, plsentlst!!,"0")

         //   txtv_last_name!!.setError("Please enter last name")
            return false
        }
       // txtv_last_name!!.setError(null)

        if (TextUtils.isEmpty(mobileNumber)) {
            val plsentmobSP = applicationContext.getSharedPreferences(Config.SHARED_PREF281, 0)
            var plsentmoble =plsentmobSP.getString("PleaseEnterMobileNumber", null)

            CustomBottomSheeet.Show(this, plsentmoble!!,"0")
           // txtv_mobile_number!!.setError("Please enter mobile number")
            return false
        }

        if (mobileNumber.length > 10 || mobileNumber.length < 10) {
            val plsentvalicmobSP = applicationContext.getSharedPreferences(Config.SHARED_PREF287, 0)
            var plsentvaldmoble =plsentvalicmobSP.getString("PleaseEnterValidMobileNumber", null)

            CustomBottomSheeet.Show(this, plsentvaldmoble!!,"0")

           // txtv_mobile_number!!.setError("Please enter valid 10 digit mobile number")
            return false
        }

      //  txtv_mobile_number!!.setError(null)

        return true

    }

    private fun getDatepicker() {
        val c = Calendar.getInstance()
        c.set(1990, 0, 1);//Year,Month -1,Day
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            txtv_dob!!.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
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