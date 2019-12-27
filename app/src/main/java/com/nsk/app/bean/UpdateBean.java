package com.nsk.app.bean;

/**
 * Description:
 * Company    :
 *
 * @author :
 * @date : 2018/9/26
 */
public class UpdateBean {

    /**
     * n_index_id : 0
     * n_version_no : 1.0.0.1
     * n_andriod_download_url : http://mtapi.aicky.cn/h5/app/cky_v1.0.apk
     * n_ios_download_url : null
     * n_upgrade_content : 版本升级1.1
     1.适配更多机型分辨率
     2.优化红包发放流程
     3.优化体现流程
     * n_enable : 1
     * n_createtime : 2018-09-19 16:20:00
     */

    private int n_index_id;
    private String n_version_no;
    private String n_andriod_download_url;
    private Object n_ios_download_url;
    private String n_upgrade_content;
    private int n_enable;
    private String n_createtime;

    public int getN_index_id() {
        return n_index_id;
    }

    public void setN_index_id(int n_index_id) {
        this.n_index_id = n_index_id;
    }

    public String getN_version_no() {
        return n_version_no;
    }

    public void setN_version_no(String n_version_no) {
        this.n_version_no = n_version_no;
    }

    public String getN_andriod_download_url() {
        return n_andriod_download_url;
    }

    public void setN_andriod_download_url(String n_andriod_download_url) {
        this.n_andriod_download_url = n_andriod_download_url;
    }

    public Object getN_ios_download_url() {
        return n_ios_download_url;
    }

    public void setN_ios_download_url(Object n_ios_download_url) {
        this.n_ios_download_url = n_ios_download_url;
    }

    public String getN_upgrade_content() {
        return n_upgrade_content;
    }

    public void setN_upgrade_content(String n_upgrade_content) {
        this.n_upgrade_content = n_upgrade_content;
    }

    public int getN_enable() {
        return n_enable;
    }

    public void setN_enable(int n_enable) {
        this.n_enable = n_enable;
    }

    public String getN_createtime() {
        return n_createtime;
    }

    public void setN_createtime(String n_createtime) {
        this.n_createtime = n_createtime;
    }
}
