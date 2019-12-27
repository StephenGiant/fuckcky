package com.nsk.app.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 * @Package com.nsk.app.db
 * @author qianpeng
 * @date 2018/9/30.
 * @describe
 */
@Table(database = AddressDatabase::class)
 class CkyAddress(   ){
    @PrimaryKey(autoincrement = true) var id :Int = 0
    @Column var name :String?=null
    @Column var areaid:String?=null
    @Column  var local:Boolean? = false
    fun isLocal() = local

    //boolean类型的 必须自己添加is 方法 因为kotlin自动生成的是get 而不是is 而 源码里反射的是is
}



