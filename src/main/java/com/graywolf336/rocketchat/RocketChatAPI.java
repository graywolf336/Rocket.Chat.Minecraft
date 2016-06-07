package com.graywolf336.rocketchat;

public class RocketChatAPI {
    private static RocketChatMain plugin;
    
    protected static void setMain(RocketChatMain pl) {
        plugin = pl;
    }
    
    public static RocketChatClient getChatClient() {
        return plugin.getRocketChatClient();
    }
    
    public static RocketChatFeatureRegistry getFeatureRegistry() {
        return plugin.getRegistry();
    }
}
