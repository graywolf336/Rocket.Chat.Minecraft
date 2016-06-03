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
    private boolean loadedChannels, loadedGroups;

    protected RocketChatRoomManager(RocketChatMain plugin, ConnectionManager connection) {
        this.plugin = plugin;
        this.conn = connection;
        this.rooms = new ArrayList<IRoom>();
        this.loadedChannels = false;
        this.loadedGroups = false;
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
                if (error == null) {
                    List<Object> channels = (ArrayList<Object>) result.getFields().get("channels");

                    int count = 0;
                    for (Object r : channels) {
                        try {
                            RocketChatRoom room = new RocketChatRoom(r, RoomType.CHANNEL);
                            rooms.add(room);
                            count++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            plugin.getLogger().severe("Failed to parse one of the channels!! See the exception above.");
                        }
                    }

                    loadedChannels = true;
                    plugin.debug(false, "Loaded " + count + " channels!");

                    if (loadedChannels && loadedGroups) {
                        plugin.getRegistry().onRoomsLoaded(plugin);
                    }
                } else {
                    plugin.getLogger().severe("Failed to load the channels!! '" + error.toString() + "'");
                }
            }));

            conn.callMethod(Method.GROUPLIST, null, new RocketChatCallListener((error, result) -> {
                if (error == null) {
                    List<Object> groups = (ArrayList<Object>) result.getFields().get("groups");

                    int count = 0;
                    for (Object r : groups) {
                        try {
                            RocketChatRoom room = new RocketChatRoom(r, RoomType.PRIVATE_GROUP);
                            rooms.add(room);
                            count++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            plugin.getLogger().severe("Failed to parse one of the private groups!! See the exception above.");
                        }
                    }

                    loadedGroups = true;
                    plugin.debug(false, "Loaded " + count + " private groups!");

                    if (loadedChannels && loadedGroups) {
                        plugin.getRegistry().onRoomsLoaded(plugin);
                    }
                } else {
                    plugin.getLogger().severe("Failed to load the private groups!! '" + error.toString() + "'");
                }
            }));
        }
    }
}
