package com.perfect.nbfcmscore.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.MyaccountsummaryActivity
import com.perfect.nbfcmscore.Activity.NoticeActivity
import com.perfect.nbfcmscore.Activity.StandingInsructionActivity
import com.perfect.nbfcmscore.R
import java.util.*

class MoreOptionFragment : Fragment() , View.OnClickListener{

    private var progressDialog: ProgressDialog? = null
    var rv_ministatementlist: RecyclerView? = null
    var llmyaccountsdetails: LinearLayout? = null
    var llpassbook: LinearLayout? = null
    var llstandinginstruction: LinearLayout? = null
    var llnotice: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.fragment_moreoption, container,
            false
        )

        llmyaccountsdetails = v.findViewById<View>(R.id.llmyaccountsdetails) as LinearLayout?
        llpassbook = v.findViewById<View>(R.id.llpassbook) as LinearLayout?
        llstandinginstruction = v.findViewById<View>(R.id.llstandinginstruction) as LinearLayout?
        llnotice = v.findViewById<View>(R.id.llnotice) as LinearLayout?
        llmyaccountsdetails!!.setOnClickListener(this)
        llpassbook!!.setOnClickListener(this)
        llstandinginstruction!!.setOnClickListener(this)
        llnotice!!.setOnClickListener(this)


        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.llmyaccountsdetails -> {
                startActivity( Intent(context, MyaccountsummaryActivity::class.java) )
            }
            R.id.llpassbook -> {
               // startActivity( Intent(context, ChangeMpinActivity::class.java) )
            }
            R.id.llstandinginstruction -> {
                startActivity( Intent(context, StandingInsructionActivity::class.java) )
            }
            R.id.llnotice -> {
                startActivity( Intent(context, NoticeActivity::class.java) )
            }
        }
    }
}