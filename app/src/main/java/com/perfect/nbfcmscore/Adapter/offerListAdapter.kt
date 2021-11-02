package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class offerListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_offerlist, parent, false
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

            if (holder is offerListAdapter.MainViewHolder) {

                holder.tv_amount!!.setText(jsonObject!!.getString("amount"))
                holder.tv_validity!!.setText(jsonObject!!.getString("validity"))
                holder.tv_description!!.setText(jsonObject!!.getString("description"))

                holder.rltv_main!!.setTag(position)
                holder.rltv_main!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(1, holder.tv_amount!!.text.toString())

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        internal var rltv_main: RelativeLayout? = null
        var tv_amount: TextView? = null
        var tv_validity: TextView? = null
        var tv_description: TextView? = null


        init {

            rltv_main = v.findViewById<View>(R.id.rltv_main) as RelativeLayout

            tv_amount = v.findViewById<View>(R.id.tv_amount) as TextView
            tv_validity = v.findViewById<View>(R.id.tv_validity) as TextView
            tv_description = v.findViewById<View>(R.id.tv_description) as TextView


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}