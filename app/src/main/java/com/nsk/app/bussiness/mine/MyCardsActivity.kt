package com.nsk.app.bussiness.mine

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bean.MyCardsBean
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import kotlinx.android.synthetic.main.activity_mycard.*
import java.util.*

/**
 * @Package com.nsk.app.bussiness.mine
 * @author qianpeng
 * @date 2018/9/5.
 * @describe
 */
@Route(path = Routers.mycards)
class MyCardsActivity :BaseTransStatusActivity() {
    var datas=ArrayList<MyCardsBean>()
//    lateinit var bind:ActivityMycardBinding
    var mAdapter:CommonAdapter<MyCardsBean>?=null
    var mEmptyWrapper=EmptyWrapper<Any>(mAdapter)
    var tag=false
    override fun setTitle(): Int {
        return R.string.mycard
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_mycard
    }

    override fun onResume() {
        super.onResume()
        if(tag){
            Handler().postDelayed({
                LogUtils.e("1")
                fetchCards()
            },500)

        }
    }

    override fun initData() {
//         bind  = binding as ActivityMycardBinding

//        serviceApi.getForString(ApiConfig.card_commonapi_url+"?")
    }

    fun fetchCards(){
        tag=true
        serviceApi.getForString(ApiConfig.getUserCards_url).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                val gson = Gson()
                val datajson = jsonElement!!.asJsonArray
                val res = gson.fromJson<ArrayList<MyCardsBean>>(datajson,object : TypeToken<ArrayList<MyCardsBean>>(){
                }.type)
                datas.clear()
                datas.addAll(res)
                LogUtils.e("2")
                mEmptyWrapper!!.notifyDataSetChanged()
//                bind.tvEmpty.visibility = View.GONE
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
                    datas!!.clear()
                LogUtils.e("3")
//                bind.tvEmpty.visibility = View.VISIBLE
                mEmptyWrapper!!.notifyDataSetChanged()
            }
        })

    }

    override fun initView() {
        iniItem()
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
        rightTitle!!.visibility=View.VISIBLE
        rightTitle!!.setText("添加卡片")
        rightTitle!!.setTextColor(resources.getColor(R.color.orange_main))
        rightTitle!!.setOnClickListener {
         ARouter.getInstance().build(Routers.addcards).navigation()

        }
    }
    private fun iniItem() {
            mAdapter = object : CommonAdapter<MyCardsBean>(this, R.layout.item_mycards, datas) {

                override fun convert(holder: ViewHolder?, t: MyCardsBean?, position: Int) {
                    val tv_bank_code = holder?.getView<TextView>(R.id.tv_bank_code)
                    val tv_status = holder?.getView<TextView>(R.id.tv_status)
                    val tv_bank = holder?.getView<TextView>(R.id.tv_bank)
                    val iv_card = holder?.getView<ImageView>(R.id.iv_card)
                    val tv_unbind = holder?.getView<TextView>(R.id.tv_unbind)
                    Glide.with(this@MyCardsActivity).load(t!!.n_bank_logo).into(iv_card)
                    tv_bank!!.setText(t!!.n_bank_name)
                    tv_bank_code!!.setText(t!!.n_card_number)
                    tv_unbind!!.visibility = View.VISIBLE
                    tv_unbind!!.setOnClickListener {
                        //解除绑定
                        unbindCard(datas.get(position).n_card_id.toString())
                    }
//                when {
//                    //审核中
//                    t.n_card_status ==0 -> {
//                        tv_status!!.text = "审核中"
//                        tv_unbind!!.visibility = View.GONE
//                    }
//                    //审核通过
//                    t.n_card_status ==1 -> {
//                        tv_status!!.text = ""
//                        tv_unbind!!.visibility = View.VISIBLE
//
//                    }
//                    //
//                    t.n_card_status ==2 -> {
//                        tv_status!!.text = "解绑中"
//                        tv_unbind!!.visibility = View.GONE
//                    }
//                    //
//                    t.n_card_status ==3 -> {
//                        tv_status!!.text = "已解绑"
//                        tv_unbind!!.visibility = View.GONE
//                    }
//                    //已还清
//                    t.n_card_status ==5 -> {
//                    }
//                }

                }
            }
            rv_mycards.layoutManager = LinearLayoutManager(this)
            val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
            decoration.setDrawable(resources.getDrawable(R.drawable.spacedrawable))
            rv_mycards.addItemDecoration(decoration)
             mEmptyWrapper = EmptyWrapper<Any>(mAdapter)
            mEmptyWrapper.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_mycards, false))
            rv_mycards.adapter = mEmptyWrapper
            fetchCards()
    }

    fun unbindCard(cardid:String){
        serviceApi.getForString(ApiConfig.unbindcard+"?n_card_id="+cardid).parseData(
                object :RxjavaUtils.CkySuccessConsumer(){
                    override fun onSuccess(jsonElement: JsonElement?) {
                        tag=true
                       fetchCards()

                    }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })
    }

}