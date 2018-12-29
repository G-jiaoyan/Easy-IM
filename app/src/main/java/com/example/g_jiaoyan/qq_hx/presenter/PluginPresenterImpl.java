package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.adapter.EMCallBackAdapter;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.fragment.PluginView;
import com.hyphenate.chat.EMClient;

public class PluginPresenterImpl implements PluginPresenter {
    private PluginView mPluginView;

    public PluginPresenterImpl(PluginView pluginView) {
        mPluginView = pluginView;
    }

    @Override
    public void logout() {
        //true 解绑推送
        EMClient.getInstance().logout(true,new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                //退出到登录界面
                gotoLogin(true,null);
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                //退出失败
                gotoLogin(false,s);
            }
        });
    }

    private void gotoLogin(final boolean success, final String msg) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                //退出
                mPluginView.afterLogout(success,msg);
            }
        });
    }
}
