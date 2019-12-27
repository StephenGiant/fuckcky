package com.nsk.app.bussiness.healthy.viewmodel;

import java.io.Serializable;
import java.util.List;

public class HealthyCategoriBean implements Serializable{


    /**
     * n_dept_id : 3001
     * n_dept_name : 肿瘤科
     * n_dept_ranking : 1
     * n_dept_logo :
     * nsk_hospitalInfo : [{"n_hospital_id":13179,"n_hospital_name":"华山医院","n_treatment":"12","n_ranking":1,"n_hospital_logo":"http://117.169.68.148:8099/upload/81503e6c-11b0-48b6-9b2e-a8fad86cedca.jpg"},{"n_hospital_id":12178,"n_hospital_name":"上海新华医院 ","n_treatment":"34sfdfsdf","n_ranking":2,"n_hospital_logo":"http://117.169.68.148:8086/upload/xinhua.jpg"}]
     */

    private int n_dept_id;
    private String n_dept_name;
    private int n_dept_ranking;
    private String n_dept_logo;
    private List<NskHospitalInfoBean> nsk_hospitalInfo;

    public int getN_dept_id() {
        return n_dept_id;
    }

    public void setN_dept_id(int n_dept_id) {
        this.n_dept_id = n_dept_id;
    }

    public String getN_dept_name() {
        return n_dept_name;
    }

    public void setN_dept_name(String n_dept_name) {
        this.n_dept_name = n_dept_name;
    }

    public int getN_dept_ranking() {
        return n_dept_ranking;
    }

    public void setN_dept_ranking(int n_dept_ranking) {
        this.n_dept_ranking = n_dept_ranking;
    }

    public String getN_dept_logo() {
        return n_dept_logo;
    }

    public void setN_dept_logo(String n_dept_logo) {
        this.n_dept_logo = n_dept_logo;
    }

    public List<NskHospitalInfoBean> getNsk_hospitalInfo() {
        return nsk_hospitalInfo;
    }

    public void setNsk_hospitalInfo(List<NskHospitalInfoBean> nsk_hospitalInfo) {
        this.nsk_hospitalInfo = nsk_hospitalInfo;
    }

    public static class NskHospitalInfoBean implements Serializable {
        /**
         * n_hospital_id : 13179
         * n_hospital_name : 华山医院
         * n_treatment : 12
         * n_ranking : 1
         * n_hospital_logo : http://117.169.68.148:8099/upload/81503e6c-11b0-48b6-9b2e-a8fad86cedca.jpg
         */

        private int n_hospital_id;
        private String n_hospital_name;
        private String n_treatment;
        private int n_ranking;
        private String n_hospital_logo;

        public int getN_hospital_id() {
            return n_hospital_id;
        }

        public void setN_hospital_id(int n_hospital_id) {
            this.n_hospital_id = n_hospital_id;
        }

        public String getN_hospital_name() {
            return n_hospital_name;
        }

        public void setN_hospital_name(String n_hospital_name) {
            this.n_hospital_name = n_hospital_name;
        }

        public String getN_treatment() {
            return n_treatment;
        }

        public void setN_treatment(String n_treatment) {
            this.n_treatment = n_treatment;
        }

        public int getN_ranking() {
            return n_ranking;
        }

        public void setN_ranking(int n_ranking) {
            this.n_ranking = n_ranking;
        }

        public String getN_hospital_logo() {
            return n_hospital_logo;
        }

        public void setN_hospital_logo(String n_hospital_logo) {
            this.n_hospital_logo = n_hospital_logo;
        }
    }
}
