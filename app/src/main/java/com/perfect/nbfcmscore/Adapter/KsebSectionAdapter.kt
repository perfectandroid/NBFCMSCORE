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

class KsebSectionAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_kseb_section, parent, false
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
            if (holder is KsebSectionAdapter.MainViewHolder) {

                holder.txt_section_name!!.setText(jsonObject!!.getString("SecName"))
                holder.txt_section_code!!.setText(jsonObject!!.getString("SecCode"))

                holder.ll_section!!.setTag(position)
                holder.ll_section!!.setOnClickListener(View.OnClickListener {
                    clickListener!!.onClick(position, "section")

                })

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var ll_section: LinearLayout? = null
        var txt_section_name: TextView? = null
        var txt_section_code: TextView? = null

        init {

            ll_section = v.findViewById<View>(R.id.ll_section) as LinearLayout
            txt_section_name = v.findViewById<View>(R.id.txt_section_name) as TextView
            txt_section_code = v.findViewById<View>(R.id.txt_section_code) as TextView

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