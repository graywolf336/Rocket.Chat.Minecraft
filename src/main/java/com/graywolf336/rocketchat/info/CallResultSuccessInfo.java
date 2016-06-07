package com.graywolf336.rocketchat.info;

import java.util.Map;

import com.graywolf336.rocketchat.interfaces.IDDPSuccessInfo;

public class CallResultSuccessInfo implements IDDPSuccessInfo {
    private Map<String, Object> resultFields;

    public CallResultSuccessInfo(Map<String, Object> resultFields) {
        this.resultFields = resultFields;
    }

    public Map<String, Object> getFields() {
        return this.resultFields;
    }
}
