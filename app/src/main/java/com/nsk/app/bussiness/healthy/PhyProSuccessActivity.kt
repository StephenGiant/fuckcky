package com.nsk.app.bussiness.healthy

import android.content.Intent
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityPhyproBinding
import com.nsk.app.caikangyu.databinding.ActivityPhyprodetailBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils

/**
 * @Package com.nsk.app.bussiness.healthy
 * @author qianpeng
 * @date 2018/9/11.
 * @describe
 */
@Route(path = Routers.topexamsuccess)
class PhyProSuccessActivity:BaseTransStatusActivity() {
    lateinit var phoneNum: String
    lateinit var fuwutime:String
    override fun setTitle(): Int {
       return R.string.order_detail
    }

    override fun getContentLayoutId(): Int {
      return R.layout.activity_phyprodetail
    }

    override fun initData() {
        val bind = binding as ActivityPhyprodetailBinding
        bind.tvCall.setOnClickListener {
            if(::phoneNum.isInitialized) {
                callPhone(phoneNum)
            }
        }
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_mobile").parseData(object : RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val array = jsonElement!!.asJsonArray
                val jsonObject = array[0].asJsonObject
                phoneNum = jsonObject.get("n_code_typename").asString
                bind.tvRexian.text="您现在可拨打"+phoneNum

            }
        },object : RxjavaUtils.CkyErrorConsumer(){

        })
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_servicetime").parseData(object : RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val array = jsonElement!!.asJsonArray
                val jsonObject = array[0].asJsonObject
                fuwutime = jsonObject.get("n_code_typename").asString
                bind.tvKefutime.text = fuwutime
            }
        },object : RxjavaUtils.CkyErrorConsumer(){

        })
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }

    fun callPhone(phoneNum:String){
        val intent = Intent(Intent.ACTION_DIAL)
        val uri = Uri.parse("tel:" + phoneNum)
        intent.setData(uri)
        startActivity(intent)
    }

}