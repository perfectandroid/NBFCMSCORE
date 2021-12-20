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
import com.perfect.nbfcmscore.Helper.CustomBottomSheeet
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class FeedbackActivity : AppCompatActivity() , View.OnClickListener, AdapterView.OnItemSelectedListener {

    var imgBack: ImageView? = null
    var applogo: ImageView? = null
    var imCompanylogo: ImageView? = null
    var imgHome: ImageView? = null
    var spn_feedbk: Spinner? = null
    var feedbackText: EditText? = null
    var btn_submit: Button? = null
    var tv_mycart: TextView? = null
    var feedback = arrayOfNulls<String>(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        setRegViews()
        val imfeedbacklogo: ImageView = findViewById(R.id.imfeedbacklogo)
        Glide.with(this).load(R.drawable.feedbackgif).into(imfeedbacklogo)
        val ID_feedbk = applicationContext.getSharedPreferences(Config.SHARED_PREF56,0)
        tv_mycart!!.setText(ID_feedbk.getString("feedback",null))

       val ID_submt = applicationContext.getSharedPreferences(Config.SHARED_PREF250,0)
        btn_submit!!.setText(ID_submt.getString("Submit",null))


        feedbackText!!.setHint(ID_feedbk.getString("feedback",null))
    }

    private fun setRegViews() {
        tv_mycart = findViewById<TextView>(R.id.tv_mycart) as TextView
        imgBack = findViewById<ImageView>(R.id.imgBack) as ImageView
        imgHome = findViewById<ImageView>(R.id.imgHome) as ImageView
        feedbackText = findViewById<EditText>(R.id.feedbackText) as EditText
        spn_feedbk = findViewById<Spinner>(R.id.spn_feedbk) as Spinner
        btn_submit = findViewById<Button>(R.id.btn_submit) as Button

        btn_submit!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this)
        imgHome!!.setOnClickListener(this)
        spn_feedbk!!.onItemSelectedListener = this

        val ID_reprterr = applicationContext.getSharedPreferences(Config.SHARED_PREF175,0)
        val ID_suggstn = applicationContext.getSharedPreferences(Config.SHARED_PREF176,0)
        val ID_anyth= applicationContext.getSharedPreferences(Config.SHARED_PREF177,0)

        var reprt =ID_reprterr.getString("Reportanerror",null)
        var sugg=ID_suggstn.getString("Giveasuggestion",null)
        var anythng=ID_anyth.getString("Anythingelse",null)

       // feedback = arrayOf<String?>("Report An Error", "Give a Suggestion", "Anything Else")
        feedback = arrayOf<String?>(reprt, sugg, anythng)
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
                startActivity(Intent(this@FeedbackActivity, HomeActivity::class.java))
            }
            R.id.btn_submit ->{
                if (isValid()){
                    sendEmail()
                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, position: View?, p2: Int, p3: Long) {
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

            val plsentfdbkSP = applicationContext.getSharedPreferences(Config.SHARED_PREF233, 0)
            var plsentfdbk =plsentfdbkSP.getString("PleaseEnterYourFeedback", null)

            CustomBottomSheeet.Show(this, plsentfdbk!!,"0")


          //  feedbackText!!.error = "Please Enter Your Feedback"
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