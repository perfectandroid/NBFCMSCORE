package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GoldEstimatorAdaptor (internal val mContext: Context, internal val jsInfo: JSONArray,internal val CalcMethod: String): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    internal var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.goldlist, parent, false
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
            if (holder is GoldEstimatorAdaptor.MainViewHolder) {
                if (CalcMethod.equals("1")){
                    holder.ll_Amount!!.visibility = View.VISIBLE
                    holder.ll_weight!!.visibility = View.GONE
                }
                if (CalcMethod.equals("2")){

                    holder.ll_Amount!!.visibility = View.GONE
                    holder.ll_weight!!.visibility = View.VISIBLE
                }

                // holder.txt_slno!!.setText()
                holder.txt_Type!!.setText(jsonObject!!.getString("TypeName"))
                holder.txt_period!!.setText(jsonObject!!.getString("PeriodFrom")+" - "+jsonObject!!.getString("PeriodTo") )
                holder.txt_roi!!.setText(jsonObject!!.getString("ROI") )
                holder.txt_Amount!!.setText("₹ "+ Config.getDecimelFormate(jsonObject!!.getDouble("Amount")))
                holder.txt_weight!!.setText(jsonObject!!.getString("OrnWeight") )


            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var ll_Amount: LinearLayout? = null
        internal var ll_weight: LinearLayout? = null
        //var txt_slno: TextView? = null
        var txt_Type: TextView? = null
        var txt_period: TextView? = null
        var txt_roi: TextView? = null
        var txt_Amount: TextView? = null
        var txt_weight: TextView? = null



        init {


            ll_Amount = v.findViewById<View>(R.id.ll_Amount) as LinearLayout
            ll_weight = v.findViewById<View>(R.id.ll_weight) as LinearLayout

            txt_Type = v.findViewById<View>(R.id.txt_Type) as TextView
            txt_period = v.findViewById<View>(R.id.txt_period) as TextView
            txt_roi = v.findViewById<View>(R.id.txt_roi) as TextView
            txt_Amount = v.findViewById<View>(R.id.txt_Amount) as TextView
            txt_weight = v.findViewById<View>(R.id.txt_weight) as TextView

        }
    }
}