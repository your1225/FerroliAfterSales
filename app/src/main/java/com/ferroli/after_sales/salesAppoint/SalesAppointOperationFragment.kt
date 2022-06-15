package com.ferroli.after_sales.salesAppoint

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferroli.after_sales.R

class SalesAppointOperationFragment : Fragment() {

    companion object {
        fun newInstance() = SalesAppointOperationFragment()
    }

    private lateinit var viewModel: SalesAppointOperationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales_appoint_operation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SalesAppointOperationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}