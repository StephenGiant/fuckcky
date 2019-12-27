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
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.ScoreDetail
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.BR
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
import kotlinx.android.synthetic.main.activity_myscore.*
import java.util.*

@Route(path = Routers.myscore)
class ScoreActivity :BaseTitleActivity(){

    override fun setTitle(): Int {
        return R.string.myscore
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_myscore
    }

    override fun initData() {
//        serviceApi.getForString(ApiConfig.getUserScore_url).parseData(object : RxjavaUtils.CkySuccessConsumer() {
//            override fun onSuccess(jsonElement: JsonElement?) {
//                val data = jsonElement!!.asJsonArray
//                val gson = Gson()
//                if (data != null) {
//                    scoreList = gson.fromJson<ArrayList<ScoreDetail>>(data, object : TypeToken<ArrayList<ScoreDetail>>() {
//                    }.type)
//                    setItData()
//                }
//            }
//
//        }, object : RxjavaUtils.CkyErrorConsumer() {
//        })

        serviceApi.getForString(ApiConfig.getUserInfo_url).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val jsonObject = jsonElement!!.asJsonObject
                tv_userScore.text = jsonObject.get("n_user_score").asInt.toString()
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })
        serviceApi.getForString(ApiConfig.getUserScore_url).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val gson = Gson()
             val array = jsonElement!!.asJsonArray
                if(array !=null) {
                    val scoreList =  gson.fromJson<List<ScoreDetail>>(array,object :TypeToken<List<ScoreDetail>>(){
                    }.type)
                    val item = scoreList.get(0)
                    val adapter_bind = object : BaseAdapterWithBinding<ScoreDetail>(scoreList) {

                        //                                    override fun getVariableId(): Int {
//                                        return BR.scoreModel
//                                    }
//
//                                    override fun getLayoutResource(): Int {
//                                        return R.layout.item_scoredetail
//                                    }
                        override fun getLayoutResource(viewType: Int): Int {
                            return R.layout.item_scoredetail
                        }

                        override fun setVariableID(): Int {
                            return BR.scoreModel
                        }

                        override fun getItemCount(): Int {
                            if(scoreList.size>3){
                                return 4
                            }else{
                                return scoreList.size
                            }
                        }
                    }
                    rv_score.layoutManager = LinearLayoutManager(this@ScoreActivity)
                    rv_score.adapter = adapter_bind
                }
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })


    }
    lateinit var mAdapter1: CommonAdapter<ScoreDetail>
    private var mEmptyWrapper: EmptyWrapper<Any>? = null
    var scoreList= ArrayList<ScoreDetail>()
    private fun setItData() {
        rv_score.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        mAdapter1 = object : CommonAdapter<ScoreDetail>(this, R.layout.item_scoredetail,  scoreList) {
            override fun convert(holder: ViewHolder?, t: ScoreDetail, position: Int) {
                val tv_source = holder?.getView<TextView>(R.id.tv_source)
                val tv_date = holder?.getView<TextView>(R.id.tv_date)
                val tv_canuse = holder?.getView<TextView>(R.id.tv_canuse)
                val tv_scoreChange = holder?.getView<TextView>(R.id.tv_scoreChange)
                tv_date!!.text=t.n_record_createtime
                tv_canuse!!.text= t.n_record_score.toString()
                tv_scoreChange!!.text="+"+ t.n_record_balance.toString()
                tv_source!!.text=t.n_record_name
            }

        }
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_score.addItemDecoration(decoration)
        mEmptyWrapper = EmptyWrapper<Any>(mAdapter1)
        mEmptyWrapper!!.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rv_score, false))
        rv_score.adapter = mEmptyWrapper

    }


    override fun initView() {

        val score = intent.getStringExtra("score")
        tv_userScore.setText(score)
        //全部积分
        tv_seeall.setOnClickListener{
            ARouter.getInstance().build(Routers.allmyscore).navigation()
        }
        //全部现金
//        all_reaward.setOnClickListener{
//            ARouter.getInstance().build(Routers.allCash).navigation()
//        }
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_score.addItemDecoration(decoration)
    }
}