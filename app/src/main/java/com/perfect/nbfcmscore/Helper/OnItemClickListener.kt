package com.perfect.nbfcmscore.Helper

import android.view.View

interface OnItemClickListener {
    fun onItemClick(v: View?, position: Int, data: String?, mode: String?)
}