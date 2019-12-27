package com.nsk.app.base

interface BasePresenter<T : BaseView> {

    fun attachView(view: T)

    fun detachView()
}
