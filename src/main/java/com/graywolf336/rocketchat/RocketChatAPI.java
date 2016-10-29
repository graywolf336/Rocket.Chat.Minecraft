package com.graywolf336.rocketchat;

import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.interfaces.IMessage;
import com.graywolf336.rocketchat.objects.RocketChatMessage;
import com.graywolf336.rocketchat.objects.RocketChatRoom;

/**
 * A static class for easy access to the Rocket.Chat API.
 * <p>
 * This class should only be used for getting instances,
 * try to avoid using this class for anything serious.
 * It will probably be changed quite a bit, depending on
 * demand for easy access of items.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public class RocketChatAPI {
    private static RocketChatMain plugin;
    
    protected static void setMain(RocketChatMain pl) {
        plugin = pl;
    }
    
    /**
     * Gets the instance of the {@link RocketChatClient} to use.
     * 
     * @return a valid {@link RocketChatClient} instance
     */
    public static RocketChatClient getChatClient() {
        return plugin.getRocketChatClient();
    }
    
    /**
     * Gets the Connection State of the connection to the Rocket.Chat server.
     * 
     * @return the {@link ConnectionState} of the connection
     */
    public static ConnectionState getConnectionState() {
        return plugin.getRocketChatClient().getConnectionState();
    }
    
    /**
     * Queues up a message to be sent to the server and the queue is processed twice a second (or every 10 ticks).
     * <p>
     * A message must have at least the <strong>room</strong> and <strong>message</strong> set, otherwise it will
     * not be a valid message format.
     * <p>
     * If you don't want to create your own {@link IMessage} wrapper, then use {@link RocketChatMessage}.
     * 
     * @param message the {@link IMessage} to send
     * @return whether the message was successfully queued up or not
     */
    public static boolean sendMessage(IMessage message) {
        return message == null ? false : plugin.getRocketChatClient().sendMessage(message);
    }
    
    /**
     * Queues up a message to be sent to the Rocket.Chat server's general room as soon as the queue gets processed.
     * 
     * @param message the message to be sent
     * @return whether the message was successfully queued up or not
     */
    public static boolean sendMessageToGeneral(String message) {
        RocketChatMessage msg = new RocketChatMessage(message);
        msg.setRoom(plugin.getRocketChatClient().getRoomManager().getRoomById("GENERAL").orElse(new RocketChatRoom("GENERAL")));
        
        return plugin.getRocketChatClient().sendMessage(msg);
    }
    
    /**
     * Gets the instance of the {@link RocketChatFeatureRegistry} to use for handling features.
     * 
     * @return a valid {@link RocketChatFeatureRegistry} instance
     */
    public static RocketChatFeatureRegistry getFeatureRegistry() {
        return plugin.getRegistry();
    }
}
