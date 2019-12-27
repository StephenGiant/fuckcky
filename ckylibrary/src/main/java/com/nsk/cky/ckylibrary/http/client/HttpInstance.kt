package com.nsk.cky.ckylibrary.http.client

import android.content.Context
import android.util.Log
import com.nsk.cky.ckylibrary.http.interceptor.MyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class HttpInstance private constructor(url:String,context: Context){
    val bassUrl = url;
     var retrofit: Retrofit
    init {
        //在构造方法调用后调用
        //一系列初始化操作
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)//打印响应体请求体
        //.addNetworkInterceptor(MyInterceptor())
        val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor).connectTimeout(10, TimeUnit.SECONDS).addNetworkInterceptor(MyInterceptor(context))
                .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()
         retrofit = Retrofit.Builder().baseUrl(bassUrl).client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create())
                // 增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                // 增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();


    }
    companion object {
        fun getInstance(url:String,context: Context):HttpInstance{
            Log.i("getInstance",url)
            return HttpInstance(url,context)
        }
    }
    fun <T>getApi (api:Class<T>)=
        retrofit.create(api)



}