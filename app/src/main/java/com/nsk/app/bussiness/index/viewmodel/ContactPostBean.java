package com.nsk.app.bussiness.index.viewmodel;

import java.util.List;

/**
 * @author qianpeng
 * @Package com.nsk.app.bussiness.index.viewmodel
 * @date 2018/9/26.
 * @describe
 */
public class ContactPostBean {

    public String getN_owner_mobile() {
        return n_owner_mobile;
    }

    public void setN_owner_mobile(String n_owner_mobile) {
        this.n_owner_mobile = n_owner_mobile;
    }

    private String n_owner_mobile;

    private List<UserAddressBookListBean> userAddressBookList;

    public List<UserAddressBookListBean> getUserAddressBookList() {
        return userAddressBookList;
    }

    public void setUserAddressBookList(List<UserAddressBookListBean> userAddressBookList) {
        this.userAddressBookList = userAddressBookList;
    }

    public static class UserAddressBookListBean {
        /**
         * n_user_name : string
         * n_user_mobile : string
         */

        private String n_user_name;
        private String n_user_mobile;

        public String getN_user_name() {
            return n_user_name;
        }

        public void setN_user_name(String n_user_name) {
            this.n_user_name = n_user_name;
        }

        public String getN_user_mobile() {
            return n_user_mobile;
        }

        public void setN_user_mobile(String n_user_mobile) {
            this.n_user_mobile = n_user_mobile;
        }
    }
}
