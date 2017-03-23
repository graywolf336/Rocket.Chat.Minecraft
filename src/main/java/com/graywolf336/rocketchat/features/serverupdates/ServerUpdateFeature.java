package com.graywolf336.rocketchat.features.serverupdates;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.interfaces.Feature;
import com.graywolf336.rocketchat.objects.RocketChatMessage;

public class ServerUpdateFeature extends Feature {
    public String getName() {
        return "Server Updates";
    }

    @SuppressWarnings("deprecation")
    public boolean onLoad(RocketChatClient client) {
        File configFile = new File(client.getPlugin().getDataFolder() + File.separator + "features", "server-updates.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(client.getPlugin().getResource("features/server-updates.yml"));
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

    public boolean onDisable(RocketChatClient client) {
        if (ServerUpdateSetting.SHUTDOWN_ENABLED.asBoolean()) {
            RocketChatMessage msg = new RocketChatMessage(ServerUpdateSetting.SHUTDOWN_FORMAT.asString());

            if (client.getRoomManager().getRoomByName(ServerUpdateSetting.SHUTDOWN_ROOM.asString()).isPresent()) {
                msg.setRoom(client.getRoomManager().getRoomByName(ServerUpdateSetting.SHUTDOWN_ROOM.asString()).get());
            } else if (client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).isPresent()) {
                msg.setRoom(client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).get());
            } else {
                this.logSevere("Failed to load the room for the onDisable!");
                return false;
            }

            client.sendMessage(msg);
        }

        return true;
    }
    
    public boolean onReload(RocketChatClient client) {
        return this.onLoad(client);
    }

    public boolean onRoomsLoaded(RocketChatClient client) {
        if (ServerUpdateSetting.STARTUP_ENABLED.asBoolean()) {
            client.getPlugin().getServer().getScheduler().runTaskAsynchronously(client.getPlugin(), () -> {
                RocketChatMessage msg = new RocketChatMessage(ServerUpdateSetting.STARTUP_FORMAT.asString());

                if (client.getRoomManager().getRoomByName(ServerUpdateSetting.STARTUP_ROOM.asString()).isPresent()) {
                    msg.setRoom(client.getRoomManager().getRoomByName(ServerUpdateSetting.STARTUP_ROOM.asString()).get());
                } else if (client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).isPresent()) {
                    msg.setRoom(client.getRoomManager().getRoomByName(ServerUpdateSetting.DEFAULTROOM.asString()).get());
                } else {
                    this.logSevere("Failed to load the room for the onEnable!");
                    return;
                }

                client.sendMessage(msg);
            });
        }

        return true;
    }
}
