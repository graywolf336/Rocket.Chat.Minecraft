package com.graywolf336.rocketchat.enums;

public enum ConnectionState {
    /** When we don't have a connection made. */
    DISCONNECTED,
    /** Connection has been made but we're not logged in. */
    CONNECTED,
    /** Logged in and ready to go. */
    LOGGEDIN,
    /** An error happened to the connection. */
    ERROR,
    /** The connection is closed. */
    CLOSED,
    /** ??? */
    UNKNOWN;
}
