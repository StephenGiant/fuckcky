package com.nsk.app.bussiness.healthy

import android.graphics.Color
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.os.Build
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.healthy.viewmodel.HealthIndexViewModel
import com.nsk.app.caikangyu.databinding.ActivityHealthBinding
import com.nsk.app.caikangyu.databinding.MineFragmentBinding
import com.nsk.app.config.Routers
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.widget.DividerItemDecoration

@Route(path = Routers.health_index)
class HealthyIndexActivity :BaseTransStatusActivity() {


    override fun setTitle(): Int {
     return R.string.healthy_title
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_health
    }

    override fun initData() {
        val  bind = binding as ActivityHealthBinding
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        bind.rvDepartments.addItemDecoration(decoration)
        val decoration2 = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration2.setDrawable(resources.getDrawable(R.drawable.divider))
        decoration2.dleft = 30
        bind.rvHospitals.addItemDecoration(decoration2)
        bind.healthyindexmodel = HealthIndexViewModel()
    }

    override fun initView() {
        title!!.setBackgroundColor(resources.getColor(R.color.health_green))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(R.drawable.back_white)
        setStatusBarColor(resources.getColor(R.color.health_green))
        reSetTitleColor(resources.getColor(R.color.white))
    }
}