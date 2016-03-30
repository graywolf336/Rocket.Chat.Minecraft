package com.graywolf336.rocketchat.info;

import java.util.Map;

public class CallResultSuccessInfo {
	private Map<String, Object> resultFields;
	
	public CallResultSuccessInfo(Map<String, Object> resultFields) {
		this.resultFields = resultFields;
	}
	
	public Map<String, Object> getFields() {
		return this.resultFields;
	}
}
