package com.perfect.nbfcmscore.Activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.perfect.nbfcmscore.Helper.IdleUtil
import com.perfect.nbfcmscore.R
import java.util.*

class DashboardActivity1 : AppCompatActivity() {
    var pieChart: PieChart? = null
    var pieData: PieData? = null
    var pieDataSet: PieDataSet? = null
    var pieEntries: ArrayList<String>? = null
    var PieEntryLabels: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dshboard)
        pieChart = findViewById(R.id.pieChart)

       // setPiechart()
    }
    override fun onResume() {
        super.onResume()
        IdleUtil.startLogoutTimer(this, this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        IdleUtil.startLogoutTimer(this, this)
    }


    }
