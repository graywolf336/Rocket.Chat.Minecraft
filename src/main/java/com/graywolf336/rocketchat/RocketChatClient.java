package com.graywolf336.rocketchat;

import com.graywolf336.rocketchat.interfaces.IMessage;

public class RocketChatClient {
    private static ConnectionManager conn;
    
    protected RocketChatClient(ConnectionManager connection) {
        conn = connection;
    }
    
    public static void sendMessage(IMessage message) {
        //TODO: This.
    }
}
