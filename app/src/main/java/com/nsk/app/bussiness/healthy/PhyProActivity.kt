package com.nsk.app.bussiness.healthy

import android.content.DialogInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityPhyproBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.HealthyJoinDialog

/**
 * @Package com.nsk.app.bussiness.healthy
 * @author qianpeng
 * @date 2018/9/5.
 * @describe
 */
@Route(path = Routers.prophy)
class PhyProActivity :BaseTransStatusActivity(),DialogInterface.OnDismissListener {
     var dialog:HealthyJoinDialog?=null
//     var phoneEditText:EditText?=null
//     var etCode:EditText? = null
    var contact:String?=null
     var phone:String? = null
     var code:String? = null
var enable = false
    var dialogInit  = false
    var id :Int = 0

    override fun setTitle(): Int {
        return R.string.phy_protitle
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_phypro
    }

    override fun initData() {
    val bind = binding as ActivityPhyproBinding
        serviceApi.getForString(ApiConfig.getTopExamination_url).parseData(
                object :RxjavaUtils.CkySuccessConsumer(){
                    override fun onSuccess(jsonElement: JsonElement?) {
                        val json = jsonElement!!.asJsonObject
                        val des = json.get("n_examination_describe").asString
                        val name = json.get("n_examination_name").asString
                        bind.tvPhyprodetail.text = name+"\n"+des
                        
                    }
                },object :RxjavaUtils.CkyErrorConsumer(){

        }
        )
       serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_ApplicationProcess")
               .compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
               .subscribe({resaponse ->
                   val jsonParser = JsonParser()
                   val json = jsonParser.parse(resaponse).asJsonArray.get(0).asJsonObject

                   val asString = json.get("n_code_typename").asString
                   val replace = asString.replace("\\n", "\n")

                   bind.tvPhyprodes.text = "服务简介\n"+replace
                   id = json.get("n_examination_id").asInt
               },{

               })

        bind.btnAdvisory.setOnClickListener {
            if (CkyApplication.getApp().hasToken()) {
                if(dialog==null) {
                    dialog = HealthyJoinDialog()
                    dialog!!.codeType = 5
                    dialog!!.listenner = object : HealthyJoinDialog.OnCommitListenner {
                        override fun onclickCommit() {
                            if (dialog!!.etCode!!.text.toString() == null || dialog!!.etCode!!.text.toString().length < 1) {
                                ToastUtils.showShort("请输入验证码")
                            } else if (dialog!!.phoneEdittext!!.text.toString() == null || dialog!!.phoneEdittext!!.text.toString().length < 1) {
                                ToastUtils.showShort("请输入手机号码")
                            }else if(dialog!!.etContact!!.text.toString()==null||dialog!!.etContact!!.text.toString().length < 1) {
                                ToastUtils.showShort("请输入联系人姓名")
                            } else {
                                val map = mapOf<String, Any>(Pair("n_examination_id", id), Pair("n_medicalorder_contacts",dialog!!.etContact!!.text.toString()),Pair("mobile", dialog!!.phoneEdittext!!.text.toString().trim())
                                        , Pair("validcode", dialog!!.etCode!!.text.toString()))
                                serviceApi.getForString(ApiConfig.createTopExamOrder, map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                                    override fun onSuccess(jsonElement: JsonElement?) {
                                       val text =  dialog!!.phoneEdittext!!.text.toString()
                                        dialog!!.dismiss()
                                        //去创建成功页面
                                        ToastUtils.showShort("预约成功！")
                                        ARouter.getInstance().build(Routers.topexamsuccess).navigation()
//                                        finish()
                                    }
                                }, object : RxjavaUtils.CkyErrorConsumer() {
                                    override fun accept(e: Throwable?) {
                                        super.accept(e)

                                        dialog!!.dismiss()
                                        ToastUtils.showShort("咨询失败")
                                    }
                                })
                            }

                        }
                    }
//                    dialogInit = true
                }else{
                    dialog!!.resetText(phone!!,code!!,contact!!)
//                    dialog!!.show(supportFragmentManager, "healthpro")
//                    phoneEditText!!.setText(phone)
//                    etCode!!.setText(code)
                }

                dialog!!.show(supportFragmentManager, "healthpro")

            }else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
       phone =  this.dialog!!.phone
        code =  this.dialog!!.code
        contact = this.dialog!!.contact
//    phone = phoneEditText!!.text.toString()
//        code = etCode!!.text.toString()
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}