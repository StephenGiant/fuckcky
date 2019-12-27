package com.nsk.cky.ckylibrary.utils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.Utils;
import com.nsk.cky.ckylibrary.UserConstants;

public class BaiduLocation {
    /**
     * 经度
     *
     */
    private static double mylongitude = -1.0;
    /**
     * 纬度
     *
     */
    private static double mylatitude = -1.0;



    /**
     * 回调经纬度的接口定义
     *
     */
    public  interface MyLocationListener {
        void myLocatin(boolean isSuccess,String pro,String city,String code);
    }

    /**
     *  获取当前经纬度
     *
     */
    public static void getLocation(MyLocationListener myLocationListener) {
        LL ll = new LL( myLocationListener);
        if(ll!=null){
            ll.run();
        }
    }
    static class LL implements Runnable{

        private final MyLocationListener listener;
        private boolean mLocationScu=false;

        public LL(MyLocationListener myLocationListener) {
            listener = myLocationListener;
        }

        @Override
        public void run() {

            final LocationClient locationClient = new LocationClient(Utils.getApp());
            if(locationClient==null){
                return;
            }
            // 设置定位条件

            LocationClientOption option = new LocationClientOption();
            if(option==null){
                return;
            }

            option.setOpenGps(true); // 是否打开GPS
            option.setCoorType("gcj02"); // 设置返回值的坐标类型。
            option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
            option.setProdName("gxb"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
            option.setScanSpan(5000); // 设置定时定位的时间间隔。单位毫秒
            option.setIsNeedAddress(true);
            locationClient.setLocOption(option);
            // 注册位置监听器
            locationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation == null) {
                        return;
                    }
                    if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                        //gps定位成功
                        mLocationScu = true;
                    } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                        //网络定位成功
                        mLocationScu = true;
                    } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                        //离线定位成功，离线定位结果也是有效的
                        mLocationScu = true;
                    } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                        //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                        mLocationScu = false;
                    } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                        //网络不同导致定位失败，请检查网络是否通畅
                        mLocationScu = false;
                    } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                        //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                        mLocationScu = false;
                    } else {
                        mLocationScu = false;
                    }

                    mylongitude = bdLocation.getLongitude();
                    mylatitude = bdLocation.getLatitude();
                 //   ToastUtils.showLong(bdLocation.getCity());
                    if(mLocationScu) {
                        DbManger.getInstance().put(UserConstants.loca_city, bdLocation.getCity());
                    }else {
                        DbManger.getInstance().put(UserConstants.loca_city, "北京市");
                    }
                    // 经纬度
                    if (listener!= null) {
                        listener.myLocatin(mLocationScu,bdLocation.getProvince(),bdLocation.getCity(),bdLocation.getCityCode());
                        locationClient.stop();
                    }
                }
            });
            locationClient.start();
            /*
             * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
             * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
             * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
             * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
             */
            locationClient.requestLocation();
        }
    }
}