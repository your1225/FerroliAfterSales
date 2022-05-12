package com.ferroli.after_sales.partImageView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentPartImageViewBinding
import com.ferroli.after_sales.entity.urlFileBase

class PartImageViewFragment : Fragment() {

    private lateinit var binding: FragmentPartImageViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartImageViewBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bPaiCode = arguments?.getString("PartCode", "null")

        if (bPaiCode != null && bPaiCode != "null") {
            Glide.with(requireContext())
                .load("$urlFileBase$bPaiCode.jpg")
                .centerCrop()
                .placeholder(R.drawable.ic_default_part_img)
                .into(binding.imageViewPartImageView)
        }
    }
}