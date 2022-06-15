package com.ferroli.after_sales.salesAppoint

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferroli.after_sales.R

class SalesAppointFragment : Fragment() {

    companion object {
        fun newInstance() = SalesAppointFragment()
    }

    private lateinit var viewModel: SalesAppointViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales_appoint, container, false)
    }



}