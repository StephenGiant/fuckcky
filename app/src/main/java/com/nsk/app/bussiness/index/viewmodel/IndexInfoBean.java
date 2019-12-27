package com.nsk.app.bussiness.index.viewmodel;

import com.nsk.cky.ckylibrary.bean.BankCardBean;

import java.io.Serializable;

/**
 * @author qianpeng
 * @Package com.nsk.app.bussiness.index.viewmodel
 * @date 2018/8/31.
 * @describe
 */
public class IndexInfoBean {



    /**
     * 轮播图信息
     */
    public static class BannerInfo {
        /**
         * n_display_ranking : 1
         * n_section_type : 1
         * n_link_type : 1  //跳转类型
         * n_link_value : https://creditcardapp.bankcomm.com/applynew/front/apply/new/reversion/cardlist.html //跳转的h5链接
         * n_sort_index : 1
         * n_content : http://117.169.68.148:8099/upload/e6ee2411-41c8-4c20-8d3e-9b19d4560b9f.png  //图片地址
         */
        private int n_display_ranking;
        private int n_section_type;
        private int n_link_type;
        private String n_link_value;
        private int n_sort_index;
        private String n_content;

        public int getN_display_ranking() {
            return n_display_ranking;
        }

        public void setN_display_ranking(int n_display_ranking) {
            this.n_display_ranking = n_display_ranking;
        }

        public int getN_section_type() {
            return n_section_type;
        }

        public void setN_section_type(int n_section_type) {
            this.n_section_type = n_section_type;
        }

        public int getN_link_type() {
            return n_link_type;
        }

        public void setN_link_type(int n_link_type) {
            this.n_link_type = n_link_type;
        }

        public String getN_link_value() {
            return n_link_value;
        }

        public void setN_link_value(String n_link_value) {
            this.n_link_value = n_link_value;
        }

        public int getN_sort_index() {
            return n_sort_index;
        }

        public void setN_sort_index(int n_sort_index) {
            this.n_sort_index = n_sort_index;
        }

        public String getN_content() {
            return n_content;
        }

        public void setN_content(String n_content) {
            this.n_content = n_content;
        }
    }

    /**
     * 邀请信息
     */
   public static class InviteInfo{

       /**
        * n_display_ranking : 3
        * n_section_type : 3
        * n_link_type : 1
        * n_link_value : https://creditcardapp.bankcomm.com/applynew/front/apply/new/reversion/cardlist.html //跳转链接
        * n_sort_index : 1
        * n_content : http://117.169.68.148:8099/upload/865bc0c3-feab-47e8-adc2-8b2a7f1533d0.png //图片链接
        */

       private int n_display_ranking;
       private int n_section_type;
       private int n_link_type;
       private String n_link_value;
       private int n_sort_index;
       private String n_content;

       public int getN_display_ranking() {
           return n_display_ranking;
       }

       public void setN_display_ranking(int n_display_ranking) {
           this.n_display_ranking = n_display_ranking;
       }

       public int getN_section_type() {
           return n_section_type;
       }

       public void setN_section_type(int n_section_type) {
           this.n_section_type = n_section_type;
       }

       public int getN_link_type() {
           return n_link_type;
       }

       public void setN_link_type(int n_link_type) {
           this.n_link_type = n_link_type;
       }

       public String getN_link_value() {
           return n_link_value;
       }

       public void setN_link_value(String n_link_value) {
           this.n_link_value = n_link_value;
       }

       public int getN_sort_index() {
           return n_sort_index;
       }

       public void setN_sort_index(int n_sort_index) {
           this.n_sort_index = n_sort_index;
       }

       public String getN_content() {
           return n_content;
       }

       public void setN_content(String n_content) {
           this.n_content = n_content;
       }
   }

    /**
     * 广告位信息
     */
   public static class AdInfo{

       /**
        * n_display_ranking : 2
        * n_section_type : 2
        * n_link_type : 1
        * n_link_value : https://creditcardapp.bankcomm.com/applynew/front/apply/new/reversion/cardlist.html
        * n_sort_index : 1
        * n_content : http://117.169.68.148:8099/upload/855c229e-6d42-4f33-bc1d-c73a89cb4301.png
        * notice : null
        */

       private int n_display_ranking;
       private int n_section_type;
       private int n_link_type;
       private String n_link_value;
       private int n_sort_index;
       private String n_content;
       private Object notice;

       public int getN_display_ranking() {
           return n_display_ranking;
       }

       public void setN_display_ranking(int n_display_ranking) {
           this.n_display_ranking = n_display_ranking;
       }

       public int getN_section_type() {
           return n_section_type;
       }

       public void setN_section_type(int n_section_type) {
           this.n_section_type = n_section_type;
       }

       public int getN_link_type() {
           return n_link_type;
       }

       public void setN_link_type(int n_link_type) {
           this.n_link_type = n_link_type;
       }

       public String getN_link_value() {
           return n_link_value;
       }

       public void setN_link_value(String n_link_value) {
           this.n_link_value = n_link_value;
       }

       public int getN_sort_index() {
           return n_sort_index;
       }

       public void setN_sort_index(int n_sort_index) {
           this.n_sort_index = n_sort_index;
       }

       public String getN_content() {
           return n_content;
       }

       public void setN_content(String n_content) {
           this.n_content = n_content;
       }

       public Object getNotice() {
           return notice;
       }

       public void setNotice(Object notice) {
           this.notice = notice;
       }
   }

    /**
     * 贷款位信息
     */
   public static class LoanInfo{

       /**
        * n_display_ranking : 4
        * n_section_type : 4
        * n_link_type : 3
        * n_link_value : 100006
        * n_sort_index : 1
        * n_content :
        * creditCard : null
        * loan : {"n_loan_id":100006,"n_loan_logo":null,"n_loan_title":"民生银行低息贷款","n_loan_subheading":"随便花逾期利率低","n_loan_bankid":2,"n_loan_monthlyinterest":"0.0000","n_loan_yearlyinterest":"0.0000","n_loan_limit":"500000","n_loan_deadline":"180","n_loan_fastest":"1","n_loan_condition1":null,"n_loan_condition2":null,"n_loan_condition3":null,"n_loan_condition4":null,"n_loan_condition5":null,"n_loan_need1":null,"n_loan_need2":null,"n_loan_need3":null,"n_loan_need4":null,"n_loan_need5":null,"n_loan_applycount":0,"n_load_endcount":0,"n_load_viewcount":0,"n_loan_type":2,"n_loan_enable":true,"n_loan_createuserid":"57d99d89-caab-482a-a0e9-a0a803eed3ba","n_loan_createtime":"2018-08-13 17:03:45","n_loan_url":null,"n_loan_memo":null}
        */

       private int n_display_ranking;
       private int n_section_type;
       private int n_link_type;
       private String n_link_value;
       private int n_sort_index;
       private String n_content;
       private BankCardBean.NskHotCreditListBean creditCard;
       private LoanBean loan;

       public int getN_display_ranking() {
           return n_display_ranking;
       }

       public void setN_display_ranking(int n_display_ranking) {
           this.n_display_ranking = n_display_ranking;
       }

       public int getN_section_type() {
           return n_section_type;
       }

       public void setN_section_type(int n_section_type) {
           this.n_section_type = n_section_type;
       }

       public int getN_link_type() {
           return n_link_type;
       }

       public void setN_link_type(int n_link_type) {
           this.n_link_type = n_link_type;
       }

       public String getN_link_value() {
           return n_link_value;
       }

       public void setN_link_value(String n_link_value) {
           this.n_link_value = n_link_value;
       }

       public int getN_sort_index() {
           return n_sort_index;
       }

       public void setN_sort_index(int n_sort_index) {
           this.n_sort_index = n_sort_index;
       }

       public String getN_content() {
           return n_content;
       }

       public void setN_content(String n_content) {
           this.n_content = n_content;
       }

       public BankCardBean.NskHotCreditListBean  getCreditCard() {
           return creditCard;
       }

       public void setCreditCard(BankCardBean.NskHotCreditListBean  creditCard) {
           this.creditCard = creditCard;
       }

       public LoanBean getLoan() {
           return loan;
       }

       public void setLoan(LoanBean loan) {
           this.loan = loan;
       }

       public static class LoanBean implements Serializable {


           /**
            * n_loan_id : 100006
            * n_loan_logo_url : http://117.169.68.148:8099/upload/c889a578-d8d7-48ac-8f20-31d2097e81f3.jpg
            * n_loan_title : 民生银行低息贷款
            * n_loan_subheading : 随便花逾期利率低
            * n_loan_bankid : 2
            * n_loan_monthlyinterest : 0.0000
            * n_loan_yearlyinterest : 0.0000
            * n_loan_dayinterest : 0.2000
            * interest : 日息：20% | 期限180天
            * n_loan_limit : 500000
            * n_loan_deadline : 180
            * n_loan_fastest : 1
            * n_loan_applycount : 0
            * n_load_endcount : 0
            * n_load_viewcount : 6
            * n_loan_type : 2
            * n_loan_enable : true
            * n_loan_url : http://t.cn/ReqWahT
            * n_loan_details : <p>test大额需抵押<img class="loadingclass" id="loading_jlyutln9" src="http://117.169.68.148:8088/scripts/ueditor/themes/default/images/spacer.gif" title="正在上传..."/></p>
            */

           private int n_loan_id;
           private String n_loan_logo_url;
           private String n_loan_title;
           private String n_loan_subheading;
           private int n_loan_bankid;
           private String n_loan_monthlyinterest;
           private String n_loan_yearlyinterest;
           private String n_loan_dayinterest;
           private String interest;
           private int n_loan_limit;
           private String n_loan_deadline;
           private String n_loan_fastest;
           private int n_loan_applycount;
           private int n_load_endcount;
           private int n_load_viewcount;
           private int n_loan_type;
           private boolean n_loan_enable;
           private String n_loan_url;
           private String n_loan_details;

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

           public String getN_loan_monthlyinterest() {
               return n_loan_monthlyinterest;
           }

           public void setN_loan_monthlyinterest(String n_loan_monthlyinterest) {
               this.n_loan_monthlyinterest = n_loan_monthlyinterest;
           }

           public String getN_loan_yearlyinterest() {
               return n_loan_yearlyinterest;
           }

           public void setN_loan_yearlyinterest(String n_loan_yearlyinterest) {
               this.n_loan_yearlyinterest = n_loan_yearlyinterest;
           }

           public String getN_loan_dayinterest() {
               return n_loan_dayinterest;
           }

           public void setN_loan_dayinterest(String n_loan_dayinterest) {
               this.n_loan_dayinterest = n_loan_dayinterest;
           }

           public String getInterest() {
               return interest;
           }

           public void setInterest(String interest) {
               this.interest = interest;
           }

           public String getN_loan_limit() {
               return n_loan_limit+"";
           }

           public void setN_loan_limit(int n_loan_limit) {
               this.n_loan_limit = n_loan_limit;
           }

           public String getN_loan_deadline() {
               return n_loan_deadline;
           }

           public void setN_loan_deadline(String n_loan_deadline) {
               this.n_loan_deadline = n_loan_deadline;
           }

           public String getN_loan_fastest() {
               return n_loan_fastest;
           }

           public void setN_loan_fastest(String n_loan_fastest) {
               this.n_loan_fastest = n_loan_fastest;
           }

           public int getN_loan_applycount() {
               return n_loan_applycount;
           }

           public void setN_loan_applycount(int n_loan_applycount) {
               this.n_loan_applycount = n_loan_applycount;
           }

           public int getN_load_endcount() {
               return n_load_endcount;
           }

           public void setN_load_endcount(int n_load_endcount) {
               this.n_load_endcount = n_load_endcount;
           }

           public int getN_load_viewcount() {
               return n_load_viewcount;
           }

           public void setN_load_viewcount(int n_load_viewcount) {
               this.n_load_viewcount = n_load_viewcount;
           }

           public int getN_loan_type() {
               return n_loan_type;
           }

           public void setN_loan_type(int n_loan_type) {
               this.n_loan_type = n_loan_type;
           }

           public boolean isN_loan_enable() {
               return n_loan_enable;
           }

           public void setN_loan_enable(boolean n_loan_enable) {
               this.n_loan_enable = n_loan_enable;
           }

           public String getN_loan_url() {
               return n_loan_url;
           }

           public void setN_loan_url(String n_loan_url) {
               this.n_loan_url = n_loan_url;
           }

           public String getN_loan_details() {
               return n_loan_details;
           }

           public void setN_loan_details(String n_loan_details) {
               this.n_loan_details = n_loan_details;
           }
       }
   }


   public static class YiliaoInfo{

       /**
        * n_display_ranking : 5
        * n_section_type : 5
        * n_link_type : 4
        * n_link_value :
        * n_sort_index : 1
        * n_content : http://117.169.68.148:8099/upload/67b3fb29-e318-411e-bf7c-713eaee3cc55.png
        */
       private String name;
       private int n_display_ranking;
       private int n_section_type;
       private int n_link_type;
       private String n_link_value;
       private int n_sort_index;
       private String n_content;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public int getN_display_ranking() {
           return n_display_ranking;
       }

       public void setN_display_ranking(int n_display_ranking) {
           this.n_display_ranking = n_display_ranking;
       }

       public int getN_section_type() {
           return n_section_type;
       }

       public void setN_section_type(int n_section_type) {
           this.n_section_type = n_section_type;
       }

       public int getN_link_type() {
           return n_link_type;
       }

       public void setN_link_type(int n_link_type) {
           this.n_link_type = n_link_type;
       }

       public String getN_link_value() {
           return n_link_value;
       }

       public void setN_link_value(String n_link_value) {
           this.n_link_value = n_link_value;
       }

       public int getN_sort_index() {
           return n_sort_index;
       }

       public void setN_sort_index(int n_sort_index) {
           this.n_sort_index = n_sort_index;
       }

       public String getN_content() {
           return n_content;
       }

       public void setN_content(String n_content) {
           this.n_content = n_content;
       }
   }

   public static class CreditBean{
//       "creditCard": {
//           "nsk_inner_credit_card": {
//               "n_loan_id": 100087,
//                       "n_loan_logo_url": "http://117.169.68.148:8099/upload/3b56e665-643d-4013-be88-f33458f7887b.JPG",
//                       "n_loan_title": "中信银行标准车主信用卡白金卡",
//                       "n_loan_subheading": "为你加油，乐享返还",
//                       "n_loan_bankid": 49,
//                       "n_loan_createtime": "2018-09-03 17:40:28",
//                       "n_credit_card_details": null,
//
//
//     "n_loan_url": null
//           }

       private String n_loan_logo_url;
       private String n_loan_title;
       private String n_loan_subheading;
       private String n_loan_createtime;
       private String n_credit_card_details;
       private String n_loan_url;
        private int n_loan_bankid;

       public int getN_loan_bankid() {
           return n_loan_bankid;
       }

       public void setN_loan_bankid(int n_loan_bankid) {
           this.n_loan_bankid = n_loan_bankid;
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

       public String getN_loan_url() {
           return n_loan_url;
       }

       public void setN_loan_url(String n_loan_url) {
           this.n_loan_url = n_loan_url;
       }
   }
}
