package com.nsk.app.bussiness.loan

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bussiness.loan.viewmodel.LoanIndexViewmodel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.LoanFragmentBinding
import com.nsk.app.config.Routers
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner

/**
 * @Package com.nsk.app.bussiness.loan
 * @author qianpeng
 * @date 2018/8/28.
 * @describe
 */
@Route(path = Routers.loan_index)
class LoanIndexActivity :BaseTransStatusActivity() {
    var loanBind:LoanFragmentBinding?=null
    override fun setTitle(): Int {
      return R.string.loan_market
    }

    override fun getContentLayoutId(): Int {
      return R.layout.loan_fragment
    }

    override fun initData() {

    }

    override fun initView() {
        loanBind = binding as LoanFragmentBinding
        val loanIndexViewmodel = LoanIndexViewmodel()
        loanIndexViewmodel.initData()
        loanBind!!.loanuimodel = loanIndexViewmodel
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        loanBind!!.rvLoans.addItemDecoration(decoration)
        loanBind!!.rvLoans.addOnItemTouchListener(object :RecyclerItemCLickListenner(this){
            override fun onItemClick(view: View, position: Int) {
               loanIndexViewmodel.onItemClick(position)
            }
        })
        setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        title!!.setCenterTitleSize(18)
        reSetTitleColor(resources.getColor(R.color.white))
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.back_white))

    }

}