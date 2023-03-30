package com.perfect.nbfcmscore.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.perfect.nbfcmscore.R

class VirtualcardFragment : Fragment() {

    fun VirtualcardFragment() {
        // Required empty public constructor
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_virtualcard, container, false)
    }

    companion object {
        const val ARG_NAME = "name"

        fun newInstance(name: String): VirtualcardFragment {
            val fragment = VirtualcardFragment()

            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
            }

            fragment.arguments = bundle

            return fragment
        }
    }


}