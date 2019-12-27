package com.nsk.cky.ckylibrary.widget

import android.os.Bundle
import android.util.DisplayMetrics
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

open class SwipeActivity :SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun setSwipeMpde(mode:Int){
        swipeBackLayout.setEdgeTrackingEnabled(mode)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val phoneWidth = dm.widthPixels
        swipeBackLayout.setEdgeSize(phoneWidth/2)
//        setSwipeMpde(mode)
    }
}