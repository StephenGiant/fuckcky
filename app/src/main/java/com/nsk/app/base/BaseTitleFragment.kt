package com.nsk.app.base

import android.app.Dialog
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewStub
import com.nsk.app.caikangyu.R
import com.nsk.cky.ckylibrary.widget.TitleBar


abstract class BaseTitleFragment : BaseFragment() {
    var title : TitleBar? = null
    var content: View?=null//拿到此content 可以使用databinding


    /**
     * title下面的布局，一定要实现
     */
    abstract fun getContentLayoutId(): Int

    override fun getLayoutId(): Int {
        return R.layout.base_content
    }

     override fun onBindViewBefore(root: Dialog) {
        val stub = root.findViewById(R.id.lay_content) as ViewStub
         title  = root.findViewById(R.id.title_bar) as TitleBar
        stub.layoutResource = getContentLayoutId()
        content = stub.inflate()
         val appCompatActivity = activity as AppCompatActivity
         appCompatActivity.setSupportActionBar(title)
         appCompatActivity.supportActionBar!!.setDisplayShowTitleEnabled(false)
         title!!.setCenterTitle(setTitle())
         title!!.setNavigationIcon(R.drawable.btn_back)
         title!!.setNavigationOnClickListener { dismiss() }
         initData()
         initView()
    }
    /**
     * title的文字，一定要实现
     */
    @StringRes
    abstract fun setTitle(): Int

    /**
     * 初始化参数和布局
     */
    abstract fun initData()
    abstract fun initView()
}