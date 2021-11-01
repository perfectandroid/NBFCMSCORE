package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BeneficiaryListAdapter (internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_beneficiarylist, parent, false
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

            if (holder is BeneficiaryListAdapter.MainViewHolder) {
                holder.tv_adp_bene_name!!.setText(jsonObject!!.getString("BeneName"))
                holder.tv_adp_bene_ifsc!!.setText(jsonObject!!.getString("BeneIFSC"))
                holder.tv_adp_bene_account!!.setText(jsonObject!!.getString("BeneAccNo"))

                holder.ll_adp_beneficiary!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(position,"beneficiary")

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var ll_adp_beneficiary: LinearLayout? = null
        var tv_adp_bene_name: TextView? = null
        var tv_adp_bene_account: TextView? = null
        var tv_adp_bene_ifsc: TextView? = null


        init {
            ll_adp_beneficiary = v.findViewById<View>(R.id.ll_adp_beneficiary) as LinearLayout
            tv_adp_bene_name = v.findViewById<View>(R.id.tv_adp_bene_name) as TextView
            tv_adp_bene_account = v.findViewById<View>(R.id.tv_adp_bene_account) as TextView
            tv_adp_bene_ifsc = v.findViewById<View>(R.id.tv_adp_bene_ifsc) as TextView

        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}