package com.graywolf336.rocketchat.listeners;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.graywolf336.rocketchat.enums.Subscription;
import com.graywolf336.rocketchat.enums.SubscriptionUpdateType;
import com.graywolf336.rocketchat.info.SubscriptionErrorInfo;
import com.graywolf336.rocketchat.info.SubscriptionUpdateInfo;
import com.graywolf336.rocketchat.interfaces.IRocketListener;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

public class RocketChatSubscriptionListener {
    private IRocketListener listener;
    private Subscription sub;
    
    public RocketChatSubscriptionListener(Subscription subscription, IRocketListener listener) {
        this.listener = listener;
        this.sub = subscription;
    }
    
    public Observer toObserver() {
        return new TheRealSubscriptionDDPListener();
    }
    
    @SuppressWarnings("unchecked")
    private class TheRealSubscriptionDDPListener implements Observer {
        public void update(Observable client, Object msg) {
            if (msg instanceof Map<?, ?>) {
                Map<String, Object> jsonFields = (Map<String, Object>) msg;
                String msgType = (String) jsonFields.get(DdpMessageField.MSG);

                if (msgType == null)
                    return;
                else if (msgType.equals(DdpMessageType.ERROR)) {
                    String mErrorSource = (String) jsonFields.get(DdpMessageField.SOURCE);
                    String mErrorMsg = (String) jsonFields.get(DdpMessageField.ERRORMSG);
                    listener.gotResults(new SubscriptionErrorInfo(mErrorSource, mErrorMsg), null);
                    return;
                }

                SubscriptionUpdateType type = SubscriptionUpdateType.getFromString(msgType);
                
                if(type != SubscriptionUpdateType.UNKNOWN) {
                    String coll = (String) jsonFields.get(DdpMessageField.COLLECTION);
                    String id = (String) jsonFields.get(DdpMessageField.ID);
                    
                    if(sub.getCollection().isEmpty() || sub.getCollection().equalsIgnoreCase(coll)) {
                        listener.gotResults(null, new SubscriptionUpdateInfo(type, coll, id, jsonFields));
                    }
                }
            }
        }
    }
}
