package com.nsk.cky.ckylibrary.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.nsk.cky.ckylibrary.R

class RoundImageView(context: Context?) : ImageView(context) {
constructor( context: Context?,attrs: AttributeSet):this(context){
//    val ta : TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.TagView)
}


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCicle(canvas)

    }

    fun drawCicle(canvas: Canvas?){
//        canvas!!.drawCircle()
    }
}