package com.graywolf336.rocketchat.serializable;

@SuppressWarnings("unused")
public class Message {
    private String rid = "WxS4rtY68yz6Jxp4v";
    private String msg;

    protected Message(String message, String roomId) {
        this.rid = roomId;
        this.msg = message;
    }
}
