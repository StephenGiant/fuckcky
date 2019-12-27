package com.nsk.cky.ckylibrary.http

/**
 * @Package com.nsk.cky.ckylibrary.http
 * @author qianpeng
 * @date 2018/8/31.
 * @describe
 */
class CkyException(msg :String,code:String) :Throwable() {
var errormessage = msg
    var errorcode = code

}