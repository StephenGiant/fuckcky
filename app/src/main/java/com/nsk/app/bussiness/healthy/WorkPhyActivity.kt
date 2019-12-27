package com.nsk.app.bussiness.healthy

import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.config.ApiConfig
import com.nsk.app.bussiness.healthy.viewmodel.WorkPhyInfo
import com.nsk.app.bussiness.healthy.viewmodel.WorkPhyOrder
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.databinding.ActivityWorkphyBinding
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.widget.HealthyJoinDialog
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner

@Route(path = Routers.workphy)
class WorkPhyActivity:BaseTransStatusActivity(),DialogInterface.OnDismissListener{
  lateinit  var adapter:BaseAdapterWithBinding<WorkPhyInfo>
    override fun setTitle(): Int {
       return R.string.work_phytitle
    }

    override fun getContentLayoutId(): Int {
       return R.layout.activity_workphy
    }
    var contact:String?=null
    var phone:String?=null
    var code:String?=null
     var dialog:HealthyJoinDialog?=null
    override fun initData() {
        val bind = binding as ActivityWorkphyBinding
        var selectPos = 0
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=healthy_Entry")
                .compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({response ->
                    val jsonParser = JsonParser()
                    val jsonObject = jsonParser.parse(response).asJsonArray[0].asJsonObject
                    Glide.with(this).load(jsonObject.get("n_code_typename").asString).into(bind.ivWorkphy)
                },{})
        serviceApi.getForString(ApiConfig.healthy_getworkphy).compose(RxjavaUtils.transformer())
                .compose(RxjavaUtils.handleResult())
                .subscribe({
                    response ->
                    val jsonParser = JsonParser()
                    val dataJson = jsonParser.parse(response).asJsonObject
                    bind.tvPhydes.text = dataJson.get("n_examination_describe").asString
                    val productsArray = dataJson.getAsJsonArray("nsk_productInfo")
                    val gson = Gson()
                    val listData = gson.fromJson<List<WorkPhyInfo>>(productsArray,object : TypeToken<List<WorkPhyInfo>>(){}.type)
                    adapter= object :BaseAdapterWithBinding<WorkPhyInfo>(listData){
                        override fun getLayoutResource(viewType: Int): Int {
                           if(viewType==0){
                               return R.layout.item_phyinfo_gray
                           }else{
                               return R.layout.item_phyinfo
                           }
                        }



                        override fun setVariableID(): Int {
                           return BR.phymodel
                        }

                        override fun getItemCount(): Int {
                         return listData.size
                        }

                        override fun getItemViewType(position: Int): Int {
                            if(position==selectPos){
                                //绿色的
                                return 1
                            }else{
                                return 0
                            }

                        }
                    }
                    bind.rvPhy.adapter = adapter

                },{})
        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.spacedrawable))
        bind.rvPhy.layoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        bind.rvPhy.addItemDecoration(decoration)
        bind.rvPhy.setHasFixedSize(true)
        bind.rvPhy.addOnItemTouchListener(object :RecyclerItemCLickListenner(this){
            override fun onItemClick(view: View, position: Int) {
               selectPos = position
                adapter.notifyDataSetChanged()
            }
        })
        //参团，发起创建订单
        bind.btnJoin.setOnClickListener {

            if(CkyApplication.getApp().hasToken()) {
                if(dialog==null) {
                    dialog = HealthyJoinDialog()
//                    dialog.show(supportFragmentManager, "health")
                    dialog!!.codeType = 6
                    dialog!!.listenner = object : HealthyJoinDialog.OnCommitListenner {
                        override fun onclickCommit() {
                            val hashMap = HashMap<String, Any>()
                            hashMap.put("n_exam_product_id", adapter!!.listData!!.get(selectPos).n_exam_product_id)
                            hashMap.put("n_medicalorder_contacts", dialog!!.etContact!!.text.toString().trim())
                            hashMap.put("n_medicalorder_contact_phone", dialog!!.phoneEdittext!!.text.toString().trim())
                            hashMap.put("validcode", dialog!!.etCode!!.text.toString().trim())
                            hashMap.put("n_medicalorder_hospitalid", adapter.listData!!.get(selectPos).n_exam_hospital_id)
                            hashMap.put("n_medicalorder_price", adapter.listData!!.get(selectPos).n_exam_price)
                            hashMap.put("n_medicalorder_group_price", adapter.listData!!.get(selectPos).n_exam_group_price)
                            serviceApi.getForString(ApiConfig.healthy_createworkphyorder, hashMap).compose(RxjavaUtils.transformer())
                                    .subscribe({ response ->
                                        val jsonParser = JsonParser()
                                        val json = jsonParser.parse(response).asJsonObject
                                        if (!json.get("hasError").asBoolean) {
//                                   Log.i("订单",json.get("data").asString)
                                            dialog!!.dismiss()
                                            val jsonObject = json.get("data").asJsonObject
                                            val gson = Gson()
                                            val order = gson.fromJson<WorkPhyOrder>(jsonObject, WorkPhyOrder::class.java)
                                            ARouter.getInstance().build(Routers.payphy).withSerializable("product", adapter.listData!!.get(selectPos))
                                                    .withSerializable("order", order).navigation()
                                        } else {
                                            ToastUtils.showShort(json.get("errorMessage").asString)
                                        }

                                    }, {})
                        }

                    }
                }else{
                    dialog!!.resetText(phone!!,code!!,contact!!)
                }
                dialog!!.show(supportFragmentManager, "health")

            }else{
                ARouter.getInstance().build(Routers.login).navigation()
            }
        }

    }

    fun checkCommitCondition(phone:String,contact:String,code:String):Boolean{
        var flag = false
        if(phone==null||contact==null||code==null){
            flag=false
        }else{
            flag = true
        }
        return flag
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }


    override fun onDismiss(dialog: DialogInterface?) {
        phone = this.dialog!!.phone
        code = this.dialog!!.code
        contact = this.dialog!!.contact
    }
}