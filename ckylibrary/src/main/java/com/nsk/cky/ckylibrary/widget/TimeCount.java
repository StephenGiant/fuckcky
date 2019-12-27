package com.nsk.cky.ckylibrary.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/12
 */
public class TimeCount extends CountDownTimer {
    private TextView textView;
    private String tickText;
    private String finishText;

    /**
     * @param millisInFuture    倒计时总时长
     * @param countDownInterval 倒计时单位 毫秒.
     */
    public TimeCount(long millisInFuture, long countDownInterval,
                     TextView textView, String tickText, String finishText) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.tickText=tickText;
        this.finishText=finishText;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(millisUntilFinished / 1000 + tickText);
        textView.setEnabled(false);
    }

    @Override
    public void onFinish() {
        textView.setEnabled(true);
        textView.setText(finishText);
    }
}