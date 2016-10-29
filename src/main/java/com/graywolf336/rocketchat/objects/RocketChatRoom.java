package com.graywolf336.rocketchat.objects;

import com.google.gson.internal.LinkedTreeMap;
import com.graywolf336.rocketchat.enums.RoomType;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatRoom implements IRoom {
    private String _id, name, subscription;
    private RoomType type;
    
    public RocketChatRoom(Object room) throws Exception {
        if (room instanceof LinkedTreeMap) {
            LinkedTreeMap<?, ?> r = (LinkedTreeMap<?, ?>) room;
            this._id = (String) r.get("_id");
            this.name = (String) r.get("name");
            this.type = RoomType.getByLetter((String) r.get("t"));
        } else {
            throw new Exception("Invalid room object type!");
        }
    }

    public RocketChatRoom(String id, String name, String subscriptionId, RoomType type) {
        this._id = id;
        this.name = name;
        this.subscription = subscriptionId;
        this.type = type;
    }
    
    public RocketChatRoom(String id) {
        this._id = id;
    }

    public String getId() {
        return this._id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String value) {
        this.name = value;
    }
    
    public String getSubscriptionId() {
        return this.subscription;
    }
    
    public void setSubscriptionId(String id) {
        this.subscription = id;
    }

    public RoomType getType() {
        return this.type;
    }
    
    public void setType(RoomType type) {
        this.type = type;
    }
}
