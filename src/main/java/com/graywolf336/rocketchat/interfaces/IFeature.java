package com.graywolf336.rocketchat.interfaces;

import org.bukkit.plugin.Plugin;

public interface IFeature {
    /**
     * Gets the name of the feature.
     *
     * @return name of the feature
     */
    public String getName();

    /**
     * Called when the Rocket.Chat plugin's onLoad method is called, is a <strong>sync</strong>.
     * This is called <strong>before</strong> a connection to Rocket.Chat is made.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onLoad(Plugin plugin);

    /**
     * Called when the Rocket.Chat plugin's onEnable method is called, is a <strong>sync</strong>.
     * This is called <strong>before</strong> a connection to Rocket.Chat is made.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onEnable(Plugin plugin);

    /**
     * Called when the Rocket.Chat plugin's onEnable method is called, is a <strong>sync</strong>.
     * This is called when the connection is still open, <strong>if</strong> one was successfully
     * made.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onDisable(Plugin plugin);

    /**
     * Called <strong>after</strong> a connection to Rocket.Chat is <strong>successfully</strong>
     * made.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onSuccessfulConnection(Plugin plugin);

    /**
     * Called <strong>after</strong> a connection to Rocket.Chat was tried and was
     * <strong>failed</strong>.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onFailedConnection(Plugin plugin);

    /**
     * Called <strong>after</strong> the public channels have been loaded. This is only called
     * <strong>once</strong> per server start up.
     *
     * @param plugin a {@link Plugin} instance
     * @return whether this method ran successfully or not
     */
    public boolean onRoomsLoaded(Plugin plugin);
}
