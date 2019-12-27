package com.nsk.cky.ckylibrary.http.callback

/**
 * http回调接口
 */
interface ResultCallBack<D> {
   fun onSuccess(msg:String,data:D)
    fun onSuccess(msg:String,data:List<D>)

}