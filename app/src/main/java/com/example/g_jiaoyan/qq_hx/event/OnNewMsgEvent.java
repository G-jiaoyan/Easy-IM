package com.example.g_jiaoyan.qq_hx.event;

import com.hyphenate.chat.EMMessage;

public class OnNewMsgEvent {
    public EMMessage eMMessage;

    public OnNewMsgEvent(EMMessage eMMessage) {
        this.eMMessage = eMMessage;
    }
}
