package com.nsk.app.base

import android.databinding.BaseObservable
import com.nsk.app.config.CkyApplication
import com.nsk.cky.ckylibrary.http.ServiceApi
import java.io.Serializable
import javax.inject.Inject

open class BaseViewModel :BaseObservable(),Serializable{
    @Inject
    lateinit var serviceApi: ServiceApi
    init {
        CkyApplication.getApp().apiComponent.inject(this)

    }





}