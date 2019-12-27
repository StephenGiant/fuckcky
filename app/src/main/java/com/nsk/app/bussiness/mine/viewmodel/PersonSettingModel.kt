package com.nsk.app.bussiness.mine.viewmodel

import android.databinding.ObservableField
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.JsonElement
import com.nsk.app.base.BaseViewModel
import com.nsk.app.business.extension.parseData
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseJson
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.http.interceptor.MyInterceptor
import com.nsk.cky.ckylibrary.utils.DbManger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class PersonSettingModel():BaseViewModel() {
    val genderList = arrayOf("","女","男")
    init {

    }
    fun initData(json:String){
        val jsonObject = JSONObject(json)
        gender.set(genderList[jsonObject.getInt("n_user_sex")])
        if(jsonObject.getString("n_user_pic")!=null) {
            iconUrl.set(jsonObject.getString("n_user_pic"))
        }
        if(jsonObject.getString("n_user_nickname")!=null) {
            userName.set(jsonObject.getString("n_user_nickname"))
        }
        val s = jsonObject.getString("n_user_birthday")
       if(jsonObject.getString("n_user_birthday")!=null&&!TextUtils.equals("null",s)) {
           birthDay.set(jsonObject.getString("n_user_birthday").substring(0,
                   jsonObject.getString("n_user_birthday").length-8 ))
       }
        if(jsonObject.getString("n_user_areaid")!=null&&!TextUtils.equals("null",jsonObject.getString("n_user_areaid"))){
//                DbManger.getInstance().put(UserConstants.areaId,jsonObject.getInt("n_user_areaid").toString())
            val addresstring = DbManger.getInstance().get(jsonObject.getString("n_user_areaid"))
               address.set( addresstring)
//            if(jsonObject.getString("n_user_areaid").equals("1001")){
//                address.set("上海")
//            }
        }


    }
    //性别
    var gender:ObservableField<String> = ObservableField()
    //头像的url
    var iconUrl:ObservableField<String> = ObservableField()
    //用户名
    var userName:ObservableField<String> = ObservableField()
    //地区
    var address:ObservableField<String> = ObservableField()
    var birthDay:ObservableField<String> = ObservableField()
    /**
     * 修改性别(显示新性别)
     */
    fun updateGender(newGender:String){
        var male :Int
        if("女".equals(newGender)){
            male=1
        }else{
            male=2
        }
        val map = mapOf<String,Int>(Pair("n_user_sex",male))
        serviceApi.get(ApiConfig.resetSex_url,map).parseJson().subscribe({
            gender.set(newGender)
        }, {error ->
            error.printStackTrace()
        })
    }

    /**
     * 修改头像
     */
    fun updateUserIcon(view:View){

    }

    fun updataBirthDay(birth:String){
        val map = mapOf<String, String>(Pair("n_user_birthday", birth))
        serviceApi.get(ApiConfig.updateBirthday_url,map).compose(RxjavaUtils.transformer())
                .subscribe({

                    birthDay.set(birth)
                },{

                },{

                })
    }
    /**
     * 修改名字并显示
     */
    fun updateUserNickName(newName:String ,view:View){
        val map = mapOf<String,String>(Pair("n_user_nickname",newName))
        serviceApi.get(ApiConfig.updateUserName_url,map).parseJson().subscribe({
            userName.set(newName)
        }, {error ->
            error.printStackTrace()

        })
    }

    fun fetchAddressName(){

    }
    /**
     * 修改地区
     */
    fun updateAddress(area:String){
        //先查询id
//        serviceApi.getForString(ApiConfig.getAreaID_url+"?city="+area).compose(RxjavaUtils.transformer())
//                .subscribe({
//                    address.set(area)
//                    for (a in com.nsk.app.Nothings.temp) {
//                        if(TextUtils.equals(a.n_area_city,area)||a.n_area_city.contains(area)){
//                            DbManger.getInstance().put(UserConstants.areaId,a.n_area_id.toString())
//                           serviceApi.getForString(ApiConfig.updateUserArea_url+"?n_user_area=")
//                        }
//                    }
//                },{})

        serviceApi.getForString(ApiConfig.getAreaID_url+"?city="+area).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
               val json = jsonElement!!.asJsonObject
                val areaid = json.get("n_area_id").asInt
                address.set(area)
                DbManger.getInstance().put(UserConstants.areaId,areaid.toString())
                serviceApi.get(ApiConfig.updateUserArea_url+"?n_user_area="+areaid).compose(RxjavaUtils.transformer()).subscribe ({},{})
            }
        },object :RxjavaUtils.CkyErrorConsumer(){
            override fun accept(e: Throwable?) {
                super.accept(e)
//                ToastUtils.showShort("更改地址失败")
            }
        })


    }

    /**
     * 更新用户头像
     * 实现比较复杂
     */
    fun upLoadImage(view: View,file: File){
        //此处不用全局context
        val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).addNetworkInterceptor(MyInterceptor(view.context))
                .build()

        val fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val des =   RequestBody.create(
                MediaType.parse("form-data"), "aaaa");
        val file2Name = "icon.jpg"
        val boundary = "----caikangyu------------caikangyu"
        val multipartBody = MultipartBody.Builder(boundary).setType(MultipartBody.FORM).addFormDataPart("caption","aaaa").addFormDataPart("image1", file.name, fileBody).build()
        val request = Request.Builder()
                .url(ApiConfig.BaseUrl+"/User/UploadImage")
                .post(multipartBody)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {


            }

            override fun onResponse(call: Call?, response: Response?) {
                val string = response!!.body()!!.string()
//                Log.i("看看",string)
                val jsonObject = JSONObject(string)
                val url = jsonObject.get("data")
                //用rxjava去指定线程
                Observable.just(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe {url->
                            Glide.with(view.context).load(url).asBitmap().into(view as ImageView)
//                })
                        }
//                runOnUiThread(Runnable {
//                    Glide.with(this@PersonalInfoActivity).load(url).asBitmap().into(circle_person)
//                })

            }
        }
        )


    }

}