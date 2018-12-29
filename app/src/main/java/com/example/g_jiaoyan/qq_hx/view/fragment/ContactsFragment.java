package com.example.g_jiaoyan.qq_hx.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.MainActivity;
import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.adapter.ContactAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseFragment;
import com.example.g_jiaoyan.qq_hx.event.ContactUpdateEvent;
import com.example.g_jiaoyan.qq_hx.presenter.ContactsPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.ContactsPresenterImpl;
import com.example.g_jiaoyan.qq_hx.utils.ToastUtils;
import com.example.g_jiaoyan.qq_hx.view.AddFriendActivity;
import com.example.g_jiaoyan.qq_hx.widget.ContactListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 * $联系人界面的实现
 */
public class ContactsFragment extends BaseFragment implements ContactsView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ContactAdapter.OnItemClickListener {

    @BindView(R.id.iv_left_header)
    ImageView ivLeftHeader;
    @BindView(R.id.tv_title_heater)
    TextView tvTitleHeater;
    @BindView(R.id.iv_right_header)
    ImageView ivRightHeader;
    @BindView(R.id.contactListView)
    ContactListView contactListView;

    private ContactsPresenter mContactsPresenter;
    private ContactAdapter mAdapter;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);
        //p层调用
        mContactsPresenter = new ContactsPresenterImpl(this);

        //显示右边图片添加好友
        ivRightHeader.setVisibility(View.VISIBLE);
        ivRightHeader.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //添加刷新监听
        contactListView.setOnRefreshListener(this);
        //切换到此界面时 初始化联系人
        mContactsPresenter.initContacts();

        //订阅事件
        EventBus.getDefault().register(this);
    }

    //声明为主线程
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactUpdateEvent contactUpdateEvent){
        //更新通讯录
        mContactsPresenter.initContacts();
    }

    //更改标题文本
    @Override
    protected void initTitle(TextView tv_title) {
        tv_title.setText("联系人");
    }

    //显示好友列表
    @Override
    public void showContacts(List<String> contacts) {
        mAdapter = new ContactAdapter(contacts);
        //设置适配器给recyclerView
        contactListView.setAdapter(mAdapter);

        //点击事件和长按事件实现
        mAdapter.setOnItemClickListener(this);
    }

    //更新联系人 根据传回来的值判断是否更新成功
    @Override
    public void updateContacts(boolean success) {
        //停止刷新状态
        contactListView.setRefreshing(false);
        if (success) {
            //如果更新成功 则刷新列表
            mAdapter.notifyDataSetChanged();
        } else {
            //更新数据失败 吐司提示
            ToastUtils.showToast(getActivity(), "通讯录同步失败");
        }
    }

    //刷新时更新数据
    @Override
    public void onRefresh() {
        //更新数据 网络缓存更新
        mContactsPresenter.updateContact();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //取消订阅事件
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_right_header:
                //点击跳转到添加好友界面
                MainActivity activity = (MainActivity) getActivity();
                activity.staticActivity(AddFriendActivity.class,false);
                break;
        }
    }

    //adapter单个条目的点击事件
    @Override
    public void onItemClick(String username) {
        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        mainActivity.startActivity(intent);
    }
    //adapter单个条目的长按事件
    @Override
    public void onItemLongClick(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(username + "真的要失去你了吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContactsPresenter.deleteContact(username);
            }
        });
        builder.setNegativeButton("留ta一命", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    //删除好友后的实现
    @Override
    public void afterContact(boolean isSuccess, String username, String msg) {
        if(isSuccess){
            ToastUtils.showToast(getActivity(),"你失去了"+username);
        }else{
            ToastUtils.showToast(getActivity(),"ta不想失去你,删除失败:"+msg);
        }
    }
}
