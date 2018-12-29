package com.example.g_jiaoyan.qq_hx.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
//吐司封装工具类
public class ToastUtils {
    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    //单例模式
    public static void showToast(final Context context, final String msg){
//        if(sToast == null){
//            sToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
//        }
//        sToast.setText(msg);
//        sToast.show();

//        //饿汉式
//        if(sToast == null){
//            sToast = Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
//        }
//        sToast.setText(msg);
//        //判断当前线程是否是主线程 如果是主线程直接show
//        //如果是子线程 在handler中show
//        //myLooper()从当前线程中获取looper对象 getMainLooper()获取主线程的looper对象
//        if(Looper.myLooper() == Looper.getMainLooper()){
//            //主线程
//            sToast.show();
//        }else{
//            //子线程
//            sHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    sToast.show();
//                }
//            });
//        }

        if(sToast == null){
            if(Looper.myLooper() == Looper.getMainLooper()){
                initToast(context,msg);
            }else{
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initToast(context,msg);
                    }
                });
            }
        }

        //判断当前代码是否是主线程
        if(Looper.myLooper() == Looper.getMainLooper()){
            sToast.setText(msg);
            sToast.show();
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    sToast.setText(msg);
                    sToast.show();
                }
            });
        }
    }

    private static void initToast(Context context, String msg) {
        sToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
    }
}
