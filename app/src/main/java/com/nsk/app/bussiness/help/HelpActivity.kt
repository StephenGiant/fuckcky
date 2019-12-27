package com.nsk.app.bussiness.help

import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/7/15
 */
//@Route(path = Routers.help)
class HelpActivity :BaseTitleActivity(){
    override fun setTitle(): Int {
        return R.string.help
    }

    override fun getContentLayoutId(): Int {
       return R.layout.webview_layout
    }

    override fun initData() {
    }

    override fun initView() {

    }
}