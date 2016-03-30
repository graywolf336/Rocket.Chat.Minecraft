package com.graywolf336.rocketchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.graywolf336.rocketchat.enums.Method;
import com.graywolf336.rocketchat.enums.RoomType;
import com.graywolf336.rocketchat.events.RocketChatSuccessfulLoginEvent;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatRoomManager implements Listener {
	private RocketChatMain plugin;
	private ConnectionManager conn;
	private List<IRoom> rooms;
	
	protected RocketChatRoomManager(RocketChatMain plugin, ConnectionManager connection) {
		this.plugin = plugin;
		this.conn = connection;
		this.rooms = new ArrayList<IRoom>();
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSuccessfulLogin(RocketChatSuccessfulLoginEvent event) {
		this.conn.callMethod(Method.CHANNELLIST, null, new RocketChatCallListener((error, result) -> {
			if(error ==  null) {
				List<Object> channels = (ArrayList<Object>)result.getFields().get("channels");
				
				for(Object r : channels) {
					try {
						RocketChatRoom room = new RocketChatRoom(r, RoomType.CHANNEL);
						this.rooms.add(room);
					} catch (Exception e) {
						e.printStackTrace();
						this.plugin.getLogger().severe("Failed to parse one of the rooms!! See the exception above.");
					}
				}
				
				plugin.debug(false, "Loaded " + this.rooms.size() + " rooms!");
			}else {
				this.plugin.getLogger().severe("Failed to load the rooms!! '" + error.toString() + "'");
			}
		}));
	}
	
	public Optional<IRoom> getRoomById(String id) {
		return this.rooms.stream().filter(r -> r.getID().equalsIgnoreCase(id)).findFirst();
	}
	
	public Optional<IRoom> getRoomByName(String name) {
		return this.rooms.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
	}
	
	public Stream<IRoom> getRoomsByType(RoomType type) {
		return this.rooms.stream().filter(r -> r.getType() == type);
	}
}
