package com.nsk.cky.ckylibrary.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.nsk.cky.ckylibrary.R;

public class DialogHelper {

    public static void showRationaleDialog(final PermissionUtils.OnRationaleListener.ShouldRequest shouldRequest) {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        new AlertDialog.Builder(topActivity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_rationale_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(false);
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    public static void showOpenAppSettingDialog() {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        new AlertDialog.Builder(topActivity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_denied_forever_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.launchAppDetailsSettings();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}