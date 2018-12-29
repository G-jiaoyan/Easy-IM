package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.view.SplashView;
import com.hyphenate.chat.EMClient;
//$实现开始界面的逻辑
//业务类判断是否登录 回调activity的功能
public class SplashPresenterImpl implements SplashPresenter {
    //p调用view里面的接口
    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        this.mSplashView = splashView;
    }

    @Override
    //判断之前是否已登录
    public void isLogined() {
        //isLoggedInBefore()判断之前是否登录过 如果登录过就进入主界面 没有登录就进入登录界面
        if(EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()){
            //已经登录了
            //在此体现了多态 mSplashView在这里已是activity
            mSplashView.checkLogined(true);
        }else{
            //没有登录
            mSplashView.checkLogined(false);
        }

    }
}
