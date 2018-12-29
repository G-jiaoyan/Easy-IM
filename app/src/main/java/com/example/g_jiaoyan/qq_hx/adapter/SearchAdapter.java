package com.example.g_jiaoyan.qq_hx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.example.g_jiaoyan.qq_hx.model.User;

import java.util.List;
//添加好友列表适配器
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<User> userList;
    private List<String> contactsList;

    public SearchAdapter(List<User> userList, List<String> contactsList) {
        this.userList = userList;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search, viewGroup, false);
        SearchViewHolder viewHolder = new SearchViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int i) {
        final User user = userList.get(i);
        holder.tv_username.setText(user.getUsername());
        holder.tv_time.setText(user.getCreatedAt());

        //判断是不是好友 是好友不可点击
        if(contactsList.contains(user.getUsername())){
            holder.btn_search.setText("已是好友");
            //不可点击
            holder.btn_search.setEnabled(false);
        }else{
            //不是好友可添加
            holder.btn_search.setText("添加");
            holder.btn_search.setEnabled(true);
            holder.btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onAddFriendClickListener != null){
                        onAddFriendClickListener.onClick(user.getUsername());
                    }
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return userList == null? 0:userList.size();
    }

    //监听器
    public interface onAddFriendClickListener{
        void onClick(String username);
    }

    private onAddFriendClickListener onAddFriendClickListener;

    public void setOnAddFriendClickListener(onAddFriendClickListener onAddFriendClickLisener) {
        this.onAddFriendClickListener = onAddFriendClickLisener;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_username;
        public TextView tv_time;
        public Button btn_search;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username_search);
            tv_time = itemView.findViewById(R.id.tv_time_search);
            btn_search = itemView.findViewById(R.id.btn_search);
        }
    }
}
