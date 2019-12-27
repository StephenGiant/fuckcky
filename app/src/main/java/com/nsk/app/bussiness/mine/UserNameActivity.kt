package com.nsk.app.bussiness.mine

import android.app.Activity
import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import kotlinx.android.synthetic.main.activity_user_name.*

@Route(path = Routers.user_name)
class UserNameActivity : BaseTitleActivity(){
    override fun setTitle(): Int {
        return R.string.user_name
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_user_name
    }

    override fun initData() {
    }

    override fun initView() {
        val name = intent.getStringExtra("name")

        //请求到的用户名
        et_userName.setHint("")
        et_userName.setText(name)
        //上传更新用户名
        btn_done.setOnClickListener{
            val intent = Intent()
            intent.putExtra("nickname",et_userName.text.toString())
            setResult(Activity.RESULT_OK,intent)
                finish()
        }
    }
}