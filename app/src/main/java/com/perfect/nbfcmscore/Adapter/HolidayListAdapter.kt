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
import java.text.SimpleDateFormat
import java.util.*

class HolidayListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_holiday_list, parent, false
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

            if (holder is HolidayListAdapter.MainViewHolder) {

              /*  val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("HolidayDate"))
                val formattedDatesString = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date)*/

                holder.tv_Date!!.setText(jsonObject!!.getString("HolidayDate"))
                holder.tv_Reason!!.setText(jsonObject!!.getString("HolidayReason"))





            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var tv_Date: TextView? = null
        var tv_Reason: TextView? = null


        init {
            tv_Date = v.findViewById<View>(R.id.tv_Date) as TextView
            tv_Reason = v.findViewById<View>(R.id.tv_Reason) as TextView


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}