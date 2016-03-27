package com.graywolf336.rocketchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.graywolf336.rocketchat.features.ServerUpdateFeature;
import com.graywolf336.rocketchat.interfaces.IFeature;

public class FeatureRegistry {
    private List<IFeature> features;
    
    public FeatureRegistry(RocketChatMain plugin) {
        this.features = new ArrayList<IFeature>();
        this.loadFeatures(plugin);
    }
    
    public void onLoad(Plugin plugin) {
        for(IFeature feature : features) {
            try {
                if(feature.onLoad(plugin)) {
                    plugin.getLogger().info("Successfully called the onLoad for '" + feature.getName() + "'.");
                }else {
                    plugin.getLogger().warning("OnLoad failed to be successful for '" + feature.getName() + "'.");
                }
            }catch(Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onLoad for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }
    
    public void onEnable(Plugin plugin) {
        for(IFeature feature : features) {
            try {
                if(feature.onEnable(plugin)) {
                    plugin.getLogger().info("Successfully called the onEnable for '" + feature.getName() + "'.");
                }else {
                    plugin.getLogger().warning("OnEnable failed to be successful for '" + feature.getName() + "'.");
                }
            }catch(Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onEnable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }
    
    public void onDisable(Plugin plugin) {
        for(IFeature feature : features) {
            try {
                if(feature.onDisable(plugin)) {
                    plugin.getLogger().info("Successfully called the onDisable for '" + feature.getName() + "'.");
                }else {
                    plugin.getLogger().warning("OnDisable failed to be successful for '" + feature.getName() + "'.");
                }
            }catch(Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onDisable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }
    
    private void loadFeatures(Plugin plugin) {
        this.features.add(new ServerUpdateFeature());
    }
}