package com.nsk.app.bussiness.card

import android.graphics.Color
import android.text.TextUtils
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
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTitleActivity
import com.nsk.app.caikangyu.R
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.Routers
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.RightListener
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.bean.BankCardBean
import com.nsk.cky.ckylibrary.utils.BaiduLocation
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import kotlinx.android.synthetic.main.activity_one_key_card.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Description: 办卡
 * Company    :
 * Author     : gene
 * Date       : 2018/7/29
 */
@Route(path = Routers.one_key_cards)
class OneClickCardActivity : BaseTitleActivity() ,RightListener{
    override fun onClick() {

        //城市选择
        val hotCities = ArrayList<HotCity>()
        hotCities.add(HotCity("北京", "北京", "101010100"))
        hotCities.add(HotCity("上海", "上海", "101020100"))
        hotCities.add(HotCity("广州", "广东", "101280101"))
        hotCities.add(HotCity("深圳", "广东", "101280601"))
        CityPicker.getInstance()
                .setFragmentManager(supportFragmentManager)	//此方法必须调用
                .enableAnimation(true)	//启用动画效果
                .setLocatedCity( LocatedCity(DbManger.getInstance().get(UserConstants.loca_city), "", ""))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)	//指定热门城市
                .setOnPickListener(object : OnPickListener {
                    override fun onPick(position: Int, data: City?) {
                        //上传选择城市数据
                        if(data==null){
                            return
                        }
                        DbManger.getInstance().put(UserConstants.city,data.name)
                        setRight(DbManger.getInstance().get(UserConstants.city),true,this@OneClickCardActivity)
                        for (a in com.nsk.app.Nothings.temp) {
                            if(TextUtils.equals(a.n_area_city,DbManger.getInstance().get(UserConstants.city))||a.n_area_city.contains(DbManger.getInstance().get(UserConstants.city))){
                                DbManger.getInstance().put(UserConstants.areaId,a.n_area_id.toString())

                                LogUtils.e(a.n_area_city+a.n_area_id)
                            }
                        }
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

    lateinit var bankCardBean: BankCardBean
    var income=""
    override fun setTitle(): Int {
        return R.string.one_key_card
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_one_key_card
    }

    override fun initData() {
    }
    lateinit var pvCustomLunar0: OptionsPickerView<String>
    lateinit var pvCustomLunar: TimePickerView
    lateinit var dateString: String
    override fun initView() {

        initMale()
        initCard()
        setRight(DbManger.getInstance().get(UserConstants.city),true,this)
        val selectedDate = Calendar.getInstance()//系统当前时间
        val startDate = Calendar.getInstance()
        startDate.set(1949, 1, 1)
        val endDate = Calendar.getInstance()
        endDate.set(2040, 1, 1)
        pvCustomLunar0 = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            income=Nothings.n_typeid[options1]
            tv_income.text = Nothings.n_code_typename[options1]
        })
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setSubCalSize(18)
                .setTitleSize(16)//标题文字大小
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .build<String>()
        pvCustomLunar0.setPicker(Nothings.n_code_typename as List<String>)
        pvCustomLunar = TimePickerBuilder(this@OneClickCardActivity,
                OnTimeSelectListener { date, _ ->
                    val cal = getTime(date)
                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
                     dateString = simpleDateFormat.format(cal.time)
                    val year = cal.get(Calendar.YEAR)
                    val month = cal.get(Calendar.MONTH)+1
                    val day = cal.get(Calendar.DAY_OF_MONTH)
                    tv_age.text = year.toString()+"."+month+"."+day
                }).setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTitleBgColor(R.color.background)
                .setSubCalSize(20)
                .setTitleSize(16)//标题文字大小
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
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
                .setDividerColor(Color.LTGRAY).build()
        tv_age.setOnClickListener {
            pvCustomLunar.show()
        }
        tv_sex.setOnClickListener {
            pvOptions.show()
        }
        tv_cred_card.setOnClickListener {
            pv1Options.show()
        }
        tv_select.setOnClickListener {
            LogUtils.e("",tv_age.text.toString(),male.isNotBlank(),hasCard.isNotBlank(),income.isNotBlank())
            if(tv_age.text.toString().isNotBlank()&&male.isNotBlank()&&hasCard.isNotBlank()&&income.isNotBlank()){
                val map = mapOf(Pair(UserConstants.n_age_segment,tv_age.text.toString()),
                        Pair(UserConstants.n_sex,male),
                        Pair(UserConstants.n_income_segment,income),
                                Pair(UserConstants.n_have_creditcard,hasCard),
                                Pair("areaid",  DbManger.getInstance().get(UserConstants.areaId)) )
                serviceApi.getForString(ApiConfig.oneKeyCard,map).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                        .subscribe({ response ->
                            val gson = Gson()
                            //接口获取数据
                            LogUtils.e(response)
                            var datas= ArrayList<BankCardBean.NskHotCreditListBean>()
                            datas = gson.fromJson<java.util.ArrayList<BankCardBean.NskHotCreditListBean>>(response,object : TypeToken<ArrayList<BankCardBean.NskHotCreditListBean>>(){
                            }.type)
                            ARouter.getInstance().build(Routers.recommendCard).withSerializable("recommend",true).withSerializable("data",datas).navigation()
                          finish()
//                            ToastUtils.showLong("办卡成功")
                        }, {
                            ToastUtils.showLong("未知错误")
                        })
            }else{
                ToastUtils.showLong(getString(R.string.input_right_format))
            }

        }
        tv_income.setOnClickListener{
            pvCustomLunar0.show()
        }
    }
    private fun getTime(date: Date): Calendar {//可根据需要自行截取数据显示
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }
    lateinit var pvOptions: OptionsPickerView<String>
    lateinit var pv1Options: OptionsPickerView<String>
     var male="1"
     var hasCard=String()
    private fun initMale() {
        val males:  List<String> = listOf("男","女")
        pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            val str = males.get(options1)
            tv_sex.text = str
            if(TextUtils.equals(str,"男")){
                male="2"
            }else{
                male="1"
            }
        })
                .setTitleSize(16)//标题文字大小
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setCancelText("取消").setSubmitText("确定").build()

        pvOptions.run {
            setPicker(males)
            setSelectOptions(0)
        }
    }
    private fun initCard() {
        val males:  List<String> = listOf("有","没")
        pv1Options = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            val str = males.get(options1)
            tv_cred_card.text = str
            if(TextUtils.equals(str,"有")){
                hasCard="1"
            }else{
                hasCard="2"
            }
        })
                .setSubCalSize(18)
                .setTitleSize(16)//标题文字大小
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setCancelText("取消").setSubmitText("确定").build()

        pv1Options.run {
            setPicker(males)
            setSelectOptions(0)
        }
    }

}