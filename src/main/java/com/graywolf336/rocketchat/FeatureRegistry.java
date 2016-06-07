package com.graywolf336.rocketchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.graywolf336.rocketchat.features.serverupdates.ServerUpdateFeature;
import com.graywolf336.rocketchat.interfaces.Feature;

public class FeatureRegistry {
    private List<Feature> features;

    public FeatureRegistry(RocketChatMain plugin) {
        this.features = new ArrayList<Feature>();
        this.loadFeatures(plugin);
    }

    public void onLoad(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onLoad(plugin)) {
                    plugin.getLogger().info("Successfully called the onLoad for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnLoad failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onLoad for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    public void onEnable(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onEnable(plugin)) {
                    plugin.getLogger().info("Successfully called the onEnable for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnEnable failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onEnable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    public void onDisable(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onDisable(plugin)) {
                    plugin.getLogger().info("Successfully called the onDisable for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnDisable failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onDisable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    public void onSuccessfulConnection(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onSuccessfulConnection(plugin)) {
                    plugin.getLogger().info("Successfully called the onSuccessfulConnection for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnSuccessfulConnection failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onSuccessfulConnection for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    public void onFailedConnection(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onFailedConnection(plugin)) {
                    plugin.getLogger().info("Successfully called the onFailedConnection for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnFailedConnection failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onFailedConnection for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    public void onRoomsLoaded(Plugin plugin) {
        for (Feature feature : features) {
            try {
                if (feature.onRoomsLoaded(plugin)) {
                    plugin.getLogger().info("Successfully called the onRoomsLoaded for '" + feature.getName() + "'.");
                } else {
                    plugin.getLogger().warning("OnRoomsLoaded failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                plugin.getLogger().severe("The onRoomsLoaded for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }
    
    /**
     * Adds a {@link Feature feature} to be called on the events.
     * 
     * @param feature the {@link Feature} to add.
     * @return whether it was added or not, returns false if it already exists
     */
    public boolean addFeature(Feature feature) {
        return this.features.contains(feature) ? false : this.features.add(feature);
    }

    private void loadFeatures(RocketChatMain plugin) {
        this.features.add(new ServerUpdateFeature(plugin.getRocketChatClient()));
    }
}
