package com.nsk.app.bussiness.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.czm.library.save.imp.LogWriter
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.index.viewmodel.ContactPostBean
import com.nsk.app.bussiness.mine.viewmodel.LoginContract
import com.nsk.app.bussiness.mine.viewmodel.LoginModel
import com.nsk.app.caikangyu.BuildConfig
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.RightListener
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONObject

/**
 * Description:
 * Company    :
 * Author     :
 * Date       : 2018/7/15
 */
@Route(path = Routers.signUp)
class SignUpActivity :BaseTitleActivity(),LoginContract.View{
    var validCode: String=""
    override fun getValidCode(code: String) {
        validCode=code
    }

    override fun success(token:String) {
        if(!CkyApplication.getApp().hasToken()) {
            val hashMap = HashMap<String, String>()
            hashMap.put("grant_type", "password")
            hashMap.put("username", text_input_phone.text.toString())
            hashMap.put("password", et_password.text.toString())
            val heads = HashMap<String, Any>()
            heads.put("logintype", 0)
            heads.put("validCode", "")
//            LogWriter.writeLog("czm", text_input_phone.text.toString())
//            LogWriter.writeLog("czm", text_input_code.text.toString())
            loginModel.login(hashMap, heads)
        }else{
            if(token.length>1) {
//                CkyApplication.getApp().token = token
                val edit = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE).edit()
                edit.putString("token", "bearer " + token).commit()
            }

            if(BuildConfig.readContact==1) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager
                                .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager
                                .PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    PermissionHelper.requestContact {
                        getContacts()
                    }
                }
            }
            finish()
        }


    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_signup
    }
    lateinit var loginModel: LoginModel
    override fun initData() {
        loginModel = LoginModel()
        loginModel.attachView(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        loginModel.detachView()
    }
    override fun initView() {
        et_password.setShakeAnimation()
        et_password.setToggleIconVisible(true)
        tv_protocol.setOnClickListener{
            ARouter.getInstance().build(Routers.xieyi).navigation()
        }
        setRight(R.string.login, object : RightListener {
            override fun onClick() {
                finish()
            }
        })
        //注册
        btn_signin.setOnClickListener {
            if(RegexUtils.isMobileSimple(text_input_phone.text )&&text_input_code.text.isNotBlank()&&et_password.text.length>=8&&16>et_password.text.length){
                //网络请求
                if(validCode.isNotBlank()){
                    val json = JSONObject()
                    json.put("Mobile", text_input_phone.text.toString())
                    json.put("Password",et_password.text.toString())
                    json.put("ValidCode",text_input_code.text.toString())
                    json.put( "LoginType", 0)

                    loginModel.register(json)
                }else{
                    ToastUtils.showLong(R.string.input_right_validcode)
                }
            }else{
                ToastUtils.showLong(R.string.input_right_format)
            }
        }
        //验证码
        tv_send_code.setOnClickListener {
            if(RegexUtils.isMobileSimple(text_input_phone.text )){
                //网络请求
                tv_send_code.startCount()
                val map = mapOf(Pair("mobile", text_input_phone.text.toString()), Pair("validCodeType", "1"))
                loginModel.getValidCode(map)
            }else{
                ToastUtils.showLong(R.string.input_right_format)
            }
        }
    }

    override fun setTitle(): Int {
        return  R.string.sign_up
    }
    fun getContacts(){
        val uri = ContactsContract.Contacts.CONTENT_URI

        val  array = arrayOf(   ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME)
        val  cursor = this.getContentResolver().query(uri, array, null, null, null)

        val arr = arrayOfNulls<String>(cursor.count)
        val arrayList = arrayListOf<ContactPostBean.UserAddressBookListBean>()
        var i = 0
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val bookListBean = ContactPostBean.UserAddressBookListBean()
                val id = cursor.getLong(0)
                //获取姓名
                val name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                bookListBean.n_user_name = name
                val phoneProjection = arrayOf( ContactsContract.CommonDataKinds.Phone.NUMBER)
//                arr[i] = id.toString() + " , 姓名：" + name;

                //根据联系人的ID获取此人的电话号码
                val phonesCusor = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        //电话号码
                        val num = phonesCusor.getString(0)
                        bookListBean.n_user_mobile = num

                    }while (phonesCusor.moveToNext())
                }
                i++
                arrayList.add(bookListBean)
            } while (cursor.moveToNext())

            val postBean = ContactPostBean()
            postBean.n_owner_mobile = text_input_phone.text.toString()
            postBean.userAddressBookList = arrayList
            serviceApi.postJsonForString(ApiConfig.updatecontact,postBean).parseData(object : RxjavaUtils.CkySuccessConsumer(){
                override fun onSuccess(jsonElement: JsonElement?) {

                }
            },object : RxjavaUtils.CkyErrorConsumer(){

            })

        }




    }
}