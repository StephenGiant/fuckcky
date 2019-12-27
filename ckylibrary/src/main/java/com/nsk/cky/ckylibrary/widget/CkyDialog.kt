package com.nsk.cky.ckylibrary.widget

import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.ViewGroup



class CkyDialog :DialogFragment() {

//从下方进入 并居于底部
    override fun onResume() {
        super.onResume()
    val window = dialog.window
    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 100 + 90)//Here!
    window.setGravity(Gravity.BOTTOM)
//    window.setWindowAnimations(R.anim.s)
    }
   init {

   }
}