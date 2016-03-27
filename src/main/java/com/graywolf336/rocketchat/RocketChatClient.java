package com.graywolf336.rocketchat;

import com.graywolf336.rocketchat.interfaces.IMessage;

public class RocketChatClient {
	private RocketChatMain plugin;
    private ConnectionManager conn;
    
    protected RocketChatClient(RocketChatMain plugin, ConnectionManager connection) {
    	this.plugin = plugin;
        this.conn = connection;
    }
    
    public RocketChatMain getPlugin() {
    	return this.plugin;
    }
    
    public ConnectionManager getConnectionManager() {
    	return this.conn;
    }
    
    public void sendMessage(IMessage message) {
    	if(message.isValid()) {
    		this.conn.queueMessage(message);
    	}
    }
}
