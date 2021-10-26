package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.OwnBankFundTransfer
import com.perfect.nbfcmscore.Activity.PassbookTransactionDetailsActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class OwnbankownacntAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_ownbankownact, parent, false
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

            if (holder is OwnbankownacntAdapter.MainViewHolder) {

              /*  val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("HolidayDate"))
                val formattedDatesString = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date)*/

                holder.tv_acnt!!.setText(jsonObject!!.getString("AccountNumber"))
                holder.tv_Branch!!.setText(jsonObject!!.getString("BranchName"))
                var amt =jsonObject!!.getString("Balance")
                val amt1 =amt.toDouble()
                holder.tv_amt!!.setText("\u20B9 " + Config.getDecimelFormate(amt1))

                holder.ll_branchrow!!.setTag(
                        position
                )
                holder.ll_branchrow!!.setOnClickListener(
                        View.OnClickListener {
                            try {
                                jsonObject = jsInfo!!.getJSONObject(position)
                                val i = Intent(mContext, OwnBankFundTransfer::class.java)
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("A/c", jsonObject!!.getString("AccountNumber"))
                                i.putExtra("Branch",  jsonObject!!.getString("BranchName"))
                                i.putExtra("Balance",  jsonObject!!.getString("Balance"))
                                mContext!!.startActivity(i)

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



        var tv_acnt: TextView? = null
        var tv_Branch: TextView? = null
        var tv_amt: TextView? = null
        public var ll_branchrow: LinearLayout? = null

        init {
            tv_acnt = v.findViewById<View>(R.id.tv_Acno) as TextView
            tv_Branch = v.findViewById<View>(R.id.tv_branch) as TextView
            tv_amt = v.findViewById<View>(R.id.tv_amt) as TextView
            ll_branchrow = v.findViewById<LinearLayout>(R.id.ll_branchrow)

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}