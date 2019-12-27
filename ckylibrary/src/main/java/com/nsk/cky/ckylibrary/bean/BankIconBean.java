package com.nsk.cky.ckylibrary.bean;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/26
 */
public class BankIconBean {
    public BankIconBean(int id,String name, String iconRes) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
    }

    public int id;
    public String name;
    public String iconRes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconRes() {
        return iconRes;
    }

    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }
}
