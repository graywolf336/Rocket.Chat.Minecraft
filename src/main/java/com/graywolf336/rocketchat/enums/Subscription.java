package com.graywolf336.rocketchat.enums;

public enum Subscription {
    ACTIVE_USERS("activeUsers"),
    SUBSCRIPTIONS("subscription", "rocketchat_subscription");
    
    private String name, collection;
    
    private Subscription(String name) {
        this.name = name;
    }
    
    private Subscription(String name, String collection) {
        this.name = name;
        this.collection = collection;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCollection() {
        return this.collection;
    }
}
