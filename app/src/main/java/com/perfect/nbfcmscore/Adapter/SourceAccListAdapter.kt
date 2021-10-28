package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.OwnBankotheraccountFundTransfer
import com.perfect.nbfcmscore.Activity.OwnBankownaccountFundTransfer
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SourceAccListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray, internal val FKAccount: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_source_acclist, parent, false
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


            if (holder is SourceAccListAdapter.MainViewHolder) {
                holder.tv_acc_no!!.setText(jsonObject!!.getString("AccountNumber"))
                holder.tv_branch_name!!.setText(jsonObject!!.getString("BranchName"))
                var amt =jsonObject!!.getString("Balance")
                val amt1 =amt.toDouble()
                holder.tv_balance!!.setText("\u20B9 " + Config.getDecimelFormate(amt1))

                holder .ll_accounts!!.setOnClickListener(View.OnClickListener {
                    try {
                        jsonObject = jsInfo!!.getJSONObject(position)
                        if (FKAccount == 0) {

                            val i = Intent(mContext, OwnBankownaccountFundTransfer::class.java)
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("A/c", jsonObject!!.getString("AccountNumber"))
                            i.putExtra("Branch", jsonObject!!.getString("BranchName"))
                            i.putExtra("Balance", jsonObject!!.getString("Balance"))
                            mContext!!.startActivity(i)
                        } else {
                            val i = Intent(mContext, OwnBankotheraccountFundTransfer::class.java)
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("A/c", jsonObject!!.getString("AccountNumber"))
                            i.putExtra("Branch", jsonObject!!.getString("BranchName"))
                            i.putExtra("Balance", jsonObject!!.getString("Balance"))
                            mContext!!.startActivity(i)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                })

            }









        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        var tv_acc_no: TextView? = null
        var tv_balance:TextView? = null
        var tv_branch_name:TextView? = null
        var ll_accounts: LinearLayout? = null


        init {

            tv_balance = v.findViewById(R.id.tv_balance)
            tv_acc_no = v.findViewById(R.id.tv_acc_no)
            tv_branch_name = v.findViewById(R.id.tv_branch_name)
            ll_accounts = v.findViewById(R.id.ll_accounts)


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}