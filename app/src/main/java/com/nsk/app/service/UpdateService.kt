package com.nsk.app.service

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.util.Log
import com.nsk.app.config.CkyApplication
import com.nsk.cky.ckylibrary.http.ServiceApi
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class UpdateService :Service() {
lateinit var url:String
    @Inject
    lateinit var serviceApi: ServiceApi
    lateinit var file :File
    override fun onBind(p0: Intent?): IBinder {
        url = p0!!.getStringExtra("url")
        return MyBinder()
    }


    override fun onCreate() {
        CkyApplication.getApp().apiComponent.inject(this)
         val tempfile = Environment.getExternalStorageDirectory()
        file = File(tempfile,"cky.apk")
        super.onCreate()
    }
    fun startDownload(){
        serviceApi.get(url)
    }

    class MyBinder :IBinder{
        override fun getInterfaceDescriptor(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isBinderAlive(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun linkToDeath(p0: IBinder.DeathRecipient?, p1: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun queryLocalInterface(p0: String?): IInterface {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun transact(p0: Int, p1: Parcel?, p2: Parcel?, p3: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun dumpAsync(p0: FileDescriptor?, p1: Array<out String>?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun dump(p0: FileDescriptor?, p1: Array<out String>?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun unlinkToDeath(p0: IBinder.DeathRecipient?, p1: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun pingBinder(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private fun getHttpURLConnection(url: URL): HttpURLConnection? {
        var conn: HttpURLConnection? = null
        try {
            conn = url.openConnection() as HttpURLConnection
            // 设置请求方法
            conn.requestMethod = "GET"
            // 设置连接主机超时时间
            conn.connectTimeout = 5 * 1000
            //设置从主机读取数据超时
            conn.readTimeout = 5 * 1000
            //设置请求中的媒体类型信息。
            //conn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            //conn.addRequestProperty("Connection", "Keep-Alive");
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return conn
    }
    /**
     * 下载线程
     *
     * @param downloadUrl 下载地址
     */
    private fun downloadAPK(downloadUrl: String): Boolean {
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            val conn = getHttpURLConnection(URL(downloadUrl))
            //开始连接
            conn!!.connect()
            val totalLength = conn!!.getContentLength()

            inputStream = conn!!.getInputStream()
            if (inputStream != null) {
                fileOutputStream = FileOutputStream(file)
                val buff = ByteArray(1024 * 10)
                var hasRead = -1
                var wanchengdu = 0
                var temp = 0
                while (inputStream!!.read(buff) != -1) {
                    hasRead = inputStream!!.read(buff)
                    fileOutputStream!!.write(buff, 0, hasRead)
//                    val progress = (100.0 * (wanchengdu.toDouble() / totalLength.toDouble())).toInt()
//                    if (progress != temp) {
//                        mBuilder.setProgress(100, progress, false)
//                        mBuilder.setContentText(progress + "%")
//                        mNotificationManager.notify(notification_id, mBuilder.build())
//                        sendBroadcast(
//                                Intent(UpgradeManager.ACTION).putExtra("path", file.getPath())
//                                        .putExtra("progress", progress))
//                        temp = progress
//                    }
//                    wanchengdu += hasRead
                }
                fileOutputStream!!.flush()
//                Log.e("=================>>", "================DOwn")
            }
            return true
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return false
    }
}