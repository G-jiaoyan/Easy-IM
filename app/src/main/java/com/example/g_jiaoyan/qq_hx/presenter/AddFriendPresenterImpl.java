package com.example.g_jiaoyan.qq_hx.presenter;

import android.util.Log;

import com.example.g_jiaoyan.qq_hx.model.User;
import com.example.g_jiaoyan.qq_hx.utils.DBUtils;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

//$实现添加好友的逻辑
public class AddFriendPresenterImpl implements AddFriendPresenter {
    private AddFriendView mAddFriendView;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
    }

    //搜索用户的逻辑
    @Override
    public void searchFriend(String keyword) {
        //去bmob服务器中搜索相关用户
        BmobQuery<User> query = new BmobQuery<>();
        //username中包含keyword
        query.addWhereContains("username",keyword);
        //且不能搜索出自己
        query.addWhereNotEqualTo("username",EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                //搜索完成
                if(e == null && list.size() > 0){
                    //搜索成功
                    //当前用户好友
                    List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());

                    mAddFriendView.afterSearch(list,contacts,true);
                }else{
                    //搜索失败 或搜索为空
                    Log.i("Qweqwe", "done: "+e);
                    mAddFriendView.afterSearch(null,null,false);
                }
            }
        });

    }

    //添加好友逻辑
    @Override
    public void addContact(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"");
                    afterAdd(true,null,username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    afterAdd(false,e.getMessage(),username);
                }
            }
        });
    }

    //添加好友后的逻辑
    private void afterAdd(final boolean success, final String msg, final String username){
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mAddFriendView.afterAddContact(success,msg,username);
            }
        });
    }
}
