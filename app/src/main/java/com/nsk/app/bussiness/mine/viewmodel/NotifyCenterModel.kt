package com.nsk.app.bussiness.mine.viewmodel

import android.databinding.ObservableField
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonParser
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils

class NotifyCenterModel:BaseViewModel() {
    val sysNotifyCount = ObservableField<String>()
    val personNotifyCount = ObservableField<String>()
    val actNotifyCount = ObservableField<String>()
    val sysNotifyTitle = ObservableField<String>()
    val personNotifyTitle = ObservableField<String>()
    val actNotifyTitle = ObservableField<String>()
    val showSys = ObservableField<Boolean>()
    val showPerson= ObservableField<Boolean>()
    val showAct = ObservableField<Boolean>()
    fun hasToken ()= CkyApplication.getApp().token==null&&CkyApplication.getApp().token!!.length>0
    fun initData(){
        serviceApi.getForString(ApiConfig.notificationList_url).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({
                    response ->
                    val jsonParser = JsonParser()
                    val jsonArray = jsonParser.parse(response).asJsonArray

                    for (i:Int in 0..jsonArray.size()-1){
                        val title = jsonArray.get(i).asJsonObject.get("latest_notification_title").asString
                        val count = jsonArray.get(i).asJsonObject.get("unread_notification_num").asInt
                        if(jsonArray.get(i).asJsonObject.get("notification_type").asInt==1){
                            sysNotifyTitle.set(title)
                            sysNotifyCount.set(count.toString())
                            if(count>0){
                                showSys.set(true)
                            }else{
                                showSys.set(false)
                            }
                        }else if(jsonArray.get(i).asJsonObject.get("notification_type").asInt==2){
                            personNotifyCount.set(count.toString())
                            personNotifyTitle.set(title)
                            if(count>0){
                                showPerson.set(true)
                            }else{
                                showPerson.set(false)
                            }
                        }else {
                            actNotifyCount.set(count.toString())
                            actNotifyTitle.set(title)
                            if(count>0){
                                showAct.set(true)
                            }else{
                                showAct.set(false)
                            }
                        }
                    }
                },{})


    }

    fun toSysNotify(view: View){
        ARouter.getInstance().build(Routers.mynotice).withInt("notifyType",1).navigation()
    }
    fun toPersonNotify(view: View){
        //需要检查登陆
        if(hasToken()){
            ARouter.getInstance().build(Routers.mynotice).withInt("notifyType",2).navigation()
        }

    }

    fun toActNotify(view: View){
        ARouter.getInstance().build(Routers.mynotice).withInt("notifyType",3).navigation()
    }
}