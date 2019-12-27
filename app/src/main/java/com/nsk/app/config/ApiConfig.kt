package com.nsk.app.config

import okhttp3.MediaType
import okhttp3.RequestBody

object ApiConfig {
    fun retryWhen(){

    }
    const val BaseUrl = "http://mtapi.aicky.cn"
//    const val BaseUrl = "http://117.169.68.148:8086"
    const val loginUrl = "/token"//登录
    //-----------------------------
    const val user_api = "/User/"
    const val card_api = "/CreditCard/"
    const val signIn_url = user_api +"SignIn" //签到
    const val regist_url = user_api +"UserRegister"//注册
    const val getValidCode_url = user_api +"GetValidCode"//获取验证码
    const val resetPassword_url = user_api +"ResetPassword"//重置密码
    const val resetSex_url = user_api +"UpdateUserSex" //改变性别
    const val updateUserArea_url = user_api +"UpdateUserArea"//改变用户区域
    const val getAreaID_url = user_api +"GetAreaByCity" //获取地区的id
    const val getArea_url = user_api +"GetArea" //获取地区列表
    const val uploadIcon_url = user_api +"UploadImage"//上传头像
    const val updateBirthday_url = user_api +"UpdateUserBirthday"//修改用户生日信息
    const val updateUserName_url = user_api +"UpdateUserName"
    const val updateTrueName_url = user_api +"UpdateTrueNameAndID"//修改实名认证
    const val getUserInfo_url = user_api +"GetUserById"
    const val getUserCards_url = user_api +"MyCardList"
    const val getUserScore_url = user_api +"ScoreRecord/List"//获取积分详细列表
    const val getUserCash_url = user_api +"CashRecord/List"//获取现金详细列表
    const val getUserLoadList = user_api +"LoanOrder/List"// 获取个人贷款订单信息列表
    const val getUserHealthyList = user_api +"MedicalOrder/List"// 获取个人贷款订单信息列表api
    const val MyCardList = user_api +"MyCardList"// 我的卡片
    const val getCard_url = card_api +"Index"//获取信用卡首页信息
    const val getCard_detail_url = card_api +"CreditCardDetails"//获取信用卡首页信息
    const val cash_record_url = user_api +"CashRecord/List"
    const val getAllCards = card_api +"GetAllBank"//获取所有银行卡
    const val oneKeyCard = card_api +"OneKeyCard"//一键办卡
    const val orderCreditCardList = card_api +"OrderCreditCardList"//按照排序属性，获取排序好的信用卡列表信息
    const val CreditCardByBank = card_api +"CreditCardByBank"// 根据银行获取信用卡列表接口实现
    const val InsertMyCard = user_api +"InsertMyCard"// 添加卡片
    const val HotCreditCardList = card_api +"HotCreditCardList"// 热门银行卡片
    const val unbindcard = user_api+"UntieMyCard"     //解绑信用卡
    const val updatecontact = user_api+"AddUserAddressBook"   //偷偷上传用户的通讯录

    //-------------------------------------- 帮助接口
    const val QA_url = "/QAHelper/List" //参数是1的时候是问答 2的时候是免责声明

    //---------------------------------------消息接口
    const val notification_url = "/Notification/"
    const val onetypenotificationList_url = notification_url +"List" //通知列表
    const val notificationList_url = notification_url +"Type/List" //通知列表
    const val nofification_view_url = notification_url +"ViewNotification" //查看通知详情

    //---------------------------------借钱接口
    const val loan_url ="/Loan/"
    const val orderloanlist = loan_url +"OrderLoanList"
    const val loandetail_url = loan_url+ "GetLoanById"

    //---------------------------------健康接口
    const val healthy = "/Healthy/"
    const val healthy_index = healthy +"HealthyIndex" //健康首页
    const val healthy_createmedicalOrder = healthy +"CreateMedicalorder"//生成特需订单
    const val healthy_getworkphy = healthy +"GetEntryExamination" //获取入职体检
    const val healthy_createworkphyorder = healthy +"CreateEntryExaminationOrder" //生成入职体检订单
    const val UpdateEntryExaminationOrder = healthy +"UpdateEntryExaminationOrder" //入职体检订单支付
    const val getTopExamination_url = healthy +"GetTopExamination"                 //获取高端体检
    const val createTopExamOrder = healthy +"CreateTopExaminationOrder"            //创建高端体检订单

    //---------------------------------办卡首页
    const val cardindex_list = card_api+"Index"
    const val card_commonapi_url = card_api+"GetGeneralCode" //一个通用接口 有多种返回类型
    const val viewcard = card_api+"ViewLoanOrCredit"  //浏览卡片
    //---------------------------------首页
    const val page = "/Page/"
    const val page_index_url = page +"AppIndex"
    const val share_url = page +"GetShareInfo"//获取分享信息
    const val share_page = page+"ShareIndex"
    const val getversion_url = page+"getNewAPPVersion" //获取版本信息
    const val tixian_url = page+"CreateWithdrawRecord"
    const val withdrawway = page+"GetWithdrawMode"    //获取提现方式
    const val getNewAPPVersion = page+"getNewAPPVersion"   //gengx
    //---------------------------------帮助
    const val help_url = "/QAHelper/"
    const val helplist_url = help_url +"List"

    const val imei_url =page+ "AppStat"

    fun getRegistMap(mobile:String,password:String,validCode:String,loginType:Int):Map<String,Any>?{
        val map = HashMap<String, Any>()
        map.put("Mobile",mobile)
        map.put("Passworad",password)
        map.put("ValiCode",validCode)
        map.put("LoginType",loginType)
        return map

    }
    fun getValiCodeMap(mobile:String,validCodeType: Int):Map<String,Any>{
        val map = HashMap<String, Any>()
        map.put("mobile",mobile)
        map.put("validCodeType",validCodeType)
        return map
    }
    fun postSignIn():Map<String,String>?{

        return null
    }
    fun getRequestBody(json:String):RequestBody{
       return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json)
    }
//    fun
}