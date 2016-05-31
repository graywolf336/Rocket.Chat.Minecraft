package com.graywolf336.rocketchat.enums;

public enum Method {
	CHANNELLIST("channelsList"),
	GROUPLIST("groupsList"),
    LOGIN("login"),
    LOGOUT("logout"),
    SENDMESSAGE("sendMessage");
    
    private String method;
    
    private Method(String m) {
        this.method = m;
    }
    
    public String get() {
        return this.method;
    }
}
