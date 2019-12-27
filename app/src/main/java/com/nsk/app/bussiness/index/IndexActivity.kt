package com.nsk.app.bussiness.index

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.nsk.app.Nothings
import com.nsk.app.base.BaseTransStatusActivity
import com.nsk.app.bean.CredBankCards
import com.nsk.app.bean.GeneralCodeBean
import com.nsk.app.bean.UpdateBean
import com.nsk.app.business.extension.parseData
import com.nsk.app.bussiness.index.viewmodel.ContactPostBean
import com.nsk.app.bussiness.mine.viewmodel.AreaBean
import com.nsk.app.caikangyu.BuildConfig
import com.nsk.app.caikangyu.R
import com.nsk.app.caikangyu.databinding.ContentMainBinding
import com.nsk.app.config.ApiConfig
import com.nsk.app.config.CkyApplication
import com.nsk.app.config.Routers
import com.nsk.app.db.CkyAddress
import com.nsk.app.utils.RxjavaUtils
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.BaiduLocation
import com.nsk.cky.ckylibrary.utils.DbManger
import com.nsk.cky.ckylibrary.utils.PermissionHelper
import com.raizlabs.android.dbflow.kotlinextensions.insert
import com.raizlabs.android.dbflow.sql.language.SQLite
import me.imid.swipebacklayout.lib.SwipeBackLayout
import java.util.*

@Route(path = Routers.index_act)
class IndexActivity :BaseTransStatusActivity (){
    var indexFragment:IndexFragment?=null
    var  loanFragment:LoanFragment?=null
    var mineFragment:MineFragment? = null
    var time:Long = 0
    override fun setTitle(): Int {
       return R.string.card
    }
    var bind:ContentMainBinding?=null
    override fun getContentLayoutId(): Int {

      return R.layout.content_main
    }

    override fun initData() {

//postPhoneInfo()
        //获取city
        PermissionHelper.requestLocation(object : PermissionHelper.OnPermissionGrantedListener{
            override fun onPermissionGranted() {
                bind!!.bottomNav.postDelayed({
                    getImei()
                },5000)
                if(BuildConfig.readContact==1) {

                    if (ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.READ_CONTACTS) != PackageManager
                                    .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.WRITE_CONTACTS) != PackageManager
                                    .PERMISSION_GRANTED) {
//            getContacts()
                        PermissionHelper.requestContact {
                            Log.i("联系人权限", "成功")
//                getContacts()
                        }
                    } else {
//            PermissionHelper.requestContact {
                        Log.i("联系人权限", "成功")
//                getContacts()
//            }
                    }
                }

                BaiduLocation.getLocation(object : BaiduLocation.MyLocationListener{
                    override fun myLocatin(isSuccess: Boolean, pro: String?, city: String?, code: String?) {
                         if(isSuccess){
                            DbManger.getInstance().put(UserConstants.city,city)
                            //获取areaid
                            serviceApi.get_noparam(ApiConfig.getAreaID_url+"?city="+DbManger.getInstance().get(UserConstants.city)).compose(RxjavaUtils.transformer())
                                    .subscribe({
                                        serviceApi.getForString(ApiConfig.getArea_url+"?containCard=1").parseData(object : RxjavaUtils.CkySuccessConsumer() {
                                            override fun onSuccess(jsonElement: JsonElement?) {
                                                val gson = Gson()
                                                val datajson = jsonElement!!.asJsonArray
                                                var datas = gson.fromJson<ArrayList<AreaBean>>(datajson,object : TypeToken<ArrayList<AreaBean>>(){
                                                }.type)
                                                Nothings.temp=datas
                                                Thread({
                                                    for (a in com.nsk.app.Nothings.temp) {
                                                        DbManger.getInstance().put(a.n_area_id.toString(),a.n_area_city)
                                                        DbManger.getInstance().put(a.n_area_city,a.n_area_id.toString())
                                                        if(TextUtils.equals(a.n_area_city,DbManger.getInstance().get(UserConstants.city))||a.n_area_city.contains(DbManger.getInstance().get(UserConstants.city))){
                                                            DbManger.getInstance().put(UserConstants.areaId,a.n_area_id.toString())

//                                                            LogUtils.e(a.n_area_city+a.n_area_id)
                                                        }

                                                    }

                                                    for (a in 0..datas.size-1){
                                                        val ckyAddress = CkyAddress()
                                                        ckyAddress.areaid = datas.get(a).n_area_id.toString()
                                                        ckyAddress.name = datas.get(a).n_area_city
                                                        ckyAddress.insert()
//        ckyAddress.save()
                                                    }

                                                }).start()


                                            }
                                        }, object : RxjavaUtils.CkyErrorConsumer() {

                                        })
                                    },{})
                        }else{


//                             if(BuildConfig.readContact==1) {
//
//                                 if (ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.READ_CONTACTS) != PackageManager
//                                                 .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.WRITE_CONTACTS) != PackageManager
//                                                 .PERMISSION_GRANTED) {
////            getContacts()
//                                     PermissionHelper.requestContact {
//                                         Log.i("联系人权限", "成功")
////                getContacts()
//                                     }
//                                 } else {
////            PermissionHelper.requestContact {
//                                     Log.i("联系人权限", "成功")
////                getContacts()
////            }
//                                 }
//                             }
                             DbManger.getInstance().put(UserConstants.city,"北京市")
                             DbManger.getInstance().put(UserConstants.loca_city,"北京市")
                             DbManger.getInstance().put(UserConstants.areaId,"100100")
                         }
                    }

                })
            }

        }
    ,object :PermissionHelper.OnPermissionDeniedListener{
            override fun onPermissionDenied() {
                bind!!.bottomNav.postDelayed({
                    getImei()
                },5000)
                if(BuildConfig.readContact==1) {

                    if (ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.READ_CONTACTS) != PackageManager
                                    .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.WRITE_CONTACTS) != PackageManager
                                    .PERMISSION_GRANTED) {
//            getContacts()
                        PermissionHelper.requestContact {
                            Log.i("联系人权限", "成功")
//                getContacts()
                        }
//                        getImei()
                    } else {
//                        getImei()
//            PermissionHelper.requestContact {
//                        Log.i("联系人权限", "成功")
//                getContacts()
//            }
                    }
                }
                DbManger.getInstance().put(UserConstants.city,"北京市")
                DbManger.getInstance().put(UserConstants.loca_city,"北京市")
                DbManger.getInstance().put(UserConstants.areaId,"100100")
            }
        }
        )


//        val sharedPreferences = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE)
//        sharedPreferences.edit().putString("token","bearer Pv08mjvJjm_vWZFLNEHTTo9byyHI_Fres5CBZ4Cy_ppK6PgGZ2j5Ouq_y9Nu0od50yF8T0iJEZJSof-U_kjT4HIx81zp61Dl5ey-ed9toy-DS7GXJ1nDpwD9I4CgmHN_jQYo7wdste6QLYT0Y3EFrJ02N09vZtYMLNVEoRufxsMMHPq741fhIjz-v-rZJSD6H326G8YNUdkSELdZzL0pjOCPClx2mLZAFNvHjaawNhQl2-B4NKQlrwIE0MconY1xe4_aJ8zptF_g6bJfeTB8QRGlFw9uulbwLiHVJa4-JxXWcF_OwkBMJM9CtfzfZycsaGRuTPlmIiGpDA8c2j7Y6EaJqXVTLf9DY5-MIk203-EZSTiCPgw9UpzCt87w5vksWJ9Q5J46jAVaHadENg0BUQVHBce8MajeirjCzGHVIoA")
//                .commit()


//        val hashMap = HashMap<String, String>()
//        hashMap.put("grant_type","password")
//        hashMap.put("username","17601380842")
//        hashMap.put("password","123456789")
//        val hashMap2 = HashMap<String, Any>()
//        hashMap2.put("validCode","")
//        hashMap2.put("loginType",0)
//        hashMap2.put("Content-Type","application/x-www-form-urlencoded")
//
//        val observable = serviceApi.login(hashMap, hashMap2)
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe({s ->
////                    Log.i("响应",s)
//                    val jsonObject = JSONObject(s)
//                    val edit = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE).edit()
//                    edit.putString("token","bearer "+jsonObject.get("access_token")).commit()
//                },{},{
//
//                })
        serviceApi.getForString(ApiConfig.card_commonapi_url+"?n_code_name=incomePerMonth").parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {
                Nothings.n_typeid.clear()
                Nothings.n_code_typename.clear()
                val gson = Gson()
                val datajson = jsonElement!!.asJsonArray
                var generalData = gson.fromJson<ArrayList<GeneralCodeBean>>(datajson,object : TypeToken<ArrayList<GeneralCodeBean>>(){
                }.type)
                for (a in generalData) {
                    Nothings.n_typeid.add(a.n_code_typeid)
                    Nothings.n_code_typename.add(a.n_code_typename)
                }
            }
        }, object : RxjavaUtils.CkyErrorConsumer() {

        })

        val sub = serviceApi.getForString(ApiConfig.getAllCards+"?containtCard=1").compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val gson = Gson()
                    var mDatas = gson.fromJson<ArrayList<CredBankCards>>(response, object : TypeToken<java.util.ArrayList<CredBankCards>>() {
                    }.type)
                    Nothings.bank_card=mDatas
                }, {})

        //记录第一次打开app
        val sp = getSharedPreferences("firstrun", Context.MODE_PRIVATE)
        val version = sp.getInt("version", 0)
        if(version<getCurVersion()){
            //是第一次安装
            sp.edit().putInt("version",getCurVersion()).apply()
            //同步通讯录


        }
        updateVersion11()

    }

    private fun updateVersion11() {
        serviceApi.getForString(ApiConfig.getNewAPPVersion).compose(RxjavaUtils.transformer()).compose(RxjavaUtils.handleResult())
                .subscribe({ response ->
                    val gson = Gson()
                    var mDatas = gson.fromJson<UpdateBean>(response, object : TypeToken<UpdateBean>() {
                    }.type)
//                    LogUtils.e(mDatas.n_version_no.replace("\\.", "0"))
//                    LogUtils.e(AppUtils.getAppVersionName().replace("\\.","0"))
//
//                    val parseInt = Integer.parseInt(mDatas.n_version_no.replace("\\\\.", "0"))
//                    val parseInt1 = Integer.parseInt(AppUtils.getAppVersionName().replace("\\\\.","0"))

                    if(mDatas.n_enable>0){
                        if(!mDatas.n_version_no.equals(AppUtils.getAppVersionName())){
                            val downloadOnly = AllenVersionChecker
                                    .getInstance()
                                    .downloadOnly(
                                            UIData.create().setDownloadUrl(mDatas.n_andriod_download_url)
                                    )
                            downloadOnly
                                    .setCustomVersionDialogListener { context, versionBundle ->
                                        val baseDialog = Dialog(context, R.style.BottomDialog1)
                                        val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null)
                                        val tv = contentView.findViewById<TextView>(R.id.tv)
                                        val versionchecklib_version_dialog_commit = contentView.findViewById<TextView>(R.id.versionchecklib_version_dialog_commit)
                                        tv.setText(mDatas.n_upgrade_content)
                                        baseDialog.setContentView(contentView)
                                        var layoutParams = contentView.getLayoutParams()
                                        layoutParams.width = getResources().getDisplayMetrics().widthPixels
                                        layoutParams.height = getResources().getDisplayMetrics().heightPixels/2
                                        contentView.setLayoutParams(layoutParams)
                                        baseDialog.getWindow().setGravity(Gravity.BOTTOM)
                                        baseDialog.setCanceledOnTouchOutside(true)
                                        baseDialog.show()
                                        baseDialog
                                    }
                            downloadOnly
                                    .excuteMission(this@IndexActivity)
                        }
                    }
                }, {})

    }

    fun getCurVersion():Int{
    var version = 0
    version = packageManager.getPackageInfo(packageName,0).versionCode
    return version
}
    override fun initView() {
        noToolBar()
//        topview!!.visibility = View.GONE
        setStatusBarColor(Color.TRANSPARENT)
        supportFragmentManager.addOnBackStackChangedListener {
            //do nothing
        }
        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fl_content,IndexFragment() as Fragment,"")
//        transaction.commit()
         bind = binding as ContentMainBinding
        bind!!.bottomNav.enableShiftingMode(false)
        bind!!.bottomNav.enableAnimation(false)
        bind!!.bottomNav.enableItemShiftingMode(false)
        bind!!.bottomNav.setOnNavigationItemSelectedListener {item ->
//            item.setChecked(true)
            val id = item.itemId

            when(id){
                R.id.regist_card ->{
                ARouter.getInstance().build(Routers.cards).navigation()

                }
                R.id.health ->{
//                    noToolBar()
//                    title!!.centerTitle = "健康"
                    ARouter.getInstance().build(Routers.health_index).navigation()
                }
                R.id.wealth -> {
                    ARouter.getInstance().build(Routers.loan_index).navigation()
//                    topview!!.visibility = View.VISIBLE
//                    showToolBar()
////                    setNaviIcon(R.drawable.back_white)
//                    title!!.setBackgroundColor(resources.getColor(R.color.orange_main))
//                    title!!.setCenterTitleColor(resources.getColor(R.color.white))
//                    setStatusBarColor(resources.getColor(R.color.orange_main))
//                    title!!.centerTitle = "借钱"
//                    if(loanFragment==null) {
//                        loanFragment = LoanFragment()
//                        transaction.addToBackStack("loan")
//                    }
//
//                        transaction.replace(R.id.fl_content, loanFragment, loanFragment!!::class.java.name)
                }
                R.id.person ->{
                    ARouter.getInstance().build(Routers.mine_activity).navigation()
//                    topview!!.visibility = View.VISIBLE
//                    showToolBar()
//                    title!!.setCenterTitleColor(resources.getColor(R.color.title_black))
//                    title!!.centerTitle = "我的"
////                    setNaviIcon(R.drawable.btn_back)
//                    title!!.setBackgroundColor(resources.getColor(R.color.white))
////                    setStatusBarColor(resources.getColor(R.color.orange_main))
//                    right_menu!!.visibility = View.VISIBLE
//                    if(mineFragment==null){
//                        mineFragment = MineFragment()
//                        transaction.addToBackStack("mine")
//                    }
//                transaction.replace(R.id.fl_content,mineFragment,mineFragment!!::class.java.name)
                       }

            }
//            transaction.commitAllowingStateLoss()
            return@setOnNavigationItemSelectedListener true
        }

        if(indexFragment==null){
            indexFragment=IndexFragment()
//            transaction.addToBackStack("card")
        }
        topview!!.visibility = View.GONE
        title!!.centerTitle = "办卡"
        transaction.replace(R.id.fl_content,indexFragment,indexFragment!!::class.java.name)
        transaction.commit()

        setSwipeMpde(SwipeBackLayout.EDGE_LEFT)


//        根据layout来绑定，index页面使用fragment，故不在此绑定


    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE)
        val  token = sharedPreferences.getString("token",null);
        CkyApplication.getApp().token = token
//        val dialog = HealthtJoinDialog()
//        dialog.show(supportFragmentManager,"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
//        test()
    }

    private fun test() {
        val hashMap = HashMap<String, Any>()
        hashMap.put("n_medicalorder_id", "180907022924983730")
        hashMap.put("n_medicalorder_payamount", 0.01f)
        hashMap.put("n_medicalorder_scoreused", 0)
        hashMap.put("n_medicalorder_pay_id", 2)

//        serviceApi.getForString(ApiConfig.UpdateEntryExaminationOrder,hashMap).compose(RxjavaUtils.transformer())
//                .subscribe({ response ->
//                    val  gson= Gson()
//                    val l = System.currentTimeMillis()
//                    val random = Random()
//                    val s = random.nextInt(100000000).toString()
////                    val builder = WXPayUtils.WXPayBuilder()
////                    builder.setAppId(UserConstants.appid)
////                            .setPartnerId("1513891351")
////                            .setPrepayId(orderid)
////                            .setPackageValue("Sign=WXPay")
////                            .setTimeStamp(l.toString())
////                            .setNonceStr(s)
////                            .build()
////                            .toWXPayAndSign(this, UserConstants.appid, "E6CF121F5F5F8AEF8DB5A198F98AF6EQ")
////                    Auth.withWX(this)
////                            .setAction(Auth.Pay)
////                            .payNonceStr(s)
////                            .payPackageValue("Sign=WXPay")
////                            .payPartnerId("1513891351")
////                            .payPrepayId(orderid)
////                            .paySign(WxUtils.signNum("1513891351",orderid,"Sign=WXPay",s,l.toString(),"E6CF121F5F5F8AEF8DB5A198F98AF6EQ"))
////                            .payTimestamp(l.toString())
////                            .build(object : AuthCallback(){
////                                override fun onFailed(msg: String?) {
////                                    super.onFailed(msg)
////                                    LogUtils.e(msg)
////                                }
////
////                                override fun onStart() {
////                                    super.onStart()
////                                    LogUtils.e("starta")
////                                }
////
////                                override fun onCancel() {
////                                    super.onCancel()
////                                    LogUtils.e("cancel")
////                                }
////
////                                override fun onSuccessForPay(result: String?) {
////                                    super.onSuccessForPay(result)
////                                    LogUtils.e(result)
////                                }
////                            })
//                }, {
//                    error ->
//                    ToastUtils.showShort(error.message)
//                })

        serviceApi.getForString(ApiConfig.page_index_url + "?userId=" + "").parseData(object : RxjavaUtils.CkySuccessConsumer() {
            override fun onSuccess(jsonElement: JsonElement?) {

            }
        }, object : RxjavaUtils.CkyErrorConsumer() {
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(event!!.action){
            KeyEvent.KEYCODE_BACK ->{
         if(time.compareTo(0)==0){
             time = System.currentTimeMillis()
             ToastUtils.showShort("再点击一次退出")
             return true
         }else{
             if(System.currentTimeMillis()-time<1000&&System.currentTimeMillis()-time>100){
                 finish()
             }else{
                 time = 0
             }
         }


            }

        }
        return super.onKeyDown(keyCode, event)
    }


    fun getImei(){
        if (ContextCompat.checkSelfPermission(this@IndexActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager
                        .PERMISSION_GRANTED ){
            PermissionHelper.requestPhone(object :PermissionHelper.OnPermissionGrantedListener{
                override fun onPermissionGranted() {
                   postPhoneInfo()
                }
            },object :PermissionHelper.OnPermissionDeniedListener{
                override fun onPermissionDenied() {

                }
            })
        }else{
            postPhoneInfo()
        }

    }

    fun postPhoneInfo(){



            //成功
            val packageInfo = packageManager.getPackageInfo(packageName, 0)

            val telephonyManager =  getSystemService(TELEPHONY_SERVICE)as TelephonyManager
            val imei = telephonyManager.getDeviceId();

            val veresion = packageInfo.versionName
            val jsonObject = JsonObject()
            jsonObject.addProperty("n_mobile_system","android")
            jsonObject.addProperty("n_version",veresion)
            jsonObject.addProperty("n_imei",imei)
            jsonObject.addProperty("n_app_package_id","com.app.nsk")
            jsonObject.addProperty("n_have_addr_book",BuildConfig.readContact)
        serviceApi.postJsonForString(ApiConfig.imei_url,jsonObject).parseData(object :RxjavaUtils.CkySuccessConsumer(){
            override fun onSuccess(jsonElement: JsonElement?) {

            }
        },object :RxjavaUtils.CkyErrorConsumer(){})


    }

}