package com.nsk.app.bussiness.loan

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.index.viewmodel.IndexInfoBean
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityLoandetailBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.utils.FinestUtils

/**
 * @Package com.nsk.app.bussiness.loan
 * @author qianpeng
 * @date 2018/9/4.
 * @describe
 */
@Route(path = Routers.loanDetail)
class LoanDetailActivity:BaseTransStatusActivity() {
lateinit var loanbean:IndexInfoBean.LoanInfo.LoanBean
    override fun setTitle(): Int {
        return R.string.loandetailtitle
    }

    override fun getContentLayoutId(): Int {
      return R.layout.activity_loandetail
    }

    override fun initData() {
        //此页面没有刷新数据的需求 故直接用bean
        val loanid = intent.getStringExtra("loanid")
        if(loanid!=null){
//            val loanbean =  intent.getSerializableExtra("loandetail")as IndexInfoBean.LoanInfo.LoanBean
            val bind = binding as ActivityLoandetailBinding
//            bind.loandetailModel = loanbean
            serviceApi.getForString(ApiConfig.loandetail_url+"?n_loan_id="+loanid).parseData(object :RxjavaUtils.CkySuccessConsumer(){
                override fun onSuccess(jsonElement: JsonElement?) {
                    val gson = Gson()
                    loanbean = gson.fromJson<IndexInfoBean.LoanInfo.LoanBean>(jsonElement!!.asJsonObject,IndexInfoBean.LoanInfo.LoanBean::class.java)
                    bind.loandetailModel = loanbean
                }
            },object :RxjavaUtils.CkyErrorConsumer(){

            })
            bind.btnRequestloan.setOnClickListener({
                if (CkyApplication.getApp().hasToken()){
                    FinestUtils.start(viewRoot,this, loanbean.n_loan_url)

                serviceApi.getForString(ApiConfig.viewcard + "?n_loan_id=" + loanbean.n_loan_id).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                    override fun onSuccess(jsonElement: JsonElement?) {
                    }
                }, object : RxjavaUtils.CkyErrorConsumer() {}
                )
            }else{
                    ARouter.getInstance().build(Routers.login).navigation()
                }
            })

        }

    }

    override fun initView() {
    setStatusBarColor(resources.getColor(R.color.white))
        title!!.setNavigationIcon(R.drawable.btn_back)
        title!!.setBackgroundColor(resources.getColor(R.color.white))
    }
}