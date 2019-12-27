package com.nsk.app.bussiness.mine.viewmodel

/**
 * @Package com.nsk.app.bussiness.mine.viewmodel
 * @author qianpeng
 * @date 2018/8/21.
 * @describe
 */
class ScoreDetail {
    var n_record_id:Int = -1  //积分记录id
    var n_record_type:Int=-1 //积分类型
    var n_record_orderid:Int=-1 //订单id
    var n_record_createuserid:String?=null //userid
    var n_record_createtime:String?=null //创建时间，要自行格式化
    var n_record_score:Int=0 //可用积分
    var n_record_touserid:String?=null
    var n_record_name:String?=null //记录名称，即积分获得途径的文字描述
    var n_record_balance:Int =-1

}