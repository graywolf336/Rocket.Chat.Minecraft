package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.enums.Emoji;

public interface IMessage {
    public IRoom getRoom();

    public void setRoom(IRoom room);

    public String getAvatar();

    public void setAvatar(String avatar);

    public Emoji getIconEmoji();

    public void setIconEmoji(Emoji iconEmoji);

    public String getMessage();

    public void setMessage(String message);
    
    public void setAlias(String alias);
    
    public String getAlias();
    
    public void setGroupable(boolean groupable);
    
    public boolean isGroupable();
    
    public void setParseUrls(boolean parse);
    
    public boolean willParseUrls();

    public boolean isValid();
}
