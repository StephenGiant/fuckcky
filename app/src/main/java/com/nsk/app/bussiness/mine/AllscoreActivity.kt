package com.nsk.app.bussiness.mine

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.ScoreDetail
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import kotlinx.android.synthetic.main.activity_recycler.*
import java.util.*

@Route(path = Routers.allmyscore)
class AllscoreActivity :BaseTransStatusActivity() {
    lateinit var mAdapter1: CommonAdapter<ScoreDetail>
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    var scoreList= ArrayList<ScoreDetail>()
    override fun setTitle(): Int {
        return R.string.score
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_recycler
    }

    override fun initData() {

        serviceApi.getForString(ApiConfig.getUserScore_url).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                val data = jsonElement!!.asJsonArray
                val gson = Gson()
                if (data != null) {
                     scoreList = gson.fromJson<ArrayList<ScoreDetail>>(data, object : TypeToken<ArrayList<ScoreDetail>>() {
                    }.type)
                    setItData()
                }
            }

        }, object : RxjavaUtils.CkyErrorConsumer() {
        })
    }

    private fun setItData() {
            rv.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
            mAdapter1 = object : CommonAdapter<ScoreDetail>(this, R.layout.item_scoredetail,  scoreList) {
                override fun convert(holder: ViewHolder?, t: ScoreDetail, position: Int) {
                    val tv_source = holder?.getView<TextView>(R.id.tv_source)
                    val tv_date = holder?.getView<TextView>(R.id.tv_date)
                    val tv_canuse = holder?.getView<TextView>(R.id.tv_canuse)
                    val tv_scoreChange = holder?.getView<TextView>(R.id.tv_scoreChange)
                    tv_date!!.text=t.n_record_createtime
                    tv_canuse!!.text= "余额:"+t.n_record_balance.toString()
                    if(t.n_record_balance>0) {
                        tv_scoreChange!!.text = "+" + t.n_record_score.toString()
                    }else{
                        tv_scoreChange!!.text = t.n_record_score.toString()
                    }
                    tv_source!!.text=t.n_record_name
                }

            }
            val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
            decoration.setDrawable(resources.getDrawable(R.drawable.divider))
            rv.addItemDecoration(decoration)
            mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
            mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv, false))
            rv.adapter = mEmptyWrapper

    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}