package com.nsk.app.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @Package com.nsk.app.widget
 * @author qianpeng
 * @date 2018/9/4.
 * @describe
 */
class FixViewPager : ViewPager {

    var lastX = 0f
    var lastY = 0f
    var downX = 0f
    var downY = 0f
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if(parent!=null){
//            parent.requestDisallowInterceptTouchEvent(true)
//        }
        var x= ev!!.rawX
        var y = ev!!.rawY
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE ->{
                val dy = Math.abs(y-lastY)
                val dx = Math.abs(x-lastX)
                lastX = x
                lastY = y
                if(dx>dy*2){
                    if(parent!=null){
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        return super.onTouchEvent(ev)
    }

}