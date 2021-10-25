package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PassbookTransactionDetailsAdapter(internal val mContext: Context, internal val jsInfo: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.activity_passbookdetails, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            jsonObject = jsInfo!!.getJSONObject(position)
            if (holder is MainViewHolder) {

                holder.tvkey!!.setText(
                        jsonObject!!.getString("Key")
                )
                holder.tvvalue!!.setText(
                        jsonObject!!.getString("Value")
                )

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        return jsInfo!!.length()
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        var tvkey: TextView? = null
        var tvvalue: TextView? = null

        init {

            tvvalue = v.findViewById(R.id.tvvalue)
            tvkey = v.findViewById(R.id.tvkey)

        }
    }


}