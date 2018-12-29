package com.example.g_jiaoyan.qq_hx.view;

import com.example.g_jiaoyan.qq_hx.model.User;

//$定义登录界面的实现的方法
public interface LoginView extends BaseView{
    void  afterLogin(User user,boolean isSuccess,String msg);
}
