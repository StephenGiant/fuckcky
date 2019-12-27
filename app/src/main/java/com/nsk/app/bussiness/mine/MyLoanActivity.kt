package com.nsk.app.bussiness.mine

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.bean.LoanOrder
import com.nsk.app.config.ApiConfig
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import kotlinx.android.synthetic.main.activity_loan.*


/**
 * 我的贷款
 */
@Route(path = Routers.my_loan)
class MyLoanActivity :BaseTitleActivity() {
    override fun setTitle(): Int {
        return R.string.myloan
    }

    override fun getContentLayoutId(): Int {
       return  R.layout.activity_loan
    }

    private var mDatas= ArrayList<LoanOrder>()
    override fun initData() {

        serviceApi.getForString(ApiConfig.getUserLoadList).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val gson = Gson()
                    mDatas = gson.fromJson<ArrayList<LoanOrder>>(response, object : TypeToken<java.util.ArrayList<LoanOrder>>() {
                    }.type)
                    iniItem()

                }, {

                })
    }

    private fun iniItem() {
        val mAdapter = object : CommonAdapter<LoanOrder>(this, R.layout.item_loan, mDatas) {

            override fun convert(holder: ViewHolder?, t: LoanOrder?, position: Int) {
                val tv_tip = holder?.getView<TextView>(R.id.tv_tip)
                val tv_des = holder?.getView<TextView>(R.id.tv_des)
                val tv_time = holder?.getView<TextView>(R.id.tv_time)
                val image = holder?.getView<ImageView>(R.id.iv_bank)
                Glide.with(this@MyLoanActivity).load(t!!.n_bank_logo).into(image)
                tv_time!!.setText(t!!.n_order_createtime)
                when {
                    //审核中
                    t.n_order_status==1 -> {
                        tv_time!!.setText(t!!.n_order_tracertime)
                        tv_tip!!.setText("审核中")
                    }
                    //审核通过
                    t.n_order_status==2 -> {
                        tv_time!!.setText(t!!.n_order_tracertime)
                        tv_tip!!.setText("审核通过")
                    }
                    //审核不通过
                    t.n_order_status==3 -> {
                        tv_tip!!.setText("审核不通过")
                    }
                    //还款中
                    t.n_order_status==4 -> {
                        tv_tip!!.setText("还款中")
                    }
                    //已还清
                    t.n_order_status==5 -> {
                        tv_tip!!.setText("已还清")
                    }
                }
                tv_des!!.setText(t!!.n_bank_name)

            }
        }
        rv_loans.addItemDecoration(DividerItemDecoration(this, com.nsk.cky.ckylibrary.widget.DividerItemDecoration.VERTICAL_LIST))
        rv_loans.layoutManager = LinearLayoutManager(this)

        val mEmptyWrapper = EmptyWrapper<Any>(mAdapter)
        mEmptyWrapper.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_loans, false))
        rv_loans.adapter=mEmptyWrapper
    }

    override fun initView() {

    }
}