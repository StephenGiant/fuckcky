package com.nsk.cky.ckylibrary.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.github.sumimakito.quickkv.QuickKV;
import com.nsk.cky.ckylibrary.UserConstants;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/12
 */
public class DbManger {
    private static final DbManger db=new DbManger();
    private static final QuickKV quick=new QuickKV(Utils.getApp());
    public static DbManger getInstance(){
        return db;
    }
    public  void put(String key,String value){
        quick.getDatabase().put(key,value);
        quick.getDatabase().persist();
    }
    public  String get(String key){
        //写法有问题 不通用
        String o = (String) quick.getDatabase().get(key);
        if(o==null){
            return "";
        }
        if(TextUtils.equals(key, UserConstants.areaId)&&TextUtils.isEmpty(o)){
            o="100100";
        }

        return o;
    }
    public  void remove(String key){
        quick.getDatabase().remove(key);
        quick.getDatabase().persist();
    }
}
