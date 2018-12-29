package com.example.g_jiaoyan.qq_hx.view.fragment;

import com.hyphenate.chat.EMConversation;

import java.util.List;
//$定义消息界面的实现的方法
public interface ConversationView {
    void initData(List<EMConversation> emConversations);
}
