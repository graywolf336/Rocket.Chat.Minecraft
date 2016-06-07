package com.graywolf336.rocketchat.objects;

import com.graywolf336.rocketchat.enums.Emoji;
import com.graywolf336.rocketchat.interfaces.IMessage;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatMessage implements IMessage {
    private Emoji emojiIcon;
    private String msg, iconUrl;
    private IRoom room;

    public RocketChatMessage() {
        this.msg = "";
    }

    public RocketChatMessage(String message) {
        this.msg = message;
    }

    public IRoom getRoom() {
        return this.room;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Emoji getIconEmoji() {
        return this.emojiIcon;
    }

    public void setIconEmoji(Emoji iconEmoji) {
        this.emojiIcon = iconEmoji;
    }

    public String getMessage() {
        return this.msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public boolean isValid() {
        return !this.msg.isEmpty() && this.room != null;
    }
}
