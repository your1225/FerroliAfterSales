package com.ferroli.after_sales.basePartInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.BasePartInfoFragmentBinding

class BasePartInfoFragment : Fragment() {

    private lateinit var binding: BasePartInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BasePartInfoFragmentBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}