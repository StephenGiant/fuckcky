package com.nsk.cky.ckylibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 信用卡列表的item
 * Company    :
 * Author     : gene
 * Date       : 2018/8/23
 */
public class BankCardBean implements Serializable{


    private List<NskHostBankListBean> nsk_host_bank_list;
    private List<NskTopicListBean> nsk_topic_list;
    private List<NskHotCreditListBean> nsk_hot_credit_list;

    public List<NskHostBankListBean> getNsk_host_bank_list() {

        return nsk_host_bank_list;
    }

    public void setNsk_host_bank_list(List<NskHostBankListBean> nsk_host_bank_list) {
        this.nsk_host_bank_list = nsk_host_bank_list;
    }

    public List<NskTopicListBean> getNsk_topic_list() {
        return nsk_topic_list;
    }

    public void setNsk_topic_list(List<NskTopicListBean> nsk_topic_list) {
        this.nsk_topic_list = nsk_topic_list;
    }

    public List<NskHotCreditListBean> getNsk_hot_credit_list() {
        return nsk_hot_credit_list;
    }

    public void setNsk_hot_credit_list(List<NskHotCreditListBean> nsk_hot_credit_list) {
        this.nsk_hot_credit_list = nsk_hot_credit_list;
    }

    public static class NskHostBankListBean implements Serializable{
        /**
         * n_bank_id : 47
         * n_bank_name : 浦发银行
         * n_bank_logo : http://117.169.68.148:8099/upload/1330d74f-6707-4db6-b6eb-70e8c32b407e.png
         */

        private int n_bank_id;
        private String n_bank_name;
        private String n_bank_logo;

        public int getN_bank_id() {
            return n_bank_id;
        }

        public void setN_bank_id(int n_bank_id) {
            this.n_bank_id = n_bank_id;
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

    public static class NskTopicListBean  implements Serializable{
        /**
         * n_topic_id : 100
         * n_topic_logo : http://117.169.68.148:8086/upload/topic_1_20180727162304.png
         * n_topic_property : n_loan_limit
         * n_create_time : 2018-07-27 00:00:00
         */

        private int n_topic_id;
        private String n_topic_logo;
        private String n_topic_property;
        private String n_create_time;

        public int getN_topic_id() {
            return n_topic_id;
        }

        public void setN_topic_id(int n_topic_id) {
            this.n_topic_id = n_topic_id;
        }

        public String getN_topic_logo() {
            return n_topic_logo;
        }

        public void setN_topic_logo(String n_topic_logo) {
            this.n_topic_logo = n_topic_logo;
        }

        public String getN_topic_property() {
            return n_topic_property;
        }

        public void setN_topic_property(String n_topic_property) {
            this.n_topic_property = n_topic_property;
        }

        public String getN_create_time() {
            return n_create_time;
        }

        public void setN_create_time(String n_create_time) {
            this.n_create_time = n_create_time;
        }
    }

    public static class NskHotCreditListBean  implements Serializable{
        /**
         * nsk_inner_credit_card : {"n_loan_id":100000,"n_loan_logo_url":"http://117.169.68.148:8086/upload/c_2017342342.jpg","n_loan_title":"周五加油享受10%优惠","n_loan_subheading":"subTitle","n_loan_bankid":1,"n_loan_createtime":"0001-01-01 00:00:00","n_credit_card_details":null}
         * n_load_preferential_rights : ["周三、周四消费半价","线下消费双倍积分","刷卡免年费"]
         * n_credit_card_tags : ["境外消费","刷卡免年费"]
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

        public static class NskInnerCreditCardBean  implements Serializable{
            /**
             * n_loan_id : 100000
             * n_loan_logo_url : http://117.169.68.148:8086/upload/c_2017342342.jpg
             * n_loan_title : 周五加油享受10%优惠
             * n_loan_subheading : subTitle
             * n_loan_bankid : 1
             * n_loan_createtime : 0001-01-01 00:00:00
             * n_credit_card_details : null
             */

            private int n_loan_id;
            private String n_loan_logo_url;
            private String n_loan_title;
            private String n_loan_subheading;
            private int n_loan_bankid;
            private String n_loan_createtime;
            private String n_credit_card_details;

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
}
