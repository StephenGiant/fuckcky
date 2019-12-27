package com.nsk.app.bussiness.index

import android.databinding.DataBindingUtil
import android.view.View
import com.nsk.app.base.BaseContentFragment
import com.nsk.app.bussiness.loan.viewmodel.LoanIndexViewmodel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.LoanFragmentBinding

class LoanFragment : BaseContentFragment() {
    override fun getContentLayoutId(): Int {
        return R.layout.loan_fragment
    }

    override fun initView(view: View) {

        val  binding = DataBindingUtil.bind<LoanFragmentBinding>(view)
        binding!!.loanuimodel = LoanIndexViewmodel()
    }

    override fun initData() {

    }
}