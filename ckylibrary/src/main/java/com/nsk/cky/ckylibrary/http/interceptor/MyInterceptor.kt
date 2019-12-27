package com.nsk.cky.ckylibrary.http.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response

class MyInterceptor (val context:Context?): Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val  sp = context!!.getSharedPreferences("ckyconst",0);
        val token = sp.getString("token", null)
        val originRequest = chain!!.request()
        val builder = originRequest.newBuilder()
        if(!originRequest.url().url().path.contains("Image")) {
            builder.header("Content-Type", "application/json;charset=UTF-8")
        }else{
            builder.header("Content-Type", "multipart/form-data; boundary=----caikangyu------------caikangyu")
        }
        if(token!=null){
            builder.header("Authorization",token)
        }
            val request = builder.build()
            return chain!!.proceed(request)

    }
}