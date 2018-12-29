package com.example.g_jiaoyan.qq_hx.view;

import com.example.g_jiaoyan.qq_hx.model.User;

import java.util.List;

//$定义添加好友实现的方法
public interface AddFriendView {
    /**
     *
     * @param users 从bmob服务器上搜索出来的用户
     * @param contacts 当前用户的好友
     * @param isSuccess 搜索是否成功
     */
    void afterSearch(List<User> users, List<String> contacts, boolean isSuccess);

    //添加好友后的回调
    void afterAddContact(boolean success,String msg,String username);
}
