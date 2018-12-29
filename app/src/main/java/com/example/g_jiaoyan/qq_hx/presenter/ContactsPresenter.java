package com.example.g_jiaoyan.qq_hx.presenter;
//$定义联系人界面的逻辑
public interface ContactsPresenter {
    //初始化联系人
    void initContacts();
    //更新联系人
    void updateContact();

    void deleteContact(String username);
}
