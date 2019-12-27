package com.nsk.cky.ckylibrary.http.response

open class BaseResponse<T> {
 var errorCode:String? = null
    var hasError :Boolean = false
    var errorMessage:String? = null
    var data:T? = null
}