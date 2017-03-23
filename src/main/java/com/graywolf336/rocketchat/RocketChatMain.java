package com.graywolf336.rocketchat;

import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.rocketchat.commands.RocketChatReloadCommand;
import com.graywolf336.rocketchat.enums.Settings;

/**
 * The {@link JavaPlugin} class for the Rocket.Chat plugin.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public class RocketChatMain extends JavaPlugin {
    private ConnectionManager connection;
    private RocketChatFeatureRegistry registry;
    private RocketChatClient client;
    private RocketChatRoomManager roomManager;
    private boolean debug;

    public void onLoad() {
        this.saveDefaultConfig();
        Settings.setPlugin(this);
        this.debug = Settings.DEBUG.asBoolean();

        this.connection = new ConnectionManager(this);
        this.roomManager = new RocketChatRoomManager(this, this.connection);
        this.client = new RocketChatClient(this, this.connection, this.roomManager);
        this.registry = new RocketChatFeatureRegistry();
        this.registry.onLoad(this.client);

        RocketChatAPI.setMain(this);
    }

    public void onEnable() {
        this.registerCommands();
        this.roomManager.registerListener();
        this.connection.acquireConnection(0);
        this.registry.onEnable(this.client);
    }

    public void onDisable() {
        this.registry.onDisable(this.client);
        this.connection.disconnectConnection();
        
        RocketChatAPI.setMain(null);
        this.registry = null;
        this.client = null;
        this.roomManager = null;
        this.connection = null;
    }
    
    public void reloadEverything() {
        this.reloadConfig();
        this.connection.disconnectConnection();
        this.connection.acquireConnection(-1);
        this.registry.onReload(client);
    }
    
    private void registerCommands() {
        this.getCommand("rocketchatreload").setExecutor(new RocketChatReloadCommand(this));
    }

    /**
     * Gets the instance of the {@link RocketChatClient} to use.
     *
     * @return a valid {@link RocketChatClient} instance
     */
    public RocketChatClient getRocketChatClient() {
        return this.client;
    }

    /**
     * Gets the instance of the {@link RocketChatFeatureRegistry} to use.
     *
     * @return a valid {@link RocketChatFeatureRegistry} instance
     */
    protected RocketChatFeatureRegistry getRegistry() {
        return this.registry;
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
