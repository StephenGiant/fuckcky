package com.nsk.app.bussiness.mine

import android.app.Activity
import android.databinding.DataBindingUtil
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.index.viewmodel.MineFragmentViewModel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.MineFragmentBinding
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers

@Route(path = Routers.mine_activity)
class MineActivity:BaseTransStatusActivity {
    constructor() : super()
    var mineFragmentViewModel:MineFragmentViewModel?=null
    override fun setTitle(): Int {
       return R.string.mine
    }

    override fun getContentLayoutId(): Int {
       return R.layout.mine_fragment
    }

    override fun initData() {

    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))
        right_menu!!.visibility = View.VISIBLE
        right_menu!!.setOnClickListener({
            if(CkyApplication.getApp().hasToken()) {
                ARouter.getInstance().build(Routers.mynotice).navigation()
            }else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        })
        title!!.setNavigationIcon(R.drawable.btn_back)
        title!!.setCenterTitleSize(18)
        title!!.setBackgroundColor(resources.getColor(R.color.white))


//        mineFragmentViewModel!!.initUser()
    }

    override fun onResume() {
        super.onResume()
        val  bind = binding as MineFragmentBinding
            mineFragmentViewModel = MineFragmentViewModel()
            bind!!.minemodel = mineFragmentViewModel
            bind.rlAccount!!.minemodel = mineFragmentViewModel
            mineFragmentViewModel!!.initUser()
        if(CkyApplication.getApp().token==null){
            bind.rlAccount.ivPersonIcon.setImageResource(R.drawable.person_icon)
        }


    }
}