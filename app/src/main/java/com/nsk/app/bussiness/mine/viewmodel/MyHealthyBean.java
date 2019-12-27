package com.nsk.app.bussiness.mine.viewmodel;

import java.io.Serializable;

public class MyHealthyBean implements Serializable {


    /**
     * n_medicalorder_id : 180906111651558606
     * n_hospital_name : 复旦大学附属-眼耳鼻喉科医院
     * n_address : 地址：上海市黄浦区瑞金二路197号
     * n_exam_product_details : 详情：外科五官科
     * n_exam_group_price : 0.01
     * n_medicalorder_createtime : 2018-09-06 23:16:51
     * n_medicalorder_status : 0
     * n_medicalorder_pay_id : 0
     * n_examination_describe : 套餐简介 本套餐是一款针对入职人群的套餐，主要检查肝肾功能、胸片及心电图。 使用城市 北京、广州、上海、天津、重庆、沈阳、南京、武汉、成都、长春、无锡、江阴、苏州、常州、烟台、潍坊、芜湖、杭州、宁波、福州、长沙、深圳、绵阳、银川
     * totalScore : 1
     * exchangeRatio : 0.15
     * deductYuan : 0
     * useSocre : 0
     * showString : 总积分：1,0积分抵用0元
     */

    private String n_medicalorder_id;
    private String n_hospital_name;
    private String n_address;
    private String n_exam_product_details;
    private double n_exam_group_price;
    private String n_medicalorder_createtime;
    private int n_medicalorder_status;
    private int n_medicalorder_pay_id;
    private String n_examination_describe;
    private int totalScore;
    private double exchangeRatio;
    private double deductYuan;
    private double n_exam_price;
    private int useSocre;
    private String showString;
    private double n_medicalorder_price;

    public double getN_medicalorder_price() {
        return n_medicalorder_price;
    }

    public void setN_medicalorder_price(double n_medicalorder_price) {
        this.n_medicalorder_price = n_medicalorder_price;
    }

    public double getN_exam_price() {
        return n_exam_price;
    }

    public void setN_exam_price(double n_exam_price) {
        this.n_exam_price = n_exam_price;
    }

    public String getN_medicalorder_id() {
        return n_medicalorder_id;
    }

    public void setN_medicalorder_id(String n_medicalorder_id) {
        this.n_medicalorder_id = n_medicalorder_id;
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

    public String getN_exam_product_details() {
        return n_exam_product_details;
    }

    public void setN_exam_product_details(String n_exam_product_details) {
        this.n_exam_product_details = n_exam_product_details;
    }

    public double getN_exam_group_price() {
        return n_exam_group_price;
    }

    public void setN_exam_group_price(double n_exam_group_price) {
        this.n_exam_group_price = n_exam_group_price;
    }

    public String getN_medicalorder_createtime() {
        return n_medicalorder_createtime;
    }

    public void setN_medicalorder_createtime(String n_medicalorder_createtime) {
        this.n_medicalorder_createtime = n_medicalorder_createtime;
    }

    public int getN_medicalorder_status() {
        return n_medicalorder_status;
    }

    public void setN_medicalorder_status(int n_medicalorder_status) {
        this.n_medicalorder_status = n_medicalorder_status;
    }

    public int getN_medicalorder_pay_id() {
        return n_medicalorder_pay_id;
    }

    public void setN_medicalorder_pay_id(int n_medicalorder_pay_id) {
        this.n_medicalorder_pay_id = n_medicalorder_pay_id;
    }

    public String getN_examination_describe() {
        return n_examination_describe;
    }

    public void setN_examination_describe(String n_examination_describe) {
        this.n_examination_describe = n_examination_describe;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getExchangeRatio() {
        return exchangeRatio;
    }

    public void setExchangeRatio(double exchangeRatio) {
        this.exchangeRatio = exchangeRatio;
    }

    public double getDeductYuan() {
        return deductYuan;
    }

    public void setDeductYuan(double deductYuan) {
        this.deductYuan = deductYuan;
    }

    public int getUseSocre() {
        return useSocre;
    }

    public void setUseSocre(int useSocre) {
        this.useSocre = useSocre;
    }

    public String getShowString() {
        return showString;
    }

    public void setShowString(String showString) {
        this.showString = showString;
    }
}
