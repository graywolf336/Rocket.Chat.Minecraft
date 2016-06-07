package com.graywolf336.rocketchat.features.serverupdates;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.interfaces.Feature;
import com.graywolf336.rocketchat.objects.RocketChatMessage;

public class ServerUpdateFeature extends Feature {
    private final static String name = "Server Updates";
    private RocketChatClient client;

    public ServerUpdateFeature(RocketChatClient rocketChatClient) {
        this.client = rocketChatClient;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("deprecation")
    public boolean onLoad(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder() + File.separator + "features", "server-updates.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(plugin.getResource("features/server-updates.yml"));
        boolean save = false;

        if (configFile.exists()) {
            if (configFile.isFile()) {
                ServerUpdateSetting.setConfig(YamlConfiguration.loadConfiguration(configFile));
                save = ServerUpdateSetting.writeNewConfigValues(defaultConfig);
                this.logInfo("Loaded the config from existing file.");
            } else {
                this.logSevere("The config file can not be a folder.");
                this.logSevere("As a result, we are reverting back to the default configuration.");
                ServerUpdateSetting.setConfig(defaultConfig);
            }
        } else {
            this.logWarn("Loading the default config.");
            ServerUpdateSetting.setConfig(defaultConfig);
            save = true;
        }

        if (save) {
            try {
                ServerUpdateSetting.getConfig().save(configFile);
            } catch (IOException e) {
                this.logSevere("Failed to save the default configuration: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
                return false;
            }
        }

        return true;
    }

    public boolean onDisable(Plugin plugin) {
        if (ServerUpdateSetting.SHUTDOWN_ENABLED.asBoolean()) {
            RocketChatMessage msg = new RocketChatMessage(ServerUpdateSetting.SHUTDOWN_FORMAT.asString());

            if (this.client.getRoomManager().getRoomByName(ServerUpdateSetting.SHUTDOWN_ROOM.asString()).isPresent()) {
                msg.setRoom(this.client.getRoomManager().getRoomByName(ServerUpdateSetting.SHUTDOWN_ROOM.asString()).get());
            } else if (this.client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).isPresent()) {
                msg.setRoom(this.client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).get());
            } else {
                this.logSevere("Failed to load the room for the onDisable!");
                return false;
            }

            this.client.sendMessage(msg);
        }

        return true;
    }

    public boolean onRoomsLoaded(Plugin plugin) {
        if (ServerUpdateSetting.STARTUP_ENABLED.asBoolean()) {
            this.client.getPlugin().getServer().getScheduler().runTaskAsynchronously(this.client.getPlugin(), () -> {
                RocketChatMessage msg = new RocketChatMessage(ServerUpdateSetting.STARTUP_FORMAT.asString());

                if (this.client.getRoomManager().getRoomByName(ServerUpdateSetting.STARTUP_ROOM.asString()).isPresent()) {
                    msg.setRoom(this.client.getRoomManager().getRoomByName(ServerUpdateSetting.STARTUP_ROOM.asString()).get());
                } else if (this.client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).isPresent()) {
                    msg.setRoom(this.client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).get());
                } else {
                    this.logSevere("Failed to load the room for the onEnable!");
                    return;
                }

                this.client.sendMessage(msg);
            });
        }

        return true;
    }
}
