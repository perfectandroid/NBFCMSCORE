package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.AccountDetailsActivity
import com.perfect.nbfcmscore.Activity.OTPActivity
import com.perfect.nbfcmscore.Activity.WelcomeActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class AccountLsitAdaptor(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_account_listing, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }

    override fun getItemCount(): Int {
        return jsInfo.length()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            jsonObject = jsInfo.getJSONObject(position)
            if (holder is MainViewHolder) {
                holder.tvaccounttype!!.setText(jsonObject!!.getString("LoanType"))
                holder.tvaccountno!!.setText(jsonObject!!.getString("AccountNumber"))
                holder.tvbal!!.setText("₹ "+ Config.getDecimelFormate(jsonObject!!.getDouble("Balance")))
                holder.tvbranch!!.setText(jsonObject!!.getString("BranchName"))


                holder.llmain!!.setTag(position)
                holder.llmain!!.setOnClickListener { v ->
                    jsonObject = jsInfo.getJSONObject(position)


                    val selectedaccountSP = mContext.getSharedPreferences(Config.SHARED_PREF16, 0)
                    val selectedaccountEditer = selectedaccountSP.edit()
                    selectedaccountEditer.putString("FK_Account", jsonObject!!.getString("FK_Account"))
                    selectedaccountEditer.commit()

                    val SubModuleSP = mContext.getSharedPreferences(Config.SHARED_PREF17, 0)
                    val SubModuleEditer = SubModuleSP.edit()
                    SubModuleEditer.putString("SubModule", jsonObject!!.getString("SubModule"))
                    SubModuleEditer.commit()

                    val StatusSP = mContext.getSharedPreferences(Config.SHARED_PREF18, 0)
                    val StatusEditer = StatusSP.edit()
                    StatusEditer.putString("Status", jsonObject!!.getString("Status"))
                    StatusEditer.commit()

                    val AccountNumberSP = mContext.getSharedPreferences(Config.SHARED_PREF19, 0)
                    val AccountNumberEditer = AccountNumberSP.edit()
                    AccountNumberEditer.putString("AccountNumber", jsonObject!!.getString("AccountNumber"))
                    AccountNumberEditer.commit()

                      val intent = Intent(v.context, AccountDetailsActivity::class.java)
                      intent.putExtra("LoanType",jsonObject!!.getString("LoanType"))
                      intent.putExtra("Balance", jsonObject!!.getString("Balance"))
                      intent.putExtra("AccountNumber", jsonObject!!.getString("AccountNumber"))
                      intent.putExtra("BranchName", jsonObject!!.getString("BranchName"))
                      intent.putExtra("FK_Account", jsonObject!!.getString("FK_Account"))
                      intent.putExtra("Status", jsonObject!!.getString("Status"))
                      intent.putExtra("SubModule", jsonObject!!.getString("SubModule"))
                      intent.putExtra("FundTransferAccount", jsonObject!!.getString("FundTransferAccount"))
                      intent.putExtra("IFSCCode", jsonObject!!.getString("IFSCCode"))
                      intent.putExtra("IsShareAc", jsonObject!!.getString("IsShareAc"))
                      intent.putExtra("EnableDownloadStatement", jsonObject!!.getString("EnableDownloadStatement"))
                      intent.putExtra("IsDue", jsonObject!!.getString("IsDue"))
                      v.context.startActivity(intent)
                    }
                }
            } catch (e: JSONException) {
            e.printStackTrace()
            }
}

inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



internal var llmain: LinearLayout? = null
var tvaccounttype: TextView? = null
var tvaccountno: TextView? = null
var tvbal: TextView? = null
var tvbranch: TextView? = null



init {

llmain = v.findViewById<View>(R.id.llmain) as LinearLayout
tvaccounttype = v.findViewById<View>(R.id.tvaccounttype) as TextView
tvaccountno = v.findViewById<View>(R.id.tvaccountno) as TextView
tvbal = v.findViewById<View>(R.id.tvbal) as TextView
tvbranch = v.findViewById<View>(R.id.tvbranch) as TextView


}
}
}