package com.nsk.app.bussiness.healthy

import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.healthy.viewmodel.HealthSpecialViewModel
import com.nsk.app.bussiness.healthy.viewmodel.HealthyCategoriBean
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivitySpecialhealthyBinding
import com.nsk.app.config.Routers

@Route(path = Routers.healthy_special)
class HealthSpecialActivity :BaseTransStatusActivity() {

    override fun setTitle(): Int {
        return R.string.special_request
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_specialhealthy
    }

    override fun initData() {
    val department = intent.getSerializableExtra("department")as HealthyCategoriBean
        val bind = binding as ActivitySpecialhealthyBinding
        bind.healthspecialModel = HealthSpecialViewModel(department)

    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}