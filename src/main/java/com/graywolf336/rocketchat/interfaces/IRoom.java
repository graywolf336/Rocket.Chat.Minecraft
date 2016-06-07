package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.RoomType;

public interface IRoom {
    public String getId();

    public String getName();
    
    public void setName(String value);
    
    public String getSubscriptionId();
    
    public void setSubscriptionId(String id);

    public RoomType getType();
    
    public void setType(RoomType type);
}
