package com.nsk.app.bussiness.index

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseContentFragment
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.index.viewmodel.IndexInfoBean
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.R
import com.czm.library.LogUtil
import com.google.gson.JsonArray
import com.nsk.app.caikangyu.databinding.FragmentIndexBinding
import com.nsk.app.caikangyu.databinding.ItemIndexbuttonsBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.GlideImageLoader
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.base.BaseWebActivity
import com.nsk.cky.ckylibrary.utils.WebUtils
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import java.io.File


class IndexFragment :BaseContentFragment() ,OnBannerListener{

    var banner :Banner?=null
    var index_refresh :ViewGroup?=null
    var bind:FragmentIndexBinding?=null
    var inited:Int = 0
    var cardList:ArrayList<IndexInfoBean.LoanInfo>?=null
    var inviteList:ArrayList<IndexInfoBean.InviteInfo>?=null
    var bannerurls = ArrayList<String>()
    var banners:JsonArray?=null
    override fun getContentLayoutId(): Int {
        return R.layout.fragment_index
    }

    override fun initView(view: View) {

         bind = DataBindingUtil.bind<FragmentIndexBinding>(view)
        bind!!.ivNotify.setOnClickListener({

            if(CkyApplication.getApp().hasToken()) {
                ARouter.getInstance().build(Routers.mynotice).navigation()
            }else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        })
        bind!!.indexRefresh.setOnRefreshListener(object :OnRefreshListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
               bind!!.indexRefresh.finishRefresh(2000)
                initData()
            }
        })

        //设置图片加载器

        banner = view.findViewById<Banner>(R.id.banner)
        index_refresh = view.findViewById<ViewGroup>(R.id.index_refresh)
        banner!!.setImageLoader(GlideImageLoader())
        banner!!.setOnBannerListener(this)
        banner!!.setDelayTime(6000)
        LogUtil.getInstance().upload(activity)
      val layoutManager1 =  object :LinearLayoutManager(activity){
           override fun canScrollVertically(): Boolean {
               return false
           }
       }
        val layoutManager2 =  object :LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val layoutManager3 =  object :LinearLayoutManager(activity){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val layoutManager4 =  object :GridLayoutManager(activity,3){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        bind!!.rvCards.setHasFixedSize(true)
        bind!!.rvAds.setHasFixedSize(true)
        bind!!.rvButtons.setHasFixedSize(true)
        bind!!.rvYaoqing.setHasFixedSize(true)
        bind!!.rvAds.layoutManager = layoutManager1
        bind!!.rvCards.layoutManager =layoutManager2
        bind!!.rvYaoqing.layoutManager=layoutManager3
        bind!!.rvButtons.layoutManager = layoutManager4
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.spacedrawable))
        bind!!.rvCards.addItemDecoration(decoration)
        //体检团购
//        ll_medical.setOnClickListener{
//            ARouter.getInstance().build(Routers.cards).navigation()
//        }
//        //医疗特需
//        ll_need.setOnClickListener{
//
//        }
//        //积分兑换
//        ll_exchange.setOnClickListener{
//
//        }
    }
     var bannerClickUrl=ArrayList<String>()
    override fun initData() {
//        ToastUtils.showLong("initData")

        serviceApi.getForString(ApiConfig.page_index_url+"?userId="+"").parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val gson = Gson()
                val datajson = jsonElement!!.asJsonObject
                 banners = datajson.get("layout_lunbo").asJsonArray     //轮播图坑位信息
                val yaoqingjsonarray = datajson.get("layout_yaoqing").asJsonArray  //邀请坑位信息
                val adsjsonarray = datajson.get("layout_ads").asJsonArray          //广告坑位信息
                val cardjsonarray = datajson.get("layout_creditOrLoan").asJsonArray//信用卡贷款坑位信息
                val healthjsonarray = datajson.get("layout_yiliao").asJsonArray    //医疗坑位信息
                bannerurls.clear()
                for (i:Int in 0..banners!!.size()-1){
                    bannerClickUrl.add(banners!!.get(i).asJsonObject.get("n_link_value").asString)
                    bannerurls.add(banners!!.get(i).asJsonObject.get("n_content").asString)
                }
                banner!!.setImages(bannerurls)
                banner!!.start()
                val yiliaoList = gson.fromJson<ArrayList<IndexInfoBean.YiliaoInfo>>(healthjsonarray,object :TypeToken<ArrayList<IndexInfoBean.YiliaoInfo>>(){}.type)
                yiliaoList.get(0).name = "体检团购"
                yiliaoList.get(1).name = "医疗特需"
                yiliaoList.get(2).name = "积分兑换"
                 cardList = gson.fromJson<ArrayList<IndexInfoBean.LoanInfo>>(cardjsonarray,object :TypeToken<ArrayList<IndexInfoBean.LoanInfo>>(){}.type)
                val adlist =  gson.fromJson<ArrayList<IndexInfoBean.AdInfo>>(adsjsonarray,object :TypeToken<ArrayList<IndexInfoBean.AdInfo>>(){}.type)
                 inviteList =  gson.fromJson<ArrayList<IndexInfoBean.InviteInfo>>(yaoqingjsonarray,object :TypeToken<ArrayList<IndexInfoBean.InviteInfo>>(){}.type)
                val adapter = object :BaseAdapterWithBinding<IndexInfoBean.YiliaoInfo>(yiliaoList){
                    override fun getLayoutResource(viewType: Int): Int {
                        return R.layout.item_indexbuttons
                    }

                    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                        super.onBindViewHolder(holder, position)
                        val b = holder.binding as ItemIndexbuttonsBinding
                        Glide.with(holder.itemView.context).load(yiliaoList.get(position).n_content).into(b.ivIndexbutton)
                        val imageview = b.ivIndexbutton
                    }

                    override fun setVariableID(): Int {
                        return BR.yiliaoinfo
                    }

                    override fun getItemCount(): Int {
                        return 3
                    }
                }
                bind!!.rvButtons.adapter = adapter
                if(inited==0) {
                    bind!!.rvButtons.addOnItemTouchListener(object : RecyclerItemCLickListenner(activity!!) {
                        override fun onItemClick(view: View, position: Int) {
                            when (position) {
                                0 -> {
                                    ARouter.getInstance().build(Routers.workphy).navigation()
                                }
                                1 -> ARouter.getInstance().build(Routers.health_index).navigation()
                                2 -> {
                                    if (CkyApplication.getApp().hasToken()) {
                                        ARouter.getInstance().build(Routers.myscore).navigation()
                                    } else {
                                        ARouter.getInstance().build(Routers.login).navigation()
                                    }

                                }
                            }


                        }

                    })
                }
                val loanAdapter = object :BaseAdapterWithBinding<IndexInfoBean.LoanInfo>(cardList!!){
                    override fun getLayoutResource(viewType: Int): Int {
                        return R.layout.item_index_card
                    }

                    override fun setVariableID(): Int {
                        return BR.indexcardmodel
                    }

                    override fun getItemCount(): Int {
                        return cardList!!.size
                    }
                }
                bind!!.rvCards.adapter = resetList(cardList!!)
                if(inited==0) {
                    bind!!.rvCards.addOnItemTouchListener(object : RecyclerItemCLickListenner(activity!!) {
                        override fun onItemClick(view: View, position: Int) {
                            if (cardList!!.get(position).creditCard == null) {
                                if(cardList!!.get(position).loan!=null)
                                ARouter.getInstance().build(Routers.loanDetail).withString("loanid", cardList!!.get(position).loan.n_loan_id.toString()).navigation()
                            } else {
                                ARouter.getInstance().build(Routers.cred_cards).withString("id", cardList!!.get(position).creditCard.nsk_inner_credit_card.n_loan_id.toString()).navigation()

//                            ARouter.getInstance().build(Routers.cred_cards).withString("id", cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_id.toString()).navigation()
//                            FinestWebView.Builder(activity as AppCompatActivity).show(cardList.get(position).creditCard.nsk_inner_credit_card.)

                            }
                        }
                    })
                }

                val adsAdapter = object :BaseAdapterWithBinding<IndexInfoBean.AdInfo>(adlist){
                    override fun getLayoutResource(viewType: Int): Int {
                        return R.layout.item_index_recom
                    }

                    override fun setVariableID(): Int {
                        return BR.recommodel
                    }

                    override fun getItemCount(): Int {
                        return  adlist.size
                    }
                }
                bind!!.rvAds.adapter = adsAdapter
                if(inited==0) {
                    bind!!.rvAds.addOnItemTouchListener(object : RecyclerItemCLickListenner(activity!!) {
                        override fun onItemClick(view: View, position: Int) {
                                when(adlist.get(position).n_link_type){
                                    1->{
                                        val intent = Intent(activity,BaseWebActivity::class.java)
                                        if(URLUtil.isHttpUrl(adlist.get(position).n_link_value)) {
                                            intent.putExtra("url", adlist.get(position).n_link_value)
                                            startActivity(intent)
                                        }
                                    }

                                    4->{
                                        //一键办卡
                                        ARouter.getInstance().build(Routers.one_key_cards).navigation()
                                    }

                                }

                        }
                    })
                }
                val inviteAdapter = object :BaseAdapterWithBinding<IndexInfoBean.InviteInfo>(inviteList!!){
                    override fun getLayoutResource(viewType: Int): Int {
                        return R.layout.item_index_invite
                    }

                    override fun setVariableID(): Int {
                        return BR.invitemodel
                    }

                    override fun getItemCount(): Int {
                        if(inviteList!=null&&inviteList!!.size>0) {
                            return 1
                        }else{
                            return 0
                        }
                    }

                    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                        super.onBindViewHolder(holder, position)
                        val textView = holder.itemView.findViewById<TextView>(R.id.tv_table_name)
                        textView.text = inviteList!!.get(1).n_content
                    }
                }
                bind!!.rvYaoqing.adapter = inviteAdapter
                //邀请有礼
                if(inited==0) {
                    bind!!.rvYaoqing.addOnItemTouchListener(object : RecyclerItemCLickListenner(activity!!) {
                        override fun onItemClick(view: View, position: Int) {
//                        FinestUtils.start(activity as Activity,inviteList[0].n_link_value)
                            if (CkyApplication.getApp().hasToken()) {
                                ARouter.getInstance().build(Routers.invite_activity).navigation()
                            } else {
                                ARouter.getInstance().build(Routers.login).navigation()
                            }
                        }
                    })
                }
                if(inited==0) {
                    inited = 1
                }
            }
        },object :RxjavaUtils.CkyErrorConsumer(){
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
            }
        })



    }

    override fun OnBannerClick(position: Int) {
        val jsonObject = banners!!.get(position).asJsonObject
        val type = jsonObject.get("n_link_type").asInt
        when(type){
            1->{
                val intent = Intent(activity,BaseWebActivity::class.java)
                if(URLUtil.isHttpUrl(bannerClickUrl[position])) {
                    intent.putExtra("url", bannerClickUrl[position])
                    startActivity(intent)
                }
            }
            2->{
                //信用卡
            ARouter.getInstance().build(Routers.cred_cards).withString("id", jsonObject.get("n_link_value").asString).navigation()
            }
            3->{
                //贷款
                ARouter.getInstance().build(Routers.loanDetail).withString("loanid", jsonObject.get("n_link_value").asString).navigation()
            }
            4->{
                //红包
                if(CkyApplication.getApp().hasToken()){
                    ARouter.getInstance().build(Routers.mypacket).navigation()
                }else{
                    ARouter.getInstance().build(Routers.login).navigation()
                }
            }
            5->{
                //nothing
//                ARouter.getInstance().build(Routers.one_key_cards).navigation()
            }
        }

        //banner点击事件
//        FinestUtils.start(index_refresh,activity as Context,  bannerClickUrl[position])
    }

    fun resetList(cardList:ArrayList<IndexInfoBean.LoanInfo>):RecyclerView.Adapter<BaseViewHolder>{
        val adapter =object :RecyclerView.Adapter<BaseViewHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

                if(viewType==1) {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_index_creadicard, parent, false)
                    return BaseViewHolder(view)
                }else{
                    val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), R.layout.item_index_card, parent, false)
                    val holder = object : BaseViewHolder(dataBinding.root){}
                    holder.binding = dataBinding
                    return holder
//                    view = LayoutInflater.from(parent.context).inflate(R.layout.item_index_card, parent, false)
                }

            }

            override fun getItemCount(): Int {
              return cardList.size
            }

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
               val  type =  holder.itemViewType
                if(type==1){
                    //1是信用卡
                    val iv = holder.itemView.findViewById<ImageView>(R.id.iv_bank)
                    val title = holder.itemView.findViewById<TextView>(R.id.tv_title)
                    val tv_sub_title = holder.itemView.findViewById<TextView>(R.id.tv_sub_title)
                    val tvContent= holder.itemView.findViewById<TagFlowLayout>(R.id.tv_content)
                    val tv_apply = holder.itemView.findViewById<TextView>(R.id.tv_apply)
                    title.text = cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_title
                    Glide.with(holder.itemView.context).load(cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_logo_url).into(iv)
                    tv_sub_title.text = cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_subheading
                    tv_apply!!.setOnClickListener {
                        //信用卡详情 申请
//                        ARouter.getInstance().build(Routers.cred_cards).withString("id", cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_bankid.toString()).navigation()
//                        ARouter.getInstance().build(Routers.cred_cards).withString("id", cardList.get(position).creditCard.nsk_inner_credit_card.n_loan_id.toString()).navigation()
                    }
                    tvContent!!.adapter = object : TagAdapter<String>(cardList.get(position).creditCard.n_credit_card_tags) {
                        override fun getView(parent: FlowLayout, position: Int, s: String): View {
                            if(position>1){
                                val view = View(activity)
                                view.visibility=View.GONE
                                return view
                            }
                            val tv =LayoutInflater.from(activity).inflate(R.layout.tv,tvContent, false) as TextView
                            tv.setTextColor(resources.getColor(R.color.orange_main))
                            tv.text = s
                            return tv
                        }
                    }
                }else{
                    //2是贷款
                    holder.binding!!.setVariable(BR.indexcardmodel,cardList!!.get(position))
                    holder.binding!!.executePendingBindings()
                }
            }

            override fun getItemViewType(position: Int): Int {
               if(cardList.get(position).creditCard==null){
                   return 2
               }else{
                   return 1
               }

            }
        }
        return adapter
    }
    fun setItemClick(){




    }


    protected fun installApk(context: Context, apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uri = FileProvider.getUriForFile(context, "com.nsk.app" + ".fileprovider",
                    apkFile)
        } else {
            uri = Uri.fromFile(apkFile)
        }
        if (uri != null) {
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }


}