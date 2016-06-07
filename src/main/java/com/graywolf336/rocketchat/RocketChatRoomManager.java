package com.graywolf336.rocketchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.gson.internal.LinkedTreeMap;
import com.graywolf336.rocketchat.enums.Method;
import com.graywolf336.rocketchat.enums.RoomType;
import com.graywolf336.rocketchat.enums.Subscription;
import com.graywolf336.rocketchat.events.RocketChatSuccessfulLoginEvent;
import com.graywolf336.rocketchat.info.SubscriptionUpdateInfo;
import com.graywolf336.rocketchat.interfaces.IRoom;
import com.graywolf336.rocketchat.listeners.RocketChatCallListener;
import com.graywolf336.rocketchat.listeners.RocketChatSubscriptionListener;
import com.graywolf336.rocketchat.objects.RocketChatRoom;

public class RocketChatRoomManager {
    private RocketChatMain plugin;
    private ConnectionManager conn;
    private List<IRoom> rooms;
    private boolean loadedChannels, loadedGroups, roomsLoadedCalled;

    protected RocketChatRoomManager(RocketChatMain plugin, ConnectionManager connection) {
        this.plugin = plugin;
        this.conn = connection;
        this.rooms = new ArrayList<IRoom>();
        this.loadedChannels = false;
        this.loadedGroups = false;
        this.roomsLoadedCalled = false;
    }

    public Optional<IRoom> getRoomById(String id) {
        return this.rooms.stream().filter(r -> r.getId().equalsIgnoreCase(id)).findFirst();
    }

    public Optional<IRoom> getRoomByName(String name) {
        return this.rooms.stream().filter(r -> r.getName().equalsIgnoreCase(name.replace("#", ""))).findFirst();
    }

    public Optional<IRoom> getRoomBySubscriptionId(String subId) {
        return this.rooms.stream().filter(r -> r.getSubscriptionId().equalsIgnoreCase(subId)).findFirst();
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

                    if (loadedChannels && loadedGroups && !roomsLoadedCalled) {
                        plugin.getRegistry().onRoomsLoaded(plugin.getRocketChatClient());
                        roomsLoadedCalled = true;
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

                    if (loadedChannels && loadedGroups && !roomsLoadedCalled) {
                        plugin.getRegistry().onRoomsLoaded(plugin.getRocketChatClient());
                        roomsLoadedCalled = true;
                    }
                } else {
                    plugin.getLogger().severe("Failed to load the private groups!! '" + error.toString() + "'");
                }
            }));

            conn.callSubscription(Subscription.SUBSCRIPTIONS, null, new RocketChatSubscriptionListener(Subscription.SUBSCRIPTIONS, (error, result) -> {
                if (error == null) {
                    SubscriptionUpdateInfo info = (SubscriptionUpdateInfo) result;
                    LinkedTreeMap<?, ?> item = (LinkedTreeMap<?, ?>) info.getFields().get("fields");

                    switch (info.getUpdateType()) {
                        case ADDED:
                            String rid = (String) item.get("rid");

                            Optional<IRoom> aRoom = getRoomById(rid);
                            if (aRoom.isPresent()) {
                                aRoom.get().setSubscriptionId(info.getId());
                            } else {
                                String name = (String) item.get("name");
                                RoomType type = RoomType.getByLetter((String) item.get("t"));

                                rooms.add(new RocketChatRoom(rid, name, info.getId(), type));
                            }
                            break;
                        case CHANGED:
                            plugin.debug(false, "RoomManager: " + item.toString());
                            Optional<IRoom> cRoom = getRoomBySubscriptionId(info.getId());
                            if (cRoom.isPresent()) {
                                if (item.containsKey("t")) {
                                    cRoom.get().setType(RoomType.getByLetter((String) item.get("t")));
                                }

                                if (item.containsKey("name")) {
                                    cRoom.get().setName((String) item.get("name"));
                                }
                            }
                            break;
                        case REMOVED:
                            Optional<IRoom> rRoom = getRoomBySubscriptionId(info.getId());
                            if (rRoom.isPresent() && rRoom.get().getType() != RoomType.CHANNEL) {
                                rooms.remove(rRoom.get());
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    plugin.getLogger().severe("Error orrured while an update in a subscription: '" + error.toString() + "'");
                }
            }));
        }
    }
}
