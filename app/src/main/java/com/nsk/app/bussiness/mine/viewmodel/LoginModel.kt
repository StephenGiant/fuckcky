package com.nsk.app.bussiness.mine.viewmodel

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParser
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBack
import com.zhouyou.http.exception.ApiException
import org.json.JSONObject
import retrofit2.HttpException


class LoginModel : BaseViewModel(), LoginContract.Presenter{
     var mMainView: LoginContract.View?=null
    override fun attachView(view: LoginContract.View) {
        mMainView = view
    }

    override fun detachView() {
        mMainView = null
    }

    /**
     * 获取验证码
     */
    fun getValidCode(map: Map<String, String>) {

        serviceApi.get(ApiConfig.getValidCode_url,map).compose(RxjavaUtils.transformer())
                .subscribe({
                    LogUtils.e(it.data.toString())
                    mMainView?.getValidCode(it.data.toString())
                },{
                    LogUtils.e(it.message)
                },{

                })
//        EasyHttp.get(ApiConfig.getValidCode_url)
//                .params("mobile", phone)
//                .params("validCodeType", "0")
//                .execute(object : NetCallBack<String>() {
//            override fun onSuccess(t: String?) {
//               LogUtils.e(t)
//            }
//
//            override fun onError(e: ApiException?) {
//                LogUtils.e(e!!.displayMessage)
//            }
//
//        })
            //serviceApi.get(ApiConfig.getValidCode_url,map).
    }

    fun login(hashMap: HashMap<String, String>, heads: HashMap<String, Any>) {
        serviceApi.login(hashMap,heads).compose(RxjavaUtils.transformer())
                .subscribe({
                    val jsonObject = JSONObject(it)
                    CkyApplication.getApp().token = jsonObject.getString("access_token")
                    DbManger.getInstance().put(UserConstants.token,"bearer " + jsonObject.getString("access_token"))
                    mMainView?.success(jsonObject.getString("access_token"))
                },{error ->
//                    ToastUtils.showLong(error.message)
                    if(error is HttpException){
                        val errorBody = error.response().errorBody()!!.string()
                        val jsonObject = JsonParser().parse(errorBody).asJsonObject
                        ToastUtils.showShort(jsonObject.get("error_description").asString)
                    }
                },{

                })
    }
    //重置
    fun ResetPassword(map: Map<String, CharSequence>) {
        serviceApi.get(ApiConfig.resetPassword_url,map).compose(RxjavaUtils.transformer())
                .subscribe({
                    LogUtils.e(it.data.toString())
                    mMainView?.success("")
                },{ error ->
                    if(error is HttpException){
                        val errorBody = error.response().errorBody()!!.string()
                        val jsonObject = JsonParser().parse(errorBody).asJsonObject
                        ToastUtils.showShort(jsonObject.get("error_description").asString)
                    }
                },{

                })
    }
    //注册
    fun register(json: JSONObject) {
        val map = mapOf(Pair("userParam", json.toString()))
        EasyHttp.post(ApiConfig.regist_url)
                .upJson(json.toString())
                .execute(object : CallBack<String>() {
                    override fun onStart() {
                        LogUtils.e("start")
                    }

                    override fun onCompleted() {
                        LogUtils.e("complete")
                    }

                    override fun onError(e: ApiException) {
                        ToastUtils.showLong(e.displayMessage)
                        LogUtils.e(e.message)
                    }

                    override fun onSuccess(o: String) {
                        val jsonObject = JsonParser().parse(o).asJsonObject
                        if(!jsonObject.get("hasError").asBoolean) {
                            mMainView?.success("")
                            LogUtils.e(o + "success")
                        }else{
                            ToastUtils.showShort(jsonObject.get("errorMessage").asString)
                        }
                    }
                })
//        serviceApi.postJson(ApiConfig.regist_url,json).compose(RxjavaUtils.transformer())
//                .subscribe({
//                    LogUtils.e(it.hasError)
//                    if(it.hasError){
//                        ToastUtils.showLong(it.errorMessage)
//                    }else{
//                        mMainView?.success("")
//                    }
//                },{
//                    ToastUtils.showLong(it.message)
//                },{
//
//                })
    }
}