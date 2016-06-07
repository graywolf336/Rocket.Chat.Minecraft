package com.graywolf336.rocketchat.info;

import java.util.Map;

import com.graywolf336.rocketchat.enums.SubscriptionUpdateType;
import com.graywolf336.rocketchat.interfaces.IDDPSuccessInfo;

public class SubscriptionUpdateInfo implements IDDPSuccessInfo {
    private Map<String, Object> fields;
    private SubscriptionUpdateType typeOfSuccess;
    private String collection;
    private String id;
    
    public SubscriptionUpdateInfo(SubscriptionUpdateType type, String collection, String id, Map<String, Object> fields) {
        this.typeOfSuccess = type;
        this.collection = collection;
        this.id = id;
        this.fields = fields;
    }
    
    public SubscriptionUpdateType getUpdateType() {
        return this.typeOfSuccess;
    }
    
    public String getCollection() {
        return this.collection;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Map<String, Object> getFields() {
        return this.fields;
    }
}
