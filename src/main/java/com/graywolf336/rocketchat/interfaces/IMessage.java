package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.Emoji;

public interface IMessage {
     public IRoom getRoom();
     public void setRoom(IRoom room);
     public String getIconUrl();
     public void setIconUrl(String iconUrl);
     public Emoji getIconEmoji();
     public void setIconEmoji(Emoji iconEmoji);
     public String getMessage();
     public void setMessage(String message);
     public boolean isValid();
}
