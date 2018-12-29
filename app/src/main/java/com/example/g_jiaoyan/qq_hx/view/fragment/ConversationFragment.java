package com.example.g_jiaoyan.qq_hx.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.adapter.ConversationAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseFragment;
import com.example.g_jiaoyan.qq_hx.presenter.ConversationPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.ConversationPresenterImpl;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * $消息界面的实现
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.onConversationItemClickListener {

    @BindView(R.id.iv_left_header)
    ImageView ivLeftHeader;
    @BindView(R.id.tv_title_heater)
    TextView tvTitleHeater;
    @BindView(R.id.iv_right_header)
    ImageView ivRightHeader;
    @BindView(R.id.recycler_conversation)
    RecyclerView recyclerConversation;
    Unbinder unbinder;

    private ConversationPresenter mConversationPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        unbinder = ButterKnife.bind(this, view);
        mConversationPresenter = new ConversationPresenterImpl(this);

        //订阅事件
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mConversationPresenter.initConversation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //接收到消息初始化
        mConversationPresenter.initConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        //接收到消息初始化
        mConversationPresenter.initConversation();
    }

    @Override
    protected void initTitle(TextView tv_title) {
        tv_title.setText("消息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    //接收到传过来的数据 显示在recyclerView上
    @Override
    public void initData(List<EMConversation> emConversations) {
        recyclerConversation.setLayoutManager(new LinearLayoutManager(getActivity()));
        ConversationAdapter adapter = new ConversationAdapter(emConversations);
        adapter.setConversationAdapter(this);
        recyclerConversation.setAdapter(adapter);

    }

    //recyclerView单个条目点击事件
    @Override
    public void onItemClick(String username) {
        //跳转到聊天界面
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
