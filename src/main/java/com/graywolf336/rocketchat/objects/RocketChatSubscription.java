package com.graywolf336.rocketchat.objects;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.graywolf336.rocketchat.RocketChatClient;
import com.graywolf336.rocketchat.enums.SubscriptionUpdateType;
import com.graywolf336.rocketchat.info.SubscriptionErrorInfo;
import com.graywolf336.rocketchat.info.SubscriptionUpdateInfo;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

/**
 * Represents a RocketChatSubscription which we want to get updates on when things happen.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class RocketChatSubscription {
    private RealRocketChatSubscriptionObserver instance;
    private RocketChatClient client;

    public RocketChatSubscription(RocketChatClient client) {
        this.client = client;
    }

    /**
     * Gets the name of the Subscription, this is what is called to Rocket.Chat.
     *
     * @return name of the subscription
     */
    public abstract String getName();

    /**
     * Gets the name of the Collection, this is optional but preferred.
     *
     * @return name of the colection
     */
    public abstract String getCollection();

    /**
     * Gets the params to call this Subscription with, defaults to null (aka none).
     *
     * @return the params to call the subscription with
     */
    public Object[] getParams() {
        return null;
    }

    /**
     * Removes this subscription from being active.
     * <p>
     * If it is still in the queue, it will be removed as well.
     *
     * @return
     */
    public boolean remove() {
        this.client.removeSubscription(this);
        this.client = null;
        return false;
    }

    /**
     * Method which gets called when there was an error result with this subscription.
     *
     * @param errorInfo the {@link SubscriptionErrorInfo error information}
     * @see {@link SubscriptionErrorInfo}
     */
    public abstract void gotErrorResults(SubscriptionErrorInfo errorInfo);

    /**
     * Method which gets called when there was a success result with this subscription.
     *
     * @param updateInfo the {@link SubscriptionUpdateInfo update information}
     * @see {@link SubscriptionUpdateInfo}
     */
    public abstract void gotSuccessResults(SubscriptionUpdateInfo updateInfo);

    /**
     * The instance of the observer used for this subscription.
     *
     * @return the {@link Observer} instance for the subscription
     */
    public Observer getObserver() {
        if (instance == null) {
            this.instance = new RealRocketChatSubscriptionObserver();
        }

        return this.instance;
    }

    @SuppressWarnings("unchecked")
    private class RealRocketChatSubscriptionObserver implements Observer {
        public void update(Observable client, Object msg) {
            if (msg instanceof Map<?, ?>) {
                Map<String, Object> jsonFields = (Map<String, Object>) msg;
                String msgType = (String) jsonFields.get(DdpMessageField.MSG);

                if (msgType == null)
                    return;
                else if (msgType.equals(DdpMessageType.ERROR)) {
                    String mErrorSource = (String) jsonFields.get(DdpMessageField.SOURCE);
                    String mErrorMsg = (String) jsonFields.get(DdpMessageField.ERRORMSG);
                    gotErrorResults(new SubscriptionErrorInfo(mErrorSource, mErrorMsg));
                    return;
                }

                SubscriptionUpdateType type = SubscriptionUpdateType.getFromString(msgType);

                if (type != SubscriptionUpdateType.UNKNOWN) {
                    String coll = (String) jsonFields.get(DdpMessageField.COLLECTION);
                    String id = (String) jsonFields.get(DdpMessageField.ID);

                    if (getCollection().isEmpty() || getCollection().equalsIgnoreCase(coll)) {
                        gotSuccessResults(new SubscriptionUpdateInfo(type, coll, id, jsonFields));
                    }
                }
            }
        }
    }
}
