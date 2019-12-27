package com.nsk.app.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nsk.app.config.CkyApplication
import com.nsk.cky.ckylibrary.http.ServiceApi
import javax.inject.Inject

/**
 * 继承于fragment的基类fragment
 */
open abstract class BaseContentFragment :Fragment(){
    @Inject
    lateinit var serviceApi: ServiceApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getContentLayoutId(), container, false)
        CkyApplication.getApp().apiComponent.inject(this)
        initView(view)
        initData()
        return view
    }

    abstract fun getContentLayoutId(): Int
    abstract fun initView(view:View)
    abstract fun initData()
}