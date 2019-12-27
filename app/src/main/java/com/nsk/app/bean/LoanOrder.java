package com.nsk.app.bean;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/28
 */
public class LoanOrder {


    /**
     * n_order_id : 0
     * n_order_enable : true
     * n_order_status : 0
     * n_order_createtime : 2018-09-04T15:24:02.543Z
     * n_order_tracertime : 2018-09-04T15:24:02.543Z
     * n_order_loan_amount : 0
     * n_bank_name : string
     * n_bank_logo : string
     */

    private int n_order_id;
    private boolean n_order_enable;
    private int n_order_status;
    private String n_order_createtime;
    private String n_order_tracertime;
    private int n_order_loan_amount;
    private String n_bank_name;
    private String n_bank_logo;

    public int getN_order_id() {
        return n_order_id;
    }

    public void setN_order_id(int n_order_id) {
        this.n_order_id = n_order_id;
    }

    public boolean isN_order_enable() {
        return n_order_enable;
    }

    public void setN_order_enable(boolean n_order_enable) {
        this.n_order_enable = n_order_enable;
    }

    public int getN_order_status() {
        return n_order_status;
    }

    public void setN_order_status(int n_order_status) {
        this.n_order_status = n_order_status;
    }

    public String getN_order_createtime() {
        return n_order_createtime;
    }

    public void setN_order_createtime(String n_order_createtime) {
        this.n_order_createtime = n_order_createtime;
    }

    public String getN_order_tracertime() {
        return n_order_tracertime;
    }

    public void setN_order_tracertime(String n_order_tracertime) {
        this.n_order_tracertime = n_order_tracertime;
    }

    public int getN_order_loan_amount() {
        return n_order_loan_amount;
    }

    public void setN_order_loan_amount(int n_order_loan_amount) {
        this.n_order_loan_amount = n_order_loan_amount;
    }

    public String getN_bank_name() {
        return n_bank_name;
    }

    public void setN_bank_name(String n_bank_name) {
        this.n_bank_name = n_bank_name;
    }

    public String getN_bank_logo() {
        return n_bank_logo;
    }

    public void setN_bank_logo(String n_bank_logo) {
        this.n_bank_logo = n_bank_logo;
    }
}
