package com.example.g_jiaoyan.qq_hx;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.Telephony;
import android.util.Log;

import com.example.g_jiaoyan.qq_hx.adapter.EMMessageListenerAdapter;
import com.example.g_jiaoyan.qq_hx.common.BaseActivity;
import com.example.g_jiaoyan.qq_hx.utils.DBUtils;
import com.example.g_jiaoyan.qq_hx.utils.ToastUtils;
import com.example.g_jiaoyan.qq_hx.view.LoginActivity;
import com.example.g_jiaoyan.qq_hx.view.fragment.ChatActivity;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

public class QQApplication extends Application {
    //定义一个集合 统一管理全局的所有的activity
    private List<BaseActivity> mBaseActivityList = new ArrayList<>();

    private SoundPool mSoundPool;
    private int mDuan;
    private int mYulu;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化自己的模块
        DBUtils.init(this);
        //初始化音乐池
        initSoundPool();
        //初始化第三方的模块
        initHX();
        initBmob();
        initListener();
    }

    //每次启动的activity都要添加进此集合
    public void addActivity(BaseActivity activity){
        if (!mBaseActivityList.contains(activity)) {
            mBaseActivityList.add(activity);
        }
    }
    //每个关闭的activity都要从集合中删除
    public void removeActivity(BaseActivity activity){
        mBaseActivityList.remove(activity);
    }

    //初始化音乐池
    private void initSoundPool() {
        mSoundPool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        mDuan = mSoundPool.load(this, R.raw.duan, 1);
        mYulu = mSoundPool.load(this, R.raw.yulu, 1);
    }

    //监听qq接收消息提示
    private void initListener() {
        //监听新的消息到来
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListenerAdapter(){
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                super.onMessageReceived(list);
                //接收到消息发出声音通知 如果应用在后台运行则播放长声音 否则播放短声音
                if (isRuninBackground()) {
                    mSoundPool.play(mYulu,1,1,0,0,1);
                    //显示通知栏
                    showNotification(list.get(0));
                }else{
                    mSoundPool.play(mDuan,1,1,0,0,1);
                }

                //发布事件
                EventBus.getDefault().post(list.get(0));
            }
        });

        //监听连接状态
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                //连接服务器成功

            }

            @Override
            public void onDisconnected(int i) {
                //连接服务器失败
                //USER_LOGIN_ANOTHER_DEVICE用户在另一地方登录 跳转到登录界面
                if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {

                    //强制退出后清空集合中的activity
                    for (BaseActivity activity : mBaseActivityList) {
                        activity.finish();
                    }
                    mBaseActivityList.clear();

                    Intent intent = new Intent(QQApplication.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    ToastUtils.showToast(getApplicationContext(),"您的账号已在其他设备登录!");
                }
            }
        });
    }

    //显示新消息在通知栏
    private void showNotification(EMMessage emMessage) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        //获取当前新消息
        String msg = "";
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
            msg = emTextMessageBody.getMessage();
        }

        //传至少两个activity 保证退出聊天界面时回退到主界面
        Intent mainIntent = new Intent(this,MainActivity.class);
        //start activity在application中不能使用 添加flags才能使用
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chatIntent = new Intent(this,ChatActivity.class);
        chatIntent.putExtra("username",emMessage.getUserName());

        Intent[] intents = {mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT);

        //显示通知
        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.avatar3))
                .setSmallIcon(R.mipmap.contact_selected_2)
                .setContentText(msg)
                .setContentTitle("您有一条新消息")
                .setContentInfo("来自"+emMessage.getUserName())
                .setAutoCancel(true)
//                .setColor()//设置小图标的颜色
                .setContentIntent(pendingIntent)
                .build();
        mNotificationManager.notify(1,notification);
    }

    //是否在后台运行
    private boolean isRuninBackground(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(100);
        //获取最上层的任务栈
        ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
        //taskInfo.topActivity任务栈中最上面的activity
        return !taskInfo.topActivity.getPackageName().equals(getPackageName());
    }

    private void initBmob() {
        Bmob.initialize(this, "e9fce3037d22435d9632ab64d895520a");
    }

    private void initHX() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //初始化

        //获取进程的名称 如果 != packagename 说明在开启其他进程
        int pid = android.os.Process.myPid();//进程id
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
    //获取进程名称
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
