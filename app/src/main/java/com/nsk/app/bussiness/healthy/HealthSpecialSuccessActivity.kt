package com.nsk.app.bussiness.healthy

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.base.BaseViewHolder

import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.healthy.viewmodel.HealthSpecialViewModel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityHealthyspecialsuccessBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils

@Route(path = Routers.special_success)
class HealthSpecialSuccessActivity:BaseTransStatusActivity() {
lateinit var phoneNum: String
    lateinit var fuwutime:String
    override fun setTitle(): Int {
        return R.string.order_detail
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_healthyspecialsuccess
    }

    override fun initData() {

        val depart = intent.getStringExtra("department")
        val hospitals = intent.getStringArrayListExtra("hospitals")
        val bind = binding as ActivityHealthyspecialsuccessBinding
        bind.tvCall.setOnClickListener {
            if(::phoneNum.isInitialized) {
                callPhone(phoneNum)
            }
        }
        bind.tvTitleDepartment.text = "科室:"+depart
        bind.rvHospitals.adapter = getAdapter(hospitals)
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_mobile").parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val array = jsonElement!!.asJsonArray
                val jsonObject = array[0].asJsonObject
                 phoneNum = jsonObject.get("n_code_typename").asString
                bind.tvRexian.text="您现在可拨打"+phoneNum

            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_servicetime").parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val array = jsonElement!!.asJsonArray
                val jsonObject = array[0].asJsonObject
                fuwutime = jsonObject.get("n_code_typename").asString
                bind.tvKefutime.text = fuwutime
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

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

    fun getAdapter(list:List<String>): RecyclerView.Adapter<BaseViewHolder>{
        val size = list.size
        val adapter = object : RecyclerView.Adapter<BaseViewHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simplehospital, parent, false)
                return BaseViewHolder(view)
            }

            override fun getItemCount(): Int {
                return list.size
            }

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                val textView = holder.itemView.findViewById<TextView>(R.id.tv_hospitalname)
                textView.text = (position+1).toString()+" " +list.get(position)
            }
        }
        return adapter
    }

}