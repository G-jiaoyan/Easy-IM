package com.example.g_jiaoyan.qq_hx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.OnItemClick;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>{
    private List<EMConversation> mEMConversations;

    public ConversationAdapter(List<EMConversation> EMConversations) {
        mEMConversations = EMConversations;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_conversation, viewGroup, false);
        ConversationViewHolder holder = new ConversationViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int i) {
        EMConversation conversation = mEMConversations.get(i);
        final String userName = conversation.getLastMessage().getUserName();
        holder.tv_username.setText(userName);
        //消息条目
        int unreadMsgCount = conversation.getUnreadMsgCount();
        if (unreadMsgCount > 99) {
            holder.tv_unread.setText("99+");
            holder.tv_unread.setVisibility(View.VISIBLE);
        } else if (unreadMsgCount > 0) {
            holder.tv_unread.setText(unreadMsgCount+"");
            holder.tv_unread.setVisibility(View.VISIBLE);
        }else{
            holder.tv_unread.setVisibility(View.GONE);
        }

        //时间
        EMMessage lastMessage = conversation.getLastMessage();
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));

        //接收到的消息显示
        EMMessageBody body = lastMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            holder.tv_msg.setText(textMessageBody.getMessage());
        }else {
            holder.tv_msg.setText("发送消息类型不支持");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(userName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEMConversations == null?0:mEMConversations.size();
    }


    public interface onConversationItemClickListener{
        void onItemClick(String username);
    }

    private onConversationItemClickListener mOnItemClickListener;
    public void setConversationAdapter(onConversationItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_username;
        public TextView tv_msg;
        public TextView tv_time;
        public TextView tv_unread;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_msg = itemView.findViewById(R.id.tv_msg_conversation);
            tv_time = itemView.findViewById(R.id.tv_time_conversation);
            tv_unread = itemView.findViewById(R.id.tv_unread);
        }
    }
}
