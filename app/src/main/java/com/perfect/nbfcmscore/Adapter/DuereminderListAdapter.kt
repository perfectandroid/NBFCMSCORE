package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DuereminderListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_duereminder_list, parent, false
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

            if (holder is DuereminderListAdapter.MainViewHolder) {

              /*  val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("HolidayDate"))
                val formattedDatesString = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date)*/

                holder.tv_Ac1!!.setText(jsonObject!!.getString("AccountNo"))
                var amt =jsonObject!!.getString("Amount")
                val amt1 =amt.toDouble()
                holder.tv_Amt1!!.setText("\u20B9 " +Config.getDecimelFormate(amt1))
                holder.tv_Duedate1!!.setText(jsonObject!!.getString("DueDate"))




            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var tv_Ac1: TextView? = null
        var tv_Duedate1: TextView? = null
        var tv_Amt1: TextView? = null


        init {
            tv_Ac1 = v.findViewById<View>(R.id.tv_Acno1) as TextView
            tv_Amt1 = v.findViewById<View>(R.id.tv_amt1) as TextView
            tv_Duedate1 = v.findViewById<View>(R.id.tv_duedate1) as TextView


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}