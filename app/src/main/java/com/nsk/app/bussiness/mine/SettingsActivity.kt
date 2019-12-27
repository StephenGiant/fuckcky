package com.nsk.app.bussiness.mine

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import kotlinx.android.synthetic.main.activity_settings.*
import me.imid.swipebacklayout.lib.SwipeBackLayout

@Route(path = Routers.settings_activity)
class SettingsActivity:BaseTitleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeMpde(SwipeBackLayout.EDGE_LEFT)
    }

    override fun setTitle(): Int {
       return R.string.settings
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_settings
    }

    override fun initData() {

    }

    override fun initView() {
        //安全设置
        ct_security.setOnClickListener {
            ARouter.getInstance().build(Routers.safe_set).navigation()
        }
        //负责声明
        ct_res.setOnClickListener {
            ARouter.getInstance().build(Routers.fzsm).navigation()
        }
        //关于我们
        ct_about_us.setOnClickListener {
            ARouter.getInstance().build(Routers.about).navigation()
        }
        //退出
        ct_unregist.setOnClickListener {
            val sharedPreferences = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE)
            sharedPreferences.edit().clear().commit()
            CkyApplication.getApp().token=null
            finish()
        }
    }
}