package com.nsk.cky.ckylibrary.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

abstract class RecyclerItemCLickListenner(context: Context) :RecyclerView.OnItemTouchListener {
    var longPress = false
    lateinit var gestureDetector:GestureDetector
init {
    gestureDetector = GestureDetector(context,object :GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            //消耗单击事件
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            longPress = true
            super.onLongPress(e)

        }
    })
}

    /**
     * 默认空实现
     */
    fun onLongClick(view: View,position:Int ){

    }

    abstract fun onItemClick(view: View,position: Int)
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        val view = rv!!.findChildViewUnder(e!!.x, e.y)//找到点击的view
        if(view!=null&&gestureDetector.onTouchEvent(e)&&!longPress){
            onItemClick(view,rv.getChildLayoutPosition(view))
        }else if(view!=null&&longPress){
            onLongClick(view,rv.getChildLayoutPosition(view))
            longPress=false
        }

       return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}