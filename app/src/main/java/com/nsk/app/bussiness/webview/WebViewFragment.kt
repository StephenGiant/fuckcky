package com.nsk.app.bussiness.webview

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import com.nsk.cky.ckylibrary.utils.WebUtils


/**
 * Created by qianpeng on 2018/4/23.
 */
class WebViewFragment :Fragment() {
//lateinit var binding: WebviewMainBinding
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
////        DataBindingUtil.inflate(inflater,R.layout.webview_main,container,false)
////       binding =  DataBindingUtil.inflate(inflater,R.layout.webview_main,container,false)
//        binding = DataBindingUtil.inflate(inflater, R.layout.webview_main,container,false)
////       val webview = CustomWebView(context)
////        binding.webviewContainer.addView(webview,container!!.layoutParams.width,container!!.layoutParams.height)
////        binding.webviewContainer.addView()
//        return binding.root;
//    }
companion object {
    fun getInstance(bundle:Bundle):WebViewFragment{
        val webViewFragment = WebViewFragment()
        webViewFragment.arguments = bundle
        return webViewFragment
    }
}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val webView = WebView(container!!.context)
        webView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        val arguments = this.arguments //拿到需要的参数
        WebUtils.initWebviewSettings(webView)
        return webView
    }
    override fun onDestroyView() {
        super.onDestroyView()

    }
}