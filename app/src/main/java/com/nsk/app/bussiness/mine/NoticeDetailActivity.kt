package com.nsk.app.bussiness.mine

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.webkit.URLUtil
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.NoticeDetailBean
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.base.BaseWebActivity
import com.nsk.cky.ckylibrary.utils.FinestUtils
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import kotlinx.android.synthetic.main.activity_recycler.*
import java.util.*

/**
 * Description: 通知信息详情
 * Company    :
 * @author     :
 * @date       : 2018/9/8
 */
@Route(path = Routers.notice_detail)
class NoticeDetailActivity : BaseTransStatusActivity(){
    var datas=ArrayList<NoticeDetailBean>()
    lateinit var mAdapter1: CommonAdapter<NoticeDetailBean>
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    override fun setTitle(): Int {
        return R.string.notice_detail_1
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_recycler
    }

    override fun initData() {
        val extras = intent.extras
        val type = extras.getInt(UserConstants.type)
        when (type) {
            1 -> {
                reSetTitle(R.string.notice_detail_1)
            }
            2 -> {
                reSetTitle(R.string.notice_detail_2)
            }
            3 -> {
                reSetTitle(R.string.notice_detail_3)
            }
        }
        initDetail(type)
    }

    private fun initDetail(type: Int) {
        serviceApi.getForString(ApiConfig.onetypenotificationList_url+"?notificationType="+type).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({
                    response ->
                    val gson = Gson()
                    datas = gson.fromJson<ArrayList<NoticeDetailBean>>(response,object : TypeToken<ArrayList<NoticeDetailBean>>(){
                    }.type)
                    setData()
                },{
                    ToastUtils.showLong(it.message)
                })

    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
    private fun setData(){
        rv.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        mAdapter1 = object : CommonAdapter<NoticeDetailBean>(this, R.layout.item_sys,  datas) {
            override fun convert(holder: ViewHolder?, t: NoticeDetailBean, position: Int) {
                val tv_title = holder?.getView<TextView>(R.id.tv_title)
                val tv_content = holder?.getView<TextView>(R.id.tv_content)
                val tv_begintime = holder?.getView<TextView>(R.id.tv_begintime)
                val ll_all = holder?.getView<LinearLayout>(R.id.ll_all)
                tv_title!!.text=t.n_notice_title
                tv_content!!.text=t.n_noticecontent
                tv_begintime!!.text=t.n_notice_createtime
                ll_all!!.setOnClickListener{
                    val map = mapOf<String, String>(Pair("n_notice_id", t.n_notice_id))
                    serviceApi.getForString(ApiConfig.nofification_view_url,map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                        override fun onSuccess(jsonElement: JsonElement?) {
                        }
                    }, object : RxjavaUtils.CkyErrorConsumer() {

                    })
                    //判断跳转类型
                    when(t.n_notice_link_type){
                        1-> {
                            ARouter.getInstance().build(Routers.cred_cards).withString("id",t.n_notice_link_value).navigation()
                        }
                        2->{
                            ARouter.getInstance().build(Routers.loanDetail).withString("loanid",t.n_notice_link_value).navigation()
                        }
                        3-> {
//                            FinestUtils.start(viewRoot, this@NoticeDetailActivity, t.n_notice_link_value)
                            val intent = Intent(this@NoticeDetailActivity, BaseWebActivity::class.java)
                            if(URLUtil.isHttpUrl(t.n_notice_link_value)){
                                intent.putExtra("url", t.n_notice_link_value)
                                startActivity(intent)
                            }
                        }
                    }

                }
            }

        }
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv.addItemDecoration(decoration)
        mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
        mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv, false))
        rv.adapter = mEmptyWrapper

    }
}