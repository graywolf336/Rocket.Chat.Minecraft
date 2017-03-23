package com.graywolf336.rocketchat.features;

import org.bukkit.entity.Player;

public class FeatureHelpers {
    public static String replacePlayerInfo(Player player, String value) {
        return value.replace("{UUID}", player.getUniqueId().toString()).replace("{USERNAME}", player.getName());
    }
}
