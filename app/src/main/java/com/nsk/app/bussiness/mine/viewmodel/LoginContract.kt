package com.nsk.app.bussiness.mine.viewmodel

import com.nsk.app.base.BasePresenter
import com.nsk.app.base.BaseView

interface LoginContract {
    interface View : BaseView {
        //登录成功
        fun success(token:String)

         fun getValidCode(code: String)
    }
    interface Presenter : BasePresenter<View> {
    }
}