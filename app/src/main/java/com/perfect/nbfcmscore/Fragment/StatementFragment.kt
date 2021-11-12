package com.perfect.nbfcmscore.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.perfect.nbfcmscore.R
import java.text.SimpleDateFormat
import java.util.*

class StatementFragment : Fragment() {

    val TAG: String = "StatementFragment"

    var rad_last_month: RadioButton? = null
    var rad_last_3_month: RadioButton? = null
    var rad_last_6_month: RadioButton? = null
    var rad_last_12_month: RadioButton? = null

    var tv_view: TextView? = null
    var tv_download: TextView? = null

    var edt_fromDate: EditText? = null
    var edt_toDate: EditText? = null

    var FromDate: String = ""
    var ToDate: String = ""
    var docType: String = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_statementdownloadview, container, false)


        rad_last_month = v.findViewById<RadioButton>(R.id.rad_last_month)
        rad_last_3_month = v.findViewById<RadioButton>(R.id.rad_last_3_month)
        rad_last_6_month = v.findViewById<RadioButton>(R.id.rad_last_6_month)
        rad_last_12_month = v.findViewById<RadioButton>(R.id.rad_last_12_month)

        edt_fromDate = v.findViewById<EditText>(R.id.edt_fromDate)
        edt_toDate = v.findViewById<EditText>(R.id.edt_toDate)

        tv_view = v.findViewById<TextView>(R.id.tv_view)
        tv_download = v.findViewById<TextView>(R.id.tv_download)

//        rad_last_month!!.setOnClickListener(this)
//        rad_last_3_month!!.setOnClickListener(this)
//        rad_last_6_month!!.setOnClickListener(this)
//        rad_last_12_month!!.setOnClickListener(this)
//
//        edt_fromDate!!.setOnClickListener(this)
//        edt_toDate!!.setOnClickListener(this)
//
//        tv_view!!.setOnClickListener(this)
//        tv_download!!.setOnClickListener(this)

        rad_last_month!!.setOnClickListener {

            val calendar: Calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            val FirstDay: Date = calendar.getTime()

            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val LastDay: Date = calendar.getTime()

            rad_last_month!!.isChecked =true
            rad_last_3_month!!.isChecked =false
            rad_last_6_month!!.isChecked =false
            rad_last_12_month!!.isChecked =false

            FromDate = sdf.format(FirstDay)
            ToDate = sdf.format(LastDay)

            Log.e(TAG,"nextMonthFirstDay    1061   "+FirstDay+ "  "+FromDate)
            Log.e(TAG,"nextMonthLastDay    1061   "+LastDay+"   "+ToDate)
        }

        rad_last_3_month!!.setOnClickListener {

            val calendar: Calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -3)
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            val FirstDay: Date = calendar.getTime()

            val calendar1: Calendar = Calendar.getInstance()
            calendar1.add(Calendar.MONTH, -1)
            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
            val LastDay: Date = calendar1.getTime()

            rad_last_month!!.isChecked =false
            rad_last_3_month!!.isChecked =true
            rad_last_6_month!!.isChecked =false
            rad_last_12_month!!.isChecked =false


            FromDate = sdf.format(FirstDay)
            ToDate = sdf.format(LastDay)

            Log.e(TAG,"nextMonthFirstDay    1063   "+FirstDay+ "  "+FromDate)
            Log.e(TAG,"nextMonthLastDay    1063   "+LastDay+"   "+ToDate)
        }

        rad_last_6_month!!.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -6)
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            val FirstDay: Date = calendar.getTime()

            val calendar1: Calendar = Calendar.getInstance()
            calendar1.add(Calendar.MONTH, -1)
            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
            val LastDay: Date = calendar1.getTime()

            rad_last_month!!.isChecked =false
            rad_last_3_month!!.isChecked =false
            rad_last_6_month!!.isChecked =true
            rad_last_12_month!!.isChecked =false

            FromDate = sdf.format(FirstDay)
            ToDate = sdf.format(LastDay)

            Log.e(TAG,"nextMonthFirstDay    1066   "+FirstDay+ "  "+FromDate)
            Log.e(TAG,"nextMonthLastDay    1066   "+LastDay+"   "+ToDate)

        }

        rad_last_12_month!!.setOnClickListener {

            val calendar: Calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -12)
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            val FirstDay: Date = calendar.getTime()

            val calendar1: Calendar = Calendar.getInstance()
            calendar1.add(Calendar.MONTH, -1)
            calendar1.set(Calendar.DATE, calendar1.getActualMaximum(Calendar.DAY_OF_MONTH))
            val LastDay: Date = calendar1.getTime()

            rad_last_month!!.isChecked =false
            rad_last_3_month!!.isChecked =false
            rad_last_6_month!!.isChecked =false
            rad_last_12_month!!.isChecked =true

            FromDate = sdf.format(FirstDay)
            ToDate = sdf.format(LastDay)

            Log.e(TAG,"nextMonthFirstDay    10612   "+FirstDay+ "  "+FromDate)
            Log.e(TAG,"nextMonthLastDay    10612   "+LastDay+"   "+ToDate)
        }

        edt_fromDate!!.setOnClickListener {
            fromDatePicker()

        }

        edt_toDate!!.setOnClickListener {

            toDatePicker()
        }



        return v
    }

    private fun fromDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_fromDate!!.setText("" + dayOfMonth + "-" + month + "-" + year)
            FromDate = year.toString()+"-"+month.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }
    private fun toDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_toDate!!.setText("" + dayOfMonth + "-" + month + "-" + year)
            ToDate = year.toString()+"-"+month.toString()+"-"+dayOfMonth.toString()
        }, year, month, day)
        dpd.show()
    }


}