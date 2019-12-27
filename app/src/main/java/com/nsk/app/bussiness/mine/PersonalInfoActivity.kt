package com.nsk.app.bussiness.mine

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
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.constant.PermissionConstants
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.bussiness.mine.viewmodel.PersonSettingModel
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ActivityPerInfoBinding
import com.nsk.app.config.Routers
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.BaiduLocation
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_per_info.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Description: 个人设置
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.perInfo)
class PersonalInfoActivity : BaseTitleActivity(){
    val REQUEST_CODE_CHOOSE = 101
    val NICKNAMEREQUEST = 0x1001
    lateinit var personSettingModel:PersonSettingModel
    lateinit var pvCustomLunar: TimePickerView
    lateinit var pvOptions: OptionsPickerView<String>
    override fun setTitle(): Int {
        return R.string.personal_info
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_per_info
    }

    override fun initData() {
        personSettingModel = PersonSettingModel()
        personSettingModel.initData(intent.extras.getString("person"))
        (binding as ActivityPerInfoBinding).personDetail = personSettingModel
    }

    override fun initView() {


        //头像
        rl_avatar.setOnClickListener{

            PermissionHelper.request(object :PermissionHelper.OnPermissionGrantedListener{
                override fun onPermissionGranted() {
                    val tempFile = File(Environment.getExternalStorageDirectory(), "avatar.jpg") //设置截图后的保存路径
                    val uri = Uri.fromFile(tempFile)
                    val options = UCrop.Options()//uCrop的参数设置
                    //设置压缩比例
                    options.setCompressionQuality(80)
                    options.setMaxBitmapSize(1000*1000)
                    options.setCircleDimmedLayer(true)
                    options.setAllowedGestures(UCropActivity.SCALE,UCropActivity.SCALE,UCropActivity.SCALE)
                    options.setToolbarColor(ContextCompat.getColor(this@PersonalInfoActivity, R.color.colorAccent))
                    Matisse.from(this@PersonalInfoActivity)
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
            },object :PermissionHelper.OnPermissionDeniedListener{
                override fun onPermissionDenied() {
                   //do nothing
                }
            }, PermissionConstants.CAMERA,PermissionConstants.STORAGE)


        }
        //用户
        ct_name.setOnClickListener{
            ARouter.getInstance().build(Routers.user_name).withString("name",ct_name.rightTextString.toString()).navigation(this,NICKNAMEREQUEST)
        }
        //性别
        ct_male.setOnClickListener{
            pvOptions.show()
        }
        //城市选择
        val hotCities = ArrayList<HotCity>()
        hotCities.add(HotCity("北京", "北京", "101010100"))
        hotCities.add(HotCity("上海", "上海", "101020100"))
        hotCities.add(HotCity("广州", "广东", "101280101"))
        hotCities.add(HotCity("深圳", "广东", "101280601"))
        //所在地
        ct_location.setOnClickListener{
            CityPicker.getInstance()
                    .setFragmentManager(getSupportFragmentManager())	//此方法必须调用
                    .enableAnimation(true)	//启用动画效果
                    .setLocatedCity( LocatedCity(DbManger.getInstance().get(UserConstants.loca_city), "", ""))  //APP自身已定位的城市，默认为null（定位失败）
                    .setHotCities(hotCities)	//指定热门城市
                    .setOnPickListener(object : OnPickListener {
                        override fun onPick(position: Int, data: City?) {
                            //上传选择城市数据
                            if(data==null){
                                return
                            }
                            personSettingModel.updateAddress(data.name)
                           // personSettingModel.address.set(data.name)
                        }

                        override fun onLocate() {
                            PermissionHelper.requestLocation(object : PermissionHelper.OnPermissionGrantedListener{
                                override fun onPermissionGranted() {
                                    BaiduLocation.getLocation(object : BaiduLocation.MyLocationListener{
                                        override fun myLocatin(isSuccess: Boolean, pro: String?, city: String?, code: String?) {
                                            if(isSuccess){
                                                CityPicker.getInstance()
                                                        .locateComplete( LocatedCity(city, pro, code), LocateState.SUCCESS)
                                            }
                                        }

                                    })
                                }
                            })

                        }
                    })
                    .show()
        }
        val selectedDate = Calendar.getInstance()//系统当前时间
        val startDate = Calendar.getInstance()
        startDate.set(1949, 1, 1)
        val endDate = Calendar.getInstance()
        endDate.set(2040, 1, 1)
        pvCustomLunar = TimePickerBuilder(this@PersonalInfoActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                    val dateString = simpleDateFormat.format(cal.time)
                    personSettingModel.updataBirthDay(dateString)
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)+1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    //todo 上传生日数据
                }).setDate(selectedDate)
                .setRangDate(startDate, endDate)
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
                }).setType(booleanArrayOf(true, true, true, false, false, false))
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED).build()
        //生日
        ct_birthday.setOnClickListener{
            pvCustomLunar.show()
        }
        initMale()
    }

    private fun getTime(date: Date): Calendar {//可根据需要自行截取数据显示
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    private fun initMale() {
        val males:  List<String> = listOf("男","女")
        pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            val str = males.get(options1)
//            ct_male.rightTextString = str
            personSettingModel.updateGender(str)
//            Toast.makeText(this@PersonalInfoActivity, str, Toast.LENGTH_SHORT).show()
        })

                .setCancelColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setTitleSize(16)//标题文字大小
                .setCancelText("取消").setSubmitText("确定").build()

        pvOptions.run {
            setPicker(males)
            setSelectOptions(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == NICKNAMEREQUEST){
                /**
                 * 更改昵称,即username
                 */
//                ct_name.rightTextString = data!!.getStringExtra("nickname")
                personSettingModel.updateUserNickName(data!!.getStringExtra("nickname"),(binding
                as ActivityPerInfoBinding).ctName)
            }else if(requestCode == REQUEST_CODE_CHOOSE){
                val obtainResult = Matisse.obtainResult(data)
                val uri1 = UCrop.getOutput(data!!)
                val uri = obtainResult[0]
                val path = getRealFilePath(this,uri)
                for (item in obtainResult){
//                    Log.i("路径",uri.toString());
                }
                val file = File(path)
                personSettingModel.upLoadImage((binding as ActivityPerInfoBinding).circlePerson,file)
//                upLoadImage(file)
//                serviceApi.uploadImage(ApiConfig.uploadIcon_url,des,part).subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io()).subscribe({
//                            Log.i("看看",it)
//                        },{
//
//                        })
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



}