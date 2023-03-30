package com.perfect.nbfcmscore.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.*
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import java.util.*

class MoreOptionFragment : Fragment() , View.OnClickListener{

    private var progressDialog: ProgressDialog? = null
    var rv_ministatementlist: RecyclerView? = null
    var llmyaccountsdetails: LinearLayout? = null
    var llpassbook: LinearLayout? = null
    var llstandinginstruction: LinearLayout? = null
    var llnotice: LinearLayout? = null
    var data: String?=null
    var fkacc:String?=null
    var submodle:String?=null

    var txtvpassbk:TextView?=null
    var txtv_acntsummry:TextView?=null
    var txtvstand:TextView?=null
    var txtv_notice:TextView?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.fragment_moreoption, container,
            false
        )
        val activity: AccountDetailsActivity? = activity as AccountDetailsActivity?
        data = activity!!.sendpassbookData()

        val st = StringTokenizer(data, ",")
         fkacc = st.nextToken()
       submodle = st.nextToken()
       // val submodule = st.nextToken()

        Log.i("Values",fkacc+"\n"+submodle)



        llmyaccountsdetails = v.findViewById<View>(R.id.llmyaccountsdetails) as LinearLayout?
        llpassbook = v.findViewById<View>(R.id.llpassbook) as LinearLayout?
        llstandinginstruction = v.findViewById<View>(R.id.llstandinginstruction) as LinearLayout?
        llnotice = v.findViewById<View>(R.id.llnotice) as LinearLayout?

        txtvpassbk = v.findViewById<View>(R.id.txtvpassbk) as TextView?
        txtv_acntsummry = v.findViewById<View>(R.id.txtv_acntsummry) as TextView?
        txtvstand = v.findViewById<View>(R.id.txtvstand) as TextView?
        txtv_notice = v.findViewById<View>(R.id.txtv_notice) as TextView?

        val ID_passbk = context!!.getSharedPreferences(Config.SHARED_PREF51,0)
        txtvpassbk!!.setText(ID_passbk.getString("passbook",null))

        val ID_accsummry = context!!.getSharedPreferences(Config.SHARED_PREF214,0)
        txtv_acntsummry!!.setText(ID_accsummry.getString("AccountSummary",null))

        val ID_stand = context!!.getSharedPreferences(Config.SHARED_PREF215,0)
        txtvstand!!.setText(ID_stand.getString("StandingInstruction",null))

        val ID_notice = context!!.getSharedPreferences(Config.SHARED_PREF216,0)
        txtv_notice!!.setText(ID_notice.getString("Notice",null))



        llmyaccountsdetails!!.setOnClickListener(this)
        llpassbook!!.setOnClickListener(this)
        llstandinginstruction!!.setOnClickListener(this)
        llnotice!!.setOnClickListener(this)


        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.llmyaccountsdetails -> {
                startActivity(Intent(context, MyaccountsummaryActivity::class.java))
            }
            R.id.llpassbook -> {
                var intent = Intent(activity, PassbookActivityMoreoption::class.java)
                intent.putExtra("fkaccount", fkacc)
                intent.putExtra("submodule", submodle)
                startActivity(intent)

            }
            R.id.llstandinginstruction -> {
                startActivity(Intent(context, StandingInsructionActivity::class.java))
            }
            R.id.llnotice -> {
                startActivity(Intent(context, NoticeActivity::class.java))
            }
        }
    }
}