package com.example.g_jiaoyan.qq_hx;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.example.g_jiaoyan.qq_hx.adapter.TabSelectedListenerAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.common.BaseFragment;
import com.example.g_jiaoyan.qq_hx.event.ContactUpdateEvent;
import com.example.g_jiaoyan.qq_hx.utils.FragmentFactory;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MVP 是从经典的模式MVC演变而来，它们的基本思想有相通的地方：
 * Controller/Presenter负责逻辑的处理，Model提供数据，View负责显示
 * <p>
 * 要保证view的回调方法在主线程创建 因为view就是用来操作UI的
 */
public class MainActivity extends BaseActivity implements EMContactListener {

    @BindView(R.id.frame_main)
    FrameLayout frameMain;
    @BindView(R.id.BN_main)
    BottomNavigationBar BNMain;
    private TextBadgeItem mBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initListener();
        initFragment();
        initBottomBar();

        EventBus.getDefault().register(this);
    }

    //初始化监听好友通讯录更新
    private void initListener() {
        EMClient.getInstance().contactManager().setContactListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //更新未读消息
        updateUnreadMsgCount();
    }

    //获取未读消息
    private void updateUnreadMsgCount() {
        //获取所有未读消息
        int messageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if(messageCount > 99){
            mBadgeItem.setText("99+");
            //true 动画效果显示/隐藏
            mBadgeItem.show(true);
        } else if (messageCount > 0) {
            mBadgeItem.setText(messageCount+"");
            mBadgeItem.show(true);
        }else{
            mBadgeItem.hide(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //进来之后先更新未读消息
        updateUnreadMsgCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(this);
        EventBus.getDefault().unregister(this);
    }

    //初始化界面
    private void initFragment() {
        BaseFragment fragment = FragmentFactory.getFragment(0);
        //默认第一个添加进去显示
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main,fragment,"0").commit();

    }

    //初始化BottomNavigationBar
    private void initBottomBar() {
        //文本角标
        mBadgeItem = new TextBadgeItem();
        mBadgeItem.hide()
                .setGravity(Gravity.RIGHT)//显示角标到右边
                .setBackgroundColor(Color.RED)//设置背景色为红色
                .setTextColor(Color.WHITE)//设置文字颜色为白色
                .setHideOnSelect(false)//切换到此界面自动隐藏角标
                .setAnimationDuration(100)//设置图片时长
                .show();

        BNMain.setActiveColor("#ffb2c6")//设置选中的图标颜色
              .setInActiveColor("#ABADBB")//设置未选中图标颜色
              .addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2,"消息").setBadgeItem(mBadgeItem))
              .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2,"联系人"))
              .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2,"我的"))
                .setFirstSelectedPosition(0)//第一个显示的页面
                .initialise();//初始化

        BNMain.setTabSelectedListener(new TabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //隐藏其他页面
                fragmentTransaction.hide(FragmentFactory.getFragment(0));
                fragmentTransaction.hide(FragmentFactory.getFragment(1));
                fragmentTransaction.hide(FragmentFactory.getFragment(2));
                //显示当前页面 判断是否添加当前页面 如果添加了则显示 否则先添加再显示
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if(!fragment.isAdded()){
                    fragmentTransaction.add(R.id.frame_main,fragment,position+"");
                }
                //显示
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onContactAdded(String s) {
        //添加成功 用eventBus发布出去一个事件
        EventBus.getDefault().post(new ContactUpdateEvent(true,s));
    }

    @Override
    public void onContactDeleted(String s) {
        //删除成功 用eventBus发布出去一个事件
        EventBus.getDefault().post(new ContactUpdateEvent(false,s));
    }

    @Override
    public void onContactInvited(final String s, String s1) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(s);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        showToast("收到"+s+"的好友请求");
    }

    @Override
    public void onFriendRequestAccepted(String s) {

    }

    @Override
    public void onFriendRequestDeclined(String s) {

    }
}
