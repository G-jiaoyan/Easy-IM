package com.example.g_jiaoyan.qq_hx.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.adapter.ContactAdapter;
import com.example.g_jiaoyan.qq_hx.utils.StringUtils;
import com.hyphenate.util.DensityUtil;

import java.util.Arrays;
import java.util.List;

//自定义联系人侧边栏
public class Slidebar extends View {
    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float mAvgHeight;

    private TextView tvFloat;
    private RecyclerView contactRecyclerView;

    public Slidebar(Context context) {
        this(context, null);
    }

    public Slidebar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context);
    }

    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //侧边栏触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //显示悬浮吐司并且定位RecyclerView
                showToastAndScroll(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //隐藏悬浮吐司 设置背景为透明
                if(tvFloat != null){
                    tvFloat.setVisibility(GONE);
                }
                setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return true;
    }

    //显示悬浮吐司并且滚动listView
    private void showToastAndScroll(float y) {
        if(tvFloat == null){
            //悬浮吐司也就是文本了 首先拿到兄弟节点和recyclerView
            ViewGroup parent = (ViewGroup) getParent();
            tvFloat = parent.findViewById(R.id.tv_floatView);
            contactRecyclerView = parent.findViewById(R.id.contact_recyclerView);
        }

        tvFloat.setVisibility(VISIBLE);
        setBackgroundResource(R.drawable.slidebar_bk);
        //当前高度除于单个高度就是当前第几个文字
        int index = (int) (y / mAvgHeight);
        if(index < 0){
            index = 0;
        }else if(index > SECTIONS.length - 1){
            index = SECTIONS.length - 1;
        }
        //获取当前文本设置到悬浮吐司上
        String slideBarSection = SECTIONS[index];
        tvFloat.setText(slideBarSection);

        //定位RecyclerView 一种实现方式
//        ContactAdapter contactAdapter = (ContactAdapter) contactRecyclerView.getAdapter();
//        List<String> date = contactAdapter.getDate();
//
//        for(int i = 0; i < date.size(); i++){
//            if(StringUtils.getInital(date.get(i)).equals(slideBarSection)){
//                //平滑滚动到指定位置
//                contactRecyclerView.smoothScrollToPosition(i);
//                return;
//            }
//        }


        RecyclerView.Adapter adapter = contactRecyclerView.getAdapter();
        if(!(adapter instanceof SectionIndexer)){
            return;
        }
        //向上转型
        SectionIndexer sectionIndexer = (SectionIndexer) adapter;
        //获取真实数据中所有的分区
        String[] sections = (String[]) sectionIndexer.getSections();
        //当前用户点击的是B 然后需要找到B在真实分区中占得角标
        int sectionIndex = Arrays.binarySearch(sections, slideBarSection);
        //根据sections的角标找到条目的角标
        if(sectionIndex < 0){
            //如果小于0 说明没有此角标 所以直接跳出不进行之后的代码
            return;
        }
        int positionForSection = sectionIndexer.getPositionForSection(sectionIndex);
        //根据条目的角标让RecyclerView定位到该位置
        contactRecyclerView.scrollToPosition(positionForSection);

    }

    //初始化画笔
    private void initView(Context context) {
        //抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#8c8c8c"));
        mPaint.setTextAlign(Paint.Align.CENTER);//文字绘制居中
        mPaint.setTextSize(DensityUtil.sp2px(context, 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        //减去底边值 显示更加好看
        mHeight = getMeasuredHeight() - getPaddingBottom();
        //获取单个文字的高度
        mAvgHeight = (mHeight +0.f) / SECTIONS.length;
    }
    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = mWidth / 2;
        //绘制文字
        for (int i = 0; i < SECTIONS.length; i++) {
            float y = mAvgHeight * (i + 1);//x不变 y累加
            canvas.drawText(SECTIONS[i], x, y,mPaint);
        }

    }

}
