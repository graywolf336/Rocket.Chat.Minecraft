package com.graywolf336.rocketchat.enums;

public enum SubscriptionUpdateType {
    ADDED,
    CHANGED,
    REMOVED,
    UNKNOWN;
    
    public static SubscriptionUpdateType getFromString(String value) {
        for(SubscriptionUpdateType t : SubscriptionUpdateType.values())
            if(t.toString().equalsIgnoreCase(value))
                return t;
        
        return SubscriptionUpdateType.UNKNOWN;
    }
}
