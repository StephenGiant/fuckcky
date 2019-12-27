package com.nsk.app.base

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.nsk.cky.ckylibrary.widget.SwipeActivity
import me.imid.swipebacklayout.lib.SwipeBackLayout

open class BaseActivity:SwipeActivity (){

    var swipeMode = SwipeBackLayout.EDGE_LEFT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.e(localClassName)
        //解决第一次安装activity栈的问题
        ARouter.getInstance().inject(this)
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }
        if(swipeMode!=0) {
            setSwipeMpde(swipeMode)//默认方案
        }
    }

}