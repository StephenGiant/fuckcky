package com.nsk.app.bussiness.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import com.orhanobut.logger.Logger;

/**
 * Desc:
 * Creator ling
 * Date:   2017/6/7 0007 14:44
 */

public class MsdWebViewClient extends WebViewClient {

    private Activity context;
    private boolean needClearHistory;
    private OnPageListener listener;

    public MsdWebViewClient(Activity context, OnPageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setNeedClearHistory(boolean needClearHistory) {
        this.needClearHistory = needClearHistory;
    }

    /**
     * 网页开始加载
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
//        Logger.e("onPageStarted==>>" + url);
    }

    /**
     * 网页加载完毕，此方法并没有方法名表现的那么美好，调用时机很不确定。
     * 如需监听网页加载完成可以使用onProgressChanged，当int progress返回100时表示网页加载完毕。
     * <p>
     * 当页面返回404或500时也会走这个方法
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
//        Logger.e("onPageFinished==>>" + url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description,
        String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        // 断网或者网络连接超时
        if (errorCode == ERROR_HOST_LOOKUP
            || errorCode == ERROR_CONNECT
            || errorCode == ERROR_TIMEOUT) {
            if (listener != null) {
                listener.setErrorPage(true);
            }
        }
    }

    /***
     * 更新历史记录
     */
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (needClearHistory) {
            setNeedClearHistory(false);
            view.clearHistory();
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Logger.e("shouldOverrideUrlLoading==>>" + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //require api 21
       /* String url = request.getUrl().toString();*/
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * WebView发生改变
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    /**
     * 应用程序重新请求网页数据
     */
    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        super.onFormResubmission(view, dontResend, resend);
    }
}
