package com.graywolf336.rocketchat.enums;

public enum Method {
    LOGIN("login"),
    SENDMESSAGE("sendMessage");
    
    private String method;
    
    private Method(String m) {
        this.method = m;
    }
    
    public String get() {
        return this.method;
    }
}
