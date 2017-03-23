package com.graywolf336.rocketchat.features.joinAndLeaves;

import org.bukkit.configuration.file.YamlConfiguration;

public enum JoinAndLeavesSetting {
    ENABLED("join-leave.enabled"),
    ROOM("join-leave.room"),
    AVATAR_URL("join-leave.avatar-url"),
    USE_DISPLAY_NAME("join-leave.use-display-name"),
    JOIN_MESSAGE("join-leave.join-message"),
    LEAVE_MESSAGE("join-leave.leave-message"),
    KICK_MESSAGE("join-leave.kick-message"),
    IGNORE_PERMISSION("join-leave.ignore-permission");
    
    private static YamlConfiguration config;
    private String path;
    
    private JoinAndLeavesSetting(String path) {
        this.path = path;
    }
    
    public boolean asBoolean() {
        return config.getBoolean(path);
    }
    
    public String asString() {
        return config.getString(path);
    }
    
    public String getPath() {
        return this.path;
    }
    
    public static void setConfig(YamlConfiguration configuration) {
        config = configuration;
    }
    
    public static YamlConfiguration getConfig() {
        return config;
    }
    
    public static boolean writeNewConfigValues(YamlConfiguration newConfig) {
        boolean changedAnything = false;
        
        for(JoinAndLeavesSetting s : values()) {
            if(!config.contains(s.getPath())) {
                config.set(s.getPath(), newConfig.get(s.getPath()));
                changedAnything = true;
            }
        }
        
        return changedAnything;
    }
}
