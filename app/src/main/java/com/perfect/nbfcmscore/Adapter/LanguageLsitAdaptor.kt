package com.perfect.nbfcmscore.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Activity.WelcomeActivity
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LanguageLsitAdaptor(internal val mContext: Context, internal val jsInfo: JSONArray): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var jsonObject: JSONObject? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_language_listing, parent, false
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
            if (holder is MainViewHolder) {
                holder.lang_name!!.setText(jsonObject!!.getString("LanguagesName"))
                holder.lang_shortname!!.setText(jsonObject!!.getString("ShortName"))
                holder.lang_shortname!!.background.setColorFilter(
                    Color.parseColor("#E5E8E8"),
                    PorterDuff.Mode.SRC_ATOP
                )
              //  holder.lang_shortname!!.setTextColor(Color.parseColor(jsonObject!!.getString("ColorCode")));
                holder.llmain!!.setTag(position)
                holder.llmain!!.setOnClickListener { v ->
                    jsonObject = jsInfo.getJSONObject(position)
                    val ID_LanguagesSP = mContext.getSharedPreferences(Config.SHARED_PREF9, 0)
                    val ID_LanguagesEditer = ID_LanguagesSP.edit()
                    ID_LanguagesEditer.putString(
                        "ID_Languages",
                        jsonObject!!.getString("ID_Languages")
                    )
                    ID_LanguagesEditer.commit()
                  //  Toast.makeText(mContext,""+jsonObject!!.getString("ID_Languages"),Toast.LENGTH_LONG).show()
                    val intent = Intent(v.context, WelcomeActivity::class.java)
                    intent.putExtra("id", jsonObject!!.getString("ID_Languages"))
                    v.context.startActivity(intent)
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
}