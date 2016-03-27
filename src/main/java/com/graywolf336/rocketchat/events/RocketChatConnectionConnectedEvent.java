package com.graywolf336.rocketchat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.info.ConnectionConnectedInfo;

public class RocketChatConnectionConnectedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private ConnectionState state;
	private ConnectionConnectedInfo info;
	
	public RocketChatConnectionConnectedEvent(ConnectionState state, ConnectionConnectedInfo connectedInfo) {
		this.state = state;
		this.info = connectedInfo;
	}
	
	public ConnectionState getConnectionState() {
		return this.state;
	}
	
	public ConnectionConnectedInfo getConnectedInfo() {
		return this.info;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
