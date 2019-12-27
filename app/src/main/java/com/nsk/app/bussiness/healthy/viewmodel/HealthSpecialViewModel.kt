package com.nsk.app.bussiness.healthy.viewmodel

import android.app.Activity
import android.databinding.ObservableField
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonElement
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.HealthyJoinDialog
import io.reactivex.internal.operators.observable.ObservableFilter
import java.io.Serializable

class HealthSpecialViewModel(department:HealthyCategoriBean):BaseViewModel() {
    val departName = ObservableField<String>() //科室的名称
    val adapterField = ObservableField<RecyclerView.Adapter<BaseViewHolder>>()
    val hospitalNameList = ArrayList<String>() //医院列表
    val liucheng = ObservableField<String>()
   lateinit var depart:HealthyCategoriBean
init {
    depart = department
    departName.set(department.n_dept_name)
//    hospitalNameList.clear()
//    hospitalNameList.addAll(getHospitalString())
    adapterField.set(getAdapter(getHospitalString()))
    getLiuCheng()

}
    fun requestHealthy(view:View){
        if(CkyApplication.getApp().hasToken()) {
            val dialog = HealthyJoinDialog()
            dialog.codeType = 4
            dialog.show((view.context as AppCompatActivity).supportFragmentManager, "")
            dialog.listenner = object : HealthyJoinDialog.OnCommitListenner {
                override fun onclickCommit() {
                    val map = mapOf<String, Any>(Pair("n_dept_id", depart.n_dept_id), Pair("n_medicalorder_contacts", dialog.etContact!!.text.toString())
                            , Pair("n_medicalorder_contact_phone", dialog.phoneEdittext!!.text.toString()), Pair("validcode", dialog.etCode!!.text.toString()))
                    serviceApi.getForString(ApiConfig.healthy_createmedicalOrder, map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                        override fun onSuccess(jsonElement: JsonElement?) {
                            dialog.dismiss()
                            ARouter.getInstance().build(Routers.special_success).withString("department", departName.get()).withStringArrayList("hospitals", hospitalNameList).navigation()
                            (view.context as Activity).finish()
                            //跳转到生成订单成功界面
                        }
                    }, object : RxjavaUtils.CkyErrorConsumer() {
                        override fun onNetError(code: String?, message: String?) {
                            super.onNetError(code, message)
                            dialog.dismiss()
                        }

                        override fun onCkyError(code: String?, message: String?) {
                            super.onCkyError(code, message)
                            dialog.dismiss()
                        }

                        override fun onError(e: Throwable?) {
                            super.onError(e)
                            dialog.dismiss()
                        }
                    })
                }
            }
        }else{
            ARouter.getInstance().build(Routers.login).navigation()
        }
    }

    fun getHospitalString():List<String>{
//        val arrayList = ArrayList<String>()
        for (i:Int in 0..depart.nsk_hospitalInfo.size-1){
            hospitalNameList.add(depart.nsk_hospitalInfo.get(i).n_hospital_name)
        }
        return hospitalNameList
    }

    fun getAdapter(list:List<String>):RecyclerView.Adapter<BaseViewHolder>{
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


    fun getLiuCheng(){
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name="+"healthy_ApplicationProcess")
                .parseData(object :RxjavaUtils.CkySuccessConsumer(){
                    override fun onSuccess(jsonElement: JsonElement?) {
                        val json = jsonElement!!.asJsonArray
                        val asString = json.get(0).asJsonObject.get("n_code_typename").asString
                        val replace = asString.replace("\\n", "\n")
                        liucheng.set("服务流程\n"+replace)
                    }
                },object :RxjavaUtils.CkyErrorConsumer(){

                })
    }
}