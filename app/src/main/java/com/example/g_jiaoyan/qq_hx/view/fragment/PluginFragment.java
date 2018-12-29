package com.example.g_jiaoyan.qq_hx.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.MainActivity;
import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.common.BaseFragment;
import com.example.g_jiaoyan.qq_hx.presenter.PluginPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.PluginPresenterImpl;
import com.example.g_jiaoyan.qq_hx.view.LoginActivity;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment implements PluginView {


    @BindView(R.id.iv_left_header)
    ImageView ivLeftHeader;
    @BindView(R.id.tv_title_heater)
    TextView tvTitleHeater;
    @BindView(R.id.iv_right_header)
    ImageView ivRightHeader;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    Unbinder unbinder;
    @BindView(R.id.tv_nowUserName_logout)
    TextView tvNowUserNameLogout;

    private PluginPresenter mPluginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        unbinder = ButterKnife.bind(this, view);

        String currentUser = EMClient.getInstance().getCurrentUser();
        tvNowUserNameLogout.setText(currentUser);
        mPluginPresenter = new PluginPresenterImpl(this);

        return view;
    }

    @Override
    protected void initTitle(TextView tv_title) {
        tv_title.setText("动态");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //点击退出
    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        //退出登录逻辑
        mPluginPresenter.logout();
    }

    //退出登录实现
    @Override
    public void afterLogout(boolean success, String msg) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (success) {
            //退出成功到主界面
            mainActivity.staticActivity(LoginActivity.class, true);
        } else {
            //退出失败
            mainActivity.showToast("退出失败:" + msg);
        }
    }
}
