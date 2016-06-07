package com.graywolf336.rocketchat.info;

import com.graywolf336.rocketchat.interfaces.IDDPErrorInfo;

public class SubscriptionErrorInfo implements IDDPErrorInfo {
    private String source, message;

    public SubscriptionErrorInfo(String source, String message) {
        this.source = source;
        this.message = message;
    }

    public String getSource() {
        return this.source;
    }

    public String getMessage() {
        return this.message;
    }
    
    public String toString() {
        return this.source + ": " + this.message;
    }
}
