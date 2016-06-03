package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.RoomType;

public interface IRoom {
    public String getID();

    public String getName();

    public RoomType getType();
}
