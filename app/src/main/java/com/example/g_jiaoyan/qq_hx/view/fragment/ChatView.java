package com.example.g_jiaoyan.qq_hx.view.fragment;

import com.hyphenate.chat.EMMessage;

import java.util.List;

//$定义聊天界面实现的方法
public interface ChatView {
    void afterInitData(List<EMMessage> eMMessagesList,boolean isSmooth);

    void updateChatData();
}
