package com.nsk.app.bussiness.card

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.bean.CardDetailBean
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.GlideImageLoader
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.FinestUtils
import com.youth.banner.listener.OnBannerListener
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_cred_cards.*


/**
 * Description: 信用卡详情
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 * update     : qianpeng
 * 银行的url没给 暂时做到这样的程度
 */
@Route(path = Routers.cred_cards)
class CredCardsDetailActivity : BaseTitleActivity() , OnBannerListener {
    override fun OnBannerClick(position: Int) {
    }

    lateinit var cardBean: CardDetailBean
    override fun setTitle(): Int {
        return R.string.cred_card
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_cred_cards
    }
    lateinit var id: String
    var bannerClickUrl=ArrayList<String>()
    override fun initData() {
        iv_logo.setImageLoader(GlideImageLoader())
        iv_logo.setOnBannerListener(this)
        val extrsa = intent.extras
         id = extrsa.getString("id")
        val map = mapOf(Pair(UserConstants.n_credit_card_id, id))
        serviceApi.getForString(ApiConfig.getCard_detail_url, map).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val gson = Gson()
                    cardBean = gson.fromJson<CardDetailBean>(response, CardDetailBean::class.java)
                    initDetail()
                }, {})
    }

    private fun initDetail() {
//        Glide.with(this@CredCardsDetailActivity).load(cardBean.nsk_inner_credit_card.n_loan_logo_url).into(iv_logo)
        tv_title.text = cardBean.nsk_inner_credit_card.n_loan_title
        tv_sub_title.text = cardBean.nsk_inner_credit_card.n_loan_subheading
        tv_tag!!.adapter = object : TagAdapter<String>(cardBean.n_credit_card_tags) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = LayoutInflater.from(this@CredCardsDetailActivity).inflate(R.layout.tv, tv_tag, false) as TextView
                tv.text = s
                tv.setTextColor(resources.getColor(R.color.orange_main))
                return tv
            }
        }
        val stringBuffer = StringBuffer()
        for (i:Int in 0..cardBean.n_load_preferential_rights.size-1){
            if(i!=0){
                stringBuffer.append( "\n"+cardBean.n_load_preferential_rights.get(i))
            }else{
                stringBuffer.append(cardBean.n_load_preferential_rights.get(i))
            }
        }
//        for(s in cardBean.n_load_preferential_rights){
//            stringBuffer.append( "\n"+s)
//        }
        tv_card_right.text=stringBuffer.toString()
        tv_card_detail.text = Html.fromHtml(cardBean.nsk_inner_credit_card.n_credit_card_details)
        tv_application.setOnClickListener {
            //跳转到银行的信 用卡申请界面
            if(CkyApplication.getApp().hasToken()) {
                FinestUtils.start(viewRoot,this@CredCardsDetailActivity,cardBean.nsk_inner_credit_card.n_loan_url)
                serviceApi.getForString(ApiConfig.viewcard + "?n_loan_id=" + id).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                    override fun onSuccess(jsonElement: JsonElement?) {
                    }
                }, object : RxjavaUtils.CkyErrorConsumer() {}
                )
            }else{
                ARouter.getInstance().build(Routers.login).navigation()
            }

        }
        iv_logo.setImages(cardBean.n_credit_card_Sowing_pictures)
        iv_logo.start()
    }

    override fun initView() {

    }
}
