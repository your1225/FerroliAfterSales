package com.ferroli.after_sales.shareFileView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ferroli.after_sales.R
import com.ferroli.after_sales.databinding.FragmentOneFileViewBinding

class OneFileViewFragment : Fragment() {

    private lateinit var binding: FragmentOneFileViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOneFileViewBinding.inflate(layoutInflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refreshGeneralMenu)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        //下面这些直接复制就好
        binding.webViewOneFileView.webViewClient = webClient

        val webSettings = binding.webViewOneFileView.settings
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // 只要缓存可用就加载缓存, 哪怕已经过期失效 如果缓存不可用就从网络上加载数据

        // 缩放操作
//        webSettings.setSupportZoom(false) // 支持缩放 默认为true 是下面那个的前提
//        webSettings.builtInZoomControls = false // 设置内置的缩放控件 若为false 则该WebView不可缩放
//        webSettings.displayZoomControls = false // 隐藏原生的缩放控件

        webSettings.setSupportMultipleWindows(false) // 设置WebView是否支持多窗口

        // 设置自适应屏幕, 两者合用
        webSettings.useWideViewPort = true  // 将图片调整到适合webView的大小
        webSettings.loadWithOverviewMode = true  // 缩放至屏幕的大小
        webSettings.allowFileAccess = true // 设置可以访问文件

        webSettings.setGeolocationEnabled(true) // 是否使用地理位置


    }
}