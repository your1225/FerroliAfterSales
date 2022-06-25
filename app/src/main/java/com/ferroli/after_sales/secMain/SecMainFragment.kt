package com.ferroli.after_sales.secMain

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.SecMainFragmentBinding

class SecMainFragment : Fragment() {

    private lateinit var binding: SecMainFragmentBinding
    private val viewModel by viewModels<SecMainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SecMainFragmentBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dingUserWithState.observe(viewLifecycleOwner){
            if (it != null){
                Glide.with(requireContext())
                    .load(it.avatar)
                    .centerCrop()
                    .placeholder(R.drawable.ic_ferroli_after_sales_logo)
                    .into(binding.ivPhotoSecMain)

                binding.tvUserNameSecMain.text = it.userName
                binding.tvCusNameSecMain.text = it.cusName
                binding.tvState1CountSecMain.text = it.stateCount1.toString()
                binding.tvState2CountSecMain.text = it.stateCount2.toString()
                binding.tvState3CountSecMain.text = it.stateCount3.toString()
                binding.tvState4CountSecMain.text = it.stateCount4.toString()
            } else {
                binding.tvUserNameSecMain.text = ""
                binding.tvCusNameSecMain.text = ""
                binding.tvState1CountSecMain.text = ""
                binding.tvState2CountSecMain.text = ""
                binding.tvState3CountSecMain.text = ""
                binding.tvState4CountSecMain.text = ""
            }
        }

        getUserInfo()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.refreshGeneralMenu -> {
                getUserInfo()
//                Toast.makeText(requireContext(), "Recor", Toast.LENGTH_LONG).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getUserInfo() {
        viewModel.getUserInfo()
    }
}