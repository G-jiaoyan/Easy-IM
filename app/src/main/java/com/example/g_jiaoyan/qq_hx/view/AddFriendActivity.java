package com.example.g_jiaoyan.qq_hx.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.adapter.SearchAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.presenter.AddFriendPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.AddFriendPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//$添加好友界面的实现
//接口可多实现 最多可实现6535个
public class AddFriendActivity extends BaseActivity implements AddFriendView , TextView.OnEditorActionListener {

    @BindView(R.id.iv_left_header)
    ImageView ivLeftHeader;
    @BindView(R.id.tv_title_heater)
    TextView tvTitleHeater;
    @BindView(R.id.iv_right_header)
    ImageView ivRightHeader;
    @BindView(R.id.et_username_addFriend)
    EditText etUsernameAddFriend;
    @BindView(R.id.iv_search_addFriend)
    ImageView ivSearchAddFriend;
    @BindView(R.id.iv_nodata_addFriend)
    ImageView ivNodataAddFriend;
    @BindView(R.id.recyclerView_addFriend)
    RecyclerView recyclerViewAddFriend;

    private AddFriendPresenter mAddFriendPresenter;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        tvTitleHeater.setText("搜索");
        recyclerViewAddFriend.setVisibility(View.GONE);
        etUsernameAddFriend.setOnEditorActionListener(this);

        mAddFriendPresenter = new AddFriendPresenterImpl(this);

        //初始化输入法管理器
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.iv_search_addFriend)
    public void onViewClicked() {
        //点击搜索图片搜索好友
        search();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            //点击action键搜索好友
            search();
            return true;
        }
        return false;
    }

    //搜索好友
    private void search() {
        String username = etUsernameAddFriend.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            showToast("搜索内容为空!");
            return;
        }
        mAddFriendPresenter.searchFriend(username);
        //隐藏输入法
        //输入法是否被激活也就是正在输入
        if (mInputMethodManager.isActive()) {
            //切换输入法的隐藏状态
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    public void afterSearch(List<User> users, List<String> contacts, boolean isSuccess) {
        if(isSuccess){
            ivNodataAddFriend.setVisibility(View.GONE);
            recyclerViewAddFriend.setVisibility(View.VISIBLE);
            //搜索成功
            recyclerViewAddFriend.setLayoutManager(new LinearLayoutManager(this));
            SearchAdapter adapter = new SearchAdapter(users, contacts);
            //定义一个适配器的监听器 添加好友实现
            adapter.setOnAddFriendClickListener(new SearchAdapter.onAddFriendClickListener() {
                @Override
                public void onClick(String username) {
                    mAddFriendPresenter.addContact(username);
                }
            });

            recyclerViewAddFriend.setAdapter(adapter);
        }else{
            //搜索失败
            ivNodataAddFriend.setVisibility(View.VISIBLE);
            recyclerViewAddFriend.setVisibility(View.GONE);
            showToast("搜索失败!");
        }
    }

    //添加好友后的回调
    @Override
    public void afterAddContact(boolean success, String msg, String username) {
        if(success){
            showToast("添加"+username+"请求发送成功");
        }else{
            showToast("添加"+username+"请求发送失败"+msg);
        }
    }
}
