package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.view.fragment.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
//$实现聊天界面的逻辑
public class ConversationPresenterImpl implements ConversationPresenter {
    private List<EMConversation> mEMConversations = new ArrayList<>();
    private ConversationView mConversationView;

    public ConversationPresenterImpl(ConversationView conversationView) {
        mConversationView = conversationView;
    }

    //初始化
    @Override
    public void initConversation() {
        //获取所有的会话
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        mEMConversations.clear();
        mEMConversations.addAll(allConversations.values());
        //排序 最新的消息放在最上面
        Collections.sort(mEMConversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation emConversation, EMConversation t1) {
                return (int) (t1.getLastMessage().getMsgTime() - emConversation.getLastMessage().getMsgTime());
            }
        });
        //排序好的集合传过去
        mConversationView.initData(mEMConversations);

    }
}
