package com.nsk.app.base

import android.annotation.TargetApi
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.nsk.app.caikangyu.R
import com.nsk.app.config.CkyApplication
import com.nsk.cky.ckylibrary.RightListener
import com.nsk.cky.ckylibrary.http.ServiceApi
import com.nsk.cky.ckylibrary.widget.TitleBar
import javax.inject.Inject

abstract class BaseTransStatusActivity : BaseActivity() {

    var title: TitleBar? = null
    var content: View? = null
    var viewRoot: ViewGroup? = null
    var binding: ViewDataBinding? = null
    var right_menu: ImageView?=null
    var topview :View?=null
    var rightTitle :TextView?=null

    @Inject
    lateinit var serviceApi: ServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewRoot = LayoutInflater.from(this).inflate(R.layout.base_translutcontent, null) as ViewGroup
        setContentView(viewRoot)
        var lay: ViewStub = findViewById(R.id.lay_content) as ViewStub
        title = findViewById(R.id.title_bar) as TitleBar
//        lay.setOnInflateListener(object : ViewStub.OnInflateListener {
//            override fun onInflate(p0: ViewStub?, topview: View?) {
////                 viewDataBinding = DataBindingUtil.bind<ViewDataBinding>(topview!!)!!
//            }
//        })
        lay.layoutResource = getContentLayoutId()
        content = lay.inflate()
        try {
            binding = DataBindingUtil.bind<ViewDataBinding>(content!!)
        } catch (exception: IllegalArgumentException) {
            //如果没有加layout就会报错 所以抓这个异常
            exception.printStackTrace()
        }
        right_menu = findViewById(R.id.iv_rightmenu) as ImageView?
        rightTitle = findViewById(R.id.tv_right_title) as TextView
        setSupportActionBar(title)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        title!!.setCenterTitle(setTitle())
//        setNaviIcon(R.drawable.arrow_leftwhite)
        title!!.setNavigationOnClickListener { onBackPressed() }
        CkyApplication.getApp().apiComponent.inject(this)
        topview = findViewById(R.id.title_status)
        if (Build.VERSION.SDK_INT >= 21) {

//            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        }else{

            topview!!.visibility = View.GONE
        }
        initView()
        initData()

    }

    /**
     * 重设title
     */
    fun reSetTitle(text: Int) {
        title!!.setCenterTitle(text)
    }
    /**
     * 重设titlecolor
     */
    fun reSetTitleColor(color: Int) {
        title!!.setCenterTitleColor(color)
    }
    fun setStatusBarColor(color: Int){
        val decorView = window.decorView
        var option = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE )
        if(topview!=null) {
            topview!!.setBackgroundColor(color)
            if(color==Color.WHITE){
                option = option  or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            }
            decorView.systemUiVisibility = option
        }
    }
    /**
     * 设置title的右边文字（可以不实现）
     */
    fun setRight(text: Int, listener: RightListener) {
        val view = LayoutInflater.from(this).inflate(R.layout.view_right, null)
        val tvR: TextView = view.findViewById(R.id.tv_right) as TextView
        tvR.setText(text)
        tvR.setOnClickListener { listener.onClick() }
        title!!.addRightView(view)
    }

    fun setNaviIcon(res:Int){
        title!!.setNavigationIcon(res)
    }

    /**
     * title的文字，一定要实现
     */
    @StringRes
    abstract fun setTitle(): Int

    /**
     * title下面的布局，一定要实现
     */
    abstract fun getContentLayoutId(): Int

    /**
     * 初始化参数和布局
     */
    abstract fun initData()

    abstract fun initView()
    fun noToolBar() {
        title!!.visibility= View.GONE
    }
    fun showToolBar() {
        title!!.visibility= View.VISIBLE
    }
    fun setContentVariable(variable: Int, model: Any) {
        //空实现
        //实际使用的时候参考注释
        //viewDataBinding!!.setVariable(variable,model)
    }
    /**
     * 设置title的右边文字（可以不实现）
     */
    var list=ArrayList<View>()
    var dex=0
    fun setRight(text: String,vis: Boolean, listener: RightListener) {
        if(list.size>=1){
            title!!.removeView(list[dex-1])
        }
        dex++
        val view = LayoutInflater.from(this).inflate(R.layout.view_right_2, null)
        val tvR: TextView = view.findViewById(R.id.tv_right) as TextView
        val iv1: ImageView = view.findViewById(R.id.iv) as ImageView
        tvR.text = text
        tvR.setOnClickListener { listener.onClick() }
        if(vis){
            iv1.visibility=View.VISIBLE
        }else{
            iv1.visibility=View.GONE
        }
        list.add(view)
        title!!.addRightView(view)
    }

    @TargetApi (23)
    fun liuhai(){

    }
}