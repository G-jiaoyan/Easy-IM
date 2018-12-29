package com.example.g_jiaoyan.qq_hx.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.g_jiaoyan.qq_hx.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private List<EMMessage> eMMessages;

    public ChatAdapter(List<EMMessage> eMMessages) {
        this.eMMessages = eMMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        if(i == 0){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_chat_receive,viewGroup,false);
        }else if(i == 1){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_chat_send,viewGroup,false);
        }
        ChatViewHolder holder = new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int i) {
        EMMessage emMessage = eMMessages.get(i);
        long msgTime = emMessage.getMsgTime();

        //如果是第一条消息 始终显示时间
        if(i == 0){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        }else{
            //否则判断距上一条消息有没有超过规定的时间
            EMMessage preMsg = eMMessages.get(i-1);
            long preMsgTime = preMsg.getMsgTime();
            //环信提供两时间的一个差值是否小于一定时间
            if(DateUtils.isCloseEnough(msgTime,preMsgTime)){
                //是就隐藏
                holder.tvTime.setVisibility(View.GONE);
            }else{
                //不是则再次显示时间
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
            }
        }

        //如果发送消息是一个文本类型就发送 否则为不支持的文本类型
        EMMessageBody body = emMessage.getBody();
        if(body instanceof EMTextMessageBody){
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            holder.tvMsg.setText(textMessageBody.getMessage());
        }else{
            holder.tvMsg.setText("接收到非文本类型消息"+body.toString());
        }

        //获取文件类型 如果是发送文本就显示进度
        int itemViewType = getItemViewType(i);
        if(itemViewType == 1){
            //判断当前发送消息的状态
            switch (emMessage.status()) {
                case INPROGRESS:
                    //正在发送就显示帧动画
                    holder.ivMsg.setVisibility(View.VISIBLE);
                    holder.ivMsg.setImageResource(R.drawable.msg_sending_anim);
                    AnimationDrawable animationDrawable = (AnimationDrawable) holder.ivMsg.getDrawable();
                    animationDrawable.start();
                    break;
                case SUCCESS:
                    //发送成功隐藏进度
                    holder.ivMsg.setVisibility(View.GONE);
                    break;
                case FAIL:
                    //发送失败显示error图片
                    holder.ivMsg.setVisibility(View.VISIBLE);
                    holder.ivMsg.setImageResource(R.mipmap.msg_error);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return eMMessages == null?0:eMMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = eMMessages.get(position);
        return emMessage.direct() == EMMessage.Direct.RECEIVE?0:1;
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTime;
        public TextView tvMsg;
        public ImageView ivMsg;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tv_time);
            tvMsg = itemView.findViewById(R.id.tv_msg);
            ivMsg = itemView.findViewById(R.id.iv_msg_state);
        }
    }
}
