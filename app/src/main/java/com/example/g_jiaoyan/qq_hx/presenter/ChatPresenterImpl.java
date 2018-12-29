package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.adapter.EMCallBackAdapter;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.fragment.ChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

//$实现聊天界面的逻辑
public class ChatPresenterImpl implements ChatPresenter{
    private ChatView mChatView;
    private List<EMMessage> eMMessagesList = new ArrayList<>();

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
    }

    //发送消息逻辑
    @Override
    public void sendMessage(final EMMessage emMessage) {
        //先添加消息到eMMessagesList中
        emMessage.setStatus(EMMessage.Status.INPROGRESS);//正在发送
        eMMessagesList.add(emMessage);
        //然后让adapter notify一下
        mChatView.updateChatData();
        //发送时监听消息的发送状态  正在发生 成功 失败
        emMessage.setMessageStatusCallback(new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                afterSend(true,null,emMessage);

            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                afterSend(false,s,emMessage);
            }
        });
        //通过环信sdk发送出去
        EMClient.getInstance().chatManager().sendMessage(emMessage);

    }

    //重新让adapter notify一下
    private void afterSend(boolean success,String msg,EMMessage emMessage){
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mChatView.updateChatData();
            }
        });
    }

    //初始化历史消息逻辑
    @Override
    public void initChatData(String username,boolean isSmooth) {
        //获取当前好友的会话消息
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation == null){
            eMMessagesList.clear();
            mChatView.afterInitData(eMMessagesList,false);
        }else{
            //消息以显示 说明户读取了数据 用设置消息为已读
            conversation.markAllMessagesAsRead();

            //统计数据库中和当前好友的所有聊天消息个数
            int allMsgCount = conversation.getAllMsgCount();
            //获取最新的一条消息
            EMMessage lastMessage = conversation.getLastMessage();

            allMsgCount = allMsgCount>19?19:allMsgCount;

            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), 19);
            eMMessagesList.clear();

            eMMessagesList.addAll(emMessages);
            eMMessagesList.add(lastMessage);
            mChatView.afterInitData(eMMessagesList,true);

        }

    }
}
