package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.ItemClickListener
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LanguageAdapter(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    val TAG: String? = "LanguageAdapter"
    private var clickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_language, parent, false
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
            Log.e(TAG,"jsInfo")
            var id = jsonObject!!.getString("ID_Languages")
            if (holder is LanguageAdapter.MainViewHolder) {
                holder.lang_name!!.setText(jsonObject!!.getString("LanguagesName"))
                holder.lang_shortname!!.setText(jsonObject!!.getString("ShortName"))
//                holder.lang_shortname!!.background.setColorFilter(Color.parseColor("#E5E8E8"), PorterDuff.Mode.SRC_ATOP)
                holder.lang_shortname!!.background.setColorFilter(Color.parseColor("#62b484"), PorterDuff.Mode.SRC_ATOP)
                Log.e(TAG,"ShortName   53   "+jsonObject!!.getString("ShortName"))
                //holder.lang_shortname!!.setTextColor(Color.parseColor(jsonObject!!.getString("ColorCode")));

                holder.llmain!!.setTag(position)
                holder.llmain!!.setOnClickListener { v ->
                    jsonObject = jsInfo.getJSONObject(position)
                    clickListener!!.onClick(position,"lang")
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        internal var llmain: LinearLayout? = null
        var lang_name: TextView? = null
        var lang_shortname: TextView? = null
        var imlogo: ImageView? = null



        init {

            llmain = v.findViewById<View>(R.id.llmain) as LinearLayout
            lang_name = v.findViewById<View>(R.id.lang_name) as TextView
            lang_shortname = v.findViewById<View>(R.id.lang_shortname) as TextView
            imlogo = v.findViewById<View>(R.id.imlogo) as ImageView


        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }
}