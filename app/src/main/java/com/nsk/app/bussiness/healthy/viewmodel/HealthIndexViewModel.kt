package com.nsk.app.bussiness.healthy.viewmodel

import android.databinding.ObservableField
import android.graphics.Color
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ItemHealthTopcateBinding
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import java.util.ArrayList

class HealthIndexViewModel :BaseViewModel() {
    var checkedPos = 0
    var departments:ArrayList<HealthyCategoriBean>?=null
    lateinit var depart_adapter:BaseAdapterWithBinding<HealthyCategoriBean>
    lateinit var hosipital_adapter:BaseAdapterWithBinding<HealthyCategoriBean.NskHospitalInfoBean>
    val departments_field = ObservableField<BaseAdapterWithBinding<HealthyCategoriBean>>()
    val hospitals_field = ObservableField<BaseAdapterWithBinding<HealthyCategoriBean.NskHospitalInfoBean>>()
    val adsPicUrl = ObservableField<String>()
    init {
        val templist = arrayListOf(HealthyCategoriBean())

        initData()
    }
    fun toWorkPhysical(view: View){
//        ToastUtils.showShort("工作体检")
        ARouter.getInstance().build(Routers.workphy).navigation()
    }
    fun toProPhysical(view: View){
        ARouter.getInstance().build(Routers.prophy).navigation()
    }
    fun initData(){


        serviceApi.getForString(ApiConfig.healthy_index).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({ response->
                    val gson = Gson()
                    departments = gson.fromJson<ArrayList<HealthyCategoriBean>>(response,object : TypeToken<ArrayList<HealthyCategoriBean>>(){
                    }.type)
                    depart_adapter = object :BaseAdapterWithBinding<HealthyCategoriBean>(departments!!){
                        override fun getLayoutResource(viewType: Int): Int {
                            return R.layout.item_health_topcate
                        }

                        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                            super.onBindViewHolder(holder, position)
                            val bind = holder.binding as ItemHealthTopcateBinding
                            if(checkedPos==position){
                                bind.root.setBackgroundColor(Color.WHITE)
                                bind.viewTag.visibility= View.VISIBLE
                            }else{
                                bind.viewTag.visibility= View.INVISIBLE
                                bind.root.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.gray_fa))
                            }
                            bind.root.setOnClickListener { view ->
                                checkedPos = position
                                if(hosipital_adapter!=null) {
                                    hosipital_adapter.refreshData(departments!!.get(checkedPos).nsk_hospitalInfo)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                        override fun setVariableID(): Int {

                            return BR.depmodel
                        }

                        override fun getItemCount(): Int {
                            return departments!!.size
                        }

                    }
                    hosipital_adapter = object :BaseAdapterWithBinding<HealthyCategoriBean.NskHospitalInfoBean>(departments!!.get(0).nsk_hospitalInfo){
                        override fun getLayoutResource(viewType: Int): Int {
                          return R.layout.item_hospitals
                        }

                        override fun setVariableID(): Int {
                          return BR.hospitalpremodel
                        }

                        override fun getItemCount(): Int {
                           return departments!!.get(checkedPos).nsk_hospitalInfo.size
                        }

                        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                            super.onBindViewHolder(holder, position)
                            holder.binding!!.root.setOnClickListener {
                               requestSpecialHeal(holder.itemView)
                            }
                        }
                    }
                    departments_field.set(depart_adapter)
                    hospitals_field.set(hosipital_adapter)





                },{

                })

        getAdPicurl()
    }

    fun getAdPicurl(){
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_ads").compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({
                    response ->
                    val jsonParser = JsonParser()
                    val jsonObject = jsonParser.parse(response).asJsonArray[0].asJsonObject
                    adsPicUrl.set(jsonObject.get("n_code_typename").asString)

                },{})
    }

    fun requestSpecialHeal(view: View){
//        ToastUtils.showShort("申请医疗特需")
        if(departments!=null&&departments!!.size>0){
           if( CkyApplication.getApp().hasToken()) {
                ARouter.getInstance().build(Routers.healthy_special).withSerializable("department", departments!!.get(checkedPos)).navigation()
            }else{
               ARouter.getInstance().build(Routers.login).navigation()
           }
        }
//        ARouter.getInstance().build("").navigation()
    }
}