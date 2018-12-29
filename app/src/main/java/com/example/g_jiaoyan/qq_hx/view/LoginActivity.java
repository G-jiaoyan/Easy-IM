package com.example.g_jiaoyan.qq_hx.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.MainActivity;
import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.presenter.LoginPresenter;
import com.example.g_jiaoyan.qq_hx.presenter.LoginPresenterImpl;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//$登录界面的实现
public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_newuser)
    TextView tvNewuser;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = new LoginPresenterImpl(this);

        //action的点击事件 OnEditorActionListener action被点击的监听
        etPwd.setOnEditorActionListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //数据回显 如果之前登录过数据显示在输入框中
        etUsername.setText(getUser().getUsername());
        etPwd.setText(getUser().getPassword());
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_newuser:
                staticActivity(RegisterActivity.class,false);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            login();
            return true;
        }

        //返回true自己把事件消耗掉了 false点击后不处理事件 让键盘自己处理
        return false;
    }

    //用户信息输入完成后的登录点击
    private void login() {
        //用正则表达式要求 3-20位u首字母必须是字母
        String username = etUsername.getText().toString().trim();
        //3-20位数字
        String pwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            showToast("用户名或密码不能为空");
            return;
        }

        //检查自己是否有权限写入内存
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED){
            //没有被授权 请求用户给予权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return;
        }else{
            //授权了继续登录
            mLoginPresenter.login(username,pwd);
        }

    }

    //获取到权限 执行代码逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                //如果确认用户已经授权了 则登录
                if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PermissionChecker.PERMISSION_GRANTED){
                    login();
                }else{
                    //用户拒绝授权
                    showToast("您已拒绝 无法继续使用");
                }
                break;
        }
    }

    @Override
    public void afterLogin(User user, boolean isSuccess, String msg) {
        if(isSuccess){
            //登录成功
            //保存user
            saveUser(user);
            //跳转到主界面
            staticActivity(MainActivity.class,true);
        }else{
            //弹吐司
            showToast(msg);
        }
    }

    @Override
    public void showProgressDialog(String msg) {
        showDialog(msg,false);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }
}
