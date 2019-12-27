package com.nsk.app.bussiness.mine

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonElement
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.mine.viewmodel.AddCardViewmodel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityAddCredCardBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_add_cred_card.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Route(path = Routers.addcards)
class AddCardActivity :BaseTransStatusActivity() {
    val REQUEST_CODE_CHOOSE = 101
    lateinit var bind:ActivityAddCredCardBinding
    lateinit var model:AddCardViewmodel
    lateinit var pvCustomLunar: TimePickerView
    lateinit var pvCustomLunar1: TimePickerView
    override fun setTitle(): Int {
       return R.string.add_creaditcard
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_add_cred_card
    }

    override fun initData() {
        bind = binding as ActivityAddCredCardBinding
        model = AddCardViewmodel()
        bind.addcardmodel = model
    }

    override fun initView() {
        setStatusBarColor(resources.getColor(R.color.white))//控制状态栏背景色
        title!!.setBackgroundColor(resources.getColor(R.color.white))
        title!!.setCenterTitleSize(18)
        title!!.setNavigationIcon(resources.getDrawable(R.drawable.btn_back))
        val selectedDate = Calendar.getInstance()//系统当前时间
        selectedDate.set(1999,7,1)
        pvCustomLunar = TimePickerBuilder(this@AddCardActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                     dayback = cal.get(Calendar.DAY_OF_MONTH).toString()
                    tv_card_back_day.setText(dayback.toString())
                }).setDate(selectedDate)
                .setLayoutRes(R.layout.pickerview_custom, object : CustomListener {

                    override fun customLayout(v: View) {
                        val tvSubmit = v.findViewById(R.id.tv_finish) as TextView
                        val ivCancel = v.findViewById(R.id.tv_cancel) as TextView
                        tvSubmit.setOnClickListener {
                            pvCustomLunar.returnData()
                            pvCustomLunar.dismiss()
                        }
                        ivCancel.setOnClickListener { pvCustomLunar.dismiss() }

                    }
                }).setType(booleanArrayOf(false, false, true, false, false, false))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED).build()
        pvCustomLunar1= TimePickerBuilder(this@AddCardActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                     day = cal.get(Calendar.DAY_OF_MONTH).toString()
                    tv_card_day.setText(day.toString())
                }).setDate(selectedDate)
                .setLayoutRes(R.layout.pickerview_custom, object : CustomListener {

                    override fun customLayout(v: View) {
                        val tvSubmit = v.findViewById(R.id.tv_finish) as TextView
                        val ivCancel = v.findViewById(R.id.tv_cancel) as TextView
                        tvSubmit.setOnClickListener {
                            pvCustomLunar1.returnData()
                            pvCustomLunar1.dismiss()
                        }
                        ivCancel.setOnClickListener { pvCustomLunar1.dismiss() }

                    }
                }).setType(booleanArrayOf(false, false, true, false, false, false))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED).build()
        //还款
        tv_card_back_day.setOnClickListener {
            pvCustomLunar.show()
        }
        //账单日
        tv_card_day.setOnClickListener {
            pvCustomLunar1.show()

        }
        //添加卡片
        tv_confirm.setOnClickListener{
            confirmInfo()
        }
    }
     var bankcard:String?=null
     var day=""
     var dayback=""
    fun confirmInfo(){
        if(et_card_code.text.toString().trim().isNullOrBlank()||et_card_code.text.toString().trim().length<16||bankcard.isNullOrBlank()||et_name.text.toString().isNullOrBlank()||
                day.isNullOrBlank()||dayback.isNullOrBlank()||et_card_ed.text.toString().isNullOrBlank()||
                model.picUrl.isNullOrBlank()){
            ToastUtils.showLong("格式错误，请检查输入格式")
            return
        }
        val map = mapOf<String, String>(Pair("n_card_number", et_card_code.text.toString().trim()),
                Pair("n_bank_id", bankcard!!),
                Pair("trueName", et_name.text.toString().trim()),
                Pair("n_card_billday", day!!),
                Pair("n_card_repaymentday", dayback!!),
                //todo 图片
                Pair("n_card_img", model.picUrl.toString()),
                Pair("n_card_limit", et_card_ed.text.toString()),
                Pair("n_card_mobile",DbManger.getInstance().get(UserConstants.phone))
                )
        serviceApi.getForString(ApiConfig.InsertMyCard,map).parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
               ToastUtils.showLong("添加成功")
                finish()
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
            override fun onCkyError(code: String?, message: String?) {
                super.onCkyError(code, message)
                ToastUtils.showLong(message)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_CHOOSE){
                val obtainResult = Matisse.obtainResult(data)
                val uri1 = UCrop.getOutput(data!!)
                val uri = obtainResult[0]
                val path = getRealFilePath(this,uri)
                for (item in obtainResult){
//                    Log.i("路径",uri.toString());
                }
                val file = File(path)
                model.upLoadImage(bind.ivShenfenzheng,file,rl_photopreview,iv_shenfenzheng,this@AddCardActivity)
//                Glide.with(this@AddCardActivity).load(file).into(iv_shenfenzheng)
//                serviceApi.upLoadImage((binding as ActivityPerInfoBinding).circlePerson,file)
            }else if(requestCode == 1001){
                val cardId =data!!.getStringExtra("cardId")
                val cardName =data!!.getStringExtra("cardName")
                bankcard=cardId
                tv_card_bank.text=cardName
            }
        }
    }

    /**
     * 获取图片真实路径
     */
    fun  getRealFilePath(context: Context, uri: Uri) :String {
        if (null == uri) return "";
        val scheme = uri.getScheme();
        var data :String?= null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();

        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            val cursor = context.getContentResolver().query(uri,  arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data!!
    }
    private fun getTime(date: Date): Calendar {//可根据需要自行截取数据显示
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

}