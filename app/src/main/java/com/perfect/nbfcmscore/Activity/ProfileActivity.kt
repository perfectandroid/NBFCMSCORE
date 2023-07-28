package com.perfect.nbfcmscore.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    var imgBack: ImageView? = null
    var imgHome: ImageView? = null
    var tv_customername: TextView? = null
    var tv_mobile: TextView? = null
    var tv_address: TextView? = null
    var tv_email: TextView? = null
    var tv_gender: TextView? = null
    var tv_dob: TextView? = null
    var tv_cusnumber: TextView? = null
    var tv_header: TextView? = null

    var profile_image: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_profile)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        imgBack!!.setOnClickListener(this)
        tv_header = findViewById<TextView>(R.id.tv_header)


        imgHome = findViewById<ImageView>(R.id.imgHome)
        profile_image = findViewById<CircleImageView>(R.id.profile_image)
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

        val AppIconImageCodeSP = applicationContext.getSharedPreferences(Config.SHARED_PREF14, 0)
        try {
            val imagepath = IMAGE_URL + AppIconImageCodeSP!!.getString("AppIconImageCode", null)
            //  PicassoTrustAll.getInstance(this)!!.load(imagepath).error(R.drawable.no_image).into(im_applogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val CustomerNameSP = applicationContext.getSharedPreferences(Config.SHARED_PREF3, 0)
        tv_customername!!.setText(CustomerNameSP.getString("CustomerName", null))
        val CusMobileSP = applicationContext.getSharedPreferences(Config.SHARED_PREF2, 0)
        tv_mobile!!.setText(CusMobileSP.getString("CusMobile", null))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transition: Transition =
                TransitionInflater.from(this).inflateTransition(android.R.transition.move)
            window.sharedElementEnterTransition = transition
        }

        val AddressSP = applicationContext.getSharedPreferences(Config.SHARED_PREF4, 0)
        val AddressSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF299, 0)


        //  Log.i("Addrstest",add!!)
        tv_address!!.setText(
            AddressSP1.getString(
                "Address1",
                null
            ) + " :" + AddressSP.getString("Address", null)
        )


        val EmailSP = applicationContext.getSharedPreferences(Config.SHARED_PREF5, 0)
        val EmailSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF300, 0)

        tv_email!!.setText(
            EmailSP1.getString("Email1", null) + " :" + EmailSP.getString(
                "Email",
                null
            )
        )


        val GenderSP = applicationContext.getSharedPreferences(Config.SHARED_PREF6, 0)
        val GenderSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF308, 0)


        tv_gender!!.setText(
            GenderSP1.getString(
                "Gender1",
                null
            ) + " :" + GenderSP.getString("Gender", null)
        )

        val DateOfBirthSP = applicationContext.getSharedPreferences(Config.SHARED_PREF7, 0)
        val DateOfBirthSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF309, 0)

        tv_dob!!.setText(
            DateOfBirthSP1.getString(
                "DateofBirth1",
                null
            ) + " :" + DateOfBirthSP.getString("DateOfBirth", null)
        )

        val CustomerNumberSP = applicationContext.getSharedPreferences(Config.SHARED_PREF20, 0)
        val CustomerNumberSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF310, 0)

        tv_cusnumber!!.setText(
            CustomerNumberSP1.getString(
                "CustomerNumber1",
                null
            ) + " :" + CustomerNumberSP.getString("CustomerNumber", null)
        )

        val profSP1 = applicationContext.getSharedPreferences(Config.SHARED_PREF344, 0)

        tv_header!!.setText(profSP1.getString("Profile", null))
        setImage()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgHome -> {
                startActivity(Intent(this@ProfileActivity, HomeActivity::class.java))
            }
        }
    }

    fun setImage() {

        val CustomerImageSP = applicationContext.getSharedPreferences(Config.SHARED_PREF356, 0)
        Log.v("dfsddd", "image  " + CustomerImageSP.getString("CusImage", ""))

        try {
            val decodedString: ByteArray =
                Base64.decode(CustomerImageSP.getString("CusImage", ""), Base64.DEFAULT)
            ByteArrayToBitmap(decodedString)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            val stream = ByteArrayOutputStream()
            decodedByte.compress(Bitmap.CompressFormat.PNG, 100, stream)
            Glide.with(this)
                .load(stream.toByteArray())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(profile_image!!)
            Glide.with(this)
                .load(stream.toByteArray())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(profile_image!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("TAG", "1354  $e")
        }
    }
    fun ByteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
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