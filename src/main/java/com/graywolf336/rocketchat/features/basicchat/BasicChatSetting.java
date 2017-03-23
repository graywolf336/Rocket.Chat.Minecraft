package com.graywolf336.rocketchat.features.basicchat;

import org.bukkit.configuration.file.YamlConfiguration;

public enum BasicChatSetting {
    ENABLED("basic-chat.enabled"),
    ROOM("basic-chat.room"),
    AVATAR_URL("basic-chat.avatar-url"),
    USE_DISPLAY_NAME("basic-chat.use-display-name");
    
    private static YamlConfiguration config;
    private String path;
    
    private BasicChatSetting(String path) {
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
        
        for(BasicChatSetting s : values()) {
            if(!config.contains(s.getPath())) {
                config.set(s.getPath(), newConfig.get(s.getPath()));
                changedAnything = true;
            }
        }
        
        return changedAnything;
    }
}
