package com.nsk.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nsk.cky.ckylibrary.UserConstants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

//import tech.jianyue.auth.AuthActivity;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/9/6
 */
    public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, UserConstants.appid, false);
        api.handleIntent(getIntent(), this);
    }
    private IWXAPI api;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("看相应",baseReq.getType()+"");
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("看相应",baseResp.getType()+"");
        finish();
//            finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
