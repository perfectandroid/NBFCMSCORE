package com.perfect.nbfcmscore.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R
import java.util.*

class EnquiryActivity : AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener {

    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    var spn_feedbk: Spinner? = null
    var feedbackText: EditText? = null
    var tv_mycart: TextView? = null
    var etxtmob: EditText? = null
    var etxtemail: EditText? = null
    var rad_callbk:RadioButton?=null
    var btn_submit: Button? = null
    var lltime: LinearLayout? = null
    var spn_time: Spinner? = null
    var etxt_tmfrm: TextView? = null
    var etxt_tmto: TextView? = null

    var feedback = arrayOfNulls<String>(0)
    var time = arrayOfNulls<String>(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enquiry)

        setRegViews()
        val imfeedbacklogo: ImageView = findViewById(R.id.imfeedbacklogo)
        Glide.with(this).load(R.drawable.feedbackgif).into(imfeedbacklogo)
     /*   val ID_enqry = applicationContext.getSharedPreferences(Config.SHARED_PREF82,0)
        tv_mycart!!.setText(ID_enqry.getString("Enquires",null))
        val ID_feedbk = applicationContext.getSharedPreferences(Config.SHARED_PREF56,0)
        feedbackText!!.setHint(ID_feedbk.getString("feedback",null))*/

    }

    private fun setRegViews() {

        tv_mycart = findViewById<TextView>(R.id.tv_mycart) as TextView
        rad_callbk = findViewById<RadioButton>(R.id.rad_callbk) as RadioButton
        imgBack = findViewById<ImageView>(R.id.imgBack) as ImageView
        imgHome = findViewById<ImageView>(R.id.imgHome) as ImageView

        feedbackText = findViewById<EditText>(R.id.feedbackText) as EditText
        etxtmob= findViewById<EditText>(R.id.etxtmob) as EditText
        etxtemail= findViewById<EditText>(R.id.etxtemail) as EditText

        lltime= findViewById<LinearLayout>(R.id.lltime) as LinearLayout

        spn_feedbk = findViewById<Spinner>(R.id.spn_feedbk) as Spinner
        btn_submit = findViewById<Button>(R.id.btn_submit) as Button

        spn_time= findViewById<Spinner>(R.id.spn_time) as Spinner

        btn_submit!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)

        spn_feedbk!!.onItemSelectedListener = this
        rad_callbk!!.setOnClickListener(this)

        etxt_tmfrm = findViewById<TextView>(R.id.etxt_tmfrm)
        etxt_tmto = findViewById<TextView>(R.id.etxt_tmto)

        etxt_tmfrm!!.setOnClickListener(this)
        etxt_tmto!!.setOnClickListener(this)

        rad_callbk!!.setOnClickListener(this)

        val ID_reprterr = applicationContext.getSharedPreferences(Config.SHARED_PREF175,0)
        val ID_suggstn = applicationContext.getSharedPreferences(Config.SHARED_PREF176,0)
        val ID_anyth= applicationContext.getSharedPreferences(Config.SHARED_PREF177,0)

        var reprt =ID_reprterr.getString("Reportanerror",null)
        var sugg=ID_suggstn.getString("Giveasuggestion",null)
        var anythng=ID_anyth.getString("Anythingelse",null)

        feedback = arrayOf<String?>("Reportanerror", "Giveasuggestion", "Anythingelse")
        getFeedback()

        time = arrayOf<String?>("Before 1pm", "2pm", "After 2pm")
        getTime()

    }

    private fun getFeedback() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, feedback)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_feedbk!!.adapter = aa
    }
    private fun getTime() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, time)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_time!!.adapter = aa
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.etxt_tmfrm ->{
                val c:Calendar= Calendar.getInstance()
                val hh=c.get(Calendar.HOUR_OF_DAY)
                val mm=c.get(Calendar.MINUTE)
                val timePickerDialog: TimePickerDialog = TimePickerDialog(this@EnquiryActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    etxt_tmfrm!!.setText( ""+hourOfDay + ":" + minute);
                },hh,mm,true)
                timePickerDialog.show()
            }
            R.id.etxt_tmto ->{
                val c:Calendar= Calendar.getInstance()
                val hh=c.get(Calendar.HOUR_OF_DAY)
                val mm=c.get(Calendar.MINUTE)
                val timePickerDialog: TimePickerDialog = TimePickerDialog(this@EnquiryActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    etxt_tmto!!.setText( ""+hourOfDay + ":" + minute);
                },hh,mm,true)
                timePickerDialog.show()
            }
            R.id.rad_callbk ->{
                if(rad_callbk!!.isChecked)
                {
                 //   lltime!!.visibility=View.VISIBLE
                    etxtmob!!.visibility=View.VISIBLE
                    etxtemail!!.visibility=View.VISIBLE
                    spn_time!!.visibility=View.VISIBLE

                    val ID_mob = applicationContext.getSharedPreferences(Config.SHARED_PREF2,0)
                    etxtmob!!.setText(ID_mob.getString("CusMobile",null))


                }
            }

            R.id.imgHome ->{
                startActivity(Intent(this@EnquiryActivity, HomeActivity::class.java))
            }
            R.id.btn_submit ->{
                if (isValid()){
                    sendEmail()
                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        var feedback =spn_feedbk!!.selectedItem.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun sendEmail() {

        val TO = arrayOf("psstechteam@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry/Executive Call Back")





            var data ="Reason: "+feedbackText!!.text.toString()+"\n"+"\n"+spn_feedbk!!.selectedItem.toString()+"\n"+"\n"+"Contact: "+etxtmob!!.text.toString()+"\n"+"Email: "+etxtemail!!.text.toString()+"\n"+"Selected time: "+
                    spn_time!!.selectedItem.toString()
            emailIntent.putExtra(Intent.EXTRA_TEXT, data)





        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
        }
     /*   val TO = arrayOf("psstechteam@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry")
        var data=""
        if(!etxtmob!!.text.toString().equals(""))
        {
            data =feedbackText!!.text.toString()+"\n"+spn_feedbk!!.selectedItem.toString()+"\n"+"Contact: "+etxtmob!!.text.toString()+"\n"+"Email"+etxtemail!!.text.toString()+"\n"+"Selected time: "+
                    spn_time!!.selectedItem.toString()
        }
        else
        {
            data =feedbackText!!.text.toString()+"\n"+spn_feedbk!!.selectedItem.toString()
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, data)
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
        }*/
    }

    private fun isValid(): Boolean {

        val fdbktxt = feedbackText!!.text.toString()
        val fdbkslctn = spn_feedbk!!.selectedItem.toString()
        val mob = etxtmob!!.text.toString()
        val mail = etxtemail!!.text.toString()

        if (TextUtils.isEmpty(fdbktxt)) {
            feedbackText!!.error = "Please Enter Your Feedback"
            return false
        }
        feedbackText!!.setError(null)

        if (TextUtils.isEmpty(fdbkslctn)) {
           Toast.makeText(applicationContext,"Please select a reason",Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(mob)) {
            etxtmob!!.setError("Please enter mobile number")
            return false
        }

        if (mob.length > 10 || mob.length < 10) {
            etxtmob!!.setError("Please enter valid 10 digit mobile number")
            return false
        }

        etxtmob!!.setError(null)

     /*   if (TextUtils.isEmpty(mail)) {
            etxtemail!!.error = "Please Enter Your Mail id"
            return false
        }
         if( !TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            etxtemail!!.error = "Please Enter a Valid Mail id"
            return false
        }
        etxtemail!!.setError(null)*/






        return true

    }


}