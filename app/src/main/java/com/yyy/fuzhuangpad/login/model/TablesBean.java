package com.yyy.fuzhuangpad.login.model;

import java.util.List;

public class TablesBean {
    private List<MenuBean> menu;
    private List<PersonBean> person;
    private List<AppInfoBean> APPInfo;

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public List<PersonBean> getPerson() {
        return person;
    }

    public void setPerson(List<PersonBean> person) {
        this.person = person;
    }

    public List<AppInfoBean> getAPPInfo() {
        return APPInfo;
    }
}