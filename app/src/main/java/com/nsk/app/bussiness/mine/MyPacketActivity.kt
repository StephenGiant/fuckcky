package com.nsk.app.bussiness.mine

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.CashBean
import com.nsk.app.caikangyu.BR
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ItemCashBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.MyDividerItemDecoration
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner
import kotlinx.android.synthetic.main.activity_mypacket.*

@Route(path = Routers.mypacket)
class MyPacketActivity:BaseTransStatusActivity() {

    override fun setTitle(): Int {
       return R.string.mypacket
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_mypacket
    }

    override fun onResume() {
        super.onResume()

        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=n_record_used").parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
               val rootarray = jsonElement!!.asJsonArray
                serviceApi.getForString(ApiConfig.getUserCash_url).parseData(object :RxjavaUtils.CkySuccessConsumer(){
                    override fun onSuccess(jsonElement: JsonElement?) {
                        val array = jsonElement!!.asJsonArray
                        val gson = Gson()
                        val list = gson.fromJson<List<CashBean>>(array,object :TypeToken<List<CashBean>>(){}.type)
                        val adapter = object :BaseAdapterWithBinding<CashBean>(list){
                            override fun getLayoutResource(viewType: Int): Int {
                                return R.layout.item_cash
                            }

                            override fun setVariableID(): Int {
                                return BR.cashmodel
                            }

                            override fun getItemCount(): Int {
                                return list.size
                            }

                            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                                super.onBindViewHolder(holder, position)
                                val bind = holder.binding as ItemCashBinding

//                                        if(list.get(position).n_record_used<array.size()) {
//                                            bind.tvStatus.text = array.get(list.get(position).n_record_used).asJsonObject.get("n_code_typename").asString
//                                        }
                                for (i:Int in 0..rootarray.size()-1 ){
                                    if(list.get(position).n_record_used.toString().equals(rootarray.get(i).asJsonObject.get("n_code_typeid").asString)){
                                        bind.tvStatus.text = rootarray.get(i).asJsonObject.get("n_code_typename").asString
                                    }
                                }

                            }
                        }
                        rv_score.adapter = adapter
                        rv_score.addOnItemTouchListener(object :RecyclerItemCLickListenner(this@MyPacketActivity){
                            override fun onItemClick(view: View, position: Int) {
                                if(list.get(position).n_record_used==0){
                                    //可以跳转到红包提取详情
                                    ARouter.getInstance().build(Routers.packetdetail).withSerializable("packet",list.get(position)).navigation()

                                }
                            }
                        })
                    }
                },object :RxjavaUtils.CkyErrorConsumer(){
                    override fun onCkyError(code: String?, message: String?) {
                        super.onCkyError(code, message)
//                        ToastUtils.showShort(message)
                    }
                })
            }
        },object :RxjavaUtils.CkyErrorConsumer(){

        })

    }
    override fun initData() {
        tv_seeall.setOnClickListener {
            ARouter.getInstance().build(Routers.allpackets).navigation()
        }

        val decoration = MyDividerItemDecoration(LinearLayoutManager.VERTICAL)
        decoration.setDrawable(resources.getDrawable(R.drawable.divider))
        rv_score.addItemDecoration(decoration)



    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
    }
}