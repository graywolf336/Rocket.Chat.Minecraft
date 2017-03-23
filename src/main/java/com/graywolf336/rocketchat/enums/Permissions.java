package com.graywolf336.rocketchat.enums;

import org.bukkit.command.CommandSender;

/**
 * Represents the permissions used in the Rocket.Chat plugin.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public enum Permissions {
    /** The permission for being able to reload the configuration and connections, defaults to op. */
    RELOAD("rocketchat.reload");

    private String node;

    private Permissions(String node) {
        this.node = node;
    }

    /**
     * Gets the permission node for this permission.
     *
     * @return the node
     */
    public String getNode() {
        return this.node;
    }

    /**
     * Checks if the given sender, can be a player, has the permission.
     *
     * @param sender the person to check
     * @return whether the player has permission or not
     */
    public boolean check(CommandSender sender) {
        return sender.hasPermission(this.node);
    }
}
