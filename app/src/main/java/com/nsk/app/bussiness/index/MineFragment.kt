package com.nsk.app.bussiness.index

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.util.Log
import android.view.View
import com.nsk.app.base.BaseContentFragment
import com.nsk.app.bussiness.index.viewmodel.MineFragmentViewModel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.MineFragmentBinding

/**
 * 我的页面
 */
class MineFragment :BaseContentFragment() {


    companion object {
        val NEEDREFRESH = 0x1000
    }
    //是否登录
    var isLogining=false
    var mineFragmentViewModel:MineFragmentViewModel?=null
    override fun initData() {

    }

    override fun initView(view: View) {
        val sp = activity!!.getSharedPreferences("ckyconst",Activity.MODE_PRIVATE)
        val token = sp.getString("token", "")
        //我的贷款
        val  binding = DataBindingUtil.bind<MineFragmentBinding>(view)
         mineFragmentViewModel = MineFragmentViewModel()
        binding!!.minemodel = mineFragmentViewModel
        binding.rlAccount!!.minemodel  = mineFragmentViewModel
        mineFragmentViewModel!!.initUser()
//        if(StringUtils.isEmpty(token)){
//            //显示登录
//        }else{
//            //调接口 显示头像
//            mineFragmentViewModel.initUser()
//        }
//        ct_myloan.setOnClickListener{
//            ARouter.getInstance().build(Routers.my_loan).navigation()
//        }
//        //我的健康
//        ct_myhealth.setOnClickListener{
//            ARouter.getInstance().build(Routers.my_heal).navigation()
//        }
//        //帮助
//        ct_help.setOnClickListener{
//            ARouter.getInstance().build(Routers.help).navigation()
//        }
//        //设置
//        ct_setting.setOnClickListener{
//            if(isLogining){
//                ARouter.getInstance().build(Routers.setInfo).navigation()
//            }else{
//                ARouter.getInstance().build(Routers.login).navigation()
//            }
//        }
    }
        fun refreshData(){
            if(mineFragmentViewModel!=null){
                mineFragmentViewModel!!.initUser()
            }
        }
    override fun getContentLayoutId(): Int {
        return R.layout.mine_fragment
    }




}