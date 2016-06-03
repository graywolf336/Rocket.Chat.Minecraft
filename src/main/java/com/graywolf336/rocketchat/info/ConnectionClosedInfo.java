package com.graywolf336.rocketchat.info;

/**
 * Represents information about when a connection is closed.
 *
 * @author graywolf336
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectionClosedInfo {
    private int closeCode;
    private String closeReason;
    private boolean closedFromRemote;

    /**
     * Creates a new {@link ConnectionClosedInfo} instance.
     * 
     * @param closeCode
     * @param closeReason
     * @param closedFromRemote
     */
    public ConnectionClosedInfo(int closeCode, String closeReason, boolean closedFromRemote) {
        this.closeCode = closeCode;
        this.closeReason = closeReason;
        this.closedFromRemote = closedFromRemote;
    }

    /**
     * Gets the close code.
     * 
     * @return close code
     */
    public int getCloseCode() {
        return this.closeCode;
    }

    /**
     * Gets the close reason.
     * 
     * @return the close reason
     */
    public String getCloseReason() {
        return this.closeReason;
    }

    /**
     * Gets whether the connection was closed by the remote or not.
     * 
     * @return closed by remote or not
     */
    public boolean wasClosedByRemote() {
        return this.closedFromRemote;
    }
}
