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

class PassbookTranscationListAdapter(internal val mContext: Context, internal val jsInfo: JSONArray, internal var submodule: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.passbook_listrow, parent, false
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
                  //  val date = SimpleDateFormat("dd-MM-yyyy").parse("14-02-2018")
                    //println(date.time)
                 //   val format = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss")
                 //   val date: Date = format.parse(jsonObject!!.getString("TransDate"))
                 //   val date = SimpleDateFormat(jsonObject!!.getString("TransDate"))
                     val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).parse(jsonObject!!.getString("TransDate"))
                    val formattedDatesString = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date)
                 //   var date = Date()
                   // val formatter = SimpleDateFormat("dd-MM-yyyy")
                    //val answer: String = formatter.format(date)
                    holder.tv_date!!.setText(formattedDatesString)
                } catch (e: ParseException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
              // holder.tv_date!!.setText(dateString)
                holder.narration!!.setText(
                        jsonObject!!.getString("Narration")
                )
                holder.tv_chequeNo!!.setText(
                        jsonObject!!.getString("chequeNo")
                )

                val transtype = jsonObject!!.getString("TransType")
                if (transtype == "C") {
                    val type = "Cr"
                    holder.trans_amount!!.setText(
                            "\u20B9 " + Config.getDecimelFormate(
                                    jsonObject!!.getDouble("Amount")
                            ).toString() + " " + type
                    )
                    holder.trans_amount!!.setTextColor(
                            ContextCompat.getColor(
                                    mContext!!, R.color.green
                            )
                    )
                } else if (transtype == "D") {
                    val type = "Dr"
                    holder.trans_amount!!.setText(
                            "\u20B9 " + Config.getDecimelFormate(
                                    jsonObject!!.getDouble("Amount")
                            ).toString() + " " + type
                    )
                    holder.trans_amount!!.setTextColor(
                            ContextCompat.getColor(
                                    mContext!!, R.color.redDark
                            )
                    )
                }



                holder.lnLayout!!.setTag(
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
                        })


                /*  var address = ""
                if(!jsonObject!!.getString("Address").equals("")){
                    address = jsonObject!!.getString("Address")
                }
                if(!jsonObject!!.getString("Place").equals("")){
                    address = address+" , "+jsonObject!!.getString("Place")
                }
                if(!jsonObject!!.getString("Post").equals("")){
                    address = address+" , "+jsonObject!!.getString("Post")
                }
                if(!jsonObject!!.getString("District").equals("")){
                    address = address+" , "+jsonObject!!.getString("District")
                }*/

                //holder.tv_Address!!.setText(address)

              /*  holder.llacntdetail!!.setOnClickListener(View.OnClickListener {
                    //  Log.e("ScratchCard","ScratchCard  114    "+jsonObject.getString("CRAmount") );
                    //  clickListener!!.onClick(position)
                    Toast.makeText(mContext, "clicked", Toast.LENGTH_LONG).show()
                    //  getAccountstatementdetails()

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


        public var tv_date: TextView? = null
        public var trans_type:TextView? = null
        public var trans_amount:TextView? = null
        public var narration:TextView? = null
        public var tv_chequeNo:TextView? = null
        public var lnLayout: LinearLayout? = null

        init {

            tv_date = v.findViewById<TextView>(R.id.tv_date)
            trans_type = v.findViewById<TextView>(R.id.trans_type)
            trans_amount = v.findViewById<TextView>(R.id.trans_amount)
            narration = v.findViewById<TextView>(R.id.narration)
            tv_chequeNo = v.findViewById<TextView>(R.id.tv_chequeNo)
            lnLayout = v.findViewById<LinearLayout>(R.id.lnLayout)

        }
    }


}