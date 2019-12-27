package com.nsk.app.bussiness.healthy

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.mine.viewmodel.MyHealthyBean
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityRepayBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.wxapi.WXProducts
import java.util.*

/**
 * @Package com.nsk.app.bussiness.healthy
 * @author qianpeng
 * @date 2018/9/14.
 * @describe 体检重付款界面(和另外一个功能一样)
 */
@Route(path = Routers.repay)
class PayOrderActivity:BaseTransStatusActivity() {
//    lateinit var product:WorkPhyInfo
    lateinit var order:MyHealthyBean
    var n_medicalorder_pay_id="2"
    var tag=false

    override fun setTitle(): Int {
        return R.string.phypay
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_repay
    }
    override fun initData() {
        val bind = binding as ActivityRepayBinding
//        product =  intent.getSerializableExtra("product") as WorkPhyInfo
        order = intent.getSerializableExtra("healthyorder")as MyHealthyBean

//        intent.getSerializableExtra("healthyorder")
//        Nothings.hospital=product.n_hospital_name
//        Nothings.location=product.n_address
//        Nothings.tcjj=product.n_exam_product_details
//        Nothings.ddbh=order.n_medicalorder_id
//        Nothings.ddsj=product.n_group_start_time
//        Nothings.orderid = order.n_medicalorder_id
//        Nothings.zffs= if (TextUtils.equals(n_medicalorder_pay_id, "2")) "微信" else "支付宝"
        bind.healthyorder = order
//        bind.orderInfo = order
        bind.btnPay.setOnClickListener({
            pay(order)
        })
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
    fun pay(order: MyHealthyBean){
        val orderid = order.n_medicalorder_id

        val hashMap = HashMap<String, Any>()
        hashMap.put("n_medicalorder_id",orderid)
        hashMap.put("n_medicalorder_payamount",order.n_exam_group_price)
        hashMap.put("n_medicalorder_scoreused",order.useSocre)
        hashMap.put("n_medicalorder_pay_id",n_medicalorder_pay_id)
        val wxProducts = WXProducts(this)

        serviceApi.getForString(ApiConfig.UpdateEntryExaminationOrder,hashMap).compose(RxjavaUtils.transformer())
                .subscribe({ response ->
                    val  gson= Gson()
                    val jsonParser = JsonParser()
                    val ordersn =  jsonParser.parse(response).asJsonObject.get("data").asString
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
                    wxProducts.startPay(this.order.n_exam_group_price.toFloat()-this.order.deductYuan.toFloat(),orderid)
                    Nothings.wanntpay=true
                    tag=true
//                    finish()
//                    bankCardBean = gson.fromJson<BankCardBean>(response, BankCardBean::class.java)

                }, {
                    error ->
                    ToastUtils.showShort(error.message)
                })
    }

    override fun onResume() {
        super.onResume()
        //支付成功或者失败
        if(Nothings.pay){
            Nothings.pay = false
            ARouter.getInstance().build(Routers.orderDetail).withString("source","repay").withSerializable("order",order).navigation()
        }else if(Nothings.wanntpay&&tag){
            Nothings.wanntpay = false
            if(CkyApplication.getApp().token==null){
                ARouter.getInstance().build(Routers.login).navigation()
            }else{
                ARouter.getInstance().build(Routers.my_heal).navigation()
            }
            finish()
        }

    }
}