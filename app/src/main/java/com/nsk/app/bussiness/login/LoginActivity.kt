package com.nsk.app.bussiness.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
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
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Description:
 * Company    :
 * Author     :
 * Date       : 2018/7/11
 */
@Route(path = Routers.login)
class LoginActivity : BaseTitleActivity(), LoginContract.View {
     var validCode: String=""
    override fun getValidCode(code: String) {
        validCode=code
    }

    override fun success(token:String) {
        if(token.length>1) {
            CkyApplication.getApp().token = token
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

    private var mode=1
    lateinit var loginModel: LoginModel
    override fun initData() {
         loginModel = LoginModel()
        loginModel.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        loginModel.detachView()
    }
    override fun setTitle(): Int {
       return R.string.login
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onResume() {
        super.onResume()
        if(CkyApplication.getApp().hasToken()){
            finish()
        }
    }


    override fun initView() {
        //财育康服务协议
        tv_protocol_login.setOnClickListener{
            ARouter.getInstance().build(Routers.xieyi).navigation()
        }
        //注册
        setRight(R.string.sign_up, object : RightListener {
            override fun onClick() {
                ARouter.getInstance().build(Routers.signUp).navigation()
            }
        })
        et_code.hint = getString(R.string.write_code)
        tv_quick.setOnClickListener {
            mode=1
//            et_code.setToggleIconVisible(false)
            tv_quick.setTextColor(resources.getColor(R.color.title_black))
            tv_password.setTextColor(resources.getColor(R.color.gray))
            reSetTitle( R.string.login)
            et_code.hint = getString(R.string.write_code)
            tv_send_code_login.visibility= View.VISIBLE
            tv_forget.visibility= View.GONE
        }
        tv_password.setOnClickListener {
            mode=2
//            et_code.setToggleIconVisible(true)
            et_code.hint = getString(R.string.inputu_pass)
            tv_send_code_login.visibility= View.GONE
            tv_quick.setTextColor(resources.getColor(R.color.gray))
            tv_password.setTextColor(resources.getColor(R.color.title_black))
            reSetTitle( R.string.password_login)
            tv_forget.visibility= View.VISIBLE
        }
        //登录
        btn_login.setOnClickListener{
            if(RegexUtils.isMobileSimple(et_phone.text)&& et_code.text.isNotEmpty()){
                //网络请求
                if(mode==1){//验证码
                    if(et_code.text.isNotBlank()&&TextUtils.equals(et_code.text.toString(),validCode)){
                        val hashMap = HashMap<String, String>()
                        hashMap.put("grant_type","password")
                        hashMap.put("username",et_phone.text.toString())
                        hashMap.put("password","")
                        val heads = HashMap<String, Any>()
                        heads.put("logintype",1)
                        heads.put("validCode",et_code.text)
//                        LogWriter.writeLog("czm", et_phone.text.toString())

                        //        val hashMap = HashMap<String, String>()
//        hashMap.put("grant_type","password")
//        hashMap.put("username","17601380842")
//        hashMap.put("password","123456789")
//        val hashMap2 = HashMap<String, Any>()
//        hashMap2.put("validCode","")
//        hashMap2.put("loginType",0)
//        hashMap2.put("Content-Type","application/x-www-form-urlencoded")
                        loginModel.login(hashMap,heads)
                    }

                }else{//密码
                    val hashMap = HashMap<String, String>()
                    hashMap.put("grant_type","password")
                    hashMap.put("username",et_phone.text.toString())
                    hashMap.put("password",et_code.text.toString())
                    val heads = HashMap<String, Any>()
                    heads.put("logintype",0)
                    heads.put("validCode","")
//                    LogWriter.writeLog("czm", et_phone.text.toString())
//                    LogWriter.writeLog("czm", et_code.text.toString())
                    loginModel.login(hashMap,heads)

                }
            }else{
                ToastUtils.showLong(getString(R.string.input_right_format))
            }
        }
        //忘记密码
        tv_forget.setOnClickListener {
            ARouter.getInstance().build(Routers.reSet).navigation()
        }
        //验证码
        tv_send_code_login.setOnClickListener {
            if(RegexUtils.isMobileSimple(et_phone.text )){
                //网络请求
                tv_send_code_login.startCount()
                val map = mapOf(Pair("mobile", et_phone.text.toString()), Pair("validCodeType", "0"))
                loginModel.getValidCode(map)
            }else{
                ToastUtils.showLong(R.string.input_right_format)
            }
        }
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
            postBean.n_owner_mobile = et_phone.text.toString()
            postBean.userAddressBookList = arrayList
            serviceApi.postJsonForString(ApiConfig.updatecontact,postBean).parseData(object : RxjavaUtils.CkySuccessConsumer(){
                override fun onSuccess(jsonElement: JsonElement?) {

                }
            },object : RxjavaUtils.CkyErrorConsumer(){

            })

        }




    }
}