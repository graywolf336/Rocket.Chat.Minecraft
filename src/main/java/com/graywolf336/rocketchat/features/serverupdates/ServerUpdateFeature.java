package com.graywolf336.rocketchat.features.serverupdates;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.RocketChatMessage;
import com.graywolf336.rocketchat.interfaces.IFeature;

public class ServerUpdateFeature implements IFeature {
    private static String name = "Server Updates";
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
    	
    	if(configFile.exists()) {
    		if(configFile.isFile()) {
    			ServerUpdateSettings.setConfig(YamlConfiguration.loadConfiguration(configFile));
    			this.logInfo("Loaded the config from existing file.");
    		}else {
    			this.logSevere("The config file can not be a folder.");
    			this.logSevere("As a result, we are reverting back to the default configuration.");
    			ServerUpdateSettings.setConfig(YamlConfiguration.loadConfiguration(plugin.getResource("features/server-updates.yml")));
    		}
    	}else {
    		this.logWarn("Loading the default config.");
    		ServerUpdateSettings.setConfig(YamlConfiguration.loadConfiguration(plugin.getResource("features/server-updates.yml")));
    		
    		try {
    			ServerUpdateSettings.getConfig().save(configFile);
    		}catch(IOException e) {
    			this.logSevere("Failed to save the default configuration: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
    			return false;
    		}
    	}
    	
        return true;
    }
    
    public boolean onEnable(Plugin plugin) {
    	if(ServerUpdateSettings.STARTUP_ENABLED.asBoolean()) {
    		this.client.getPlugin().getServer().getScheduler().runTaskAsynchronously(this.client.getPlugin(), () -> {
        		this.client.sendMessage(new RocketChatMessage(ServerUpdateSettings.STARTUP_FORMAT.asString()));
        	});
    	}
    	
        return true;
    }

    public boolean onDisable(Plugin plugin) {
        return false;
    }
    
    public boolean onSuccessfulConnection(Plugin plugin) {
        return false;
    }
    
    public boolean onFailedConnection(Plugin plugin) {
        return false;
    }
    
    private void logInfo(String message) {
    	this.client.getPlugin().getLogger().info("[" + name + "]: " + message);
    }
    
    private void logWarn(String message) {
    	this.client.getPlugin().getLogger().warning("[" + name + "]: " + message);
    }
    
    private void logSevere(String message) {
    	this.client.getPlugin().getLogger().severe("[" + name + "]: " + message);
    }
}
