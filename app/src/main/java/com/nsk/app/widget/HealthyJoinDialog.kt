package com.nsk.app.widget

import android.app.Dialog

import android.os.Bundle
import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v4.app.DialogFragment
import android.view.*
import com.nsk.app.caikangyu.R
import android.widget.Button
import android.widget.EditText
import com.blankj.utilcode.util.ToastUtils
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.cky.ckylibrary.http.ServiceApi
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.widget.CountDownTextView
import javax.inject.Inject


class HealthyJoinDialog : DialogFragment(){
    var listenner:OnCommitListenner?=null
    var etCode :EditText?=null
    var phoneEdittext:EditText?=null
    var etContact:EditText?=null
    var codeType =0
    var phone:String?=null
    var code:String? = null
    var contact:String?=null
    @Inject
    lateinit var serviceApi: ServiceApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater!!.inflate(R.layout.dialog_special,container,false)
    }
var button:Button?=null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//
        val builder = AlertDialog.Builder(activity!!, R.style.BottomDialog)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_special, null)
        CkyApplication.getApp().apiComponent.inject(this)

        initView(view)

        builder.setView(view)

       val dialog = builder.create()

        dialog.setCanceledOnTouchOutside(true)
//
//        // 设置宽度为屏宽、靠近屏幕底部。
//        val window = dialog.window
//        val wlp = window.attributes
//        wlp.gravity = Gravity.BOTTOM
//        window.attributes = wlp
//
//        return dialog
//        val dialog = Dialog(activity, R.style.Dialog_NoTitle)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(initView(null))
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onResume() {
        //居于底部
        val window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//Here!
        window.setGravity(Gravity.BOTTOM)
        super.onResume()
    }

    fun initView(view:View?):View?{
         button = view!!.findViewById<Button>(R.id.btn_cmommitphy)
        val downTextView = view!!.findViewById<CountDownTextView>(R.id.tv_send_code)
         phoneEdittext = view!!.findViewById(R.id.et_phone) as EditText
        etCode = view!!.findViewById(R.id.et_code) as EditText
        etContact = view!!.findViewById(R.id.et_contact) as EditText
        if(phone!=null){
            phoneEdittext!!.setText(phone)
        }
        if(code!=null){
            etCode!!.setText(code)
        }
        if(contact!=null){
            etContact!!.setText(contact)
        }
        downTextView.setOnClickListener {

            if(phoneEdittext!!.text.toString()!=null&&phoneEdittext!!.text.toString().length==11) {
                val map = mapOf<String, String>(Pair("mobile", phoneEdittext!!.text.toString().trim()),
                        Pair("validCodeType", codeType.toString()))
                downTextView.startCount()
                serviceApi.getForString(ApiConfig.getValidCode_url, map).compose(RxjavaUtils.transformer())
                        .subscribe({}, {})
            }else{
                ToastUtils.showShort("请输入手机号")
            }
        }
        button!!.setOnClickListener({
            listenner!!.onclickCommit()
//            dismiss()
        })
        return view
    }

    interface OnCommitListenner{
        abstract fun onclickCommit()
    }

    /**
     * cel;phone 手机号
     * c :验证码
     * con:联系人
     */
    fun resetText(celphone:String,c:String,con:String){
        phone = celphone
        code = c
        contact = con
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        phone = phoneEdittext!!.text.toString()
        code = etCode!!.text.toString()
        contact = etContact!!.text.toString()
        if(activity is DialogInterface.OnDismissListener){
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }
}