package com.graywolf336.rocketchat;

import java.util.ArrayList;
import java.util.List;

import com.graywolf336.rocketchat.features.serverupdates.ServerUpdateFeature;
import com.graywolf336.rocketchat.interfaces.Feature;

/**
 * The Feature Registry for the Rocket.Chat plugin.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public class RocketChatFeatureRegistry {
    private List<Feature> features;

    public RocketChatFeatureRegistry() {
        this.features = new ArrayList<Feature>();
        this.loadFeatures();
    }

    protected void onLoad(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onLoad(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onLoad for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnLoad failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onLoad for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    protected void onEnable(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onEnable(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onEnable for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnEnable failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onEnable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    protected void onDisable(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onDisable(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onDisable for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnDisable failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onDisable for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    protected void onSuccessfulConnection(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onSuccessfulConnection(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onSuccessfulConnection for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnSuccessfulConnection failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onSuccessfulConnection for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    protected void onFailedConnection(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onFailedConnection(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onFailedConnection for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnFailedConnection failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onFailedConnection for '" + feature.getName() + "' threw an exception! See the above.");
            }
        }
    }

    protected void onRoomsLoaded(RocketChatClient client) {
        for (Feature feature : features) {
            try {
                if (feature.onRoomsLoaded(client)) {
                    client.getPlugin().getLogger().info("Successfully called the onRoomsLoaded for '" + feature.getName() + "'.");
                } else {
                    client.getPlugin().getLogger().warning("OnRoomsLoaded failed to be successful for '" + feature.getName() + "'.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                client.getPlugin().getLogger().severe("The onRoomsLoaded for '" + feature.getName() + "' threw an exception! See the above.");
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

    /**
     * Gets a list of all the {@link Feature}s loaded.
     * 
     * @return the list of {@link Feature features}
     */
    public List<Feature> getFeatures() {
        return this.features;
    }

    private void loadFeatures() {
        this.features.add(new ServerUpdateFeature());
    }
}
