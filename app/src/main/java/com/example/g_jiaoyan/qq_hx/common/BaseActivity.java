package com.example.g_jiaoyan.qq_hx.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.g_jiaoyan.qq_hx.QQApplication;
import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.utils.ToastUtils;
//$抽取出来的activity 封装成父类 后面所有的activity继承此类
//用的多的代码提取出来封装成方法
public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private QQApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进度条对话框提取
        mProgressDialog = new ProgressDialog(this);
        //私有
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        //继承的每个activity都会走这个方法 都会添加进集合
        mApplication = (QQApplication) getApplication();
        mApplication.addActivity(this);
    }

    //保存数据到本地
    public void saveUser(User user){
        mSharedPreferences.edit()
                .putString("username",user.getUsername())
                .putString("password",user.getPassword())
                .commit();
    }

    //获取用户
    public User getUser(){
        String username = mSharedPreferences.getString("username","");
        String password = mSharedPreferences.getString("password","");
        User user = new User(username,password);
        return user;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时关闭进度条对话框
        mProgressDialog.dismiss();

        mApplication.removeActivity(this);
    }

    //显示进度条对话框
    public void showDialog(String msg,boolean isCancelable){
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    //隐藏进度条对话框
    public void hideDialog(){
        if(mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    public void staticActivity(Class clazz, boolean isFinish){
        startActivity(new Intent(this,clazz));
        if(isFinish){
            finish();
        }
    }

    //封装成方法 直接在此调用 更加方便
    public void  showToast(String msg){
        ToastUtils.showToast(this,msg);
    }
}
