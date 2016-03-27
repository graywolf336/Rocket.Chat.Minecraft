package com.graywolf336.rocketchat;

import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.rocketchat.enums.Settings;

public class RocketChatMain extends JavaPlugin {
    private ConnectionManager connection;
    private FeatureRegistry registry;
    private RocketChatClient client;
    private boolean debug;
    
    public void onLoad() {
        this.saveDefaultConfig();
        Settings.setPlugin(this);
        this.debug = Settings.DEBUG.asBoolean();
        
        this.connection = new ConnectionManager(this);
        this.client = new RocketChatClient(this, this.connection);
        this.registry = new FeatureRegistry(this);
        this.registry.onLoad(this);
    }
    
    public void onEnable() {
        this.connection.acquireConnection(0);
        this.registry.onEnable(this);
    }
    
    public void onDisable() {
        this.registry.onDisable(this);
        this.connection.disconnectConnection();
    }
    
    public RocketChatClient getRocketChatClient() {
    	return this.client;
    }
    
    /**
     * Prints messages if the plugin is in a debugging state.
     * 
     * @param colored whether the messages are colored.
     * @param msgs the messages to print.
     */
    public void debug(boolean colored, String... msgs) {
        if (this.debug) {
            for (String s : msgs) {
                if (colored) {
                    getServer().getConsoleSender().sendMessage("[Rocket.Chat] [Debug]: " + s);
                } else {
                    getLogger().info("[Debug]: " + s);
                }
            }
        }
    }
}
