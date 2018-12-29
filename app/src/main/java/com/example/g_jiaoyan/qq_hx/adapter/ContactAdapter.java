package com.example.g_jiaoyan.qq_hx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
//$联系人recyclerView的适配器
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements SectionIndexer {
    private List<String> data;

    //SparseIntArray 专用于保存int对int的键值对 Google推出
    private SparseIntArray sparseIntArray = new SparseIntArray();

    public ContactAdapter(List<String> data) {
        this.data = data;
    }

    public List<String> getDate(){
        return data;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //单个条目布局
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_contact, viewGroup, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(inflate);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        final String username = data.get(position);
        String inital = StringUtils.getInital(username);

        holder.mTvUsername.setText(username);
        holder.mTvSection.setText(inital);

        //判断是不是第一个数据 是第一个则显示节点 不是则继续
        if(position == 0){
            holder.mTvSection.setVisibility(View.VISIBLE);
        }else{
            //上一个名字
            String preUsername = data.get(position - 1);
            String preInital = StringUtils.getInital(preUsername);
            if(inital.equals(preInital)){
                //如果和上一个首字母一样 则隐藏
                holder.mTvSection.setVisibility(View.GONE);
            }else{
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(username);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(username);
                    return true;
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return data == null? 0:data.size();
    }
    //长按点击事件接口
    public interface OnItemClickListener{
        void onItemClick(String username);
        void onItemLongClick(String username);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    //SectionIndexer 在此用于定位RecyclerView
    //分区
    @Override
    public String[] getSections() {
        ArrayList<String> sections = new ArrayList<>();
        //对数据进行一个分区
        for(int i = 0;i<data.size();i++){
            String inital = StringUtils.getInital(data.get(i));
            if(!(sections.contains(inital))){
                sections.add(inital);
                sparseIntArray.put(sections.size() -1 ,i);
            }
        }
        return sections.toArray(new String[sections.size()]);
    }
    //传一个分区的索引返回一个该分区第一个条目在所有listView中的位置索引
    @Override
    public int getPositionForSection(int i) {
        return sparseIntArray.get(i);
    }
    //传一个条目位置返回所在分区的索引
    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvSection;
        public TextView mTvUsername;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvSection = itemView.findViewById(R.id.tv_section_contact);
            mTvUsername = itemView.findViewById(R.id.tv_username_contact);
        }
    }
}
