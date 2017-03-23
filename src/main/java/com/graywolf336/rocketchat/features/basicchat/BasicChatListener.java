package com.graywolf336.rocketchat.features.basicchat;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.interfaces.IRoom;
import com.graywolf336.rocketchat.objects.RocketChatMessage;

public class BasicChatListener implements Listener {
    private RocketChatClient c;
    
    public BasicChatListener(RocketChatClient client) {
        this.c = client;
    }
    
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        if (!BasicChatSetting.ENABLED.asBoolean()) return;
        
        Optional<IRoom> room = this.c.getRoomManager().getRoomByIdOrName(BasicChatSetting.ROOM.asString());
        if (!room.isPresent()) return;
        
        String avatar = BasicChatSetting.AVATAR_URL.asString().replace("{UUID}", event.getPlayer().getUniqueId().toString()).replace("{USERNAME}", event.getPlayer().getName());
        
        RocketChatMessage msg = new RocketChatMessage(ChatColor.stripColor(event.getMessage()));
        msg.setRoom(room.get());
        msg.setAvatar(avatar);
        msg.setGroupable(true);
        msg.setParseUrls(false);
        
        if (BasicChatSetting.USE_DISPLAY_NAME.asBoolean()) {
            msg.setAlias(ChatColor.stripColor(event.getPlayer().getDisplayName()));
        } else {
            msg.setAlias(ChatColor.stripColor(event.getPlayer().getName()));
        }
        
        this.c.sendMessage(msg);
    }
}
