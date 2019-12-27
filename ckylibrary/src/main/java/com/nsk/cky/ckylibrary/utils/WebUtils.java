package com.nsk.cky.ckylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


import java.util.Map;

/**
 * Desc:   webview 相关工具类
 * Creator ling
 * Date:   2017/6/6 0006 17:54
 */

public class WebUtils {
    @SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
    public static void initWebviewSettings(WebView webView) {
        WebSettings settings = webView.getSettings();
        //设置是否支持Javascript
        settings.setJavaScriptEnabled(true);
        //设置是否支持变焦
        settings.setSupportZoom(true);
       /* settings.setBuiltInZoomControls(true);*/
        //启用或禁止WebView访问文件数据
        settings.setAllowFileAccess(true);
        //
        settings.setUseWideViewPort(true);
        //是否显示网络图像
        settings.setBlockNetworkImage(false);
        //
        settings.setDomStorageEnabled(true);
        // 防止用户密码被webview 明文存储
        settings.setSavePassword(false);

        //自适应手机屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        //解决三星手机图片显示问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //移除几个有风险的接口
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }

//    public static void startWebByCode(Activity activity, String code) {
//        startWeb(activity, code, null, null, null);
//    }
//
//    public static void startWebByCode(Activity activity, String code, Map<String, String> queryMap,
//        Bundle flags) {
//        startWeb(activity, code, null, queryMap, flags);
//    }
//
//    public static void startWebByUrl(Activity activity, String url) {
//        startWeb(activity, null, url, null, null);
//    }
//
//    private static void startWebByUrl(Activity activity, String url, Map<String, String> queryMap,
//        Bundle flags) {
//        startWeb(activity, null, url, queryMap, flags);
//    }
//
//    /**
//     * @param code     根据code跳转相应的H5页面
//     * @param url      根据url跳转相应的H5页面
//     * @param queryMap 请求参数(不包含公共参数),需要转化成  ?a=1&b=2
//     * @param flags    webview其他标志参数(可为空)
//     */
//    private static void startWeb(Activity activity, String code, String url,
//        Map<String, String> queryMap, Bundle flags) {
//        Intent intent = new Intent(activity, WebViewActivity.class);
//        if (!StrUtils.isEmpty(code)) {
//            intent.putExtra(WebConst.KEY_CODE, code);
//        } else {
//            intent.putExtra(WebConst.KEY_URL, url);
//        }
//        if (flags != null) {
//            intent.putExtra(WebConst.KEY_FLAG, flags);
//        }
//        String queries = map2Str(queryMap);
//        Logger.e("query ==>>" + queries);
//        intent.putExtra(WebConst.KEY_QUERY, queries);
//        activity.startActivity(intent);
//    }
//
//
//    public static String map2Str(Map<String, String> paramMap) {
//        String queries = "";
//        if (paramMap != null && paramMap.keySet().size() > 0) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("?");
//            for (String key : paramMap.keySet()) {
//                String value = String.valueOf(paramMap.get(key));
//                sb.append(key).append("=").append(value).append("&");
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            queries = sb.toString();
//        }
//        return queries;
//    }
}
