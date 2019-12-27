package com.nsk.app.bussiness.healthy.viewmodel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class WorkPhyInfo implements Serializable{


    /**
     * n_exam_product_id : 10023
     * n_exam_hospital_id : 13180
     * n_hospital_name : null
     * n_address : asfasf
     * n_exam_price : 2213.00
     * n_exam_group_price : 35
     * n_group_start_time : 2018/8/8 0:00:00
     * n_group_end_time : 2018/8/28 0:00:00
     * n_exam_product_details : 34532453245
     */

    private int n_exam_product_id;
    private String n_exam_hospital_id;
    private String n_hospital_name;
    private String n_address;
    private String n_exam_price;
    private double n_exam_group_price;
    private String n_group_start_time;
    private String n_group_end_time;
    private String n_exam_product_details;

    public int getN_exam_product_id() {
        return n_exam_product_id;
    }

    public void setN_exam_product_id(int n_exam_product_id) {
        this.n_exam_product_id = n_exam_product_id;
    }

    public String getN_exam_hospital_id() {
        return n_exam_hospital_id;
    }

    public void setN_exam_hospital_id(String n_exam_hospital_id) {
        this.n_exam_hospital_id = n_exam_hospital_id;
    }

    public String getN_hospital_name() {
        return n_hospital_name;
    }

    public void setN_hospital_name(String n_hospital_name) {
        this.n_hospital_name = n_hospital_name;
    }

    public String getN_address() {
        return n_address;
    }

    public void setN_address(String n_address) {
        this.n_address = n_address;
    }

    public String getN_exam_price() {
        return n_exam_price;
    }

    public void setN_exam_price(String n_exam_price) {
        this.n_exam_price = n_exam_price;
    }

    public double getN_exam_group_price() {
        return n_exam_group_price;
    }
    public String getPriceString(){
        return "￥"+n_exam_group_price;
    }
    public void setN_exam_group_price(double n_exam_group_price) {
        this.n_exam_group_price = n_exam_group_price;
    }

    public String getN_group_start_time() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(n_group_start_time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String format = formatter.format(n_group_start_time);

        return "最新开团时间: "+n_group_start_time
                +" - "+n_group_end_time;
    }

    public void setN_group_start_time(String n_group_start_time) {
        this.n_group_start_time = n_group_start_time;
    }

    public String getN_group_end_time() {
        return n_group_end_time;
    }

    public void setN_group_end_time(String n_group_end_time) {
        this.n_group_end_time = n_group_end_time;
    }

    public String getN_exam_product_details() {
        return "详情: "+n_exam_product_details;
    }

    public void setN_exam_product_details(String n_exam_product_details) {
        this.n_exam_product_details = n_exam_product_details;
    }
}
