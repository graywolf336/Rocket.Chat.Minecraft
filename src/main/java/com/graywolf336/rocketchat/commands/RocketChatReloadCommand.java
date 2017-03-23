package com.graywolf336.rocketchat.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.graywolf336.rocketchat.RocketChatMain;
import com.graywolf336.rocketchat.enums.Permissions;
import com.graywolf336.rocketchat.interfaces.ICommand;

public class RocketChatReloadCommand implements ICommand {
    private RocketChatMain pl;
    
    public RocketChatReloadCommand(RocketChatMain plugin) {
        this.pl = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Permissions.RELOAD.check(sender)) {
            this.pl.reloadEverything();
            sender.sendMessage(ChatColor.GREEN + "Successfully ran the reload.");
            return true;
        }

        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
