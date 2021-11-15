package com.perfect.nbfcmscore.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class EnquiryActivity : AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener {

    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    var spn_feedbk: Spinner? = null
    var feedbackText: EditText? = null
    var btn_submit: Button? = null
    var feedback = arrayOfNulls<String>(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enquiry)

        setRegViews()


    }

    private fun setRegViews() {

        imgBack = findViewById<ImageView>(R.id.imgBack) as ImageView
        imgHome = findViewById<ImageView>(R.id.imgHome) as ImageView
        feedbackText = findViewById<EditText>(R.id.feedbackText) as EditText
        spn_feedbk = findViewById<Spinner>(R.id.spn_feedbk) as Spinner
        btn_submit = findViewById<Button>(R.id.btn_submit) as Button

        btn_submit!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)

        spn_feedbk!!.onItemSelectedListener = this

        feedback = arrayOf<String?>("Report An Error", "Give a Suggestion", "Anything Else")
        getFeedback()
    }

    private fun getFeedback() {
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this!!, android.R.layout.simple_spinner_item, feedback)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spn_feedbk!!.adapter = aa
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
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
        //val Sendemail = applicationContext.getSharedPreferences("Sendemail", 0)
        //val Sendmail = Sendemail.getString("Sendemail", "")
        //  Toast.makeText(this@SuggestionActivity, Sendmail + "", Toast.LENGTH_SHORT).show()
        val TO = arrayOf("psstechteam@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Call back Form")
        var data =feedbackText!!.text.toString()+"\n"+spn_feedbk!!.selectedItem.toString()

        emailIntent.putExtra(Intent.EXTRA_TEXT, data)
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
        }
    }

    private fun isValid(): Boolean {

        val fdbktxt = feedbackText!!.text.toString()
        val fdbkslctn = spn_feedbk!!.selectedItem.toString()


        if (TextUtils.isEmpty(fdbktxt)) {
            feedbackText!!.error = "Please Enter Your Feedback"
            return false
        }
        feedbackText!!.setError(null)

        if (TextUtils.isEmpty(fdbkslctn)) {
           Toast.makeText(applicationContext,"Please select a reason",Toast.LENGTH_LONG).show()
            return false
        }








        return true

    }
}