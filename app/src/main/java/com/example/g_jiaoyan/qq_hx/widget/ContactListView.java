package com.example.g_jiaoyan.qq_hx.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.example.g_jiaoyan.qq_hx.R;
//$联系人布局
public class ContactListView extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ContactListView(Context context) {
        this(context,null);
    }

    public ContactListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    //所有的布局最后都会走到这里
    public ContactListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //绑定布局文件
        LayoutInflater.from(context).inflate(R.layout.contact_list_layout,this,true);
        mRecyclerView = findViewById(R.id.contact_recyclerView);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        //设置刷新时的颜色
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    //RecyclerView设置适配器方法
    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    //SwipeRefreshLayout刷新监听
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    //设置刷新状态
    public void setRefreshing(boolean b) {
        if(mSwipeRefreshLayout != null){
            //在获取数据完成后设置刷新状态为false
            mSwipeRefreshLayout.setRefreshing(b);
        }
    }
}

