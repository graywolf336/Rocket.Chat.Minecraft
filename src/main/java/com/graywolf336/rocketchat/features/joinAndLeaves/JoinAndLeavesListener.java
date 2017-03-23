package com.graywolf336.rocketchat.features.joinAndLeaves;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.features.FeatureHelpers;
import com.graywolf336.rocketchat.interfaces.IRoom;
import com.graywolf336.rocketchat.objects.RocketChatMessage;

public class JoinAndLeavesListener implements Listener {
    private RocketChatClient c;
    
    public JoinAndLeavesListener(RocketChatClient client) {
        this.c = client;
    }
    
    private void prepareAndSend(JoinAndLeavesSetting msgSetting, Player player) {
        if (!JoinAndLeavesSetting.ENABLED.asBoolean()) return;
        
        Optional<IRoom> room = this.c.getRoomManager().getRoomByIdOrName(JoinAndLeavesSetting.ROOM.asString());
        if (!room.isPresent()) return;
        
        if (player.hasPermission(JoinAndLeavesSetting.IGNORE_PERMISSION.asString())) return;
        
        String avatar = FeatureHelpers.replacePlayerInfo(player, JoinAndLeavesSetting.AVATAR_URL.asString());
        
        RocketChatMessage msg = new RocketChatMessage();
        msg.setMessage(FeatureHelpers.replacePlayerInfo(player, msgSetting.asString()));
        msg.setRoom(room.get());
        msg.setAvatar(avatar);
        msg.setGroupable(false);
        msg.setParseUrls(false);
        
        if (JoinAndLeavesSetting.USE_DISPLAY_NAME.asBoolean()) {
            msg.setAlias(ChatColor.stripColor(player.getDisplayName()));
        } else {
            msg.setAlias(ChatColor.stripColor(player.getName()));
        }
        
        this.c.sendMessage(msg);
    }
    
    @EventHandler
    public void playerJoinedUsEvent(PlayerJoinEvent event) { 
        this.prepareAndSend(JoinAndLeavesSetting.JOIN_MESSAGE, event.getPlayer());
    }
    
    @EventHandler
    public void playerLeftEvent(PlayerQuitEvent event) {
        this.prepareAndSend(JoinAndLeavesSetting.LEAVE_MESSAGE, event.getPlayer());
    }
    
    @EventHandler
    public void playerKickedEvent(PlayerKickEvent event) {
        this.prepareAndSend(JoinAndLeavesSetting.KICK_MESSAGE, event.getPlayer());
    }
}
