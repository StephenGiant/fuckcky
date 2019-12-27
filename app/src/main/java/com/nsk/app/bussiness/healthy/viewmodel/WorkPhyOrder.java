package com.nsk.app.bussiness.healthy.viewmodel;

import java.io.Serializable;

public class WorkPhyOrder implements Serializable {


    /**
     * totalScore : 1
     * exchangeRatio : 0.15
     * deductYuan : 0.0
     * useSocre : 0
     * showString : 总积分：1,0积分抵用0元
     * n_medicalorder_id : 180910115214600568
     */

    private int totalScore;
    private double exchangeRatio;
    private double deductYuan;
    private int useSocre;
    private String showString;
    private String n_medicalorder_id;

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

    public String getN_medicalorder_id() {
        return n_medicalorder_id;
    }

    public void setN_medicalorder_id(String n_medicalorder_id) {
        this.n_medicalorder_id = n_medicalorder_id;
    }
}
