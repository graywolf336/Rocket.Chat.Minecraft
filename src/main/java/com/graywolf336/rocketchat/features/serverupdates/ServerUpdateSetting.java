package com.graywolf336.rocketchat.features.serverupdates;

import org.bukkit.configuration.file.YamlConfiguration;

public enum ServerUpdateSetting {
	ENABLED("server-updates.enabled"),
	DEFAULTROOM("server-updates.default-room"),
	STARTUP_ENABLED("server-updates.start-up.enabled"),
	STARTUP_ROOM("server-updates.start-up.room"),
	STARTUP_FORMAT("server-updates.start-up.format"),
	SHUTDOWN_ENABLED("server-updates.shut-down.enabled"),
	SHUTDOWN_ROOM("server-updates.shut-down.room"),
	SHUTDOWN_FORMAT("server-updates.shut-down.format");
	
	
	private static YamlConfiguration config;
	private String path;
	
	private ServerUpdateSetting(String path) {
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
	    
	    for(ServerUpdateSetting s : values()) {
	        if(!config.contains(s.getPath())) {
	            config.set(s.getPath(), newConfig.get(s.getPath()));
	            changedAnything = true;
	        }
	    }
	    
	    return changedAnything;
	}
}
