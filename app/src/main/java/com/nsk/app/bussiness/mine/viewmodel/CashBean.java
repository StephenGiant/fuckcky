package com.nsk.app.bussiness.mine.viewmodel;

import java.io.Serializable;

public class CashBean implements Serializable{


    /**
     * n_record_id : 0
     * n_record_type : 0
     * n_record_cardid : 0
     * n_record_createuserid : string
     * n_record_createtime : 2018-09-11T14:30:53.635Z
     * n_record_cash : 0
     * n_record_touserid : string
     * n_record_title : string
     * n_record_enable : 0
     * n_record_used : 0
     */

    private int n_record_id;
    private int n_record_type;
    private int n_record_cardid;
    private String n_record_createuserid;
    private String n_record_createtime;
    private int n_record_cash;
    private String n_record_touserid;
    private String n_record_title;
    private int n_record_enable;
    private int n_record_used;

    public int getN_record_id() {
        return n_record_id;
    }

    public void setN_record_id(int n_record_id) {
        this.n_record_id = n_record_id;
    }

    public int getN_record_type() {
        return n_record_type;
    }

    public void setN_record_type(int n_record_type) {
        this.n_record_type = n_record_type;
    }

    public int getN_record_cardid() {
        return n_record_cardid;
    }

    public void setN_record_cardid(int n_record_cardid) {
        this.n_record_cardid = n_record_cardid;
    }

    public String getN_record_createuserid() {
        return n_record_createuserid;
    }

    public void setN_record_createuserid(String n_record_createuserid) {
        this.n_record_createuserid = n_record_createuserid;
    }

    public String getN_record_createtime() {
        return n_record_createtime;
    }

    public void setN_record_createtime(String n_record_createtime) {
        this.n_record_createtime = n_record_createtime;
    }

    public int getN_record_cash() {
        return n_record_cash;
    }

    public void setN_record_cash(int n_record_cash) {
        this.n_record_cash = n_record_cash;
    }

    public String getN_record_touserid() {
        return n_record_touserid;
    }

    public void setN_record_touserid(String n_record_touserid) {
        this.n_record_touserid = n_record_touserid;
    }

    public String getN_record_title() {
        return n_record_title;
    }

    public void setN_record_title(String n_record_title) {
        this.n_record_title = n_record_title;
    }

    public int getN_record_enable() {
        return n_record_enable;
    }

    public void setN_record_enable(int n_record_enable) {
        this.n_record_enable = n_record_enable;
    }

    public int getN_record_used() {
        return n_record_used;
    }

    public void setN_record_used(int n_record_used) {
        this.n_record_used = n_record_used;
    }
}
