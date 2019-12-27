package com.nsk.app.bussiness.mine

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.bean.HealthBean
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.MyHealthyBean
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ItemHealthBinding
import com.nsk.app.caikangyu.databinding.ItemMyhealthBinding
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.widget.DividerItemDecoration
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner
import kotlinx.android.synthetic.main.activity_loan.*
import java.math.BigDecimal
import java.text.DecimalFormat

@Route(path = Routers.my_heal)
class MyHealthActivity : BaseTitleActivity(){
     var dataList:List<MyHealthyBean>?=null
    override fun setTitle(): Int {
        return R.string.myhealth
    }

    override fun getContentLayoutId(): Int {
        return  R.layout.activity_loan
    }
    private var mDatas= ArrayList<HealthBean>()
    override fun initData() {

        rv_loans.addItemDecoration(android.support.v7.widget.DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        rv_loans.layoutManager = LinearLayoutManager(this)
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_loans.addItemDecoration(decoration)
        rv_loans.addOnItemTouchListener(object :RecyclerItemCLickListenner(this){
            override fun onItemClick(view: View, position: Int) {
                if(dataList!=null){
                    if(dataList!!.get(position).n_medicalorder_status<3||dataList!!.get(position).n_medicalorder_status==4){
                        //跳转到支付界面 healthyorder
                        ARouter.getInstance().build(Routers.repay).withSerializable("healthyorder",dataList!!.get(position)).navigation()

                    }
                }
            }
        })
        serviceApi.getForString(ApiConfig.getUserHealthyList).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val gson = Gson()
                 dataList = gson.fromJson<List<MyHealthyBean>>(jsonElement!!.asJsonArray
                ,object :TypeToken<List<MyHealthyBean>>(){}.type)
                val adapter = object :BaseAdapterWithBinding<MyHealthyBean>(dataList!!){
                    override fun getLayoutResource(viewType: Int): Int {
                       return R.layout.item_myhealth
                    }

                    override fun setVariableID(): Int {
                        return BR.myorderinfo
                    }

                    override fun getItemCount(): Int {
                       return dataList!!.size
                    }

                    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                        super.onBindViewHolder(holder, position)
                        val bind = holder.binding as ItemMyhealthBinding
                        val price = BigDecimal(dataList!!.get(position).n_exam_group_price)
                        val deduct = BigDecimal(dataList!!.get(position).deductYuan)
//                        val unit = "￥"
                        val df = DecimalFormat("0.00")
                        bind.tvCost.text = "￥"+df.format(price-deduct)
                        when(dataList!!.get(position).n_medicalorder_status){
                            0->{
                                bind.tvStatus.text = "未支付"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.mainred))
                            }
                            1->{
                                bind.tvStatus.text = "未支付"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.mainred))
                            }
                            2->{
                                bind.tvStatus.text = "未支付"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.mainred))

                            }
                            3->{
                                bind.tvStatus.text = "已支付"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.boder_gray))
                            }
                            4->{
                                bind.tvStatus.text = "未支付"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.mainred))
                            }
                            5->{
                                bind.tvStatus.text = "已取消"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.boder_gray))
                            }
                            6->{
                                bind.tvStatus.text = "已退款"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.boder_gray))
                            }
                            7->{
                                bind.tvStatus.text = "已完成"
                                bind.tvStatus.setTextColor(resources.getColor(R.color.boder_gray))
                            }
                        }
                    }
                }
                rv_loans.adapter = adapter
            }

        },object :RxjavaUtils.CkyErrorConsumer(){

        })


    }

//    private fun iniItem() {
//
//        val mAdapter = object : CommonAdapter<HealthBean>(this, R.layout.item_health, mDatas) {
//            override fun convert(holder: ViewHolder?, t: HealthBean?, position: Int) {
//                val civ_avator = holder?.getView<CircleImageView>(R.id.civ_avator)
//                val tv_hos = holder?.getView<TextView>(R.id.tv_hos)
//                val tv_ys = holder?.getView<TextView>(R.id.tv_ys)
//                val tv_ks = holder?.getView<TextView>(R.id.tv_ks)
//                val tv_fee = holder?.getView<TextView>(R.id.tv_fee)
//                val tv_time = holder?.getView<TextView>(R.id.tv_time)
//                val tv_status = holder?.getView<TextView>(R.id.tv_status)
//                Glide.with(this@MyHealthActivity).load(t!!.n_medicalorder_describe).into(civ_avator)
//            }
//        }
//        rv_loans.addItemDecoration(android.support.v7.widget.DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
//        rv_loans.layoutManager = LinearLayoutManager(this)
//
//        val mEmptyWrapper = EmptyWrapper<Any>(mAdapter)
//        mEmptyWrapper.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_loans, false))
//        rv_loans.adapter=mEmptyWrapper
//    }

    override fun initView() {
//        iniItem()
    }
}