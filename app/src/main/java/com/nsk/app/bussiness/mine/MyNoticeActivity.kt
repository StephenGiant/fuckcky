package com.nsk.app.bussiness.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.mine.viewmodel.NoticeDetailBean
import com.nsk.app.bussiness.mine.viewmodel.NotifyCenterModel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityNotifycenterBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import java.util.ArrayList

/**
 * @Package com.nsk.app.bussiness.mine
 * @author qianpeng
 * @date 2018/9/5.
 * @describe
 */
@Route(path = Routers.mynotice)
class MyNoticeActivity :BaseTransStatusActivity() {
lateinit var model :NotifyCenterModel
   lateinit var bind:ActivityNotifycenterBinding
    override fun setTitle(): Int {
        return R.string.mynotice
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_notifycenter
    }

    override fun onResume() {
        super.onResume()
        model.initData()
    }
    override fun initData() {
         bind  = binding as ActivityNotifycenterBinding
        model = NotifyCenterModel()
        bind.notifycentermodel = model
        model.initData()
        //升级改造通知
        bind.rlSys.setOnClickListener{
            ARouter.getInstance().build(Routers.notice_detail).withInt(UserConstants.type,1).navigation()
        }
        //个人消息
        bind.rlPerson.setOnClickListener{
            ARouter.getInstance().build(Routers.notice_detail).withInt(UserConstants.type,2).navigation()
        }
        //活动通知
        bind.rlActivity.setOnClickListener{
            ARouter.getInstance().build(Routers.notice_detail).withInt(UserConstants.type,3).navigation()
        }
//        bind.rvNotigications.layoutManager = LinearLayoutManager(this)
//        serviceApi.getForString(ApiConfig.notificationList_url+"?notificationType=2").compose(RxjavaUtils.transformer())
//                .subscribe {
//                    val jsonParser = JsonParser()
//                    val list = arrayListOf<NotificationBean>(NotificationBean())
//                    val json = jsonParser.parse(it).asJsonObject
//                    if(!json.get("hasError").asBoolean){
//                        bind.rvNotigications.adapter = object :BaseAdapterWithBinding<NotificationBean>(list){
//                            override fun getLayoutResource(viewType: Int): Int {
//
//                            }
//
//                            override fun setVariableID(): Int {
//                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                            }
//
//                            override fun getItemCount(): Int {
//                               return 0
//                            }
//                        }
//                    }
//                }

        initDetail(1)

    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }

    /**
     * 递归实现3个接口按顺序调用
     */
    private fun initDetail( type: Int) {
        serviceApi.getForString(ApiConfig.onetypenotificationList_url+"?notificationType="+type).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({
                    response ->
                    val gson = Gson()
                   val list =  gson.fromJson<ArrayList<NoticeDetailBean>>(response,object : TypeToken<ArrayList<NoticeDetailBean>>(){
                    }.type)
                    when(type){
                        1 ->{
                            bind.tvTip.text = list.get(0).n_notice_title
                        }
                        2->{
                            bind.tvTipPerson.text = list.get(0).n_notice_title
                        }
                        3->{
                            bind.tvTipActivity.text = list.get(0).n_notice_title
                        }
                    }
                    if(type<3){
                        initDetail(type+1)
                    }

                },{
//                    ToastUtils.showLong(it.message)
                })

    }
}