package com.nsk.app.bussiness.card

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.bean.BankCardBean
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.ScreenTools
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_recommend_cards.*


/**
 * Description: 信用卡超市-推荐办卡
 * Company    :
 * Author     : gene
 * Date       : 2018/8/23
 */
@Route(path = Routers.recommendCard)
class RecommendCardActivity : BaseTitleActivity() {
     var id=""
     var re=false
     var bankCardBean=BankCardBean()
    override fun setTitle(): Int {
        return R.string.super_card
    }
    var datas= java.util.ArrayList<BankCardBean.NskHotCreditListBean>()
    override fun getContentLayoutId(): Int {
        return R.layout.activity_recommend_cards
    }
    override fun initData() {
        val extrsa = intent.extras
        if(extrsa.getString("id")!=null){
         id = extrsa.getString("id")
        }
         re = extrsa.getBoolean("recommend")
        if(re){
             datas = extrsa.getSerializable("data") as java.util.ArrayList<BankCardBean.NskHotCreditListBean>
        }
    }
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    override fun initView() {
        refreshLayout.setOnRefreshListener {
            it.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            initD()
        }
        refreshLayout.setOnLoadMoreListener {
            it.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
        initD()

    }
    private fun initD(){
        if(re){
            initRecommendCard(datas)
        }else{
            val map = mapOf(Pair("n_area_id", DbManger.getInstance().get(UserConstants.areaId)), Pair("n_bank_id",id))
            serviceApi.getForString(ApiConfig.CreditCardByBank,map).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                    .subscribe({ response ->
                        val  gson= Gson()
                        val listdata =  gson.fromJson<ArrayList<BankCardBean.NskHotCreditListBean>>(response,object : TypeToken<ArrayList<BankCardBean.NskHotCreditListBean>>(){}.type)
//                    bankCardBean = gson.fromJson<BankCardBean>(response, BankCardBean::class.java)
                        initRecommendCard(listdata)
                    }, {
                        initRecommendCard(ArrayList<BankCardBean.NskHotCreditListBean>())
                    })
        }
    }
    private fun initRecommendCard(nsk_hot_credit_list: MutableList<BankCardBean.NskHotCreditListBean>) {
         val mAdapter1 = object : CommonAdapter<BankCardBean.NskHotCreditListBean>(this, R.layout.item_bank_detail, nsk_hot_credit_list) {
            override fun convert(holder: ViewHolder?, t:BankCardBean.NskHotCreditListBean?, position: Int) {
                val iv = holder?.getView<ImageView>(R.id.iv_bank)
                val tv_title = holder?.getView<TextView>(R.id.tv_title)
                val tv_sub_title = holder?.getView<TextView>(R.id.tv_sub_title)
                val tv_content = holder?.getView<TagFlowLayout>(R.id.tv_content)
                val tv_apply = holder?.getView<TextView>(R.id.tv_apply)
                val rl_todetail = holder?.getView<RelativeLayout>(R.id.rl_todetail)
                Glide.with(this@RecommendCardActivity).load(t!!.nsk_inner_credit_card.n_loan_logo_url).into(iv)
                tv_content!!.adapter = object : TagAdapter<String>(t.n_credit_card_tags) {
                    override fun getView(parent: FlowLayout, position: Int, s: String): View {
                        if(position>1){
                            val view = View(this@RecommendCardActivity)
                            view.visibility=View.GONE
                            return view
                        }
                        val tv =LayoutInflater.from(this@RecommendCardActivity).inflate(R.layout.tv,tv_content, false) as TextView
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

        mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
        mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_view, false))
        rv_view.adapter = mEmptyWrapper
    }

}