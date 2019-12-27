package com.nsk.cky.ckylibrary.http

import com.nsk.cky.ckylibrary.http.response.BaseResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File

/**
 * 封装整体的请求方式
 */
interface ServiceApi {
    /**
     * Form表单Post
     */
    @FormUrlEncoded
    @POST
    abstract fun post(@Url path: String, @FieldMap params:@JvmSuppressWildcards Map<String, Any>): @JvmSuppressWildcards Observable<BaseResponse<Any>>

    /**
     * 请求体Post
     */
    @POST
    abstract  fun post(@Url path: String, @Body requestBody: RequestBody): @JvmSuppressWildcards Observable<BaseResponse<Any>>
    /**
     * 请求体Post json
     */
    @POST
    abstract  fun postJson(@Url path: String, @Body any: @JvmSuppressWildcards Any): @JvmSuppressWildcards Observable<BaseResponse<Any>>

    /**
     * 带请求参数的Get
     */
    @GET
    abstract operator fun get(@Url path: String, @QueryMap params: @JvmSuppressWildcards Map<String, Any>): @JvmSuppressWildcards Observable<BaseResponse<Any>>

    /**
     * 无参Get
     */
    @GET
    abstract operator fun get(@Url path: String): @JvmSuppressWildcards Observable<BaseResponse<Any>>
    /**
     * 无参Get
     */
    @GET
    abstract  fun get_noparam(@Url path: String): @JvmSuppressWildcards Observable<String>

//    -------------------------------------------
//    以上都是不用解析data的情况 因为retrofit的动态代理模型不支持二层泛型,而实体类种类繁多，用any会损失数据
    /**
     * Form表单Post
     */
    @FormUrlEncoded
    @POST
    abstract fun postForString(@Url path: String, @FieldMap params:@JvmSuppressWildcards Map<String, Any>): @JvmSuppressWildcards Observable<String>

    /**
     * 请求体Post
     */
    @POST
    abstract  fun postForString(@Url path: String, @Body requestBody: RequestBody): @JvmSuppressWildcards Observable<String>
    /**
     * 请求体Post json
     */
    @POST
    abstract  fun postJsonForString(@Url path: String, @Body any: @JvmSuppressWildcards Any): @JvmSuppressWildcards Observable<String>

    /**
     * 带请求参数的Get
     */
    @GET
    abstract  fun getForString(@Url path: String, @QueryMap params: @JvmSuppressWildcards Map<String, Any>): @JvmSuppressWildcards Observable<String>
    /**
     * 无参Get
     */
    @GET
    abstract  fun getForString(@Url path: String): @JvmSuppressWildcards Observable<String>

    /**
     *  登录
     */
    @FormUrlEncoded
    @POST("token")
    abstract   fun login(@FieldMap map:Map<String,String>, @HeaderMap heads:@JvmSuppressWildcards Map<String,Any>):Observable<String>
    @POST
    abstract fun postFile(@Url url:String,@Body file:File):@JvmSuppressWildcards Observable<BaseResponse<Any>>
    @Multipart
    @POST
    abstract fun uploadImage(@Url url:String, @Part("caption") des:RequestBody, @Part()body: MultipartBody.Part)
            :Observable<String>
}