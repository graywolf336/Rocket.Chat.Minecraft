package com.graywolf336.rocketchat.enums;

public enum RoomType {
    CHANNEL,
    DIRECT_MESSAGE,
    PRIVATE_GROUP;
    
    public static RoomType getByLetter(String value) {
        switch(value) {
            case "d":
                return RoomType.DIRECT_MESSAGE;
            case "p":
                return RoomType.PRIVATE_GROUP;
            case "c":
            default:
                return RoomType.CHANNEL;
        }
    }
}
