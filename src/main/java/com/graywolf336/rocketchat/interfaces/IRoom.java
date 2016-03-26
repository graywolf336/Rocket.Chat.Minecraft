package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.RoomType;

public interface IRoom {
    public String getId();
    public String getName();
    public RoomType getType();
}
