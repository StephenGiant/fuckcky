package com.nsk.app.bussiness.card

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_add_cred_card.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description: 添加信用卡
 * Company    :
 * Author     : gene
 * Date       : 2018/9/2
 */
//@Route(path = Routers.add_cred_card)
class AddCredCardActivity : BaseTitleActivity(){
    val REQUEST_CODE_CHOOSE = 101
    override fun setTitle(): Int {
        return R.string.add_cred_card
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_add_cred_card
    }

    override fun initData() {

    }
    lateinit var dateString: String
    lateinit var dateString1: String
    lateinit var pvCustomLunar: TimePickerView
    lateinit var pvCustomLunar1: TimePickerView
    override fun initView() {
        val tempFile = File(Environment.getExternalStorageDirectory(), "credCard.jpg") //设置截图后的保存路径
        val uri = Uri.fromFile(tempFile)
        val options = UCrop.Options()//uCrop的参数设置
        options.setCircleDimmedLayer(true)
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE)
        options.setToolbarColor(ContextCompat.getColor(this@AddCredCardActivity, R.color.colorAccent))
        iv_photo.setOnClickListener {
            PermissionHelper.requestStorage(object : PermissionHelper.OnPermissionGrantedListener{
                override fun onPermissionGranted() {
                    Matisse.from(this@AddCredCardActivity)
                            .choose(MimeType.ofAll())
                            .countable(true)
                            .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                            .captureStrategy( CaptureStrategy(true,"com.nsk.app.MyFileProvider")) // 拍照的图片路径
                            .crop(true)
                            .cropOptions(options) //设置uCrop裁剪参数
                            .cropUri(uri)
                            .theme(R.style.Matisse_Dracula)
                            .maxSelectable(1)                     // 最多选择一张
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(GlideEngine())
                            .forResult(REQUEST_CODE_CHOOSE)

                }
            })

        }
        val selectedDate = Calendar.getInstance()//系统当前时间
        selectedDate.set(1999,7,1)
        pvCustomLunar = TimePickerBuilder(this@AddCredCardActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                    dateString = simpleDateFormat.format(cal.time)
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)+1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    tv_card_back_day.setText(day.toString())
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
        pvCustomLunar1 = TimePickerBuilder(this@AddCredCardActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                    dateString1 = simpleDateFormat.format(cal.time)
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)+1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
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
        //添加信用卡
        tv_confirm.setOnClickListener {
            if(et_card_code.text.isNotBlank()&&et_name.text.isNotBlank()&&tv_card_day.text.isNotBlank()&&tv_card_back_day.text.isNotBlank()&&et_card_ed.text.isNotBlank()){

            }
        }
        //选择银行
        tv_card_bank.setOnClickListener {
            ARouter.getInstance().build(Routers.select_bank).navigation()
        }

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
//                serviceApi.upLoadImage((binding as ActivityPerInfoBinding).circlePerson,file)
            }
        }
    }
    /**
     * 获取图片真实路径
     */
    fun  getRealFilePath(context: Context, uri:Uri) :String {
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