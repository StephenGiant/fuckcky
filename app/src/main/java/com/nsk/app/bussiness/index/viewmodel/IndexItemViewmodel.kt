package com.nsk.app.bussiness.index.viewmodel

import android.databinding.ObservableField
import com.nsk.app.base.BaseViewModel

/**
 * @Package com.nsk.app.bussiness.index.viewmodel
 * @author qianpeng
 * @date 2018/9/3.
 * @describe
 */
class IndexItemViewmodel:BaseViewModel() {

    var modelType :Int = 0 //数据类型      0代表邀请 1代表贷款或信用卡 2 代表广告
    val picurl:ObservableField<String> = ObservableField<String>() //图片链接
    val url:ObservableField<String> = ObservableField() //跳转链接

}