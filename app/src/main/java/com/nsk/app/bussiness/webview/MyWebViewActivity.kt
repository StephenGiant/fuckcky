package com.nsk.app.bussiness.webview

import android.os.Bundle
import android.webkit.WebView
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.utils.WebUtils


/**
 * Created by qianpeng on 2018/4/23.
 */
@Route(path = Routers.webview)
class MyWebViewActivity: BaseTitleActivity() {

    lateinit var webView:WebView
    lateinit var parent:FrameLayout
    override fun setTitle(): Int {
      return R.string.noletter
    }

    override fun getContentLayoutId(): Int {
        return R.layout.webview_layout
    }

    override fun initData() {

    }

    override fun initView() {

       val url =  intent.getStringExtra("url")
         parent = findViewById(R.id.fl_parent) as FrameLayout
         webView = WebView(this)
        webView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
        WebUtils.initWebviewSettings(webView)
        parent.addView(webView)
        webView.loadUrl(url)

//        webView.webChromeClient = MsdWebChromeClient()
    }

    private var code: String? = null
    private var url: String? = null
    private var queries: String? = null
    private var flags: Bundle? = null
//    override fun initView(savedInstanceState: Bundle?) {
//        code = intent.getStringExtra(WebConst.KEY_CODE)
//        url = intent.getStringExtra(WebConst.KEY_URL)
//        queries = intent.getStringExtra(WebConst.KEY_QUERY)
//        flags = intent.getBundleExtra(WebConst.KEY_FLAG)
//        supportFragmentManager.beginTransaction().add(WebViewFragment(),"webview").commit()
//    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
        parent.removeView(webView)

    }
}