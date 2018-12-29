package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.adapter.EMCallBackAdapter;
import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.LoginView;
import com.hyphenate.chat.EMClient;

//$实现登录界面的逻辑
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        //登录 是登录的环信
        //显示/隐藏进度条对话框
        mLoginView.showProgressDialog("正在登录");
        EMClient.getInstance().login(username, pwd, new EMCallBackAdapter() {
            @Override
            public void onSuccess() {
                //从本地数据库加载会话对象到内存中
                EMClient.getInstance().chatManager().loadAllConversations();
                //登录成功
                hideDialog(new User(username,pwd),true,null);
            }

            @Override
            public void onError(int i, String s) {
                //登录失败
                hideDialog(new User(username,pwd),false,s);
            }
        });
    }

    //隐藏和登录信息都在主线程中
    private void hideDialog(final User user, final boolean isSuccess, final String msg) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                //将登录信息回调给view
                mLoginView.afterLogin(user,isSuccess,msg);
                //隐藏进度条对话框
                mLoginView.hideProgressDialog();
            }
        });
    }
}
