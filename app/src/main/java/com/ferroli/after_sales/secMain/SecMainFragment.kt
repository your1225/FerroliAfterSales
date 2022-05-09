package com.ferroli.after_sales.secMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.SecMainFragmentBinding

class SecMainFragment : Fragment() {

    private lateinit var binding: SecMainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecMainFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}