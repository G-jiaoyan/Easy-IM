package com.example.g_jiaoyan.qq_hx.view;

import com.example.g_jiaoyan.qq_hx.model.User;

//$定义注册界面的实现的方法
public interface RegisterView extends BaseView{
    //用户是否注册成功
    void afterRegist(User user,boolean success,String msg);
}
