package com.nsk.app.bussiness.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.android.databinding.library.baseAdapters.BR
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.config.ApiConfig
import com.nsk.app.bussiness.mine.viewmodel.HelpBean
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityHelpBinding
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils

@Route(path = Routers.help)
class HelpActivity:BaseTransStatusActivity() {

    override fun setTitle(): Int {
       return R.string.help
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_help
    }

    override fun initData() {
      val bind = binding as ActivityHelpBinding
        serviceApi.getForString(ApiConfig.helplist_url+"?n_qa_type=1").compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({
                    val jsonParser = JsonParser()
                    val gson = Gson()
                    val jsonArray = jsonParser.parse(it).asJsonArray
                    val listdata = gson.fromJson<ArrayList<HelpBean>>(jsonArray,object :TypeToken<ArrayList<HelpBean>>(){}.type)
                    bind.rvHelps.adapter = object :BaseAdapterWithBinding<HelpBean>(listdata){
                        override fun getLayoutResource(viewType: Int): Int {
                            return R.layout.item_help
                        }

                        override fun setVariableID(): Int {
                           return BR.helpModel
                        }

                        override fun getItemCount(): Int {
                           return listdata.size
                        }
                    }
                },{
//                    ToastUtils.showShort(it.message)
                })
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}