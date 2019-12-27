package com.nsk.cky.ckylibrary.bean;

import java.util.List;

/**
 * Description:
 * Company    :
 *
 * @author :
 * @date : 2018/9/11
 */
public class HotCardBean {

    /**
     * nsk_inner_credit_card : {"n_loan_id":100009,"n_loan_logo_url":"http://117.169.68.148:8099/upload/580e3c8d-85e6-48f2-8dbd-0d63f1569430.png","n_loan_title":"平安曼联红魔白金卡","n_loan_subheading":"球星卡面限时优免","n_loan_bankid":48,"n_loan_createtime":"2018-08-28 17:05:52","n_credit_card_details":null,"n_loan_url":"http://t.cn/RBrIYgF"}
     * n_load_preferential_rights : ["银联卡：每月4笔288集印花兑曼联壕礼","银联卡：非职业运动意外保障/综合交通意外保障","VISA卡：境外单笔消费满$50返￥50","VISA卡：0货币兑换手续费/境外旅行意外保障推荐办卡赢限量签名球衣球票等"]
     * n_credit_card_tags : ["白金卡","境外消费"]
     */

    private NskInnerCreditCardBean nsk_inner_credit_card;
    private List<String> n_load_preferential_rights;
    private List<String> n_credit_card_tags;

    public NskInnerCreditCardBean getNsk_inner_credit_card() {
        return nsk_inner_credit_card;
    }

    public void setNsk_inner_credit_card(NskInnerCreditCardBean nsk_inner_credit_card) {
        this.nsk_inner_credit_card = nsk_inner_credit_card;
    }

    public List<String> getN_load_preferential_rights() {
        return n_load_preferential_rights;
    }

    public void setN_load_preferential_rights(List<String> n_load_preferential_rights) {
        this.n_load_preferential_rights = n_load_preferential_rights;
    }

    public List<String> getN_credit_card_tags() {
        return n_credit_card_tags;
    }

    public void setN_credit_card_tags(List<String> n_credit_card_tags) {
        this.n_credit_card_tags = n_credit_card_tags;
    }

    public static class NskInnerCreditCardBean {
        /**
         * n_loan_id : 100009
         * n_loan_logo_url : http://117.169.68.148:8099/upload/580e3c8d-85e6-48f2-8dbd-0d63f1569430.png
         * n_loan_title : 平安曼联红魔白金卡
         * n_loan_subheading : 球星卡面限时优免
         * n_loan_bankid : 48
         * n_loan_createtime : 2018-08-28 17:05:52
         * n_credit_card_details : null
         * n_loan_url : http://t.cn/RBrIYgF
         */

        private int n_loan_id;
        private String n_loan_logo_url;
        private String n_loan_title;
        private String n_loan_subheading;
        private int n_loan_bankid;
        private String n_loan_createtime;
        private Object n_credit_card_details;
        private String n_loan_url;

        public int getN_loan_id() {
            return n_loan_id;
        }

        public void setN_loan_id(int n_loan_id) {
            this.n_loan_id = n_loan_id;
        }

        public String getN_loan_logo_url() {
            return n_loan_logo_url;
        }

        public void setN_loan_logo_url(String n_loan_logo_url) {
            this.n_loan_logo_url = n_loan_logo_url;
        }

        public String getN_loan_title() {
            return n_loan_title;
        }

        public void setN_loan_title(String n_loan_title) {
            this.n_loan_title = n_loan_title;
        }

        public String getN_loan_subheading() {
            return n_loan_subheading;
        }

        public void setN_loan_subheading(String n_loan_subheading) {
            this.n_loan_subheading = n_loan_subheading;
        }

        public int getN_loan_bankid() {
            return n_loan_bankid;
        }

        public void setN_loan_bankid(int n_loan_bankid) {
            this.n_loan_bankid = n_loan_bankid;
        }

        public String getN_loan_createtime() {
            return n_loan_createtime;
        }

        public void setN_loan_createtime(String n_loan_createtime) {
            this.n_loan_createtime = n_loan_createtime;
        }

        public Object getN_credit_card_details() {
            return n_credit_card_details;
        }

        public void setN_credit_card_details(Object n_credit_card_details) {
            this.n_credit_card_details = n_credit_card_details;
        }

        public String getN_loan_url() {
            return n_loan_url;
        }

        public void setN_loan_url(String n_loan_url) {
            this.n_loan_url = n_loan_url;
        }
    }
}
