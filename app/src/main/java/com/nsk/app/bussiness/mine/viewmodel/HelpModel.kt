package com.nsk.app.bussiness.mine.viewmodel

import android.databinding.ObservableField
import com.nsk.app.base.BaseViewModel

class HelpModel:BaseViewModel() {
    val question = ObservableField<String>()
    val answer = ObservableField<String>()
}