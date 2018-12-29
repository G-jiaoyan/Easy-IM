package com.example.g_jiaoyan.qq_hx.presenter;

import com.example.g_jiaoyan.qq_hx.utils.DBUtils;
import com.example.g_jiaoyan.qq_hx.utils.ThreadUtils;
import com.example.g_jiaoyan.qq_hx.view.fragment.ContactsFragment;
import com.example.g_jiaoyan.qq_hx.view.fragment.ContactsView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//$实现联系人界面的逻辑
public class ContactsPresenterImpl implements ContactsPresenter {
    private ContactsView mContactsView;

    private List<String> contactsList = new ArrayList<>();

    //回调
    public ContactsPresenterImpl(ContactsView contactsView) {
        mContactsView = contactsView;
    }

    //初始化联系人逻辑 二级缓存 先显示本地缓存数据 再异步加载网络数据
    @Override
    public void initContacts() {
        //首先走本地缓存
        List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
        //先清空数据 再重新添加数据 避免数据重复
        contactsList.clear();
        contactsList.addAll(contacts);
        //回到实现界面
        mContactsView.showContacts(contactsList);

        //然后走网络
        update();
    }

    //网络缓存
    private void update(){
        //走网络 获取环信的好友列表
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取到当前环信用户的好友列表
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //Collections用于操作集合类  sort对集合进行排序
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return s.compareTo(t1);
                        }
                    });
                    //先清空数据 再重新添加数据
                    contactsList.clear();
                    contactsList.addAll(allContactsFromServer);

                    //获取完数据后更新本地缓存
                    DBUtils.saveContacts(EMClient.getInstance().getCurrentUser(),contactsList);

                    //通知界面更新
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            //调用实现界面的方法刷新
                            mContactsView.updateContacts(true);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactsView.updateContacts(false);
                        }
                    });
                }
            }
        });
    }

    //更新数据
    @Override
    public void updateContact() {
        //网络缓存数据
        update();
    }

    //删除好友
    @Override
    public void deleteContact(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    //删除成功
                    afterDelete(true,username,null);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //删除失败
                    afterDelete(false,username,e.getMessage());
                }
            }
        });
    }

    private void afterDelete(final boolean b, final String username, final String message) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                mContactsView.afterContact(b,username,message);
            }
        });
    }
}
