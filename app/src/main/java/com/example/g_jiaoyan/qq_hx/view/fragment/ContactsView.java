package com.example.g_jiaoyan.qq_hx.view.fragment;

import java.util.List;
//$定义联系人界面的实现的方法
public interface ContactsView {
    //显示联系人
    void showContacts(List<String> contacts);

    //更新联系人
    void updateContacts(boolean success);

    //删除好友后的实现
    void afterContact(boolean isSuccess,String username,String msg);
}
