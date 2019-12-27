package com.nsk.app.bussiness.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityAboutusBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils

/**
 * Description: 安全设置
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.about)
class AboutActivity : BaseTransStatusActivity(){
    override fun setTitle(): Int {
        return R.string.about
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_aboutus
    }

    override fun initData() {
        val bind = binding as ActivityAboutusBinding
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        bind.contentCurversion.text = packageInfo.versionName
        serviceApi.getForString(ApiConfig.getversion_url).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
              val json = jsonElement!!.asJsonObject
                val string = json.get("n_version_no").asString
                bind.contentLatestversion.text = string
            }
        },object :RxjavaUtils.CkyErrorConsumer(){})


    }

    override fun initView() {

        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}