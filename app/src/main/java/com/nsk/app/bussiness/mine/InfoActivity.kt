package com.nsk.app.bussiness.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.config.ApiConfig
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import kotlinx.android.synthetic.main.activity_set_info.*

/**
 * Description: 安全设置
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.user_info)
class InfoActivity : BaseTitleActivity(){
    override fun setTitle(): Int {
        return R.string.set_info
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_set_info
    }

    override fun initData() {
    }

    override fun initView() {
        et_name.setText(DbManger.getInstance().get(UserConstants.name))
        et_auth.setText(DbManger.getInstance().get(UserConstants.cercard))
        btn_signin.setOnClickListener{
            if(et_name.text.isNotBlank()&&RegexUtils.isIDCard18(et_auth.text.toString())){
              val map = mapOf(Pair("n_user_truename",et_name.text.toString()), Pair("n_user_IDcard",et_auth.text.toString()))


                serviceApi.get(ApiConfig.updateTrueName_url,map).compose(RxjavaUtils.transformer())
                        .subscribe({
                            response ->
//                            ToastUtils.showLong(response.errorMessage)
                            if(!response.hasError) {
                                DbManger.getInstance().put(UserConstants.name,et_name.text.toString())
                                DbManger.getInstance().put(UserConstants.cercard,et_auth.text.toString())
                                ToastUtils.showLong("保存成功")
                                finish()
                            }
                        },{
                        })
            }else{
                ToastUtils.showLong("格式不正确")
            }
        }
    }
}