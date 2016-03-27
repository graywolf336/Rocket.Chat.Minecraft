package com.graywolf336.rocketchat;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.bukkit.scheduler.BukkitTask;

import com.graywolf336.rocketchat.enums.ConnectionState;
import com.graywolf336.rocketchat.enums.Method;
import com.graywolf336.rocketchat.enums.Settings;
import com.keysolutions.ddpclient.DDPClient;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.EmailAuth;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

public class ConnectionManager {
    private RocketChatMain plugin;
    private DDPClient ddp;
    private ConnectionState state;
    private String userId;
    private String resumeToken;
    private long reconnectTime;
    private boolean weDisconnected;

    protected ConnectionManager(RocketChatMain plugin) {
        this.plugin = plugin;
        this.setConnectionState(ConnectionState.DISCONNECTED);
        this.resumeToken = "";
        this.reconnectTime = 10;
        this.weDisconnected = false;
    }

    /**
     * Acquires a connection to the server, with the options provided. <strong>Runs in an asynchronous task.</strong>
     * 
     * @params timeUntilRan the amount of ticks until the task is ran
     * @return the instance of the {@link BukkitTask} the code is running in.
     */
    public BukkitTask acquireConnection(long timeUntilRan) {
        return this.plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                this.ddp = new DDPClient(Settings.HOST.asString(), Settings.PORT.asInt(), Settings.SSL.asBoolean());
                this.ddp.addObserver(new ConnectionAndLoginObserver());
                this.ddp.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                this.setConnectionState(ConnectionState.ERROR);
                this.plugin.getLogger().severe("The host connection string is not valid URI syntax.");
            }
        }, timeUntilRan);
    }
    
    public void disconnectConnection() {
        //Is this even required? Does it even get called?
        if(!this.resumeToken.isEmpty()) {
            this.ddp.call("logout", null);
        }
        
        this.weDisconnected = true;
        this.ddp.disconnect();
    }

    public ConnectionState getConnectionState() {
        return this.state;
    }
    
    private void setConnectionState(ConnectionState state) {
        this.plugin.debug(false, "The connection is now changed to: " + state.toString());
        this.state = state;
        
        switch(this.state) {
            case CONNECTED:
                this.reconnectTime = 10;
                this.attemptLogin();
                break;
            case LOGGEDIN:
                this.plugin.getLogger().info("Successfully logged into Rocket.Chat!");
                break;
            case CLOSED:
                if(!weDisconnected) {
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
            this.plugin.debug(false, "Attempting to log into Rocket.Chat as: " + Settings.EMAIL.asString());
            
            Object[] emailArgs = new Object[1];
            emailArgs[0] = new EmailAuth(Settings.EMAIL.asString(), Settings.PASSWORD.asString());
            this.ddp.call(Method.LOGIN.get(), emailArgs, new ConnectionAndLoginObserver());
        }, 0);
    }
    
    @SuppressWarnings("unchecked")
    private class ConnectionAndLoginObserver extends DDPListener implements Observer {
        public void update(Observable client, Object msg) {
            if (msg instanceof Map<?, ?>) {
                Map<String, Object> jsonFields = (Map<String, Object>) msg;
                String msgtype = (String) jsonFields.get(DDPClient.DdpMessageField.MSG);

                if (msgtype == null) {
                    return;
                }

                if (msgtype.equals(DdpMessageType.ERROR)) {
                    String mErrorSource = (String) jsonFields.get(DdpMessageField.SOURCE);
                    String mErrorMsg = (String) jsonFields.get(DdpMessageField.ERRORMSG);
                    plugin.getLogger().warning(mErrorSource + ": " + mErrorMsg);
                }

                if (msgtype.equals(DdpMessageType.CONNECTED)) {
                    String mSessionId = (String) jsonFields.get(DdpMessageField.SESSION);
                    setConnectionState(ConnectionState.CONNECTED);
                    plugin.getLogger().info("Connected and the session id is: " + mSessionId);
                }

                if (msgtype.equals(DdpMessageType.CLOSED)) {
                    int mCloseCode = Integer.parseInt(jsonFields.get(DdpMessageField.CODE).toString());
                    String mCloseReason = (String) jsonFields.get(DdpMessageField.REASON);
                    boolean mCloseFromRemote = (Boolean) jsonFields.get(DdpMessageField.REMOTE);

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
                
                //The user login failed, so we just need to disconnect.
                disconnectConnection();
            }else {
                Map<String, Object> result = (Map<String, Object>) resultFields.get(DdpMessageField.RESULT);
                resumeToken = (String) result.get("token");
                userId = (String) result.get("id");
                setConnectionState(ConnectionState.LOGGEDIN);
            }
        }
    }
}