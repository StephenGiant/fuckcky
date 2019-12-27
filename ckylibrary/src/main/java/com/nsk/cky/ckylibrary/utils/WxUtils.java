package com.nsk.cky.ckylibrary.utils;

import com.nsk.cky.ckylibrary.UserConstants;

/**
 * Description:
 * Company    :
 *
 * @author :
 * @date : 2018/9/7
 */
public class WxUtils {
    public static String signNum(String partnerId,String prepayId,String packageValue,String nonceStr,String timeStamp,String key){
        String stringA=
                "appid="+ UserConstants.appid
                        +"&noncestr="+nonceStr
                        +"&package="+packageValue
                        +"&partnerid="+partnerId
                        +"&prepayid="+prepayId
                        +"&timestamp="+timeStamp;


        String stringSignTemp = stringA+"&key="+key;
        String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
        return  sign;
    }
}
