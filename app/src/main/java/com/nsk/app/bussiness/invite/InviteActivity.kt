package com.nsk.app.bussiness.invite

import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.cky.ckylibrary.UserConstants
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import android.graphics.Bitmap
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.nsk.app.config.ApiConfig
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.invite.viewmodel.ShareInfo
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityInviteBinding
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import java.io.ByteArrayOutputStream

@Route(path = Routers.invite_activity)
class InviteActivity:BaseTransStatusActivity() {
    val api = WXAPIFactory.createWXAPI(this, null)
    override fun setTitle(): Int {
        return R.string.myinvite
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_invite
    }

    lateinit var shareInfo:ShareInfo
    override fun initData() {
        val bind = binding as ActivityInviteBinding
        bind.ivWechat.setOnClickListener({
            if(CkyApplication.getApp().hasToken()) {
            if(shareInfo!=null) {
                shareToFriend(shareInfo, 0)
            }}else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        })

        bind.ivCicleshare.setOnClickListener({
            if(CkyApplication.getApp().hasToken()) {
                if(shareInfo!=null) {
                    shareToFriend(shareInfo, 1)
                }}else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        })
        api.registerApp(UserConstants.appid)
        serviceApi.getForString(ApiConfig.share_url+"?n_share_type=1").parseData(
                object :RxjavaUtils.CkySuccessConsumer(){
                    override fun onSuccess(jsonElement: JsonElement?) {
                            val data = jsonElement!!.asJsonObject
                        val gson = Gson()
                         shareInfo = gson.fromJson<ShareInfo>(data, ShareInfo::class.java)
                    }
                },object :RxjavaUtils.CkyErrorConsumer(){

        }

        )
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=n_share_img_url").parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val jsonArray = jsonElement!!.asJsonArray
               val url =  jsonArray.get(0).asJsonObject.get("n_code_typeid").asString
                Glide.with(this@InviteActivity).load(url).asBitmap().into(bind.ivInvitetitle)
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })
        serviceApi.getForString(ApiConfig.share_page).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val json = jsonElement!!.asJsonObject
                bind.tvJianglitotal.text = json.get("cashCount").asJsonObject.get("totalcount").asInt.toString()
                bind.tvJianguseful.text = json.get("cashCount").asJsonObject.get("canUseCount").asInt.toString()
                val array = json.get("cashInfo").asJsonArray
                for (i:Int in 0..2){
                    val pre_typeid = array[i].asJsonObject.get("n_code_pretypeid").asString
                    val n_code_typename = array[i].asJsonObject.get("n_code_typename").asString
                    val n_code_typeid = array[i].asJsonObject.get("n_code_typeid").asString//金额
                    when(i){
                        0 -> {
                            bind.tvTitleRegist.text = n_code_typename
                            bind.tvUnit1.text = pre_typeid
                            bind.tvRegistjiangli.text = n_code_typeid
                        }
                            1 -> {
                                bind.tvTitleCard.text = n_code_typename
                                bind.tvCardjiangli.text = n_code_typeid
                                bind.tvUnit3.text = pre_typeid
                            }
                        2 ->{

                            bind.tvTitlrLoan.text = n_code_typename
                            bind.tvLoanjiangli.text = n_code_typeid
                            bind.tvUnit2.text = pre_typeid

                        }
                    }

                }
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }


    fun shareToFriend(share:ShareInfo,type:Int){


        val wxWebpageObject = WXWebpageObject()
        wxWebpageObject.webpageUrl = share.n_share_url
        val wxMediaMessage = WXMediaMessage(wxWebpageObject)
        wxMediaMessage.title = share.n_share_title
        wxMediaMessage.description = share.n_share_content

        Glide.with(this).
                load(share.n_share_image_url).asBitmap().into(object :SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        if(resource!=null){
                            val bitmap = Bitmap.createScaledBitmap(resource, 100, 100, true)
                            resource.recycle()
                            val req = SendMessageToWX.Req()
                            req.transaction =buildTransaction("webpage")
                            wxMediaMessage.thumbData = Bitmap2Bytes(bitmap)
                            req.message = wxMediaMessage
                            if(type==0) {
                                req.scene = SendMessageToWX.Req.WXSceneSession
                            }else{
                                req.scene = SendMessageToWX.Req.WXSceneTimeline
                            }
                            api.sendReq(req)
                        }
            }
        })
        if(share.n_share_image_url==null) {
//        wxMediaMessage.thumbData = Bitmap2Bytes()
            val req = SendMessageToWX.Req()
            req.transaction = buildTransaction("webpage")
            req.message = wxMediaMessage
            if(type==0) {
                req.scene = SendMessageToWX.Req.WXSceneSession
            }else{
                req.scene = SendMessageToWX.Req.WXSceneTimeline
            }
            api.sendReq(req)
        }

    }

    fun Bitmap2Bytes(bm: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}