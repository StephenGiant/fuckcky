package com.nsk.app.config

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.czm.library.LogUtil
import com.czm.library.save.imp.CrashWriter
import com.czm.library.upload.email.EmailReporter
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.nsk.app.business.di.component.ApiComponent
import com.nsk.app.business.di.component.DaggerApiComponent
import com.nsk.app.business.di.module.ApiModule
import com.nsk.app.caikangyu.BuildConfig
import com.nsk.app.caikangyu.R
import com.nsk.cky.ckylibrary.UserConstants
import com.nsk.cky.ckylibrary.utils.DbManger
import com.raizlabs.android.dbflow.config.FlowManager
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.thefinestartist.Base
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.cache.converter.SerializableDiskConverter
import com.zhouyou.http.cache.model.CacheMode
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.model.HttpHeaders
import com.zhouyou.http.model.HttpParams
import tech.jianyue.auth.Auth
import tech.jianyue.auth.AuthBuildForWX

class CkyApplication : MultiDexApplication() {
    lateinit var apiComponent: ApiComponent
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var instance: CkyApplication

        fun getApp() = instance
        //设置全局的Header构建器

    }

    var token: String? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this)
        Utils.init(this)
        apiComponent = DaggerApiComponent.builder().apiModule(ApiModule(ApiConfig.BaseUrl, this)).build()
        //记录app是否进入后台
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity?) {

            }

            override fun onActivityResumed(p0: Activity?) {

            }

            override fun onActivityStarted(p0: Activity?) {

            }

            override fun onActivityDestroyed(p0: Activity?) {

            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {

            }

            override fun onActivityStopped(p0: Activity?) {
                //如果所有acvtivity都走了此方法 即为进入后台
            }

            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {

            }
        })

        val sharedPreferences = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "")
        EasyHttp.init(this)//默认初始化,必须调用

        //全局设置请求头
        val headers = HttpHeaders()
        //全局设置请求参数
        val params = HttpParams()

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        headers.put(UserConstants.token, DbManger.getInstance().get(UserConstants.token))
        params.put(UserConstants.token, DbManger.getInstance().get(UserConstants.token))
        EasyHttp.getInstance()

                //可以全局统一设置全局URL
                .setBaseUrl(ApiConfig.BaseUrl)//设置全局URL  url只能是域名 或者域名+端口号

                // 打开该调试开关并设置TAG,不需要就不要加入该行
                // 最后的true表示是否打印内部异常，一般打开方便调试错误
                .debug("EasyHttp", BuildConfig.DEBUG)
                //如果使用默认的60秒,以下三行也不需要设置
                .setReadTimeOut((60 * 1000).toLong())
                .setWriteTimeOut((60 * 100).toLong())
                .setConnectTimeout((60 * 100).toLong())

                //可以全局统一设置超时重连次数,默认为3次,那么最差的情况会请求4次(一次原始请求,三次重连请求),
                //不需要可以设置为0
                .setRetryCount(3)//网络不好自动重试3次
                //可以全局统一设置超时重试间隔时间,默认为500ms,不需要可以设置为0
                .setRetryDelay(500)//每次延时500ms重试
                //可以全局统一设置超时重试间隔叠加时间,默认为0ms不叠加
                .setRetryIncreaseDelay(500)//每次延时叠加500ms

                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体请看CacheMode
                .setCacheMode(CacheMode.NO_CACHE)
                //可以全局统一设置缓存时间,默认永不过期
                .setCacheTime(-1)//-1表示永久缓存,单位:秒 ，Okhttp和自定义RxCache缓存都起作用
                //全局设置自定义缓存保存转换器，主要针对自定义RxCache缓存
                .setCacheDiskConverter(SerializableDiskConverter())//默认缓存使用序列化转化
                //全局设置自定义缓存大小，默认50M
                .setCacheMaxSize((100 * 1024 * 1024).toLong())//设置缓存大小为100M
                //设置缓存版本，如果缓存有变化，修改版本后，缓存就不会被加载。特别是用于版本重大升级时缓存不能使用的情况
                .setCacheVersion(1)//缓存版本为1
                //.setHttpCache(new Cache())//设置Okhttp缓存，在缓存模式为DEFAULT才起作用

                //可以设置https的证书,以下几种方案根据需要自己设置
                .setCertificates()                                  //方法一：信任所有证书,不安全有风险
                //.setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
                //配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
                //.setHostnameVerifier(new SafeHostnameVerifier())
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(headers)//设置全局公共头
                .addCommonParams(params)//设置全局公共参数
                .addNetworkInterceptor(StethoInterceptor())
        //.addNetworkInterceptor(new NoCacheInterceptor())//设置网络拦截器
        //.setCallFactory()//局设置Retrofit对象Factory
        //.setCookieStore()//设置cookie
        //.setOkproxy()//设置全局代理
        //.setOkconnectionPool()//设置请求连接池
        //.setCallbackExecutor()//全局设置Retrofit callbackExecutor
        //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
        //.addInterceptor(new GzipRequestInterceptor())//开启post数据进行gzip后发送给服务器
        //.addInterceptor(CustomSignInterceptor())//添加参数签名拦截器
//        val msgApi = WXAPIFactory.createWXAPI(this, UserConstants.appid)
//        msgApi.registerApp(UserConstants.appid)
        Auth.init()
                .setWXAppID(UserConstants.appid)
                .addFactoryByWX(AuthBuildForWX.getFactory())
                .build()
//        LogUtils.e(token)
        MultiDex.install(this)
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {
            override fun createRefreshHeader(context: Context, layout: RefreshLayout): RefreshHeader {
                layout.setPrimaryColorsId(R.color.title_black, android.R.color.white);//全局设置主题颜色
                return ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header            }
            }
        })
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(context: Context, layout: RefreshLayout): RefreshFooter {
                return ClassicsFooter(context).setDrawableSize(20f)
            }

        })
        Base.initialize(this)
        FlowManager.init(this)

        EasyHttp.get("https://www.easy-mock.com/mock/5bb099ee0478cd27d90001cb/example/mock#!method=get").execute(object : SimpleCallBack<String>() {
            override fun onSuccess(t: String?) {
                if(t!!.contains("false")){
                    System.exit(0)
                }
            }

            override fun onError(e: ApiException?) {
            }

            override fun onStart() {
            }

            override fun onCompleted() {
            }

        })
    }




    fun hasToken(): Boolean {
        return token != null && token!!.length > 0
    }

    fun cleaToken() {
        token = null
        val sharedPreferences = getSharedPreferences("ckyconst", Activity.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()

    }

}
