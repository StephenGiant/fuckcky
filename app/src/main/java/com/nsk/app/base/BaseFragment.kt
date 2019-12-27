package com.nsk.app.base

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.nsk.app.caikangyu.R





abstract class BaseFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        ARouter.getInstance().inject(this)
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        val dialog = Dialog(activity, R.style.windowDialogFragment)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(getLayoutId())
        dialog.setCanceledOnTouchOutside(false) // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        val window = dialog.window
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        lp.height = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        window.attributes = lp
        onBindViewBefore(dialog)
        initWidget(dialog)
        return dialog
    }


    protected abstract fun getLayoutId(): Int
    protected open fun onBindViewBefore(root: Dialog) {}


    protected open fun initWidget(root: Dialog) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}