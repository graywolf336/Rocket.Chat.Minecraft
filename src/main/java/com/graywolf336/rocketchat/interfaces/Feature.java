package com.graywolf336.rocketchat.interfaces;

import org.bukkit.Bukkit;

import com.graywolf336.rocketchat.RocketChatClient;

public abstract class Feature {
    /**
     * Gets the name of the feature.
     *
     * @return name of the feature
     */
    public abstract String getName();

    /**
     * Called when the Rocket.Chat plugin's onLoad method is called, is a <strong>sync</strong>.
     * This is called <strong>before</strong> a connection to Rocket.Chat is made. Your feature
     * probably won't have this called since your plugin will need to depend on this one being loaded.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onLoad(RocketChatClient client) {
        return true;
    }

    /**
     * Called when the Rocket.Chat plugin's onEnable method is called, is a <strong>sync</strong>.
     * This is called <strong>before</strong> a connection to Rocket.Chat is made.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onEnable(RocketChatClient client) {
        return true;
    }

    /**
     * Called when the Rocket.Chat plugin's onDisable method is called, is a <strong>sync</strong>.
     * This is called when the connection is still open, <strong>if</strong> one was successfully
     * made.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onDisable(RocketChatClient client) {
        return true;
    }
    
    /**
     * Called when the Rocket.Chat plugin was reloaded via the `/rocketchatreload command and it is <strong>sync</strong>.
     * This is called after the internals have completed after the reload, no good connection is promised.
     * 
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onReload(RocketChatClient client) {
        return true;
    }

    /**
     * Called <strong>after</strong> a connection to Rocket.Chat is <strong>successfully</strong>
     * made.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onSuccessfulConnection(RocketChatClient client) {
        return true;
    }

    /**
     * Called <strong>after</strong> a connection to Rocket.Chat was tried and was
     * <strong>failed</strong>.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onFailedConnection(RocketChatClient client) {
        return true;
    }

    /**
     * Called <strong>after</strong> the public channels have been loaded. This is only called
     * <strong>once</strong> per server start up.
     *
     * @param client a {@link RocketChatClient} instance
     * @return whether this method ran successfully or not
     */
    public boolean onRoomsLoaded(RocketChatClient client) {
        return true;
    }
    
    public void logInfo(String message) {
        Bukkit.getLogger().info("[" + this.getName() + "]: " + message);
    }

    public void logWarn(String message) {
        Bukkit.getLogger().warning("[" + this.getName() + "]: " + message);
    }

    public void logSevere(String message) {
        Bukkit.getLogger().severe("[" + this.getName() + "]: " + message);
    }
}
