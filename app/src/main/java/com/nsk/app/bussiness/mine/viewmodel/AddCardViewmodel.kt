package com.nsk.app.bussiness.mine.viewmodel

import android.app.Activity
import android.content.pm.ActivityInfo
import android.databinding.ObservableField
import android.net.Uri
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParser
import com.nsk.app.base.BaseViewModel
import com.nsk.app.bussiness.mine.AddCardActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.utils.LoadingDialog
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBack
import com.zhouyou.http.exception.ApiException
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddCardViewmodel :BaseViewModel() {
    val cardImage = ObservableField<String>()
    var picUrl:String?=null
    var bankcard:String?=null
    var urls:String?=null
    fun chooseBank(view: View){
        ARouter.getInstance().build(Routers.select_bank).navigation(view.context as Activity,1001)
    }
    fun takephoto(view :View){
        PermissionHelper.requestStorage(object : PermissionHelper.OnPermissionGrantedListener {
            override fun onPermissionGranted() {
                //知乎图片
                val tempFile = File(Environment.getExternalStorageDirectory(), "avatar.jpg") //设置截图后的保存路径
                val uri = Uri.fromFile(tempFile)
                val options = UCrop.Options()//uCrop的参数设置
//                options.setCircleDimmedLayer(true)
                options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE)
                options.setToolbarColor(ContextCompat.getColor(view.context as Activity, R.color.colorAccent))
                Matisse.from(view.context as Activity)
                        .choose(MimeType.ofAll())
                        .countable(true)
                        .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                        .captureStrategy( CaptureStrategy(true,"com.nsk.app.MyFileProvider")) // 拍照的图片路径
                        .crop(true)
                        .cropOptions(options) //设置uCrop裁剪参数
                        .cropUri(uri)
                        .theme(R.style.Matisse_Dracula)
                        .maxSelectable(1)                     // 最多选择一张
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(GlideEngine())
                        .forResult(101)
            }
        })
    }
    //添加卡片
    fun confirmInfo(view: View){

    }
    /**
     * 更新用户头像
     * 实现比较复杂
     */
    fun upLoadImage(view: View, file: File, rl_photopreview: RelativeLayout, iv_shenfenzheng: ImageView, addCardActivity: AddCardActivity){
        val dialog = LoadingDialog.Builder(view.context, "加载中").create()
        val boundary = "----caikangyu------------caikangyu"
        val fileBody = RequestBody.create(MediaType.parse("multipart/jpeg"), file)
//        val fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val build = MultipartBody.Builder(boundary).setType(MultipartBody.FORM).addFormDataPart("caption", "aaaa").addFormDataPart("image1", file.name, fileBody).build()
        EasyHttp.post("/User/UploadCard")
                .headers("Authorization",CkyApplication.getApp().token)
                .requestBody(build)
                .timeStamp(true)
                .execute(object : CallBack<String>() {
            override fun onStart() {
                dialog.show()
                LogUtils.e("start")
            }

            override fun onCompleted() {
                dialog.dismiss()
                LogUtils.e("complete")
            }

            override fun onError(e: ApiException) {
                LogUtils.e(e.message)
                ToastUtils.showShort("上传照片失败")
            }

            override fun onSuccess(o: String) {

                LogUtils.e(o+"success")
                val parser = JsonParser()
                val jsonObject = parser.parse(o).asJsonObject
                if(!jsonObject.get("hasError").asBoolean){
                    rl_photopreview.visibility=View.VISIBLE
                    picUrl = jsonObject.get("data").asString
                    cardImage.set(picUrl)
                    LogUtils.e(picUrl)

                }
            }
        })
        //此处不用全局context
//        val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).addNetworkInterceptor(MyInterceptor(view.context))
//                .build()
//
//        val fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
//        val boundary = "----caikangyu------------caikangyu"
//        val multipartBody = MultipartBody.Builder(boundary).setType(MultipartBody.FORM).addFormDataPart("caption","asdfasdf").addFormDataPart("image2", file.name, fileBody).build()
//        val request = Request.Builder()
//                .url((ApiConfig.BaseUrl+"/User/UploadCard").trim())//一用这个url,contenttype就出问题
//                .post(multipartBody)
//                .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call?, e: IOException?) {
//                Log.e("错误",e!!.message)
//            }
//
//            override fun onResponse(call: Call?, response: Response?) {
//                if(response!!.code()==200) {
//                    val string = response!!.body()!!.string()
//                    Log.i("看看", string + "响应")
//                    val jsonObject = JSONObject(string)
//                    val url = jsonObject.getString("data")
//                    picUrl = url
//                    //用rxjava去指定线程
//                    Observable.just(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                            .subscribe {url->
//                                Glide.with(view.context).load(url).asBitmap().into(view as ImageView)
////                })
//                            }
//                }else{
//                    //调用出错
//                    Log.i("看看",  response!!.message() )
//
//                }
////                val jsonObject = JSONObject(string)
////                val url = jsonObject.get("data")
////                //用rxjava去指定线程
////                Observable.just(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
////                        .subscribe {url->
////                            Glide.with(view.context).load(url).asBitmap().into(view as ImageView)
//////                })
////                        }
////                runOnUiThread(Runnable {
////                    Glide.with(this@PersonalInfoActivity).load(url).asBitmap().into(circle_person)
////                })
//
//            }
//        }
//        )


    }

    fun setCardId(cardId: String?) {
        bankcard=cardId
    }
    fun getUrl():String? {
        return urls
    }
}