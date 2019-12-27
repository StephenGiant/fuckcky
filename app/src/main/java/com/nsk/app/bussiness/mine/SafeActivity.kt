package com.nsk.app.bussiness.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import kotlinx.android.synthetic.main.activity_safe_set.*

@Route(path = Routers.safe_set)
class SafeActivity : BaseTitleActivity(){
    override fun setTitle(): Int {
        return R.string.safe_set
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_safe_set
    }

    override fun initData() {
    }

    override fun initView() {
        //修改密码
        ct_change.setOnClickListener {
            ARouter.getInstance().build(Routers.reSet).navigation()
        }
        //完善用户信息
        ct_info.setOnClickListener {
            ARouter.getInstance().build(Routers.user_info).navigation()
        }
    }
}