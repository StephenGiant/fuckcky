package com.nsk.app.bussiness.card

import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.nsk.app.adapter.CkyBanksAdapter
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.db.CkyAddress
import com.nsk.app.db.CkyAddress_Table
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.app.widget.MyPicker
import com.nsk.cky.ckylibrary.RightListener
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.bean.BankCardBean
import com.nsk.cky.ckylibrary.utils.BaiduLocation
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.nsk.cky.ckylibrary.utils.ScreenTools
import com.nsk.cky.ckylibrary.widget.GridViewAdapter
import com.nsk.cky.ckylibrary.widget.CkyPagerAdapter
import com.raizlabs.android.dbflow.sql.language.Select
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_banks_cards.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Description: 办卡
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.cards)
class BankCardsActivity : BaseTransStatusActivity() , RightListener {
    lateinit var bankCardBean: BankCardBean
    var pageCount: Int = 0//总页数
    var oldCount :Int = 0;
    private val pageSize = 8//每一页的个数
    private var curIndex = 0//当前显示的事第几页
    private var mPagerList: java.util.ArrayList<View> = ArrayList()
    private var inflater: LayoutInflater? = null
    var mAdapter1: CommonAdapter<BankCardBean.NskTopicListBean>?=null
    var hotadapter:CommonAdapter<BankCardBean.NskHotCreditListBean>?=null
    var viewPagerAdapter: CkyPagerAdapter?=null
    var chooseAddres :CkyAddress?=null
    override fun onClick() {

        //城市选择
        val hotCities = ArrayList<HotCity>()
        hotCities.add(HotCity("北京", "北京", "101010100"))
        hotCities.add(HotCity("上海", "上海", "101020100"))
        hotCities.add(HotCity("广州", "广东", "101280101"))
        hotCities.add(HotCity("深圳", "广东", "101280601"))
        CityPicker.getInstance()
                .setFragmentManager(supportFragmentManager)	//此方法必须调用
                .enableAnimation(true)	//启用动画效果
                .setLocatedCity( LocatedCity(DbManger.getInstance().get(UserConstants.loca_city), "", ""))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(object : OnPickListener {
                    override fun onPick(position: Int, data: City?) {
                        //上传选择城市数据
                        if(data==null){
                            return
                        }

                        chooseAddres = Select().from(CkyAddress::class.java).where(CkyAddress_Table.name.like(data.name+"%")).querySingle()
                        if(chooseAddres!=null) {
                            setRight(chooseAddres!!.name!!,true,this@BankCardsActivity)
                            LogUtils.e("城市:" + chooseAddres!!.name+data.code)
//                            DbManger.getInstance().put(UserConstants.loca_city,chooseAddres!!.name!!)
                            DbManger.getInstance().put(UserConstants.city,chooseAddres!!.name)
                            DbManger.getInstance().put(UserConstants.areaId,chooseAddres!!.areaid)
//                            for (a in com.nsk.app.Nothings.temp) {
//                                if(TextUtils.equals(a.n_area_city,DbManger.getInstance().get(UserConstants.city))||a.n_area_city.contains(DbManger.getInstance().get(UserConstants.city))){
////                                    DbManger.getInstance().put(UserConstants.areaId,a.n_area_id.toString())
//
//                                    LogUtils.e(a.n_area_city+a.n_area_id)
//                                }
//                            }
                            oldCount = pageCount
                            pageCount = 0//总页数
                            curIndex = 0//当前显示的事第几页
                            mPagerList!!.clear()
                            initData()
                        }else{
                           ToastUtils.showShort("暂不支持此地区或未打开定位权限")
                        }

//                        setRight(DbManger.getInstance().get(UserConstants.city),true,this@BankCardsActivity)

                    }

                    override fun onLocate() {
                        PermissionHelper.requestLocation(object : PermissionHelper.OnPermissionGrantedListener{
                            override fun onPermissionGranted() {
                                BaiduLocation.getLocation(object : BaiduLocation.MyLocationListener{
                                    override fun myLocatin(isSuccess: Boolean, pro: String?, city: String?, code: String?) {
                                        if(isSuccess){
                                            CityPicker.getInstance()
                                                    .locateComplete( LocatedCity(city, pro, code), LocateState.SUCCESS)
                                        }
                                    }

                                })
                            }
                        })

                    }
                })
                .show()
    }
    override fun setTitle(): Int {
        return R.string.card
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_banks_cards
    }

    override fun initData() {
        viewPagerAdapter = null


var code  = "100100"
    if(chooseAddres==null){
        if(!"".equals(DbManger.getInstance().get(UserConstants.areaId))) {
            code = DbManger.getInstance().get(UserConstants.areaId)
        }else{
            code="100100"
        }
        }else{
       code = chooseAddres!!.areaid!!.toString()
    }

        val map = mapOf(Pair("n_area_id", code))

        serviceApi.getForString(ApiConfig.getCard_url,map).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val  gson=Gson()
                bankCardBean = gson.fromJson<BankCardBean>(jsonElement, BankCardBean::class.java)
//                        Flowable.fromIterable( it.data.nsk_hot_credit_list).subscribe(Consumer {
//                            bankCardBean.data.nsk_hot_credit_list
//                        })

                initHotBank()
                initHotCard()
                initThemeCard()
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })

//        serviceApi.getForString(ApiConfig.getCard_url,map).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
//                .subscribe({ response ->
//                    val  gson=Gson()
//                        bankCardBean = gson.fromJson<BankCardBean>(response, BankCardBean::class.java)
////                        Flowable.fromIterable( it.data.nsk_hot_credit_list).subscribe(Consumer {
////                            bankCardBean.data.nsk_hot_credit_list
////                        })
//
//                        initHotBank()
//                        initHotCard()
//                        initThemeCard()
//
//                }, {
//                    LogUtils.e(it.message)
//                })
    }

    /**
     * 主题选卡
     */
    private fun initThemeCard() {
        if(::bankCardBean.isInitialized ) {
            rv_themes2.layoutManager = GridLayoutManager(this, 2)
            if(mAdapter1==null) {
                mAdapter1 = object : CommonAdapter<BankCardBean.NskTopicListBean>(this, R.layout.item_theme_select_card, bankCardBean.nsk_topic_list) {
                    override fun convert(holder: ViewHolder?, t: BankCardBean.NskTopicListBean?, position: Int) {
                        val iv = holder?.getView<ImageView>(R.id.iv_logo)
                        val tv_title = holder?.getView<TextView>(R.id.tv_title)
                        val tv_sub_title = holder?.getView<TextView>(R.id.tv_property)
                        val ll_1 = holder?.getView<LinearLayout>(R.id.ll_1)
                        Glide.with(this@BankCardsActivity).load(t!!.n_topic_logo).into(iv!!)
                        tv_sub_title!!.text = t.n_topic_property

                        if (position == 3) {
                            //新手办卡
                            ll_1!!.setOnClickListener {
                                ARouter.getInstance().build(Routers.one_key_cards).navigation()
                            }
                        } else {
                            var type = "n_loan_success_applyRate"
                            if (position == 1) {
                                type = "n_loan_fastest"
                            } else if (position == 2) {
                                type = "n_loan_limit"
                            }
                            ll_1!!.setOnClickListener {
                                //主题选卡 其他
                                ARouter.getInstance().build(Routers.supermark_card).withSerializable("cardList", bankCardBean).withString("type", type).navigation()
                            }
                        }

                    }
                }
                rv_themes2.setHasFixedSize(true)
                rv_themes2.adapter = mAdapter1
            }else{
                mAdapter1!!.notifyDataSetChanged()
            }

        }

    }

    /**
     * 热卡排行
     */
    private fun initHotCard() {
        if(::bankCardBean.isInitialized) {
            if(hotadapter==null) {
                hotadapter = object : CommonAdapter<BankCardBean.NskHotCreditListBean>(this, R.layout.item_bank_detail, bankCardBean.nsk_hot_credit_list) {
                    override fun convert(holder: ViewHolder?, t: BankCardBean.NskHotCreditListBean?, position: Int) {
                        val iv = holder?.getView<ImageView>(R.id.iv_bank)
                        val tv_title = holder?.getView<TextView>(R.id.tv_title)
                        val tv_sub_title = holder?.getView<TextView>(R.id.tv_sub_title)
                        val tv_content = holder?.getView<TagFlowLayout>(R.id.tv_content)
                        val tv_apply = holder?.getView<TextView>(R.id.tv_apply)
                        val rl_todetail = holder?.getView<RelativeLayout>(R.id.rl_todetail)
                        Glide.with(this@BankCardsActivity).load(t!!.nsk_inner_credit_card.n_loan_logo_url).into(iv!!)
                        tv_content!!.adapter = object : TagAdapter<String>(t.n_credit_card_tags) {
                            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                                if (position > 1) {
                                    val view = View(this@BankCardsActivity)
                                    view.visibility = View.GONE
                                    return view
                                }
                                val tv = LayoutInflater.from(this@BankCardsActivity).inflate(R.layout.tv, tv_content, false) as TextView
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
                rv_cards.setHasFixedSize(true)
                val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
                decoration.dleft = ScreenTools(this).dp2px(20f)
                decoration.setDrawable(resources.getDrawable(R.drawable.divider))
                rv_cards.addItemDecoration(decoration)
                rv_cards.layoutManager = object : LinearLayoutManager(this) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }

                rv_cards.adapter = hotadapter
            }else{
                hotadapter!!.notifyDataSetChanged()
            }
        }
    }

    /**
     * 热门银行
     */
    private fun initHotBank() {

            if(::bankCardBean.isInitialized) {

                if(bankCardBean.nsk_host_bank_list.size>0) {
                        pageCount = Math.ceil(bankCardBean.nsk_host_bank_list.size * 1.0 / pageSize).toInt()
                    }else{
                        pageCount=0
                    }


                val arrayList = java.util.ArrayList<List<BankCardBean.NskHostBankListBean>>()

                for (i:Int in 0..pageCount-1){
                    if(i==pageCount-1){
                        arrayList.add(bankCardBean.nsk_host_bank_list.subList(0 + 8 * i, bankCardBean.nsk_host_bank_list.size))
                    }else{
                        arrayList.add(bankCardBean.nsk_host_bank_list.subList(0 + 8 * i, 8 * i + 8))
                    }
//                    if((i+1)*8<bankCardBean.nsk_host_bank_list.size) {
//                        arrayList.add(bankCardBean.nsk_host_bank_list.subList(0 + 8 * i, 8 * i + 8))
//                    }else{
//                        arrayList.add(bankCardBean.nsk_host_bank_list)
//                    }
                }
//                arrayList.add(bankCardBean.nsk_host_bank_list)
//                pageCount=2
//                arrayList.add(bankCardBean.nsk_host_bank_list)
                viewpager.adapter = CkyBanksAdapter(this,arrayList)
                setOvalLayout()
//                if(viewPagerAdapter==null) {
//
//
//
//                    if(viewpager.adapter!=null){
//                        viewpager.adapter!!.notifyDataSetChanged()
//                    }
//                    if(bankCardBean.nsk_host_bank_list.size>0) {
//                        pageCount = Math.ceil(bankCardBean.nsk_host_bank_list.size * 1.0 / pageSize).toInt()
//                    }else{
//                        pageCount=0
//                    }
//                    val tempList = ArrayList<View>()
//                    for (i in 0 until pageCount) {
//                        //每个页面都是inflate出的一个新实例
//                        var gridView = GridView(this)
//                        gridView.numColumns = 4
//                        gridView.overScrollMode = View.OVER_SCROLL_NEVER
//                        gridView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
////                var gridView = inflater!!.inflate(R.layout.gridview, null) as GridView
//                        gridView.adapter = GridViewAdapter(this, bankCardBean.nsk_host_bank_list as java.util.ArrayList<BankCardBean.NskHostBankListBean>?, i, pageSize)
//
//                        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//                            //title银行点击，选择银行
//                            val pos = position + curIndex * pageSize
//                            ARouter.getInstance().build(Routers.recommendCard).withString("id", bankCardBean.nsk_host_bank_list[pos].n_bank_id.toString()).navigation()
//                        }
//                        tempList.add(gridView)
//
//                    }
//                    mPagerList = ArrayList()
//                    mPagerList.addAll(tempList)
//                    viewPagerAdapter = CkyPagerAdapter(mPagerList)
//                    viewpager.adapter = null
//                    viewpager.adapter = viewPagerAdapter
//                    //设置viewpageAdapter
//                    if (bankCardBean.nsk_host_bank_list.size > 4) {
//                        val layoutParams = rl_banks.layoutParams
//                        layoutParams.height = DensityUtil.dp2px(200f)
//                        rl_banks.layoutParams = layoutParams
//                    } else {
//                        val layoutParams = rl_banks.layoutParams
//                        layoutParams.height = DensityUtil.dp2px(100f)
//                        rl_banks.layoutParams = layoutParams
//                    }
//
//
//                    //设置小圆点
////                viewpager.adapter!!.notifyDataSetChanged()
//                }
//                setOvalLayout()
            }


    }

    override fun initView() {
//        setSwipeBackEnable(false)

        var code  = "100100"
        if(chooseAddres==null){
            if(!"".equals(DbManger.getInstance().get(UserConstants.areaId))) {
                code = DbManger.getInstance().get(UserConstants.areaId)
            }else{
                code="100100"
            }
        }else{
            code = chooseAddres!!.areaid!!.toString()
        }
        var add = DbManger.getInstance().get(UserConstants.city)
        if("100100".equals(code)){
            add = "北京市"
        }
        setRight(add,true,this)
        //信用卡市
        tv_all_cards.setOnClickListener {
            ARouter.getInstance().build(Routers.supermark_card).withSerializable("cardList",bankCardBean).navigation()
        }
        inflater = LayoutInflater.from(this)
        setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        title!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        reSetTitleColor(resources.getColor(R.color.white))
        title!!.setNavigationIcon(R.drawable.back_white)

    }

    private fun setOvalLayout() {
        ll_dot.removeAllViews()
        if(pageCount>0) {
            for (i in 0 until pageCount) {
                ll_dot.addView(inflater!!.inflate(R.layout.dot, null))
            }
            //默认显示第一页
            ll_dot.getChildAt(0).findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected)
            viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    //取消选中
                    ll_dot.getChildAt(curIndex).findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.dot_normal)
                    //选中
                    ll_dot.getChildAt(position).findViewById<View>(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected)

                    curIndex = position

                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }

    }

}
