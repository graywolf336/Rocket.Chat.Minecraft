package com.graywolf336.rocketchat.features.basicchat;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.interfaces.Feature;

public class BasicChatFeature extends Feature {
    public String getName() {
        return "Basic Chat";
    }
    
    @SuppressWarnings("deprecation")
    public boolean onLoad(RocketChatClient client) {
        File configFile = new File(client.getPlugin().getDataFolder() + File.separator + "features", "basic-chat.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(client.getPlugin().getResource("features/basic-chat.yml"));
        boolean save = false;

        if (configFile.exists()) {
            if (configFile.isFile()) {
                BasicChatSetting.setConfig(YamlConfiguration.loadConfiguration(configFile));
                save = BasicChatSetting.writeNewConfigValues(defaultConfig);
                this.logInfo("Loaded the config from existing file.");
            } else {
                this.logSevere("The config file can not be a folder.");
                this.logSevere("As a result, we are reverting back to the default configuration.");
                BasicChatSetting.setConfig(defaultConfig);
            }
        } else {
            this.logWarn("Loading the default config.");
            BasicChatSetting.setConfig(defaultConfig);
            save = true;
        }

        if (save) {
            try {
                BasicChatSetting.getConfig().save(configFile);
            } catch (IOException e) {
                this.logSevere("Failed to save the default configuration: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
                return false;
            }
        }

        return true;
    }
    
    public boolean onReload(RocketChatClient client) {
        return this.onLoad(client);
    }
    
    public boolean onEnable(RocketChatClient client) {
        client.getPlugin().getServer().getPluginManager().registerEvents(new BasicChatListener(client), client.getPlugin());
        return true;
    }
}
