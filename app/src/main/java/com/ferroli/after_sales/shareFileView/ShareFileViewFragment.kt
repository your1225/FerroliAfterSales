package com.ferroli.after_sales.shareFileView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentShareFileViewBinding
import com.ferroli.after_sales.entity.CusServiceFile
import com.ferroli.after_sales.entity.urlShareFile

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

        viewModel.shareFileRecord.observe(viewLifecycleOwner) {
            it?.run {
                mmAdapter.submitList(it.childList)
            }
        }

        viewModel.shareFilePath.observe(viewLifecycleOwner) {
            val newPath = urlShareFile + it.replace("\"", "")

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newPath))
            startActivity(browserIntent)

//            val bundle = bundleOf(
//                "FileInfo" to it
//            )
//
//            findNavController().navigate(
//                R.id.action_shareFileViewFragment_to_showPdfFragment,
//                bundle
//            )
        }

        viewModel.refreshFile()
    }

    override fun onItemChecked(selectedFile: CusServiceFile) {
        if (selectedFile.isFolder) {
            viewModel.shareFileRecord.value = selectedFile
        } else {
            viewModel.getFilePath(selectedFile.filePath + selectedFile.fileName)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refreshGeneralMenu -> {
                viewModel.refreshFile()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}