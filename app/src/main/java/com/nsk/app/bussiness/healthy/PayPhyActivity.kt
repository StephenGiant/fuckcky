package com.nsk.app.bussiness.healthy

import android.text.TextUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.healthy.viewmodel.WorkPhyInfo
import com.nsk.app.bussiness.healthy.viewmodel.WorkPhyOrder
import com.nsk.app.bussiness.mine.viewmodel.MyHealthyBean
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityPayphyBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.wxapi.WXProducts
import java.util.*

@Route(path = Routers.payphy)
class PayPhyActivity :BaseTransStatusActivity() {
    lateinit var product:WorkPhyInfo
    lateinit var order:WorkPhyOrder
    override fun setTitle(): Int {
        return R.string.phypay
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_payphy
    }
    var n_medicalorder_pay_id="2"
    override fun initData() {
        val bind = binding as ActivityPayphyBinding
        product =  intent.getSerializableExtra("product") as WorkPhyInfo
        order = intent.getSerializableExtra("order")as WorkPhyOrder
        Nothings.hospital=product.n_hospital_name
        Nothings.location=product.n_address
        Nothings.tcjj=product.n_exam_product_details
        Nothings.ddbh=order.n_medicalorder_id
        Nothings.ddsj=product.n_group_start_time
        Nothings.orderid = order.n_medicalorder_id
        Nothings.zffs= if (TextUtils.equals(n_medicalorder_pay_id, "2")) "微信" else "支付宝"
        bind.paymodel = product
        bind.orderInfo = order
        bind.btnPay.setOnClickListener({
            pay(order,product)
        })
    }

    override fun onResume() {
        super.onResume()
        if(Nothings.pay){
            Nothings.pay = false
            ARouter.getInstance().build(Routers.orderDetail).withString("source","putong").navigation()
            finish()
        }
        if(Nothings.wanntpay){
            Nothings.wanntpay = false
                if(CkyApplication.getApp().token==null){
                    ARouter.getInstance().build(Routers.login).navigation()
                }else{
                    ARouter.getInstance().build(Routers.my_heal).navigation()
                }
            finish()
        }
    }
    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }

    fun pay(order: WorkPhyOrder,product:WorkPhyInfo){
        val orderid = order.n_medicalorder_id

        val hashMap = HashMap<String, Any>()
        hashMap.put("n_medicalorder_id",orderid)
        hashMap.put("n_medicalorder_payamount",product.n_exam_group_price)
        hashMap.put("n_medicalorder_scoreused",order.useSocre)
        hashMap.put("n_medicalorder_pay_id",n_medicalorder_pay_id)

        serviceApi.getForString(ApiConfig.UpdateEntryExaminationOrder,hashMap).compose(RxjavaUtils.transformer())
                .subscribe({ response ->
                    val  gson= Gson()
                    val jsonParser = JsonParser()
                    val order =  jsonParser.parse(response).asJsonObject.get("data").asString
//                    val l = System.currentTimeMillis()
//                    val random = Random()
//                    val s = random.nextInt(100000000).toString()
//                    val builder = WXPayUtils.WXPayBuilder()
//                    builder.setAppId(UserConstants.appid)
//                            .setPartnerId("1513891351")
//                            .setPrepayId(orderid)
//                            .setPackageValue("Sign=WXPay")
//                            .setTimeStamp(l.toString())
//                            .setNonceStr(s)
//                            .build()
//                            .toWXPayAndSign(this, UserConstants.appid, "E6CF121F5F5F8AEF8DB5A198F98AF6EQ")
                    val wxProducts = WXProducts(this)
                    wxProducts.startPay(product.n_exam_group_price.toFloat()-this.order.deductYuan.toFloat(),orderid)
                    Nothings.wanntpay=true
//                    finish()
//                    bankCardBean = gson.fromJson<BankCardBean>(response, BankCardBean::class.java)

                }, {
                    error ->
                    ToastUtils.showShort(error.message)
                })
    }

    fun createHealthOrderBean(): MyHealthyBean{

        val myHealthyBean = MyHealthyBean()
        myHealthyBean.deductYuan = order.deductYuan
        myHealthyBean.exchangeRatio = order.exchangeRatio
        myHealthyBean.n_exam_group_price = product.n_exam_group_price
        myHealthyBean.n_exam_price = product.n_exam_price.toDouble()
        return myHealthyBean
    }
}