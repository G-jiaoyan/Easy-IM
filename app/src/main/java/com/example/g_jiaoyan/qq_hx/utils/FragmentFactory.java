package com.example.g_jiaoyan.qq_hx.utils;

import com.example.g_jiaoyan.qq_hx.common.BaseFragment;
import com.example.g_jiaoyan.qq_hx.view.fragment.ContactsFragment;
import com.example.g_jiaoyan.qq_hx.view.fragment.ConversationFragment;
import com.example.g_jiaoyan.qq_hx.view.fragment.PluginFragment;
//$Fragment工厂 直接获取单个Fragment
public class FragmentFactory {
    private static ConversationFragment sConversationFragment;
    private static ContactsFragment sContactsFragment;
    private static PluginFragment sPluginFragment;

    //单例模式
    private static ConversationFragment getConversationFragment(){
        if(sConversationFragment == null){
            sConversationFragment = new ConversationFragment();
        }
        return sConversationFragment;
    }
    private static ContactsFragment getContactsFragment(){
        if(sContactsFragment == null){
            sContactsFragment = new ContactsFragment();
        }
        return sContactsFragment;
    }
    private static PluginFragment getPluginFragment(){
        if(sPluginFragment == null){
            sPluginFragment = new PluginFragment();
        }
        return sPluginFragment;
    }

    //获取到单个的Fragment
    public static BaseFragment getFragment(int index){
        switch (index) {
            case 0:
                return getConversationFragment();
            case 1:
                return getContactsFragment();
            case 2:
                return getPluginFragment();
        }
        return null;
    }

}
