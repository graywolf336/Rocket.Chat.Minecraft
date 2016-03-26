package com.graywolf336.rocketchat.enums;

import java.util.List;

import org.bukkit.plugin.Plugin;

public enum Settings {
    DEBUG("system.debug"),
    HOST("connection.host"),
    PORT("connection.port"),
    SSL("connection.ssl"),
    EMAIL("connection.email"),
    PASSWORD("connection.password");
    
    private static Plugin pl;
    private String path;
    
    private Settings(String path) {
        this.path = path;
    }
    
    public boolean asBoolean() {
        return pl.getConfig().getBoolean(path);
    }
    
    public int asInt() {
        return pl.getConfig().getInt(path);
    }
    
    public String asString() {
        return pl.getConfig().getString(path);
    }
    
    public List<String> asStringList() {
        return pl.getConfig().getStringList(path);
    }
    
    public static void setPlugin(Plugin plugin) {
        pl = plugin;
    }
}
