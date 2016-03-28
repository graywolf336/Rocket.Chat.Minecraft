package com.graywolf336.rocketchat.serializable;

import com.graywolf336.rocketchat.interfaces.IMessage;

public class RocketChatSerializerFactory {
	public static Message getMessage(IMessage message) {
		return new Message(message.getMessage());
	}
}
