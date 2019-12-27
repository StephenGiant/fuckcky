package com.nsk.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;


/**
 * 权限管理工具类
 */

public class PermissionUtil {
    private PermissionInterface callBack;

    public static final int READ_STORAGE_CODE = 0;
    public static final int WRITE_STORAGE_CODE = 1;
    public static final int GROUP_STORAGE_CODE = 2;
    public static final int CAMERA = 3;
    public static final int CAMERA_WRITE_READ_STORAGE_CODE = 310;

    public class Group {
        public static final String CALENDAR = "android.permission-group.CALENDAR";
        public static final String CAMERA = "android.permission-group.CAMERA";
        public static final String CONTACTS = "android.permission-group.CONTACTS";
        public static final String LOCATION = "android.permission-group.LOCATION";
        public static final String MICROPHONE = "android.permission-group.MICROPHONE";
        public static final String PHONE = "android.permission-group.PHONE";
        public static final String SENSORS = "android.permission-group.SENSORS";
        public static final String SMS = "android.permission-group.SMS";
        public static final String STORAGE = "android.permission-group.STORAGE";
    }

    public class Single {
        public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
        public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

        public static final String CAMERA = "android.permission.CAMERA";

        public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
        public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
        public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

        public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
        public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

        public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

        public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
        public static final String CALL_PHONE = "android.permission.CALL_PHONE";
        public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
        public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
        public static final String ADD_VOICEMAIL = "android.permission.ADD_VOICEMAIL";
        public static final String USE_SIP = "android.permission.USE_SIP";
        public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

        public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

        public static final String SEND_SMS = "android.permission.SEND_SMS";
        public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
        public static final String READ_SMS = "android.permission.READ_SMS";
        public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
        public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

        public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
        public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    }

    public void setCallBack(PermissionInterface callBack) {
        this.callBack = callBack;
    }

    /**
     * 申请单独一个权限
     *
     * @param pPermission 枚举
     */
    public void applyPermission(Activity activity, String pPermission, int pRequestCode) {
        //大于23才需要申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermisson;
            try {
                checkPermisson = ActivityCompat.checkSelfPermission(activity, pPermission);
            } catch (RuntimeException e) {
                return;
            }
            if (checkPermisson == 0 && callBack != null) {
                callBack.permissionSucceed(pRequestCode);
                return;
            }
            if (checkPermisson != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        pPermission)) {
                    shouldShowRationale(activity, pRequestCode, pPermission);
                } else
                    ActivityCompat.requestPermissions(activity, new String[]{pPermission}, pRequestCode);
            } else {
                if (callBack != null) {
                    callBack.permissionSucceed(pRequestCode);
                }
            }
        }else {
            if (callBack != null) {
                callBack.permissionSucceed(pRequestCode);
            }
        }
    }



    private void shouldShowRationale(final Activity activity, final int requestCode, final String requestPermission) {
        showMessageOKCancel(activity, "没有此权限，会导致某些功能无法使用，请开启权限", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{requestPermission},
                        requestCode);
            }
        });
    }

    private void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();

    }

    public void applyMultiPermission(Activity activity, String[] pPermission, int pRequestCode) {
        if (!hasPermissions(activity, pPermission)) {
            ActivityCompat.requestPermissions(activity, pPermission, pRequestCode);
        } else {
            if (callBack != null) {
                callBack.permissionSucceed(pRequestCode);
            }
        }
    }

    private boolean hasPermissions(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public interface PermissionInterface {
        void permissionSucceed(int requestCode);
        void permissionFailed(int requestCode);
    }

    public void attachRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.CAMERA_WRITE_READ_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (callBack != null) {
                    callBack.permissionSucceed(requestCode);
                }
            }
        }
    }

}
