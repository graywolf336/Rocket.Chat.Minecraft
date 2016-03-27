package com.graywolf336.rocketchat.features.serverupdates;

import org.bukkit.configuration.file.YamlConfiguration;

public enum ServerUpdateSettings {
	ENABLED("server-updates.enabled"),
	DEFAULTROOM("server-updates.default-room"),
	STARTUP_ENABLED("server-updates.start-up.enabled"),
	STARTUP_ROOM("server-updates.start-up.room"),
	STARTUP_FORMAT("server-updates.start-up.format");
	
	
	private static YamlConfiguration config;
	private String path;
	
	private ServerUpdateSettings(String path) {
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
}
