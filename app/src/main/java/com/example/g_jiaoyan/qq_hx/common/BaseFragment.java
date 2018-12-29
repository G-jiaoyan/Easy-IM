package com.example.g_jiaoyan.qq_hx.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
//$抽取出来的fragment 后面用到的fragment继承于此类
//其中有一个头标题 左右有两个图片默认隐藏
public abstract class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_title = getView().findViewById(R.id.tv_title_heater);
        initTitle(tv_title);
    }
    //此方法修改每个页面标题
    protected abstract void initTitle(TextView tv_title);
}
