package com.example.g_jiaoyan.qq_hx.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.g_jiaoyan.qq_hx.MainActivity;
import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.presenter.SplashPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.SplashPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
//mvp 主要面向接口 功能放在接口里面 用类再去实现
//$开始界面的实现
public class SplashActivity extends BaseActivity implements SplashView{
    //常量
    private static final long DURATION = 2000;

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    //可使用p程
    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        ButterKnife.bind(this);
        //此程序启动新建一个p层 再p层中又调用了此activity的方法 此为回调
        mSplashPresenter = new SplashPresenterImpl(this);
        //判断之前是否已登录 如果登录了 直接进入主界面 如果没登录 闪两秒进入登录界面
        mSplashPresenter.isLogined();
    }

    @Override
    public void checkLogined(boolean isLogined) {
        if(isLogined){
            //跳转到主界面
            staticActivity(MainActivity.class,true);
        }else{
            //闪两秒跳转到登录界面
            ObjectAnimator.ofFloat(ivSplash,"alpha",0,1)
                    .setDuration(DURATION).start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    staticActivity(LoginActivity.class,true);
                }
            },DURATION);
        }
    }
}
