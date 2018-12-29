package com.example.g_jiaoyan.qq_hx.adapter;

import com.hyphenate.EMCallBack;
//环信适配器设计模式 如果一个类中又很多方法 但是有些又用不到 就实现一下 用哪个方法覆写哪个
public class EMCallBackAdapter implements EMCallBack {
    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onProgress(int i, String s) {

    }
}
