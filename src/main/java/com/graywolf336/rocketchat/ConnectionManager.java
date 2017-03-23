package com.graywolf336.rocketchat;

import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.bukkit.scheduler.BukkitTask;

import com.google.gson.GsonBuilder;
import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.enums.Method;
import com.graywolf336.rocketchat.enums.Settings;
import com.graywolf336.rocketchat.events.RocketChatConnectionClosedEvent;
import com.graywolf336.rocketchat.events.RocketChatConnectionConnectedEvent;
import com.graywolf336.rocketchat.events.RocketChatSuccessfulLoginEvent;
import com.graywolf336.rocketchat.info.ConnectionClosedInfo;
import com.graywolf336.rocketchat.info.ConnectionConnectedInfo;
import com.graywolf336.rocketchat.listeners.RocketChatCallListener;
import com.graywolf336.rocketchat.objects.RocketChatAuthModel;
import com.graywolf336.rocketchat.objects.RocketChatMessage;
import com.graywolf336.rocketchat.objects.RocketChatSubscription;
import com.keysolutions.ddpclient.DDPClient;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

/**
 * Handles all of the connection related items, including connecting, calling methods, subscribing,
 * and sending messages.
 * <p>
 * There should really only be one instance of this, or else multiple events will probably be called
 * when we expect only one.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public class ConnectionManager {
    private RocketChatMain plugin;
    private DDPClient ddp;
    private ConnectionState state;
    private String userId;
    private String resumeToken;
    private long reconnectTime;
    private boolean weDisconnected;

    private ConnectionConnectedInfo connectedInfo;
    private ConnectionClosedInfo closedInfo;

    private BukkitTask queueTask;
    private List<RocketChatMessage> messageQueue;
    private List<RocketChatSubscription> subscriptionQueue, activeSubscriptions;

    protected ConnectionManager(RocketChatMain plugin) {
        this.plugin = plugin;
        this.setConnectionState(ConnectionState.DISCONNECTED);
        this.resumeToken = "";
        this.reconnectTime = 10;
        this.weDisconnected = false;

        this.messageQueue = new LinkedList<RocketChatMessage>();
        
        this.subscriptionQueue = new LinkedList<RocketChatSubscription>();
        this.activeSubscriptions = new LinkedList<RocketChatSubscription>();
    }
    
    /**
     * Gets the current state of the connection.
     *
     * @return the {@link ConnectionState state of the connection}
     */
    public ConnectionState getConnectionState() {
        return this.state;
    }

    /**
     * Acquires a connection to the server, with the options provided. <strong>Runs in an
     * asynchronous task unless the time is -1.</strong>
     *
     * @param timeUntilRun the amount of ticks until the task is ran
     * @return the instance of the {@link BukkitTask} the code is running in.
     */
    protected BukkitTask acquireConnection(long timeUntilRun) {
        this.weDisconnected = false;
        this.connectedInfo = null;
        this.closedInfo = null;

        if (timeUntilRun == -1) {
            this.tryToConnect();
            return null;
        } else {
            return this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                this.tryToConnect();
            }, timeUntilRun);
        }
    }
    
    private void tryToConnect() {
        try {
            this.ddp = new DDPClient(Settings.HOST.asString(), Settings.PORT.asInt(), Settings.SSL.asBoolean(), new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE).excludeFieldsWithoutExposeAnnotation().create());
            this.ddp.addObserver(new ConnectionAndLoginObserver());
            this.ddp.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            this.setConnectionState(ConnectionState.ERROR);
            this.plugin.getLogger().severe("The host connection string is not valid URI syntax.");
        }
    }

    /** Disconnects from the Rocket.Chat server, without reconnecting. */
    protected void disconnectConnection() {
        // Finish processing the queue
        this.processMessageQueue();

        // Is this even required? Does it even get called?
        if (!this.resumeToken.isEmpty()) {
            this.ddp.call(Method.LOGOUT.get(), null);
        }

        this.state = ConnectionState.CLOSED;
        this.weDisconnected = true;
        this.ddp.disconnect();
    }

    private void setConnectionState(ConnectionState state) {
        this.plugin.debug(false, "The connection is now changed to: " + state.toString());
        this.state = state;

        switch (this.state) {
            case CONNECTED:
                this.plugin.getServer().getPluginManager().callEvent(new RocketChatConnectionConnectedEvent(this.state, connectedInfo));
                this.reconnectTime = 10;
                this.attemptLogin();
                break;
            case LOGGEDIN:
                this.plugin.getServer().getPluginManager().callEvent(new RocketChatSuccessfulLoginEvent(this.state, this.userId));
                this.plugin.getLogger().info("Successfully logged into Rocket.Chat!");
                this.startProcessingQueue();
                this.setUserOnlineStatus(true);
                this.plugin.getRegistry().onSuccessfulConnection(this.plugin.getRocketChatClient());
                break;
            case CLOSED:
                this.plugin.getServer().getPluginManager().callEvent(new RocketChatConnectionClosedEvent(this.state, this.closedInfo));
                this.setUserOnlineStatus(false);
                this.stopProcessingQueue();
                this.plugin.getRegistry().onFailedConnection(this.plugin.getRocketChatClient());
                if (!weDisconnected) {
                    this.plugin.getLogger().warning("Lost connection, attempting to acquire a connection in " + this.reconnectTime + " ticks.");
                    this.acquireConnection(reconnectTime);
                    this.reconnectTime = this.reconnectTime * 2;
                }
                break;
            default:
                break;
        }
    }

    private BukkitTask attemptLogin() {
        return this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            Object[] loginArgs = new Object[1];
            if (Settings.EMAIL.asString().isEmpty() && Settings.USERNAME.asString().isEmpty()) {
                this.plugin.debug(false, "Attempting to log into Rocket.Chat with the auth token.");
                loginArgs[0] = new RocketChatAuthModel("", Settings.PASSWORD.asString(), RocketChatAuthModel.RocketChatAuthMethod.TOKEN);
            } else if (Settings.USERNAME.asString().isEmpty()) {
                this.plugin.debug(false, "Attempting to log into Rocket.Chat as: " + Settings.EMAIL.asString());
                loginArgs[0] = new RocketChatAuthModel(Settings.EMAIL   .asString(), Settings.PASSWORD.asString(), RocketChatAuthModel.RocketChatAuthMethod.USERNAME);
            } else {
                this.plugin.debug(false, "Attempting to log into Rocket.Chat as: " + Settings.USERNAME.asString());
                loginArgs[0] = new RocketChatAuthModel(Settings.USERNAME.asString(), Settings.PASSWORD.asString(), RocketChatAuthModel.RocketChatAuthMethod.USERNAME);
            }
            this.ddp.call(Method.LOGIN.get(), loginArgs, new ConnectionAndLoginObserver());
        }, 0);
    }

    private void startProcessingQueue() {
        this.plugin.debug(false, "Started processing the queue.");
        this.queueTask = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.processSubscriptionQueue();
            this.processMessageQueue();
        }, 0, 10);
    }
    
    private void processSubscriptionQueue() {
        List<RocketChatSubscription> processing = new LinkedList<RocketChatSubscription>(this.subscriptionQueue);
        this.subscriptionQueue.clear();
        
        processing.forEach(s -> {
            this.ddp.subscribe(s.getName(), s.getParams());
            this.ddp.addObserver(s.getObserver());
            this.activeSubscriptions.add(s);
        });
    }

    private void processMessageQueue() {
        List<RocketChatMessage> processing = new LinkedList<RocketChatMessage>(this.messageQueue);
        this.messageQueue.clear();

        processing.forEach(m -> ddp.call(Method.SENDMESSAGE.get(), new Object[] { m }));
    }

    private void stopProcessingQueue() {
        if (this.queueTask != null) {
            this.plugin.debug(false, "Stopped processing the queue.");
            this.queueTask.cancel();
            this.queueTask = null;
        }
    }

    private void setUserOnlineStatus(boolean online) {
        Object[] params = new Object[1];
        params[0] = online ? "online": "offline";
        
        this.callMethod(Method.ONLINESTATUS, params, null);
    }
    
    //TODO: Queue up methods to be called if the connection isn't active
    protected int callMethod(Method method, Object[] params, RocketChatCallListener listener) {
        return this.ddp.call(method.get(), params, listener == null ? null : listener.toDDPListener());
    }

    /**
     * Removes a subscription from the system, will remove it from the active or queued subscription lists.
     * 
     * @param subscription the {@link RocketChatSubscription} to remove
     * @return whether it was removed or not, false indicates it didn't exist
     */
    protected boolean removeSubscription(RocketChatSubscription subscription) {
        if(this.activeSubscriptions.contains(subscription)) {
            this.ddp.deleteObserver(subscription.getObserver());
            this.activeSubscriptions.remove(subscription);
            return true;
        } else if(this.subscriptionQueue.contains(subscription)) {
            this.subscriptionQueue.remove(subscription);
            return true;
        }
        
        return false;
    }
    
    /**
     * Adds a {@link RocketChatSubscription} to the queue. This queue is process twice a second.
     * <p>
     * The subscription is not active until the queue is processed, so there is a 10 tick delay (0.5 second).
     * It will not add add duplicate subscriptions.
     * 
     * @param subscription the {@link RocketChatSubscription} to queue up
     * @return whether the subscription was added or not
     */
    protected boolean queueSubscription(RocketChatSubscription subscription) {
        if(!this.subscriptionQueue.contains(subscription) && !this.activeSubscriptions.contains(subscription)) {
            return this.subscriptionQueue.add(subscription);
        }
        
        return false;
    }

    /**
     * Adds a message to the queue. The queue is processed twice a second.
     *
     * @param message the {@link RocketChatMessage} to queue
     * @return whether it was added to the queue or not
     */
    protected boolean queueMessage(RocketChatMessage message) {
        return this.messageQueue.add(message);
    }

    @SuppressWarnings("unchecked")
    private class ConnectionAndLoginObserver extends DDPListener implements Observer {
        public void update(Observable client, Object msg) {
            if (msg instanceof Map<?, ?>) {
                Map<String, Object> jsonFields = (Map<String, Object>) msg;
                String msgtype = (String) jsonFields.get(DDPClient.DdpMessageField.MSG);

                if (msgtype == null)
                    return;

                if (msgtype.equals(DdpMessageType.ERROR)) {
                    String mErrorSource = (String) jsonFields.get(DdpMessageField.SOURCE);
                    String mErrorMsg = (String) jsonFields.get(DdpMessageField.ERRORMSG);
                    plugin.getLogger().warning(mErrorSource + ": " + mErrorMsg);
                }

                if (msgtype.equals(DdpMessageType.CONNECTED)) {
                    String mSessionId = (String) jsonFields.get(DdpMessageField.SESSION);

                    connectedInfo = new ConnectionConnectedInfo(mSessionId);
                    setConnectionState(ConnectionState.CONNECTED);
                    plugin.debug(false, "Connected and the session id is: " + mSessionId);
                    return;
                }

                if (msgtype.equals(DdpMessageType.CLOSED)) {
                    int mCloseCode = Integer.parseInt(jsonFields.get(DdpMessageField.CODE).toString());
                    String mCloseReason = (String) jsonFields.get(DdpMessageField.REASON);
                    boolean mCloseFromRemote = (Boolean) jsonFields.get(DdpMessageField.REMOTE);

                    closedInfo = new ConnectionClosedInfo(mCloseCode, mCloseReason, mCloseFromRemote);
                    setConnectionState(ConnectionState.CLOSED);
                    plugin.debug(false, "Closed connection" + (mCloseFromRemote ? " from the remote " : " ") + "with a reason of '" + mCloseReason + "' and a close code of " + mCloseCode);
                }

                if (msgtype.equals(DdpMessageType.ADDED)) {
                    String collName = (String) jsonFields.get(DdpMessageField.COLLECTION);
                    String id = (String) jsonFields.get(DdpMessageField.ID);
                    plugin.debug(false, "Added docid " + id + " to collection " + collName);
                }

                if (msgtype.equals(DdpMessageType.REMOVED)) {
                    String collName = (String) jsonFields.get(DdpMessageField.COLLECTION);
                    String docId = (String) jsonFields.get(DdpMessageField.ID);
                    plugin.debug(false, "Removed docid " + docId + " from collection " + collName);
                }

                if (msgtype.equals(DdpMessageType.CHANGED)) {
                    String collName = (String) jsonFields.get(DdpMessageField.COLLECTION);
                    String docId = (String) jsonFields.get(DdpMessageField.ID);

                    plugin.debug(false, "Changed docid " + docId + " in collection " + collName);
                }
            }
        }

        public void onResult(Map<String, Object> resultFields) {
            if (resultFields.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) resultFields.get(DdpMessageField.ERROR);
                plugin.getLogger().severe("Failed to log into Rocket.Chat, the result came back: " + ((String) error.get("reason")));

                // The user login failed, so we just need to disconnect.
                disconnectConnection();
            } else {
                Map<String, Object> result = (Map<String, Object>) resultFields.get(DdpMessageField.RESULT);
                resumeToken = (String) result.get("token");
                userId = (String) result.get("id");
                setConnectionState(ConnectionState.LOGGEDIN);
            }
        }
    }
}
