package com.graywolf336.rocketchat.interfaces;

@FunctionalInterface
public interface IRocketListener {
    public void gotResults(IDDPErrorInfo error, IDDPSuccessInfo result);
}
