package com.graywolf336.rocketchat.features;

import org.bukkit.plugin.Plugin;

import com.graywolf336.rocketchat.interfaces.IFeature;

public class ServerUpdateFeature implements IFeature {
    private static String name = "Server Updates";
    
    public String getName() {
        return name;
    }
    
    public boolean onLoad(Plugin plugin) {
        return true;
    }
    
    public boolean onEnable(Plugin plugin) {
        return true;
    }

    public boolean onDisable(Plugin plugin) {
        return false;
    }
    
    public boolean onSuccessfulConnection(Plugin plugin) {
        return false;
    }
    
    public boolean onFailedConnection(Plugin plugin) {
        return false;
    }
}
