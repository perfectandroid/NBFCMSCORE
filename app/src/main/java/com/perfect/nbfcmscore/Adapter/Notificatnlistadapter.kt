package com.perfect.nbfc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.R
import java.util.ArrayList

class Notificatnlistadapter(val userList: ArrayList<Notifctnlist>) : RecyclerView.Adapter<Notificatnlistadapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Notificatnlistadapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notification_list_row, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: Notificatnlistadapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Notifctnlist) {
            val textViewtitle = itemView.findViewById(R.id.title) as TextView
            val textViewtitle1  = itemView.findViewById(R.id.title1) as TextView
            val textViewdate  = itemView.findViewById(R.id.tvdate) as TextView
       //     val textView3  = itemView.findViewById(R.id.txtv3) as TextView
            textViewtitle.text = user.title
            textViewtitle1.text = user.title1
            textViewdate.text = user.date

        }
    }
}