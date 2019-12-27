package com.nsk.app.bussiness.index.viewmodel

import android.app.Activity
import android.databinding.ObservableField
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.bussiness.index.MineFragment
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.app.utils.RxjavaUtils
import com.wx.goodview.GoodView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MineFragmentViewModel() :BaseViewModel() {
    val url= ObservableField<String>()
    val phoneNO= ObservableField<String>()
    val score= ObservableField<String>()
    val singned = ObservableField<Boolean>()
    val logined=ObservableField<Boolean>()
     var data:JSONObject? = null
    fun hasToken() = CkyApplication.getApp().token!=null
    init {
        singned.set(false)
    }
    fun signIn(view: View){
        //签到
        if(hasToken()) {
            serviceApi.get(ApiConfig.signIn_url).compose(RxjavaUtils.transformer()).subscribe(
                    {
                        if (!it.hasError) {
                            val goodView = GoodView(view.context)
                            goodView.setText("+1")
                            goodView.show(view)
                            singned.set(true)
                            //刷新个人信息
                            initUser()
                        } else {
//                        val goodView = GoodView(topview.context)
//                        goodView.setText("+1")
//                        goodView.show(topview)
                            singned.set(true)
                            Toast.makeText(view.context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }, {

            }
            )
        }else{

        }

    }
    fun toMyLoan(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.my_loan).navigation()
        }
    }
    fun toMyHealth(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else{
            ARouter.getInstance().build(Routers.my_heal).navigation()
        }

    }
    fun toHelp(view:View){
        ARouter.getInstance().build(Routers.help).navigation()
    }
    fun toPersonInfo(view:View){
        if(hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.perInfo).navigation()
        }
    }
    fun toLogin(view: View){
        if(hasToken()){
            if(data!=null)
            ARouter.getInstance().build(Routers.perInfo).withString("person",data.toString()).navigation(view.context as Activity,MineFragment.NEEDREFRESH)
        }else {
            ARouter.getInstance().build(Routers.login).navigation()
        }
    }
    fun toSettings(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.settings_activity).navigation()
        }
    }
    fun toScoreDetail(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            if(data!=null)
            ARouter.getInstance().build(Routers.myscore).withString("score",data!!.getInt("n_user_score").toString()).navigation()
        }
    }
    fun toInvites(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.invite_activity).navigation()
        }
    }

    fun toMyCards(view: View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.mycards).navigation()
        }
    }

    fun toMyPacket(view:View){
        if(!hasToken()){
            ARouter.getInstance().build(Routers.login).navigation()
        }else {
            ARouter.getInstance().build(Routers.mypacket).navigation()
        }
    }
    fun initUser(){
        if(hasToken()) {
            serviceApi.getForString(ApiConfig.getUserInfo_url).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                        val jsonObject = JSONObject(response)
                        val error = jsonObject.getBoolean("hasError")

                        if (!error) {
                            data = jsonObject.getJSONObject("data")
                            url.set(data!!.getString("n_user_pic"))
                            phoneNO.set(data!!.getString("n_user_mobile"))
                            score.set("积分" + data!!.getInt("n_user_score").toString())
                            singned.set(data!!.getBoolean("n_user_signin"))
                            logined.set(true)

                            DbManger.getInstance().put(UserConstants.name,data!!.getString("n_user_nickname"))
                            DbManger.getInstance().put(UserConstants.cercard,data!!.getString("n_user_idcard"))
                            DbManger.getInstance().put(UserConstants.phone,data!!.getString("n_user_mobile"))
                        } else {
                            url.set("")
                            phoneNO.set("")
                            score.set("")
                            singned.set(false)
                            logined.set(false)
                        }

                    }, { error ->
                        error.printStackTrace()

                    }
                    )
        }
    }
}