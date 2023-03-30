package com.perfect.nbfcmscore.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import java.util.*
import java.util.regex.Pattern

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
    var rad_callbk:CheckBox?=null
    var btn_submit: Button? = null
    var lltime: LinearLayout? = null
    var llmob: LinearLayout? = null
    var lltimeinterval: LinearLayout? = null
    var llemail: LinearLayout? = null
    var lldate: LinearLayout? = null

    var spn_ampm: Spinner? = null
    var spn_bfreafter: Spinner? = null
    var spn_time: Spinner? = null

    var etxt_tmfrm: TextView? = null
    var etxt_tmto: TextView? = null
    var etxtDate: EditText? = null
    var btn_clear: Button? = null
    var matter:String?=null

    var feedback = arrayOfNulls<String>(0)
    var bfraftr = arrayOfNulls<String>(0)
    var ampm = arrayOfNulls<String>(0)
    var time = arrayOfNulls<String>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enquiry1)

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
        rad_callbk = findViewById<CheckBox>(R.id.rad_callbk) as CheckBox
        imgBack = findViewById<ImageView>(R.id.imgBack) as ImageView
        imgHome = findViewById<ImageView>(R.id.imgHome) as ImageView

        btn_clear = findViewById<Button>(R.id.btn_clear) as Button

        feedbackText = findViewById<EditText>(R.id.feedbackText) as EditText
        etxtmob= findViewById<EditText>(R.id.etxtmob) as EditText
        etxtemail= findViewById<EditText>(R.id.etxtemail) as EditText
        etxtDate= findViewById<EditText>(R.id.etxtDate) as EditText

        lltime= findViewById<LinearLayout>(R.id.lltime) as LinearLayout
        lldate= findViewById<LinearLayout>(R.id.lldate) as LinearLayout
        llmob= findViewById<LinearLayout>(R.id.llmob) as LinearLayout
        lltimeinterval= findViewById<LinearLayout>(R.id.lltimeinterval) as LinearLayout
        llemail= findViewById<LinearLayout>(R.id.llemail) as LinearLayout

        spn_feedbk = findViewById<Spinner>(R.id.spn_feedbk) as Spinner
        btn_submit = findViewById<Button>(R.id.btn_submit) as Button

        spn_time= findViewById<Spinner>(R.id.spn_time) as Spinner
        spn_ampm= findViewById<Spinner>(R.id.spn_ampm) as Spinner
        spn_bfreafter= findViewById<Spinner>(R.id.spn_bfreafter) as Spinner


        btn_submit!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
        btn_clear!!.setOnClickListener(this)

        spn_feedbk!!.onItemSelectedListener = this
        rad_callbk!!.setOnClickListener(this)

        etxt_tmfrm = findViewById<TextView>(R.id.etxt_tmfrm)
        etxt_tmto = findViewById<TextView>(R.id.etxt_tmto)

        etxt_tmfrm!!.setOnClickListener(this)
        etxt_tmto!!.setOnClickListener(this)
        etxtDate!!.setOnClickListener(this)

        rad_callbk!!.setOnClickListener(this)

        val ID_reprterr = applicationContext.getSharedPreferences(Config.SHARED_PREF175, 0)
        val ID_suggstn = applicationContext.getSharedPreferences(Config.SHARED_PREF176, 0)
        val ID_anyth= applicationContext.getSharedPreferences(Config.SHARED_PREF177, 0)

        var reprt =ID_reprterr.getString("Reportanerror", null)
        var sugg=ID_suggstn.getString("Giveasuggestion", null)
        var anythng=ID_anyth.getString("Anythingelse", null)

        feedback = arrayOf<String?>("Reportanerror", "Giveasuggestion", "Anythingelse")
        getFeedback()

        time = arrayOf<String?>("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        getTime()

        bfraftr = arrayOf<String?>("Before", "After")
        getbfre()
        ampm= arrayOf<String?>("AM", "PM")
        getampm()

        etxtDate!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        getDatepicker()
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    private fun getampm() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, ampm)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_ampm!!.adapter = aa
    }

    private fun getbfre() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, bfraftr)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_bfreafter!!.adapter = aa
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
            R.id.imgBack -> {
                finish()
            }
            R.id.etxt_tmfrm -> {
                val c: Calendar = Calendar.getInstance()
                val hh = c.get(Calendar.HOUR_OF_DAY)
                val mm = c.get(Calendar.MINUTE)
                val timePickerDialog: TimePickerDialog = TimePickerDialog(this@EnquiryActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    etxt_tmfrm!!.setText("" + hourOfDay + ":" + minute);
                }, hh, mm, true)
                timePickerDialog.show()
            }
            R.id.etxt_tmto -> {
                val c: Calendar = Calendar.getInstance()
                val hh = c.get(Calendar.HOUR_OF_DAY)
                val mm = c.get(Calendar.MINUTE)
                val timePickerDialog: TimePickerDialog = TimePickerDialog(this@EnquiryActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    etxt_tmto!!.setText("" + hourOfDay + ":" + minute);
                }, hh, mm, true)
                timePickerDialog.show()
            }
            R.id.rad_callbk -> {
                if (rad_callbk!!.isChecked) {
                    //   lltime!!.visibility=View.VISIBLE
                    etxtmob!!.visibility = View.VISIBLE
                    etxtemail!!.visibility = View.VISIBLE
                    spn_time!!.visibility = View.VISIBLE
                    llmob!!.visibility = View.VISIBLE
                    lltimeinterval!!.visibility = View.VISIBLE
                    llemail!!.visibility = View.VISIBLE
                    lldate!!.visibility = View.VISIBLE

                    val ID_mob = applicationContext.getSharedPreferences(Config.SHARED_PREF2, 0)
                    etxtmob!!.setText(ID_mob.getString("CusMobile", null))


                } else if (!rad_callbk!!.isChecked) {
                    etxtmob!!.visibility = View.GONE
                    etxtemail!!.visibility = View.GONE
                    spn_time!!.visibility = View.GONE
                    llmob!!.visibility = View.GONE
                    lltimeinterval!!.visibility = View.GONE
                    llemail!!.visibility = View.GONE
                    lldate!!.visibility = View.GONE
                }
            }

            R.id.imgHome -> {
                startActivity(Intent(this@EnquiryActivity, HomeActivity::class.java))
            }
            R.id.btn_clear -> {
                getFeedback()
                getTime()
                etxtmob!!.setText("")
                etxtemail!!.setText("")
                etxtDate!!.setText("")
                feedbackText!!.setText("")

            }
            R.id.btn_submit -> {
                if (isValid()) {
                    if (rad_callbk!!.isChecked) {
                        var time =   spn_bfreafter!!.selectedItem.toString()+" "+spn_time!!.selectedItem.toString()+" "+spn_ampm!!.selectedItem.toString()
                        matter = "The customer is ready to contact on "+etxtDate!!.text.toString() +" within the time limit "+time+" and the given Contact details are :"+"\n"+"\n"+"Mobile: " + etxtmob!!.text.toString() + "\n" + "Email: " + etxtemail!!.text.toString() +"\n"+"\n"+"their valuable feedback is : " +"\n"+"Subject :"+spn_feedbk!!.selectedItem.toString()+"\n"+"\n"+ feedbackText!!.text.toString() + "\n" + "\n" +
                              "\n" + "\n" + "\n" + "Thank you"
                    } else {

                        matter = "The valuable feedback from the customer is  " + "\n"+"\n"+"Subject :"+spn_feedbk!!.selectedItem.toString() + "\n"+"\n"+feedbackText!!.text.toString() +"\n"+"\n"+"\n"+"Thank you"
                    }

                     sendEmail(matter!!)
                 //   sendEmail1(matter!!)
                }
            }
            R.id.etxtDate -> {
                // getDatepicker()
            }
        }
    }

    private fun sendEmail1(matter: String) {

        BackgroundMail.newBuilder(this)
                .withUsername("psstechteam@gmail.com")
                .withPassword("pssandroid2020")
                .withMailto("psstechteam@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Enquiry/ExecutiveCallBack Mail")
                .withBody(matter)
                .withOnSuccessCallback(object : BackgroundMail.OnSuccessCallback {
                    override fun onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(object : BackgroundMail.OnFailCallback {
                    override fun onFail() {
                        //do some magic
                    }
                })
                .send()

    }

    private fun getDatepicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView


            etxtDate!!.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)
        }, year, month, day)
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        val now = System.currentTimeMillis() - 1000
       // dpd.getDatePicker().setMaxDate(now + 1000 * 60 * 60 * 24 * 2)
        dpd.show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        var feedback =spn_feedbk!!.selectedItem.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun sendEmail(matter: String) {

        val TO = arrayOf("psstechteam@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry/Executive Call Back")



        emailIntent.putExtra(Intent.EXTRA_TEXT, this.matter)






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
        val date = etxtDate!!.text.toString()
        val mail = etxtemail!!.text.toString()



        if (TextUtils.isEmpty(fdbktxt)) {
            feedbackText!!.error = "Please Enter Your Feedback"
            return false
        }
        feedbackText!!.setError(null)




        if (TextUtils.isEmpty(fdbkslctn)) {
           Toast.makeText(applicationContext, "Please select a reason", Toast.LENGTH_LONG).show()
            return false
        }
        if (rad_callbk!!.isChecked)
        {
            if (TextUtils.isEmpty(mob)) {
                etxtmob!!.setError("Please enter mobile number")
                return false
            }

            if (mob.length > 10 || mob.length < 10) {
                etxtmob!!.setError("Please enter valid 10 digit mobile number")
                return false
            }

            etxtmob!!.setError(null)

            if (TextUtils.isEmpty(date)) {
                etxtDate!!.error = "Please Select Date"
                return false
            }
            etxtDate!!.setError(null)

            if (TextUtils.isEmpty(mail)) {

            }
            else
            {
                if( !mail.isEmailValid())
                {
                    etxtemail!!.error = "Please Enter a Valid Email"
                    return false
                }
                etxtemail!!.setError(null)
            }





        }

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
    fun String.isEmailValid() =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
            ).matcher(this).matches()

}