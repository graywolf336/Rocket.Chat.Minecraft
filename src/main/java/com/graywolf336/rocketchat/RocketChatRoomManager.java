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

public class RocketChatRoomManager {
	private RocketChatMain plugin;
	private ConnectionManager conn;
	private List<IRoom> rooms;
	
	protected RocketChatRoomManager(RocketChatMain plugin, ConnectionManager connection) {
		this.plugin = plugin;
		this.conn = connection;
		this.rooms = new ArrayList<IRoom>();
	}
	
	public Optional<IRoom> getRoomById(String id) {
		return this.rooms.stream().filter(r -> r.getID().equalsIgnoreCase(id)).findFirst();
	}
	
	public Optional<IRoom> getRoomByName(String name) {
		return this.rooms.stream().filter(r -> r.getName().equalsIgnoreCase(name.replace("#", ""))).findFirst();
	}
	
	public Stream<IRoom> getRoomsByType(RoomType type) {
		return this.rooms.stream().filter(r -> r.getType() == type);
	}
	
	protected void registerListener() {
		this.plugin.getServer().getPluginManager().registerEvents(new RocketChatRoomManagerListener(), plugin);
	}
	
	private class RocketChatRoomManagerListener implements Listener {
		@SuppressWarnings("unchecked")
		@EventHandler
		public void onSuccessfulLogin(RocketChatSuccessfulLoginEvent event) {
			conn.callMethod(Method.CHANNELLIST, null, new RocketChatCallListener((error, result) -> {
				if(error ==  null) {
					List<Object> channels = (ArrayList<Object>)result.getFields().get("channels");
					
					for(Object r : channels) {
						try {
							RocketChatRoom room = new RocketChatRoom(r, RoomType.CHANNEL);
							rooms.add(room);
						} catch (Exception e) {
							e.printStackTrace();
							plugin.getLogger().severe("Failed to parse one of the rooms!! See the exception above.");
						}
					}
					
					plugin.debug(false, "Loaded " + rooms.size() + " rooms!");
					plugin.getRegistry().onRoomsLoaded(plugin);
				}else {
					plugin.getLogger().severe("Failed to load the rooms!! '" + error.toString() + "'");
				}
			}));
		}
	}
}
