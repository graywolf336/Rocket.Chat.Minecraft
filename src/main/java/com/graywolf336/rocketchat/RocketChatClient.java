package com.graywolf336.rocketchat;

import com.graywolf336.rocketchat.interfaces.IMessage;

public class RocketChatClient {
	private RocketChatMain plugin;
    private ConnectionManager conn;
    private RocketChatRoomManager rooms;
    
    protected RocketChatClient(RocketChatMain plugin, ConnectionManager connection, RocketChatRoomManager rooms) {
    	this.plugin = plugin;
        this.conn = connection;
        this.rooms = rooms;
    }
    
    public RocketChatMain getPlugin() {
    	return this.plugin;
    }
    
    public ConnectionManager getConnectionManager() {
    	return this.conn;
    }
    
    public RocketChatRoomManager getRoomManager() {
    	return this.rooms;
    }
    
    public void sendMessage(IMessage message) {
    	if(message.isValid()) {
    		this.conn.queueMessage(message);
    	}
    }
}
