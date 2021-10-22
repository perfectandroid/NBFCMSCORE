package com.perfect.nbfcmscore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.perfect.nbfcmscore.R

class RechargeActivity : AppCompatActivity() , View.OnClickListener {

    var im_back: ImageView? = null
    var im_home: ImageView? = null

    var tv_header: TextView? = null

    var tie_mobilenumber: TextInputEditText? = null
    var tie_operator: TextInputEditText? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)

        setInitialise()
        setRegister()

        if(intent.getStringExtra("from")!!.equals("prepaid")){

            tv_header!!.text = "Prepaid Mobile"
        }
        if(intent.getStringExtra("from")!!.equals("postpaid")){
            tv_header!!.text = "Postpaid Mobile"
        }
        if(intent.getStringExtra("from")!!.equals("landline")){
            tv_header!!.text = "Landline"
        }
        if(intent.getStringExtra("from")!!.equals("dth")){
            tv_header!!.text = "DTH"
        }


    }



    private fun setInitialise() {

        im_back = findViewById<ImageView>(R.id.im_back)
        im_home = findViewById<ImageView>(R.id.im_home)

        tv_header = findViewById<TextView>(R.id.tv_header)

        tie_mobilenumber = findViewById<TextInputEditText>(R.id.tie_mobilenumber)
        tie_operator = findViewById<TextInputEditText>(R.id.tie_operator)


    }

    private fun setRegister() {
        im_back!!.setOnClickListener(this)
        im_home!!.setOnClickListener(this)
        tie_operator!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.im_back ->{
               // onBackPressed()
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }

            R.id.im_home ->{
                startActivity(Intent(this@RechargeActivity, HomeActivity::class.java))
                finish()
            }
            R.id.tie_operator ->{
              //  tie_operator!!.setText("Airtel")

                val dialog = BottomSheetDialog(this)

                // on below line we are inflating a layout file which we have created.
                val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

                val tv_Close = view.findViewById<TextView>(R.id.tv_Close)

                tv_Close.setOnClickListener {
                    tie_operator!!.setText("Airtel")
                    dialog.dismiss()
                }

                dialog.setCancelable(true)

                dialog.setContentView(view)

                dialog.show()
            }
        }
    }
}