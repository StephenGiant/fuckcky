package com.nsk.app.bean;

import java.util.List;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/28
 */
public class CardDetailBean {

    /**
     * nsk_inner_credit_card : {"n_loan_id":0,"n_loan_logo_url":"string","n_loan_title":"string","n_loan_subheading":"string","n_loan_bankid":0,"n_loan_createtime":"2018-08-27T15:56:22.849Z","n_credit_card_details":"string"}
     * n_load_preferential_rights : ["string"]
     * n_credit_card_tags : ["string"]
     * n_credit_card_Sowing_pictures : ["string"]
     */

    private NskInnerCreditCardBean nsk_inner_credit_card;
    private List<String> n_load_preferential_rights;
    private List<String> n_credit_card_tags;
    private List<String> n_credit_card_Sowing_pictures;

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

    public List<String> getN_credit_card_Sowing_pictures() {
        return n_credit_card_Sowing_pictures;
    }

    public void setN_credit_card_Sowing_pictures(List<String> n_credit_card_Sowing_pictures) {
        this.n_credit_card_Sowing_pictures = n_credit_card_Sowing_pictures;
    }

    public static class NskInnerCreditCardBean {
        /**
         * n_loan_id : 0
         * n_loan_logo_url : string
         * n_loan_title : string
         * n_loan_subheading : string
         * n_loan_bankid : 0
         * n_loan_createtime : 2018-08-27T15:56:22.849Z
         * n_credit_card_details : string
         */

        private int n_loan_id;
        private String n_loan_logo_url;
        private String n_loan_title;
        private String n_loan_subheading;
        private int n_loan_bankid;
        private String n_loan_createtime;
        private String n_credit_card_details;
        private String n_loan_url;

        public String getN_loan_url() {
            return n_loan_url == null ? "" : n_loan_url;
        }

        public void setN_loan_url(String n_loan_url) {
            this.n_loan_url = n_loan_url;
        }

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

        public String getN_credit_card_details() {
            return n_credit_card_details;
        }

        public void setN_credit_card_details(String n_credit_card_details) {
            this.n_credit_card_details = n_credit_card_details;
        }
    }
}
