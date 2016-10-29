package com.graywolf336.rocketchat;

import org.apache.commons.lang.Validate;

import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.enums.Method;
import com.graywolf336.rocketchat.interfaces.IMessage;
import com.graywolf336.rocketchat.listeners.RocketChatCallListener;
import com.graywolf336.rocketchat.objects.RocketChatMessage;
import com.graywolf336.rocketchat.objects.RocketChatSubscription;

/**
 * The main client which should be consumed and center point for all the API calls.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public class RocketChatClient {
    private RocketChatMain plugin;
    private ConnectionManager conn;
    private RocketChatRoomManager rooms;

    protected RocketChatClient(RocketChatMain plugin, ConnectionManager connection, RocketChatRoomManager rooms) {
        this.plugin = plugin;
        this.conn = connection;
        this.rooms = rooms;
    }

    /**
     * Gets the instance of the {@link RocketChatMain} which is useful for tasks and resources.
     * <p>
     * If you're using this in your plugin, please release this is the Rocket.Chat plugin and not
     * yours thus resources and config files your plugin creates won't be in this one.
     *
     * @return the {@link RocketChatMain} instance
     */
    public RocketChatMain getPlugin() {
        return this.plugin;
    }

    /**
     * Gets the current state of the connection.
     *
     * @return the {@link ConnectionState state of the connection}
     */
    public ConnectionState getConnectionState() {
        return this.conn.getConnectionState();
    }

    public int callMethod(Method method, RocketChatCallListener listener) {
        Validate.notNull(method, "The method you're calling needs to be defind when calling a method.");

        return this.conn.callMethod(method, null, listener);
    }

    /**
     * Calls the given method to the Rocket.Chat server.
     *
     * @param method the {@link Method} to call.
     * @param params the params to pass, can be null.
     * @param listener the {@link RocketChatCallListener}, can be null.
     * @return the internal id associated with the call and listener
     */
    public int callMethod(Method method, Object[] params, RocketChatCallListener listener) {
        Validate.notNull(method, "The method you're calling needs to be defind when calling a method.");

        return this.conn.callMethod(method, params, listener);
    }

    /**
     * Queues up a {@link RocketChatSubscription} to be made active as soon as it is possible.
     *
     * @param subscription the {@link RocketChatSubscription} to queue
     * @return whether the subscription was successfully added to the queue
     */
    public boolean queueSubscription(RocketChatSubscription subscription) {
        return this.conn.queueSubscription(subscription);
    }

    /**
     * Removes a {@link RocketChatSubscription} from the internal system (queue and active), returns
     * false if it didn't exist.
     *
     * @param subscription the subscription to remove
     * @return whether there was a subscription removed by that id or not
     */
    public boolean removeSubscription(RocketChatSubscription subscription) {
        return this.conn.removeSubscription(subscription);
    }

    /**
     * Gets the {@link RocketChatRoomManager} instance.
     *
     * @return a valid {@link RocketChatRoomManager} instance
     */
    public RocketChatRoomManager getRoomManager() {
        return this.rooms;
    }

    /**
     * Queues up a message to be sent to the server and the queue is processed twice a second (or
     * every 10 ticks).
     * <p>
     * A message must have at least the <strong>room</strong> and <strong>message</strong> set,
     * otherwise it will not be a valid message format.
     * <p>
     * If you don't want to create your own {@link IMessage} wrapper, then use {@link RocketChatMessage}.
     *
     * @param message the {@link IMessage} to send
     * @return whether the message was successfully queued up or not
     */
    public boolean sendMessage(IMessage message) {
        return message != null && message.isValid() ? this.conn.queueMessage(message) : false;
    }
}
