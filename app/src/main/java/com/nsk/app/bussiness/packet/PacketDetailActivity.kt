package com.nsk.app.bussiness.packet

import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.databinding.library.baseAdapters.BR
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.nsk.app.base.BaseAdapterWithBinding
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.base.BaseViewHolder
import com.nsk.app.bean.MyCardsBean
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.CashBean
import com.nsk.app.bussiness.packet.viewmodel.WithdrawWay
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityPackettobankBinding
import com.nsk.app.caikangyu.databinding.ItemWithdrawAliBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.app.widget.PacketDialog
import com.nsk.cky.ckylibrary.widget.RecyclerItemCLickListenner
import java.util.ArrayList
import kotlin.collections.HashMap

/**
 * 红包提现的界面
 */
@Route(path = Routers.packetdetail)
class PacketDetailActivity:BaseTransStatusActivity(),DialogInterface.OnDismissListener {
    var checkPos = 0
    var dialog:PacketDialog?=null
    var phone :String?=null
    var code:String?=null
    var waylist:List<WithdrawWay>?=null
    lateinit var bind :ActivityPackettobankBinding
    override fun setTitle(): Int {
        return R.string.packetwithdraw
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_packettobank
    }

    override fun initData() {
    val packet = intent.getSerializableExtra("packet") as CashBean
         bind = binding as ActivityPackettobankBinding
        bind.packetdetail = packet
        bind.rvTixianway.layoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        bind.rvTixianway.addOnItemTouchListener(object :RecyclerItemCLickListenner(this){
            override fun onItemClick(view: View, position: Int) {
                if(waylist!!.get(position).modeId!=2) {
                    checkPos = position
                    bind.rvTixianway.adapter.notifyDataSetChanged()
                }
            }
        })
        serviceApi.getForString(ApiConfig.getUserCards_url).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                val gson = Gson()
                val datajson = jsonElement!!.asJsonArray
               val  datas = gson.fromJson<ArrayList<MyCardsBean>>(datajson,object : TypeToken<ArrayList<MyCardsBean>>(){
                }.type)
//                iniItem()
//                bind.tvEmpty.visibility = View.GONE
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
//                bind.tvEmpty.visibility = View.VISIBLE
            }
        })

        //是支付宝的时候 取对话框
        bind.btnTixian.setOnClickListener{
            if(dialog==null) {
                dialog = PacketDialog()
                dialog!!.listenner = object : PacketDialog.OnCommitListenner {
                    override fun onclickCommit() {
                        val account = (bind.rvTixianway.adapter as PacketAdapter).getAccount(checkPos)
                       val map =  mapOf(Pair("n_cash_record_id",packet.n_record_id), Pair("n_account",account), Pair("n_mode_id",waylist!!.get(checkPos).modeId)
                        , Pair("mobile",dialog!!.phoneEdittext.text.toString().trim()), Pair("validCode",dialog!!.etCode.text.toString().trim()))
                        serviceApi.getForString(ApiConfig.tixian_url,map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
                            override fun onSuccess(jsonElement: JsonElement?) {
                                dialog!!.dismiss()
                                ToastUtils.showShort("提现成功！")
                                finish()
//                            val array = jsonElement!!.asJsonArray
//                            val gson = Gson()
//                            val list = gson.fromJson<List<WithdrawWay>>(array, object : TypeToken<List<WithdrawWay>>() {}.type)
                            }
                        }, object : RxjavaUtils.CkyErrorConsumer() {

                        })
                    }
                }
            }else{
                if(dialog!=null){
                    if(phone.isNullOrBlank()){
                        phone=""
                    }
                    if(code.isNullOrBlank()){
                        code=""
                    }
                    dialog!!.resetText(phone!!,code!!)
                }
            }
            dialog!!.show(supportFragmentManager,"")
//            dialog.button!!.setBackgroundColor(resources.getColor(R.color.orange_main))
        }

        serviceApi.getForString(ApiConfig.withdrawway).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {
                val array = jsonElement!!.asJsonArray
                val gson = Gson()
                 waylist = gson.fromJson<List<WithdrawWay>>(array,object :TypeToken<List<WithdrawWay>>(){}.type)
                val adapter = getPacketAdapter(waylist!!)
                bind.rvTixianway.adapter = adapter
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

    val onclick = object :View.OnClickListener{
        override fun onClick(p0: View?) {
           when(p0!!.id){
               R.id.iv_alicheck ->{
                   if(checkPos!=0){
                       //未选中 变成选中，若已选中则不变
                       checkPos = 0
//                       bind.ivAlicheck.setImageResource(R.drawable.pay_checked)
                   }
               }
           }
        }
    }

    private fun iniItem(datas:ArrayList<MyCardsBean>) {


    }

fun getPacketAdapter(list:List<WithdrawWay>):BaseAdapterWithBinding<WithdrawWay>{

    return object :PacketAdapter(list) {

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val bind = holder.binding as ItemWithdrawAliBinding
//            bind.etAcctount.removeTextChangedListener()

            if(bind.etAcctount.getTag()is TextWatcher){
                bind.etAcctount.removeTextChangedListener(bind.etAcctount.getTag() as TextWatcher)
            }
            bind.etAcctount.setText(waylist!!.get(position).inputdata)
            val watcher = object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            }
            if(bind.etAcctount.visibility==View.VISIBLE){
                bind.etAcctount.addTextChangedListener(watcher)
                bind.etAcctount.setTag(watcher)
            }
            if(listData!!.get(position).input==1){
                //有输入框
                map.put(position,bind.etAcctount)
            }
            if(checkPos==position){
                bind.ivAlicheck.setImageResource(R.drawable.pay_checked)
                if(bind.etAcctount.visibility==View.VISIBLE){
                    bind.etAcctount.requestFocus()
                }
            }else{
                bind.ivAlicheck.setImageResource(R.drawable.grayoval)
            }
        }

    }
}

    /**
     * 对话框消失的监听
     */
    override fun onDismiss(dialog: DialogInterface?) {
     phone = this.dialog!!.phone
        code = this.dialog!!.code

    }
    companion object {
       open class PacketAdapter(list:List<WithdrawWay>):BaseAdapterWithBinding<WithdrawWay>(list){
           val map = HashMap<Int,EditText>()

            override fun getLayoutResource(viewType: Int): Int {
                return R.layout.item_withdraw_ali
            }

            override fun setVariableID(): Int {
               return BR.withdrawway
            }

            override fun getItemCount(): Int {
                return listData!!.size
            }

           fun getAccount(position: Int):String{
               if(listData!!.get(position).input==0){
                   return listData!!.get(position).cardBank
               }else{
                   return map.get(position)!!.text.toString()
               }
           }
        }
    }
}