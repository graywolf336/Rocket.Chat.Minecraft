package com.graywolf336.rocketchat;

import com.graywolf336.rocketchat.enums.Emoji;
import com.graywolf336.rocketchat.interfaces.IMessage;
import com.graywolf336.rocketchat.interfaces.IRoom;

public class RocketChatMessage implements IMessage {
	private Emoji emojiIcon;
	private String msg;
	
	public RocketChatMessage(String message) {
		this.msg = message;
	}

	public IRoom getRoom() {
		return null;
	}

	public void setRoom(IRoom room) {
	}

	public String getIconUrl() {
		return null;
	}

	public void setIconUrl(String iconUrl) {
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
		return !msg.isEmpty();
	}
}