package com.graywolf336.rocketchat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.info.ConnectionClosedInfo;

public class RocketChatConnectionClosedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private ConnectionState state;
	private ConnectionClosedInfo info;
	
	public RocketChatConnectionClosedEvent(ConnectionState state, ConnectionClosedInfo closedInfo) {
		this.state = state;
		this.info = closedInfo;
	}
	
	public ConnectionState getConnectionState() {
		return this.state;
	}
	
	public ConnectionClosedInfo getClosedInformation() {
		return this.info;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
