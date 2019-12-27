package com.nsk.app.bussiness.card

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.adapter.FeatureAdapter
import com.nsk.cky.ckylibrary.bean.BankCardBean
import com.nsk.cky.ckylibrary.http.callback.ISelectListener
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.ScreenTools
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_supermarket_cards.*


/**
 * Description: 信用卡超市
 * Company    :
 * Author     : gene
 * Date       : 2018/8/23
 */
@Route(path = Routers.supermark_card)
class SuperMarketActivity : BaseTransStatusActivity() {
     //var bankCardBean: BankCardBean? = null
     var temp= ArrayList<BankCardBean.NskHotCreditListBean>()
     var datas= ArrayList<BankCardBean.NskHotCreditListBean>()
    lateinit var featureAdapter: FeatureAdapter
    lateinit var s: String
    lateinit var type: String
    override fun setTitle(): Int {
        return R.string.super_card
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_supermarket_cards
    }
    override fun initData() {
        val extrsa = intent.extras
        if(extrsa.getString("type").isNullOrBlank()){
            type =""
        }else{
            type =extrsa.getString("type")
        }

         cardList = extrsa.getSerializable("cardList") as BankCardBean?
        mDatas.add("全部")
        cardList.let {
            it!!.nsk_host_bank_list.forEach {
                mDatas.add(it.n_bank_name)
            }
        }
    }
    private var mDatas= ArrayList<String>()
    private var headers=ArrayList<String>()
    private val popupViews = ArrayList<View>()
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    private var cardList: BankCardBean? = null
    private var mAdapter1: CommonAdapter<BankCardBean.NskHotCreditListBean>? = null
    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        title!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        reSetTitleColor(resources.getColor(R.color.white))
        title!!.setNavigationIcon(R.drawable.back_white)
        headers.add("选银行")
        headers.add("易下卡")
        headers.add("审批快")
        headers.add("大额度")
        refreshLayout.setOnRefreshListener {
            it.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener {
            it.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
        val featureView = layoutInflater.inflate(R.layout.grid_bank, null) as GridView
        val featureView1 = layoutInflater.inflate(R.layout.grid_bank, null) as GridView
        val featureView2 = layoutInflater.inflate(R.layout.grid_bank, null) as GridView
        val featureView3 = layoutInflater.inflate(R.layout.grid_bank, null) as GridView
//        val grid = featureView.findViewById<GridView>(R.id.constellation)
        (content as ViewGroup).removeView(featureView)
        (content as ViewGroup).removeView(featureView1)
        (content as ViewGroup).removeView(featureView2)
        (content as ViewGroup).removeView(featureView3)

         featureAdapter = FeatureAdapter(this,mDatas, ISelectListener { pos ->
              s = mDatas[pos]
             popup_menu.setTabText(s)
             popup_menu.closeMenu()
             featureAdapter.setCheckItem(pos)
             notifyRV(s)
         })
        featureView.adapter = featureAdapter

        //init PopupMenu
        popupViews.add(featureView)
        popupViews.add(featureView1)
        popupViews.add(featureView2)
        popupViews.add(featureView3)
        val map = mapOf(Pair("n_area_id", DbManger.getInstance().get(UserConstants.areaId)))
        serviceApi.getForString(ApiConfig.HotCreditCardList,map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                temp.clear()
                val  gson= Gson()
                val datajson = jsonElement!!.asJsonArray
                var datas = gson.fromJson<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(datajson,object : TypeToken<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(){
                }.type)
                temp.addAll(datas)
                initHotCard()
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
                temp.clear()
                mAdapter1!!.notifyDataSetChanged()
                mEmptyWrapper!!.notifyDataSetChanged()
                rv_view.adapter = mEmptyWrapper
            }
        })

        serviceApi.getForString(ApiConfig.HotCreditCardList,map).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    temp.clear()
                    val  gson= Gson()
                     datas = gson.fromJson<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(response,object : TypeToken<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(){
                    }.type)
//                    bankCardBean = gson.fromJson<BankCardBean>(response, BankCardBean::class.java)
                    temp.addAll(datas)
                    initHotCard()

                }, {})
        (content as ViewGroup).removeView(refreshLayout)
        (content as ViewGroup).removeView(rv_view)
        (content as ViewGroup).removeView(tv_head)

        popup_menu.initPopupMenu(headers, popupViews, refreshLayout,object :ISelectListener{
            override fun select(pos: Int) {
                when (pos) {
                    0->{

                    }

                    1 -> {
                        tv_head.visibility=View.VISIBLE
                        tv_head.text="易下卡"
                        getPXList("n_loan_success_applyRate")
                    }
                    2 -> {
                        tv_head.visibility=View.VISIBLE
                        tv_head.text="审批快"
                        getPXList("n_loan_fastest")
                    }
                    3 -> {
                        tv_head.visibility=View.VISIBLE
                        tv_head.text="大额度"
                        getPXList("n_loan_limit")
                    }

                }
            }

        })
    }
    //根据排序重新排序
    private fun getPXList(type:String){
        val map = mapOf(Pair("n_area_id", DbManger.getInstance().get(UserConstants.areaId)),Pair("orderProperty", type))
        serviceApi.getForString(ApiConfig.orderCreditCardList,map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                temp.clear()
                val gson = Gson()
                val datajson = jsonElement!!.asJsonArray
                var datas = gson.fromJson<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(datajson,object : TypeToken<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(){
                }.type)
                temp.addAll(datas)
                mEmptyWrapper!!.notifyDataSetChanged()
                mAdapter1!!.notifyDataSetChanged()
                    rv_view.adapter = mEmptyWrapper
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
                temp.clear()
                mAdapter1!!.notifyDataSetChanged()
                mEmptyWrapper!!.notifyDataSetChanged()
                    rv_view.adapter = mEmptyWrapper
            }
        })

    }

    private fun setPxItem() {
    }

    //刷新rv
    private fun notifyRV(s: String) {
        tv_head.visibility=View.VISIBLE
        tv_head.text=s
        temp.clear()
        if(TextUtils.equals(s,"全部")){
            temp.addAll(datas)
        }else{
            var id=0
            for(qq in Nothings.bank_card){
                if(qq.n_bank_name.contains(s.substring(0,2))){
                    id=qq.n_bank_id
                }
            }
            for(zz in  datas){
                if(zz.nsk_inner_credit_card.n_loan_bankid==id){
                    LogUtils.e(zz.nsk_inner_credit_card.n_loan_title)
                    temp.add(zz)
                }
            }
        }

        mEmptyWrapper!!.notifyDataSetChanged()
    }

    private fun initHotCard() {
         mAdapter1 = object : CommonAdapter<BankCardBean.NskHotCreditListBean>(this, R.layout.item_bank_detail,  temp) {
            override fun convert(holder: ViewHolder?, t: BankCardBean.NskHotCreditListBean?, position: Int) {
                val iv = holder?.getView<ImageView>(R.id.iv_bank)
                val tv_title = holder?.getView<TextView>(R.id.tv_title)
                val tv_sub_title = holder?.getView<TextView>(R.id.tv_sub_title)
                val tv_content = holder?.getView<TagFlowLayout>(R.id.tv_content)
                val tv_apply = holder?.getView<TextView>(R.id.tv_apply)
                val rl_todetail = holder?.getView<RelativeLayout>(R.id.rl_todetail)
                Glide.with(this@SuperMarketActivity).load(t!!.nsk_inner_credit_card.n_loan_logo_url).into(iv)
                tv_content!!.adapter = object : TagAdapter<String>(t.n_credit_card_tags) {
                    override fun getView(parent: FlowLayout, position: Int, s: String): View {
                        if(position>1){
                            val view = View(this@SuperMarketActivity)
                            view.visibility=View.GONE
                            return view
                        }
                        val tv =LayoutInflater.from(this@SuperMarketActivity).inflate(R.layout.tv,tv_content, false) as TextView
                        tv.setTextColor(resources.getColor(R.color.orange_main))
                        tv.text = s
                        return tv
                    }
                }
                tv_title!!.text = t.nsk_inner_credit_card.n_loan_title
                tv_sub_title!!.text = t.nsk_inner_credit_card.n_loan_subheading
                rl_todetail!!.setOnClickListener {
                    //信用卡详情 申请
                    ARouter.getInstance().build(Routers.cred_cards).withString("id", t.nsk_inner_credit_card.n_loan_id.toString()).navigation()
                }
            }
        }
        rv_view.setHasFixedSize(true)
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.dleft = ScreenTools(this).dp2px(20f)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_view.addItemDecoration(decoration)
        rv_view.layoutManager = LinearLayoutManager(this)
        (content as ViewGroup).removeView(rv_view)
        mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
        mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_view, false))
        rv_view.adapter = mEmptyWrapper

        if(type.isNotBlank()){
            var i=2
            if(type=="n_loan_success_applyRate"){
                i=2
            }else if( type=="n_loan_fastest"){
                i=4
            }else if( type=="n_loan_limit"){
                i=6
            }
            getPXList(type)
            popup_menu.initSwitch(i)
        }else{
            rv_view.adapter = mEmptyWrapper
        }


    }

}