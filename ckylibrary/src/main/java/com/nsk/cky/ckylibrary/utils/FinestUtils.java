package com.nsk.cky.ckylibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.just.agentweb.AgentWeb;
import com.nsk.cky.ckylibrary.R;
import com.thefinestartist.finestwebview.FinestWebView;

/**
 * Description:
 * Company    :
 *
 * @author :
 * @date : 2018/9/9
 */
public class FinestUtils {
    public static void start(ViewGroup view, Context context, String url){
        Activity activity= (Activity) context;
//        AgentWeb go = AgentWeb.with(activity)
//                .setAgentWebParent(view, new LinearLayout.LayoutParams(-1, -1))
//                .useDefaultIndicator()
//                .createAgentWeb()
//                .ready()
//                .go(url);
        new FinestWebView.Builder(context).theme(R.style.FinestWebViewTheme)
                .stringResRefresh(R.string.refresh_1)
                .showMenuShareVia(false)
                .showMenuCopyLink(false)
                .showMenuOpenWith(false)
                .titleDefault("")
                .showUrl(false)
                .toolbarScrollFlags(0)
                .showSwipeRefreshLayout(false)
                .menuTextGravity(Gravity.CENTER)
                .dividerHeight(0)
                .gradientDivider(false)
                .webViewAppCacheEnabled(true)
                .webViewAllowContentAccess(true)
                .webViewUseWideViewPort(true)
                .webViewDesktopMode(true)
                .webViewDomStorageEnabled(true)
                .webViewAllowFileAccess(true)
                .webViewJavaScriptEnabled(true)
                .webViewJavaScriptCanOpenWindowsAutomatically(true)
                .webViewMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
                .webViewBlockNetworkImage(false)
                .webViewSupportZoom(true)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show(url);
    }


}
