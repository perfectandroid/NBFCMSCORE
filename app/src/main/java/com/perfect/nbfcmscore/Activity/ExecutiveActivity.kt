package com.perfect.nbfcmscore.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ExecutiveActivity : AppCompatActivity() ,View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var txtv_Name: TextInputEditText? = null
    var txtv_mob: TextInputEditText? = null
    var txtv_Date: TextInputEditText? = null
    var txt_time: TextInputEditText? = null
    var btn_submit: Button? = null
    var ll_dob: LinearLayout? = null
    var tv_header: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_executive)

        setRegViews()

        val imcallbacklogo: ImageView = findViewById(R.id.imcallbacklogo)
       // Glide.with(this).load(R.drawable.callbackgif).into(imcallbacklogo)


        val HeaderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF84, 0)
        val NameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF171, 0)
        val MobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF172, 0)
        val DateSP = applicationContext.getSharedPreferences(Config.SHARED_PREF173, 0)
        val TimeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF174, 0)
        val SubmitSP = applicationContext.getSharedPreferences(Config.SHARED_PREF174, 0)

        tv_header!!.setText(HeaderSP.getString("ExecutiveCallBack", null))
        txtv_Name!!.setHint(NameSP.getString("Name", null))
        txtv_mob!!.setHint(MobileSP.getString("Mobile", null))
        txt_time!!.setHint(TimeSP.getString("Time", null))
        txtv_Date!!.setHint(DateSP.getString("Date", null))

    }

    private fun setRegViews() {
        tv_header = findViewById<TextView>(R.id.tv_header)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)



        ll_dob= findViewById<LinearLayout>(R.id.ll_dob)

        txtv_Name= findViewById<TextInputEditText>(R.id.txtv_Name)
        txtv_mob= findViewById<TextInputEditText>(R.id.txtv_mob)
        txtv_Date= findViewById<TextInputEditText>(R.id.txtv_Date)
        txt_time= findViewById<TextInputEditText>(R.id.txt_time)

        btn_submit= findViewById<Button>(R.id.btn_submit)


        btn_submit!!.setOnClickListener(this)
        txt_time!!.setOnClickListener(this)
        txtv_Date!!.setOnClickListener(this)
        ll_dob!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@ExecutiveActivity, HomeActivity::class.java))
            }
            R.id.btn_submit -> {
                if (isValid()){
                    sendEmail()
                }

            }
            R.id.txt_time -> {
                val c:Calendar= Calendar.getInstance()
                val hh=c.get(Calendar.HOUR_OF_DAY)
                val mm=c.get(Calendar.MINUTE)
                val timePickerDialog:TimePickerDialog= TimePickerDialog(this@ExecutiveActivity,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    txt_time!!.setText( ""+hourOfDay + ":" + minute);
                },hh,mm,true)
                timePickerDialog.show()

            }
            R.id.ll_dob -> {
                getDatepicker()
            }
        }
    }

    private fun getDatepicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
            view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView


            txtv_Date!!.setText("" + dayOfMonth + "-" +(monthOfYear+1) + "-" + year)
        }, year, month, day)
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show()
    }

    private fun sendEmail() {
        //val Sendemail = applicationContext.getSharedPreferences("Sendemail", 0)
        //val Sendmail = Sendemail.getString("Sendemail", "")
      //  Toast.makeText(this@SuggestionActivity, Sendmail + "", Toast.LENGTH_SHORT).show()
        val TO = arrayOf("psstechteam@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Call back Form")
        var data =txtv_Name!!.text.toString()+"\n"+txtv_mob!!.text.toString()+"\n"+
                txtv_Date!!.text.toString()+"\n"+txt_time!!.text.toString()
        emailIntent.putExtra(Intent.EXTRA_TEXT, data)
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail successfully..."))
        } catch (ex: ActivityNotFoundException) {
        }
    }

    private fun isValid(): Boolean {

        val name = txtv_Name!!.text.toString()
        val mob = txtv_mob!!.text.toString()
        val date = txtv_Date!!.text.toString()
        val time = txt_time!!.text.toString()

        if (TextUtils.isEmpty(name)) {
            txtv_Name!!.error = "Please Enter Name"
            return false
        }
        txtv_Name!!.setError(null)

        if (TextUtils.isEmpty(mob)) {
            txtv_mob!!.setError("Please enter mobile number")
            return false
        }

        if (mob.length > 10 || mob.length < 10) {
            txtv_mob!!.setError("Please enter valid 10 digit mobile number")
            return false
        }

        txtv_mob!!.setError(null)

        if (TextUtils.isEmpty(date)) {
            txtv_Date!!.error = "Please Select Date"
            return false
        }


    /*    if (TextUtils.isEmpty(time)) {
            txt_time!!.error = "Please Enter Time"
            return false
        }
        txt_time!!.setError(null)*/

        /* val mPinString = mPin!!.text.toString()
         if (mPinString.trim { it <= ' ' }.length == 0) {
             mPin!!.error = "Please enter the M-PIN"
             return false
         }
 */

        return true

    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }
}