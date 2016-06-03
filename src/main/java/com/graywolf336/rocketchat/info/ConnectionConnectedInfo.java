package com.graywolf336.rocketchat.info;

public class ConnectionConnectedInfo {
    private String sessionID;

    public ConnectionConnectedInfo(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return this.sessionID;
    }
}
