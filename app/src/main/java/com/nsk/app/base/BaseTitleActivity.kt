package com.nsk.app.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
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


/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/7/11
 */
abstract class BaseTitleActivity : BaseActivity() {
    var title: TitleBar? = null
    var content: View? = null
    var viewRoot: ViewGroup? = null
    var binding: ViewDataBinding? = null
    var right_menu:ImageView?=null

    @Inject
    lateinit var serviceApi: ServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewRoot = LayoutInflater.from(this).inflate(R.layout.base_content, null) as ViewGroup
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
        setSupportActionBar(title)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        title!!.setCenterTitle(setTitle())
        title!!.setNavigationIcon(R.drawable.btn_back)
        title!!.setNavigationOnClickListener { onBackPressed() }
        CkyApplication.getApp().apiComponent.inject(this)
        initData()
        initView()

    }

    /**
     * 重设title
     */
    fun reSetTitle(text: Int) {
        title!!.setCenterTitle(text)
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
    /**
     * 设置title的右边文字（可以不实现）
     */
    fun setRight(text: Int,vis: Boolean, listener: RightListener) {

        val view = LayoutInflater.from(this).inflate(R.layout.view_right, null)
        val tvR: TextView = view.findViewById(R.id.tv_right) as TextView
        val iv1: ImageView = view.findViewById(R.id.iv) as ImageView
        tvR.setText(text)
        if(vis){
            iv1.visibility=View.VISIBLE
        }else{
            iv1.visibility=View.GONE
        }
        tvR.setOnClickListener { listener.onClick() }
        title!!.addRightView(view)
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
        val view = LayoutInflater.from(this).inflate(R.layout.view_right, null)
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
        title!!.visibility=View.GONE
    }
    fun showToolBar() {
        title!!.visibility=View.VISIBLE
    }
    fun setContentVariable(variable: Int, model: Any) {
        //空实现
        //实际使用的时候参考注释
        //viewDataBinding!!.setVariable(variable,model)
    }

}
