package com.perfect.nbfcmscore.Adapter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.perfect.nbfcmscore.Helper.Config
import com.perfect.nbfcmscore.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.CalendarContract.Reminders

import android.widget.Toast
import java.text.DateFormat


class DuedateAdapter(internal val mContext: Context, internal val jsInfo: JSONArray, strHeader: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var jsonObject: JSONObject? = null
    var etdate: EditText? = null
    var ettime: EditText? = null
    var title: String? = ""
    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0
    private  var mHour:Int = 0
    private  var mMinute:Int = 0
    var yr = 0
    var month:Int = 0
    var day:Int = 0
    var hr:Int = 0
    var min:Int = 0
    var newDate: Date? = null
    var datecurrent:java.util.Date? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_duedate, parent, false
        )
        vh = MainViewHolder(v)
        return vh
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            jsonObject = jsInfo.getJSONObject(position)
            if (holder is MainViewHolder) {
                if (position % 2 == 1) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#CED1D1"))
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))
                }

                if (jsonObject!!.getString("Type") == "Y") {
                    holder.tvSLNO!!.setTextColor(Color.parseColor("#ff0000"))
                    holder.txtv_duedate!!.setTextColor(Color.parseColor("#ff0000"))
                    holder.txtv_accno!!.setTextColor(Color.parseColor("#ff0000"))
                    holder.txtv_amount!!.setTextColor(Color.parseColor("#ff0000"))
                }
                else {

                }
                holder.txtv_duedate!!.setText(jsonObject!!.getString("DueDate"))
                holder.tvSLNO!!.setText("" + (position + 1))
                holder.txtv_accno!!.setText(jsonObject!!.getString("AccountNo") + "\n(" + jsonObject!!.getString("AccountType") + ")")
                holder.txtv_amount!!.setText("₹" + Config.getDecimelFormate(jsonObject!!.getString("Amount").toDouble()))


                holder.addReminder!!.setTag(position)
                holder.addReminder!!.setOnClickListener(
                        View.OnClickListener {
                            try {
                                jsonObject = jsInfo.getJSONObject(position)
                                setReminder(jsonObject!!.getString("DueDate"), jsonObject!!.getString("AccountType"), jsonObject!!.getString("AccountBranchName"))
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        })
            }
          


        } catch (   /*| ParseException*/e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setReminder(dueDate: String, accountType: String, branchName: String) {
        try {
            val builder = AlertDialog.Builder(mContext)
            val inflater1 = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater1.inflate(R.layout.reminder_popup, null)
            val ll_ok = layout.findViewById<View>(R.id.ll_ok) as LinearLayout
            val ll_cancel = layout.findViewById<View>(R.id.ll_cancel) as LinearLayout
            etdate = layout.findViewById<View>(R.id.etdate) as EditText
            ettime = layout.findViewById<View>(R.id.ettime) as EditText
            val etdis = layout.findViewById<View>(R.id.etdis) as EditText
            etdate!!.setKeyListener(null)
            ettime!!.setKeyListener(null)
            builder.setView(layout)
            val alertDialog = builder.create()
            alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.setView(layout, 0, 0, 0, 0);

            etdis.setText("Your " + accountType.toLowerCase() + " account in " + mContext.getString(R.string.app_name) +
                    "(" + branchName.toLowerCase() + ") will due on " + dueDate + ". Please do the needful actions.")
            val c = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val sdf1 = SimpleDateFormat("hh:mm a")
            val sdf2 = SimpleDateFormat("hh:mm")
            val currentdate = sdf.format(c.time)
            try {
                datecurrent = sdf.parse(currentdate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            //Date datecurrent = c.getTime();
            val datedue = sdf.parse(dueDate)
            val calendar = Calendar.getInstance()
            calendar.time = datedue
            calendar.add(Calendar.DAY_OF_YEAR, -3)
            newDate = calendar.time

            /*   String date = sdf.format(newDate);
            yr= calendar.get(Calendar.YEAR);
            month= calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            //etdate.setText(sdf.format(c.getTime()));
            etdate.setText(date);*/if (newDate!!.after(datecurrent)) {
                val date = sdf.format(newDate)
                yr = calendar[Calendar.YEAR]
                month = calendar[Calendar.MONTH]
                day = calendar[Calendar.DAY_OF_MONTH]
                etdate!!.setText(date)
                newDate = sdf.parse(date)
            }
            if (newDate == datecurrent) {
                val date = sdf.format(newDate)
                yr = calendar[Calendar.YEAR]
                month = calendar[Calendar.MONTH]
                day = calendar[Calendar.DAY_OF_MONTH]
                etdate!!.setText(date)
                newDate = sdf.parse(date)
            }
            if (newDate!!.before(datecurrent)) {
                yr = c[Calendar.YEAR]
                month = c[Calendar.MONTH]
                month = month + 1
                day = c[Calendar.DAY_OF_MONTH]
                etdate!!.setText(sdf.format(c.time))
                newDate = sdf.parse(sdf.format(c.time))
            }
            ettime!!.setText(sdf1.format(c.time))
            val s = sdf2.format(c.time)
            val split = s.split(":".toRegex()).toTypedArray()
            val strhr = split[0]
            val strmin = split[1]
            hr = strhr.toInt()
            min = strmin.toInt()

            /*    yr= c.get(Calendar.YEAR);
            month= c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);*/
            ettime!!.setOnClickListener(View.OnClickListener { timeSelector() })
            etdate!!.setOnClickListener(View.OnClickListener { dateSelector(dueDate) })
            ll_cancel.setOnClickListener { alertDialog.dismiss() }
            ll_ok.setOnClickListener {
                //  alertDialog.dismiss();
                if (newDate!!.after(datecurrent)) {

//                    val inFormat = SimpleDateFormat("dd-MM-yyyy")
//                    val date = inFormat.parse(etdate!!.text.toString())

                    val dates1 = etdate!!.text.toString()
                    val dateParts = dates1.split("-").toTypedArray()

                    day = dateParts[0].toInt()
                    month = dateParts[1].toInt()
                    yr = dateParts[2].toInt()

                    Log.e("TAG","day    201   "+day)
                    Log.e("TAG","month  201   "+month)
                    Log.e("TAG","year   201   "+yr)


                    addEvent(yr, month, day, hr, min, etdis.text.toString(), title + " Due Notification")
                    alertDialog.dismiss()
                }
                if (newDate == datecurrent) {
                    val date = Date()
                    val dateFormat = SimpleDateFormat("HH:mm")
                    dateFormat.format(date)
                    println(dateFormat.format(date))
                    try {
                        if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(hr.toString() + ":" + min))) {
                            val builder = AlertDialog.Builder(mContext)
                            builder.setMessage("Set time greater than current time.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK") { dialog, id -> dialog.dismiss() }
                            val alert = builder.create()
                            alert.show()
                        } else {

                            val dates1 = etdate!!.text.toString()
                            val dateParts = dates1.split("-").toTypedArray()

                            day = dateParts[0].toInt()
                            month = dateParts[1].toInt()
                            yr = dateParts[2].toInt()

                            addEvent(yr, month, day, hr, min, etdis.text.toString(), title + " Due Notification")
                            alertDialog.dismiss()
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
                if (newDate!!.before(datecurrent)) {
                    val builder = AlertDialog.Builder(mContext)
                    builder.setMessage("Set date greater than or equal to current date.")
                            .setCancelable(false)
                            .setPositiveButton("OK") { dialog, id -> dialog.dismiss() }
                    val alert = builder.create()
                    alert.show()
                }
                //addEvent(yr, month, day, hr, min, etdis.getText().toString(),title+" Due Notification");
            }
            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun timeSelector() {
        val c = Calendar.getInstance()
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]
        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(mContext,
                { view, hourOfDay, minute ->
                    val strDate = String.format("%02d:%02d %s", if (hourOfDay == 0) 12 else hourOfDay,
                            minute, if (hourOfDay < 12) "am" else "pm")
                    ettime!!.setText(strDate)
                    hr = hourOfDay
                    min = minute
                }, mHour, mMinute, false)
        timePickerDialog.show()
    }

    fun dateSelector(strdate: String?) {
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(mContext,
                    { view, year, monthOfYear, dayOfMonth ->
                        yr = year
                        month = monthOfYear
                        day = dayOfMonth
                        etdate!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        try {
                            newDate = sdf.parse(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }, mYear, mMonth, mDay)
            /*
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");*/
            var parse: Date? = null
            parse = sdf.parse(strdate)
            val c1 = Calendar.getInstance()
            c1.time = parse
            //  datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
            datePickerDialog.datePicker.minDate = c.timeInMillis
            datePickerDialog.show()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    fun addEvent(iyr: Int, imnth: Int, iday: Int, ihour: Int, imin: Int, descriptn: String, Title: String?) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((mContext as Activity?)!!, arrayOf(Manifest.permission.WRITE_CALENDAR), 1)
        }

        try {
//            val cr: ContentResolver = mContext.getContentResolver()
//            val beginTime = Calendar.getInstance()
//            beginTime[iyr, imnth, iday, ihour] = imin
//            beginTime[2019, 11 - 1, 28, 9] = 30
//            val endTime = Calendar.getInstance()
//            endTime[iyr, imnth, iday, ihour] = imin
//            val values = ContentValues()
//            values.put(CalendarContract.Events.DTSTART, endTime.timeInMillis)
//            values.put(CalendarContract.Events.DTEND, endTime.timeInMillis)
//            values.put(CalendarContract.Events.TITLE, Title)
//            values.put(CalendarContract.Events.DESCRIPTION, "[ $descriptn ]")
//            values.put(CalendarContract.Events.CALENDAR_ID, 1)
//            val tz = TimeZone.getDefault()
//            values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.id)
//            values.put(CalendarContract.Events.EVENT_LOCATION, "India")
//            val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
//            val reminders = ContentValues()
//            reminders.put(CalendarContract.Reminders.EVENT_ID, uri!!.lastPathSegment)
//            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
//            reminders.put(CalendarContract.Reminders.MINUTES, 10)
//            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders)
//            val builder = AlertDialog.Builder(mContext)
//            builder.setMessage("Due date reminder set on calender successfully.")
//                .setCancelable(false)
//                .setPositiveButton("OK") { dialog, id -> dialog.dismiss() }
//            val alert = builder.create()
//            alert.show()


            val cal = Calendar.getInstance()
            val EVENTS_URI: Uri = Uri.parse(getCalendarUriBase(true).toString() + "events")
            val cr: ContentResolver = mContext.getContentResolver()
            val endTime = Calendar.getInstance()
            endTime[iyr, imnth-1, iday, ihour] = imin

            val timeZone = TimeZone.getDefault()

            var values = ContentValues()
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.TITLE,Title)
            values.put(CalendarContract.Events.DESCRIPTION, "[ $descriptn ]")
            values.put(CalendarContract.Events.ALL_DAY, 0)
//            values.put(CalendarContract.Events.DTSTART, endTime.timeInMillis+ 1 * 60 * 1000)
//            values.put(CalendarContract.Events.DTEND, endTime.timeInMillis+ 1 * 60 * 1000)
            values.put(CalendarContract.Events.DTSTART, endTime.timeInMillis)
            values.put(CalendarContract.Events.DTEND, endTime.timeInMillis)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
            values.put(CalendarContract.Events.HAS_ALARM, 1)
            val event: Uri? = cr.insert(EVENTS_URI, values)

          //  Toast.makeText(mContext, "Event added :: ID :: " + event!!.getLastPathSegment(), Toast.LENGTH_SHORT).show()

            val REMINDERS_URI: Uri = Uri.parse(getCalendarUriBase(true).toString() + "reminders")
            values = ContentValues()
            values.put(Reminders.EVENT_ID, event!!.getLastPathSegment()!!.toLong())
            values.put(Reminders.METHOD, Reminders.METHOD_ALERT)
            values.put(Reminders.MINUTES, 10)
            cr.insert(REMINDERS_URI, values)
            Log.e("TAG","Success  1963    "+endTime.time)
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Due date reminder set on calender successfully.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }
        catch (e: Exception){
            Log.e("TAG","Exception  1964    "+e.toString())
        }

    }



    override fun getItemCount(): Int {
        return jsInfo!!.length()
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    inner class MainViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        var txtv_duedate: TextView? = null
        var txtv_amount:TextView? = null
        var txtv_accno:TextView? = null
        var tvSLNO:TextView? = null
        var addReminder: ImageView? = null

        init {

            txtv_duedate = v.findViewById(R.id.txtv_duedate)
            txtv_amount = v.findViewById(R.id.txtv_amount)
            txtv_accno = v.findViewById(R.id.txtv_accno)
            tvSLNO = v.findViewById(R.id.tvSLNO)
            addReminder = v.findViewById(R.id.addReminder)

        }
    }

    private fun getCalendarUriBase(eventUri: Boolean): String? {
        var calendarURI: Uri? = null
        try {
            calendarURI = if (Build.VERSION.SDK_INT <= 7) {
                if (eventUri) Uri.parse("content://calendar/") else Uri.parse("content://calendar/calendars")
            } else {
                if (eventUri) Uri.parse("content://com.android.calendar/") else Uri
                    .parse("content://com.android.calendar/calendars")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return calendarURI.toString()
    }


}