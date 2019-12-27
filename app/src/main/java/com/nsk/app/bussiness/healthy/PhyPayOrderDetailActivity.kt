package com.nsk.app.bussiness.healthy

import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.mine.viewmodel.MyHealthyBean
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import kotlinx.android.synthetic.main.activity_workphyorderdetail.*

/**
 * 订单详情
 */
@Route(path = Routers.orderDetail)
class PhyPayOrderDetailActivity:BaseTransStatusActivity() {

    override fun setTitle(): Int {
        return R.string.order_detail
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_workphyorderdetail
    }

    override fun initData() {
        //有空指针风险的要用var
        Nothings.pay=false
        var source = intent.getStringExtra("source")
        var order = intent.getSerializableExtra("order")as MyHealthyBean?
        if(!source.equals("repay")) {
            tv_hos.text = Nothings.hospital
            tv_loca.text = Nothings.location
            tv_jj.text = Nothings.tjtitle
            tv_jj_detail.text = "套餐简介\n"+Nothings.tcjj
            tv_ddbh_detail.text = Nothings.ddbh
            tv_ddsj_detail.text = Nothings.ddsj
            tv_zffs.text = Nothings.zffs
        }else{
            tv_hos.text = order!!.n_hospital_name
            tv_loca.text = order.n_address
//            tv_jj.text = Nothings.tjtitle
            tv_jj_detail.text = "套餐简介\n"+order.n_exam_product_details
            tv_ddbh_detail.text = order.n_medicalorder_id
            tv_ddsj_detail.text = order.n_medicalorder_createtime
           if(order.n_medicalorder_pay_id==1){
               tv_zffs.text = "支付宝"
           }else{
               tv_zffs.text = "微信"
           }
        }
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))

    }
}