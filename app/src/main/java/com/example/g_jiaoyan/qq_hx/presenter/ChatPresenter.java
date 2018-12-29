package com.example.g_jiaoyan.qq_hx.presenter;

import com.hyphenate.chat.EMMessage;
//$定义聊天界面的逻辑
public interface ChatPresenter {
    void sendMessage(EMMessage emMessage);

    //初始化加载数据
    void initChatData(String username,boolean isSmooth);
}
