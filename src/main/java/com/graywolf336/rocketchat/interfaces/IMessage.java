package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.Emoji;

public interface IMessage {
     public IRoom getRoom();
     public String getIconUrl();
     public Emoji getIconEmoji();
     public boolean isValid();
}
