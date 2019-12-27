package com.nsk.cky.ckylibrary.widget

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.TypedValue
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import android.view.animation.Animation
import android.view.animation.AlphaAnimation
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.view.View


/**
 * 签到加积分的控件
 * 初步完成，更多功能还没细化
 */
class PointPopuView(context: Context):PopupWindow() {
    lateinit var textView:TextView
    lateinit var mContext:Context
    lateinit var animationSet:AnimationSet
    var mChanged = false
    var mFromY =0f
    var  mToY = 0f
    var mFromAlpha = 0f
    var mToAlpha = 0f
    var mDuration = 0L
    val DISTANCE = 60f  // 默认移动距离


    val FROM_Y_DELTA = 0f // Y轴移动起始偏移量

    val TO_Y_DELTA = DISTANCE // Y轴移动最终偏移量

    val FROM_ALPHA = 1.0f    // 起始时透明度

    val TO_ALPHA = 0.0f  // 结束时透明度

    val DURATION = 800L // 动画时长

    val TEXT = "" // 默认文本

    val TEXT_SIZE = 16 // 默认文本字体大小

    var TEXT_COLOR = Color.BLACK   // 默认文本字体颜色
    init {
        initView(context)
        mContext = context
    }
    fun initView(context: Context){
        val layout = RelativeLayout(context)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
         textView = TextView(context)//显示增加的积分
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        textView.setTextColor(666666)//暂时这么写
        mFromY = FROM_Y_DELTA
        mToY = TO_Y_DELTA
        mFromAlpha =FROM_ALPHA
        mToAlpha = TO_ALPHA
        mDuration = DURATION
        contentView = layout
        initAnime()

    }

    /**
     * 提示文案
     */
    fun setText(string: String){
            textView.text = string
    }
    fun setMyTextColor(resource: Int){
        textView.setTextColor(resource)//设定颜色的资源
    }
    fun setBGTResource (){

    }

    /**
     * 最好是color的id
     */
    fun setTextColor(resource:Int){
        textView.setTextColor(resource)
    }
    /**
     * 初始化签到成功的动画
     */
    fun initAnime ():AnimationSet{
        animationSet = AnimationSet(true)
        val translateAnim = TranslateAnimation(0f, 0f, mFromY, -mToY)
        val alphaAnim = AlphaAnimation(mFromAlpha, mToAlpha)
        animationSet.addAnimation(translateAnim)
        animationSet.addAnimation(alphaAnim)
        animationSet.setDuration(mDuration)
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (isShowing) {
                    Handler().post(Runnable { dismiss() })
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        return animationSet


//        TranslateAnimation(mContext,0f)
    }
    fun show(view:View){
        if (!isShowing) {
            val offsetY = -view.getHeight() - height
            showAsDropDown(view, view.getWidth() / 2 - width / 2, offsetY)
            if (animationSet == null || mChanged) {
                animationSet = initAnime()
                mChanged = false
            }
           startAnime()
        }
    }
    fun startAnime(){
        textView.startAnimation(animationSet)
    }


}