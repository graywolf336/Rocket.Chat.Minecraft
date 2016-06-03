package com.graywolf336.rocketchat;

import com.google.gson.internal.LinkedTreeMap;
import com.graywolf336.rocketchat.enums.RoomType;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatRoom implements IRoom {
    private String _id, name;
    private RoomType type;

    @SuppressWarnings("rawtypes")
    public RocketChatRoom(Object room, RoomType type) throws Exception {
        if (room instanceof LinkedTreeMap) {
            LinkedTreeMap r = (LinkedTreeMap) room;
            this._id = (String) r.get("_id");
            this.name = (String) r.get("name");
        } else
            throw new Exception("Invalid room object type!");

        this.type = type;
    }

    public RocketChatRoom(String id, String name, RoomType type) {
        this._id = id;
        this.name = name;
        this.type = type;
    }

    public String getID() {
        return this._id;
    }

    public String getName() {
        return this.name;
    }

    public RoomType getType() {
        return this.type;
    }
}
