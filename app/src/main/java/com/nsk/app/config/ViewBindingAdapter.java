package com.nsk.app.config;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nsk.app.caikangyu.R;
import com.nsk.cky.ckylibrary.widget.CommonTextView;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;


/**
 * Created by dengmingjia on 2017/6/16.
 */

public class ViewBindingAdapter {
    //目前此方法只限于头像
    @BindingAdapter("url")
    public static void loadImage(ImageView view, String url) {
//        ImageLoader.LoaderImg(topview.getContext(), url).into(topview);
        if(url!=null&&url.length()>1) {
            Glide.with(((Activity) view.getContext())).load(url).asBitmap().into(view);
        }else {
//            view.setImageResource(R.drawable.person_icon);
        }
    }
//
//
//
//    @BindingAdapter("headurl")
//    public static void loadHeadImage(ImageView topview, String url) {
//        ImageLoader.LoaderImg(topview.getContext(), url).into(topview);
//    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view, RecyclerView.LayoutManager manager) {
        view.setLayoutManager(manager);
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        if(adapter!=null)
        view.setAdapter(adapter);
    }

    @BindingAdapter("itemDecoration")
    public static void setDecoration(RecyclerView view, RecyclerView.ItemDecoration itemDecoration) {
        view.addItemDecoration(itemDecoration);
    }


    @BindingAdapter("fromhtml")
    public static void setHtmlText(TextView textView, String message) {
        if(message!=null)
        textView.setText(Html.fromHtml(message));
    }

    @BindingAdapter("textColor")
    public static void setTextColor(TextView textView, int resId) {
        textView.setTextColor(textView.getResources().getColor(resId));
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter("android:background")
    public static void setBackground(View view, int resId) {
        view.setBackgroundResource(resId);
    }

    /**
     * 结合commintextview的使用，不适用其他控件
     * @param ct
     * @param text
     */
    @BindingAdapter("cRightTextString")
    public static void setRightString(CommonTextView ct,String text){
        ct.setRightTextString(text);
    }

    /**
     * 当字符串是url的时候就不显示
     * @param textView
     * @param text
     */
    @BindingAdapter("textnourl")
    public static void setTextNourl(TextView textView,String text){
        if(!URLUtil.isValidUrl(text)){
            textView.setText(text);
        }
    }

    /**
     * 金额的显示
     * @param textView
     * @param money
     */
    @BindingAdapter("money")
    public static void setMoney(TextView textView,double money){
        String unit = "￥";
        BigDecimal decimal = new BigDecimal(money);
        DecimalFormat df = new DecimalFormat("0.00");
      textView.setText(unit + df.format(decimal));
    }
    /**
     * 金额的显示
     * @param textView
     * @param money
     */
    @BindingAdapter("money")
    public static void setMoney(TextView textView,String money){
        String unit = "￥";
        BigDecimal decimal = new BigDecimal(money);
        DecimalFormat df = new DecimalFormat("0.00");
        textView.setText(unit + df.format(decimal));
    }
//    @BindingAdapter("round_background")
//    public static void setTextColor(RoundTextView textView, int color) {
//        textView.getDelegate().setBackgroundColor(color).update();
//    }

//    @BindingAdapter("textColorItem")
//    public static void setTextColorItem(TextView textView, boolean color) {
//        textView.setTextColor(textView.getResources().getColor(color ? R.color.cardview_dark_background : R.color.cardview_dark_background));
//    }
//
//    @BindingAdapter("textColorTitle")
//    public static void setTextColorTitle(TextView textView, boolean color) {
//        textView.setTextColor(textView.getResources().getColor(color ? R.color.cardview_dark_background : R.color.cardview_dark_background));
//    }

//    @BindingAdapter("round_stroke_color")
//    public static void setStrokeColor(RoundTextView textView, int color) {
//        textView.getDelegate().setStrokeColor(color);
//    }



//    @BindingAdapter("round_enable_style")
//    public static void setEnableStyle(RoundTextView textView, boolean enable) {
//        Context context = textView.getContext();
//        if (enable) {
//            textView.setTextColor(context.getResources().getColor(R.color.cardview_dark_background));
//            textView.getDelegate().setStrokeColor(context.getResources().getColor(R.color.cardview_dark_background));
//            textView.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.cardview_dark_background));
//        } else {
//            textView.setTextColor(textView.getResources().getColor(R.color.cardview_dark_background));
//            textView.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.cardview_dark_background));
//            textView.getDelegate().setStrokeColor(context.getResources().getColor(android.R.color.transparent));
//        }
//        textView.getDelegate().update();
//    }
//
//    @BindingAdapter("tagColor")
//    public static void tagColor(RoundTextView textView, int colorResId) {
//        int color = textView.getContext().getResources().getColor(colorResId);
//        textView.setTextColor(color);
//        textView.getDelegate().setStrokeColor(color).update();
//    }

}
