package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.PassbookTransactionDetailsActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(internal val mContext: Context, internal val jsInfo: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.msg_listrow, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            jsonObject = jsInfo!!.getJSONObject(position)
            if (holder is MainViewHolder) {
               /* val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val dateString = sdf.format(jsonObject!!.getString("TransDate"))*/


               /* val date = Date(jsonObject!!.getString("TransDate"))
                val formatter5 = SimpleDateFormat("dd-MM-yyyy")
                val formats1 = formatter5.format(date)*/
               /* val date = Date(jsonObject!!.getString("TransDate"))
                val formatter5 = SimpleDateFormat("dd-MM-yyyy")
                val formats1 = formatter5.format(date)
                println(formats1)*/


                try {

                    val head = jsonObject!!.getString("messageHead")
                    val date = jsonObject!!.getString("messageDate")
                    val msg = jsonObject!!.getString("messageDetail")


                    holder.tv_date!!.setText(date)
                    holder.tv_header!!.setText(head)
                    holder.tv_msgdetail!!.setText(msg)
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }



               /* holder.lnLayout!!.setTag(
                        position
                )
                holder.lnLayout!!.setOnClickListener(
                        View.OnClickListener {
                            try {
                                jsonObject = jsInfo!!.getJSONObject(position)
                                val i = Intent(mContext, PassbookTransactionDetailsActivity::class.java)
                                i.putExtra("TransactionID", jsonObject!!.getString("TransactionID"))
                                i.putExtra("SubModule", submodule)
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
    override fun getItemCount(): Int {
        return jsInfo!!.length()
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        public var tv_header: TextView? = null
        public var tv_date:TextView? = null
        public var tv_msgdetail:TextView? = null


        init {

            tv_header = v.findViewById<TextView>(R.id.tv_header)
            tv_date = v.findViewById<TextView>(R.id.tv_date)
            tv_msgdetail = v.findViewById<TextView>(R.id.tv_msgdetail)


        }
    }


}