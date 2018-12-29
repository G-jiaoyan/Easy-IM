package com.example.g_jiaoyan.qq_hx.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.adapter.ChatAdapter;
import com.example.g_jiaoyan.qq_hx.adapter.TextWatcherAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.presenter.ChatPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.ChatPresenterImpl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//$聊天界面的实现
public class ChatActivity extends BaseActivity implements ChatView,TextView.OnEditorActionListener {

    @BindView(R.id.iv_left_header)
    ImageView ivLeftHeader;
    @BindView(R.id.tv_title_heater)
    TextView tvTitleHeater;
    @BindView(R.id.iv_right_header)
    ImageView ivRightHeader;
    @BindView(R.id.recycler_chat)
    RecyclerView recyclerChat;
    @BindView(R.id.et_msg_chat)
    EditText etMsgChat;
    @BindView(R.id.btn_send_chat)
    Button btnSendChat;

    private ChatPresenter mChatPresenter;
    private String mUsername;
    private ChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");
        if(TextUtils.isEmpty(mUsername)){
            finish();
            showToast("聊天失败");
            return;
        }
        ivLeftHeader.setVisibility(View.VISIBLE);
        tvTitleHeater.setText(mUsername);

        mChatPresenter = new ChatPresenterImpl(this);

        //绑定键盘的点击事件
        etMsgChat.setOnEditorActionListener(this);
        //添加文本监听
        etMsgChat.addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if(editable.toString().trim().length() == 0){
                    btnSendChat.setEnabled(false);
                }else{
                    btnSendChat.setEnabled(true);
                }
            }
        });

        //初始化recyclerView 显示历史聊天记录最多20条
        mChatPresenter.initChatData(mUsername,false);

        //订阅
        EventBus.getDefault().register(this);

    }

    //更新消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //判断当前接收到的消息是否是发送给当前聊天对象的
        if(mUsername.equals(emMessage.getUserName())){
            //为当前聊天好友发送的消息 更新当前recyclerView
            mChatPresenter.initChatData(mUsername,true);

        }
    }

    @OnClick({R.id.iv_left_header, R.id.btn_send_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left_header:
                finish();
                break;
            case R.id.btn_send_chat:
                sendMsg();
                break;
        }
    }

    //发送消息实现
    private void sendMsg() {
        String msg = etMsgChat.getText().toString().trim();
        if(TextUtils.isEmpty(msg)){
            showToast("消息不能为空!");
            return;
        }
        //发送后清空文本
        etMsgChat.getText().clear();
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg,mUsername);
        mChatPresenter.sendMessage(emMessage);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_SEND){
            sendMsg();
            return true;
        }
        return false;
    }

    //接收到好友历史消息
    @Override
    public void afterInitData(List<EMMessage> eMMessagesList,boolean isSmooth) {
        if (mAdapter == null) {
            recyclerChat.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new ChatAdapter(eMMessagesList);
            recyclerChat.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
        if (isSmooth) {
            recyclerChat.smoothScrollToPosition(eMMessagesList.size() - 1);
        }else {
            recyclerChat.scrollToPosition(eMMessagesList.size() - 1);
        }

    }

    //刷新界面
    @Override
    public void updateChatData() {
        mAdapter.notifyDataSetChanged();
        recyclerChat.smoothScrollToPosition(Integer.MAX_VALUE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
