package com.nsk.app.bussiness.loan.viewmodel

import android.databinding.ObservableField
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.bussiness.index.viewmodel.IndexInfoBean
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.R
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger

class LoanIndexViewmodel() :BaseViewModel() {
    val LoanAdapter = ObservableField<BaseAdapterWithBinding<IndexInfoBean.LoanInfo.LoanBean>>()
    lateinit var list:List<IndexInfoBean.LoanInfo.LoanBean>
    fun initData(){
     fetchData(1)
    }

    fun toSmallLoan(view: View){
     fetchData(1)
    }
    fun toBigLoan(view: View){
   fetchData(2)
    }
    fun toBankLoan(view: View){
     fetchData(3)
    }
    fun toMyLoan(view: View){
        if( CkyApplication.getApp().token!=null&& CkyApplication.getApp().token!!.length>0){
            ARouter.getInstance().build(Routers.my_loan).navigation()
        }else{
            ARouter.getInstance().build(Routers.login).navigation()
        }


    }

    fun onItemClick(position:Int){
        ARouter.getInstance().build(Routers.loanDetail).withString("loanid",list.get(position).n_loan_id.toString()).navigation()
    }

    fun fetchData(type:Int){
        val map =  mapOf<String,String>(Pair("n_area_id",DbManger.getInstance().get(UserConstants.areaId)), Pair("orderProperty",type.toString()))
        serviceApi.getForString(ApiConfig.orderloanlist,map).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val parser = JsonParser()
                    val loanDatas = parser.parse(response).asJsonArray
                    val jsonList = ArrayList<JsonObject>()
                    for (i:Int in 0..loanDatas.size()-1){
                        jsonList.add(loanDatas.get(i).asJsonObject)
                    }
                    val gson = Gson()
                    list = gson.fromJson<List<IndexInfoBean.LoanInfo.LoanBean>>(loanDatas, object : TypeToken<List<IndexInfoBean.LoanInfo.LoanBean>>() {}.type)

                    //借款超市数据
                    val  adapter = object :BaseAdapterWithBinding<IndexInfoBean.LoanInfo.LoanBean>(list){
                        override fun getLayoutResource(viewType: Int): Int {
                            return R.layout.item_loanpreview
                        }

                        override fun setVariableID(): Int {
                            return BR.loanpremodel
                        }

                        override fun getItemCount(): Int {
                            return list.size
                        }
                    }
                    LoanAdapter.set(adapter)

                },{
                    error ->
//                    ToastUtils.showShort(error.message)
                })
    }

}