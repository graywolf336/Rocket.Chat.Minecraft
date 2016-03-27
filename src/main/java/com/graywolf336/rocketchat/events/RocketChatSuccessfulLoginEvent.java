package com.graywolf336.rocketchat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.rocketchat.enums.ConnectionState;

public class RocketChatSuccessfulLoginEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private ConnectionState state;
	private String userId;
	
	public RocketChatSuccessfulLoginEvent(ConnectionState state, String userId) {
		this.state = state;
		this.userId = userId;
	}
	
	public ConnectionState getConnectionState() {
		return this.state;
	}
	
	public String getUserID() {
		return this.userId;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
