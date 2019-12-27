package com.nsk.app.bussiness.login

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.bussiness.mine.viewmodel.LoginContract
import com.nsk.app.bussiness.mine.viewmodel.LoginModel
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Description: 重置密码
 * Company    :
 * Author     : gene
 * Date       : 2018/7/15
 */
@Route(path = Routers.reSet)
class ReSetActivity :BaseTitleActivity(),LoginContract.View{
    var validCode: String=""
    override fun success(token:String) {
        ToastUtils.showLong("修改成功")
       finish()
    }

    override fun getValidCode(code: String) {
      validCode=code
    }

    lateinit var loginModel: LoginModel
    override fun setTitle(): Int {
       return R.string.reSet
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_signup
    }

    override fun initData() {
         loginModel = LoginModel()
         loginModel.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        loginModel.detachView()
    }
    override fun initView() {

        text_input_phone.setText(DbManger.getInstance().get(UserConstants.phone))
        btn_signin.text=getString(R.string.done)
        ll_con.visibility= View.GONE
        //注册
        btn_signin.setOnClickListener {
            if(RegexUtils.isMobileSimple(text_input_phone.text )&&text_input_code.text.isNotBlank()&&et_password.text.length>=8&&16>=et_password.text.length){
                //网络请求
                if(validCode.isNotBlank()){
                    val map = mapOf(Pair("mobile", text_input_phone.text.toString()), Pair("password",et_password.text), Pair("validCode",text_input_code.text.toString()))
                    loginModel.ResetPassword(map)
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
                val map = mapOf(Pair("mobile", text_input_phone.text.toString()), Pair("validCodeType", "2"))
                loginModel.getValidCode(map)
            }else{
                ToastUtils.showLong(R.string.input_right_format)
            }
        }
    }
}