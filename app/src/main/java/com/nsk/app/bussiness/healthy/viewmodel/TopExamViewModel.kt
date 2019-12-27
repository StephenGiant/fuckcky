package com.nsk.app.bussiness.healthy.viewmodel

import android.databinding.ObservableField
import com.google.gson.JsonParser
import com.nsk.app.base.BaseViewModel
import com.nsk.app.config.ApiConfig
import com.nsk.app.utils.RxjavaUtils

class TopExamViewModel():BaseViewModel() {
    var enable = false

    val topexamDes = ObservableField<String>()
    init {
        serviceApi.getForString(ApiConfig.getTopExamination_url).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult()).subscribe({ response ->
                    val json = JsonParser().parse(response).asJsonObject
                    enable = json.get("n_examination_enable").asInt==1
                    topexamDes.set(json.get("n_examination_describe").asString)
                },{})
    }


}