package com.example.g_jiaoyan.qq_hx.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.presenter.RegisterPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.RegisterPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//$注册界面的实现

/**
 * 内存溢出分两种 栈溢出和堆溢出
 *
 * 栈溢出抛出java.lang.StackOverflowError错误
 * 栈溢出就是自己调用自己方法 例如，通过递归调用方法,不停的产生栈帧,一直把栈空间堆满,直到抛出异常
 */
public class RegisterActivity extends BaseActivity implements RegisterView, TextView.OnEditorActionListener {

    @BindView(R.id.et_username_register)
    EditText etUsernameRegister;
    @BindView(R.id.et_pwd_register)
    EditText etPwdRegister;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mRegisterPresenter = new RegisterPresenterImpl(this);

        //设置文本输入键盘的监听事件
        etPwdRegister.setOnEditorActionListener(this);
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        regist();
    }

    //文本输入键盘的监听事件
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //判断点击的是哪个按钮 此为输入键盘右下角的键的事件类似于'搜素''登录''注册'一类 则注册
        if(actionId == EditorInfo.IME_ACTION_GO){
            regist();
            return true;
        }

        //返回true自己把事件消耗掉了 false点击后不处理事件 让键盘自己处理
        return false;
    }

    //注册的实现实现
    private void regist() {
        String username = etUsernameRegister.getText().toString().trim();
        String pwd = etPwdRegister.getText().toString().trim();
        //非空判断 不为空则注册
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)){
            showToast("用户名或密码不能为空");
        }else{
            mRegisterPresenter.register(username,pwd);
        }
    }

    //显示进度条对话框
    @Override
    public void showProgressDialog(String msg) {
        //不取消
        showDialog(msg,false);
    }

    //隐藏进度条对话框
    @Override
    public void hideProgressDialog() {
        hideDialog();
    }

    //用户是否注册成功
    @Override
    public void afterRegist(User user, boolean success,String msg) {
        if(success){
            //注册成功
            //保存数据到本地
            saveUser(user);
            //跳转到登录界面
            staticActivity(LoginActivity.class,true);
        }else{
            //注册失败
            showToast("注册失败 "+msg);
        }
    }
}
