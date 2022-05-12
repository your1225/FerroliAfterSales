package com.ferroli.after_sales.shareFileView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentShareFileViewBinding
import com.ferroli.after_sales.entity.CusServiceFile

class ShareFileViewFragment : Fragment(), ShareFileViewCellAdapter.OnItemCheckedListener {

    private lateinit var binding: FragmentShareFileViewBinding
    private val viewModel by viewModels<ShareFileViewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareFileViewBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mmAdapter = ShareFileViewCellAdapter(this)

        binding.rvDataShareFileView.apply {
            adapter = mmAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.shareFileRecord.observe(viewLifecycleOwner){
            it?.run {
                mmAdapter.submitList(it.childList)
            }
        }

        viewModel.refreshFile()
    }

    override fun onItemChecked(selectedFile: CusServiceFile) {
        viewModel.shareFileRecord.value = selectedFile

//        val bundle = bundleOf(
//            "CkNo" to ckNo,
//            "ITM" to this.selectedItm,
//            "PrdName" to this.selectedItmName,
//            "ScannedCount" to this.selectedScannedCount
//        )
//
//        findNavController().navigate(
//            R.id.action_shareFileViewFragment_to_oneFileViewFragment,
//            bundle
//        )
    }

}