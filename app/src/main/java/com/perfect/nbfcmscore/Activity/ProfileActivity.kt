package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.PicassoTrustAll
import com.perfect.nbfcmscore.R

class ProfileActivity : AppCompatActivity() , View.OnClickListener{

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_customername: TextView? = null
    var tv_mobile: TextView? = null
    var tv_address: TextView? = null
    var tv_email: TextView? = null
    var tv_gender: TextView? = null
    var tv_dob: TextView? = null
    var tv_cusnumber: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome!!.setOnClickListener(this)
        tv_customername = findViewById<TextView>(R.id.tv_customername)
        tv_mobile = findViewById<TextView>(R.id.tv_mobile)
        tv_address = findViewById<TextView>(R.id.tv_address)
        tv_email = findViewById<TextView>(R.id.tv_email)
        tv_gender = findViewById<TextView>(R.id.tv_gender)
        tv_dob = findViewById<TextView>(R.id.tv_dob)
        tv_cusnumber = findViewById<TextView>(R.id.tv_cusnumber)

        val ImageURLSP = applicationContext.getSharedPreferences(Config.SHARED_PREF165, 0)
        val IMAGE_URL = ImageURLSP.getString("ImageURL", null)

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14,0)
        try {
            val imagepath = IMAGE_URL+AppIconImageCodeSP!!.getString("AppIconImageCode",null)
          //  PicassoTrustAll.getInstance(this)!!.load(imagepath).error(R.drawable.no_image).into(im_applogo)
        }catch (e: Exception) {
            e.printStackTrace()}
        val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF3,0)
        tv_customername!!.setText(CustomerNameSP.getString("CustomerName",null))
        val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2,0)
        tv_mobile!!.setText(CusMobileSP.getString("CusMobile",null))
        val AddressSP = applicationContext.getSharedPreferences(Config.SHARED_PREF4,0)
        val AddressSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF299,0)


      //  Log.i("Addrstest",add!!)
        tv_address!!.setText(AddressSP.getString("Address",null))


        val EmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF5,0)
        val EmailSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF300,0)

        tv_email!!.setText(EmailSP1.getString("Email1",null)+" :"+EmailSP.getString("Email",null))


        val GenderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF6,0)
        tv_gender!!.setText("Gender : "+GenderSP.getString("Gender",null))
        val DateOfBirthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF7,0)
        tv_dob!!.setText("Date Of Birth : "+DateOfBirthSP.getString("DateOfBirth",null))
        val CustomerNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF20,0)
        tv_cusnumber!!.setText("Customer Number : "+CustomerNumberSP.getString("CustomerNumber",null))



    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack ->{
                finish()
            }
            R.id.imgHome ->{
                startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
            }
        }
    }
}