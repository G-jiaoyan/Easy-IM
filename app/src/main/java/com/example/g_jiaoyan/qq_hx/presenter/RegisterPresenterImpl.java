package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.RegisterView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
//$实现注册方法的逻辑
//注册实现  用户注册首先注册到第三方数据库中 然后用户注册到环信服务器
//如果环信注册失败 之前在第三方数据库中注册的就要删除
//如果数据库中注册失败 那么不继续注册环信
public class RegisterPresenterImpl implements RegisterPresenter{
    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        this.mRegisterView = registerView;
    }

    //注册的逻辑
    @Override
    public void register(final String username, final String pwd) {
        //在真正的注册完成之前 显示进度条对话框
        mRegisterView.showProgressDialog("正在注册");
        final User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);

        //bmob添加存储数据
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    //异常为空表明bmob注册成功 接着继续环信注册
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //环信注册
                                EMClient.getInstance().createAccount(username,pwd);
                                //环信注册成功
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //隐藏对话框
                                        mRegisterView.hideProgressDialog();
                                        //两个都注册成功 信息传送给view成层
                                        mRegisterView.afterRegist(user,true,null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //环信注册失败 则删除之前bmob注册的
                                user.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                    }
                                });
                                ThreadUtils.runOnUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //隐藏对话框
                                        mRegisterView.hideProgressDialog();
                                        //注册失败信息传送给view成层
                                        mRegisterView.afterRegist(user,false,e1.getMessage());
                                    }
                                });
                            }
                        }
                    });
                }else{
                    //bmob注册失败
                    mRegisterView.hideProgressDialog();
                    //注册失败信息传送给view成层
                    mRegisterView.afterRegist(user,false,e.getMessage());
                }
            }
        });

    }
}
