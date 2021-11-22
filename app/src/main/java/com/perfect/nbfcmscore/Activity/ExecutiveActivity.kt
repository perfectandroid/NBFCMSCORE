package com.perfect.nbfcmscore.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExecutiveActivity : AppCompatActivity() ,View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var txtv_Name: TextInputEditText? = null
    var txtv_mob: TextInputEditText? = null
    var txtv_Date: TextInputEditText? = null
    var txt_time: TextInputEditText? = null
    var btn_submit: Button? = null

    var tv_header: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_executive)

        setRegViews()

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

        txtv_Name= findViewById<TextInputEditText>(R.id.txtv_Name)
        txtv_mob= findViewById<TextInputEditText>(R.id.txtv_mob)
        txtv_Date= findViewById<TextInputEditText>(R.id.txtv_Date)
        txt_time= findViewById<TextInputEditText>(R.id.txt_time)

        btn_submit= findViewById<Button>(R.id.btn_submit)


        btn_submit!!.setOnClickListener(this)


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
        }
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
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
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
}