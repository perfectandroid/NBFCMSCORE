package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
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

class FundTransferStatusAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_fund_transfer, parent, false
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

            if (holder is FundTransferStatusAdapter.MainViewHolder) {

              /*  val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("HolidayDate"))
                val formattedDatesString = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(date)*/

                holder.TransDate!!.setText(jsonObject!!.getString("TransDate"))
                holder.Amount!!.setText(jsonObject!!.getString("Amount"))
                holder.Remarks!!.setText(jsonObject!!.getString("Remarks"))

           /*     holder.ll_productlist!!.setTag(
                        position
                )
                holder.ll_productlist!!.setOnClickListener(
                        View.OnClickListener {
                            try {
                                jsonObject = jsInfo!!.getJSONObject(position)
                                val i = Intent(mContext, ProductListDetailsActivity::class.java)
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("FK_Product", jsonObject!!.getString("FK_Product"))
                                mContext!!.startActivity(i)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        })*/



            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var TransDate: TextView? = null
        var Amount: TextView? = null
        var Remarks: TextView? = null
        public var ll_productlist: LinearLayout? = null

        init {
            TransDate = v.findViewById<View>(R.id.TransDate) as TextView
            Amount = v.findViewById<View>(R.id.Amount) as TextView
            Remarks = v.findViewById<View>(R.id.Remarks) as TextView


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}