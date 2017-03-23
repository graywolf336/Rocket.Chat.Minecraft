package com.graywolf336.rocketchat.objects;

import com.google.gson.annotations.Expose;
import com.graywolf336.rocketchat.enums.Emoji;
import com.graywolf336.rocketchat.interfaces.IMessage;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatMessage implements IMessage {
    private Emoji emojiIcon;
    @Expose()
    private String rid, msg, alias, avatar, emoji;
    @Expose()
    private boolean groupable, parseUrls;
    private IRoom room;

    public RocketChatMessage() {
        this.msg = "";
        this.rid = "";
    }

    public RocketChatMessage(String message) {
        this.msg = message;
        this.rid = "";
    }

    public IRoom getRoom() {
        return this.room;
    }

    public void setRoom(IRoom room) {
        this.room = room;

        if (room == null) {
            this.rid = "";
        } else {
            this.rid = room.getId();
        }
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Emoji getIconEmoji() {
        return this.emojiIcon;
    }

    public void setIconEmoji(Emoji iconEmoji) {
        this.emojiIcon = iconEmoji;

        if (iconEmoji == null) {
            this.emoji = "";
        } else {
            this.emoji = this.emojiIcon.getCode();
        }
    }

    public String getMessage() {
        return this.msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public String getAlias() {
        return this.alias;
    }

    public void setGroupable(boolean groupable) {
        this.groupable = groupable;
    }

    public boolean isGroupable() {
        return this.groupable;
    }
    
    public void setParseUrls(boolean parse) {
        this.parseUrls = parse;
    }
    
    public boolean willParseUrls() {
        return this.parseUrls;
    }

    public boolean isValid() {
        return !this.msg.isEmpty() && !this.rid.isEmpty();
    }
}
