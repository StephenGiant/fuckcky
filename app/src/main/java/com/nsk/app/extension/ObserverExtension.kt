package com.nsk.app.business.extension


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.nsk.cky.ckylibrary.http.response.BaseResponse
import com.nsk.app.utils.RxjavaUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

fun <T>Observable<BaseResponse<T>>.parseJson():Observable<T> {
   return this.subscribeOn(Schedulers.io()).flatMap {
        Observable.just(it.data).observeOn(AndroidSchedulers.mainThread())
    }
    fun <T>Observable<String>.parseJsonFromString():Observable<String>{
        return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }



}

/**
 * 只针对返回值类型为observable<String>的情况
 * rxjava针对网络api的类扩展，当接口响应码为200的时候，走onsuccess 和 ckyerror的逻辑
 * 同时不影响其他的异常捕获,做到了解析时不抛异常不闪退
 */
fun <String>Observable<String>.parseData(successConsumer: RxjavaUtils.CkySuccessConsumer, errorConsumer: RxjavaUtils.CkyErrorConsumer){
//    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(Function {
//        val parser = JsonParser()
//        val jsonElement =  parser.parse(it.toString()).asJsonObject.get("data")//
//        return@Function Observable.just(jsonElement)
//    })

    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap<JsonObject>(Function {

      return@Function Observable.just(JsonParser().parse(it.toString()).asJsonObject)
    })
            .compose(RxjavaUtils.handleResult()).subscribe(successConsumer ,
            errorConsumer as Consumer<in Throwable> )
}

fun <String>Observable<String>.parseData():Observable<JsonElement>{
        return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(Function {
        val parser = JsonParser()
        val jsonElement =  parser.parse(it.toString()).asJsonObject.get("data")//
        return@Function Observable.just(jsonElement)
    })

}