package com.nsk.app.bussiness.webview;

import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
//import com.orhanobut.logger.Logger;

/**
 * Created by miaozhiyong on 2017/2/9.
 * <p>
 * 辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
 */

public class MsdWebChromeClient extends WebChromeClient {
    private ProgressBar mProgressBar;
    private OnPageListener listener;

    public MsdWebChromeClient(ProgressBar mProgressBar, OnPageListener listener) {
        this.mProgressBar = mProgressBar;
        this.listener = listener;
    }

    /**
     * 处理Javascript中的Alert对话框
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    /**
     * 重写onJsPrompt 方法,需要我们自定一个提示的布局文件
     * 就是一个提示的TextView和输入文本的EditTex而已.
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
        JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 网页Title更改
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
//        Logger.e("网页标题==>>" + title);
        // android 6.0 以下通过title获取 ，6.0以上通过WebViewClient中的 onReceivedHttpError(不过这方法页面上所有的url请求都会定位到，不好用) 判断
        if (title.contains("404") || title.contains("500") || title.contains("Error")) {
            listener.setErrorPage(true);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            if (View.GONE == mProgressBar.getVisibility()) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(newProgress);
        }
    }
}
