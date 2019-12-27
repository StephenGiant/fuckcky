package com.nsk.app.business.extension

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter

fun Activity.toActivity(url:String){
    ARouter.getInstance().build(url).navigation()
}