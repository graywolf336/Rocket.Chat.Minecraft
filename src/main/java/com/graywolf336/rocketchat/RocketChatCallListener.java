package com.graywolf336.rocketchat;

import java.util.Map;

import com.graywolf336.rocketchat.info.CallResultErrorInfo;
import com.graywolf336.rocketchat.info.CallResultSuccessInfo;
import com.graywolf336.rocketchat.interfaces.IRocketListener;

import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;

public class RocketChatCallListener {
	private IRocketListener listener;
	
	public RocketChatCallListener(IRocketListener listener) {
		this.listener = listener;
	}
	
	protected TheRealDDPListener toDDPListener() {
		return new TheRealDDPListener();
	}
	
	@SuppressWarnings("unchecked")
	private class TheRealDDPListener extends DDPListener {
		public void onResult(Map<String, Object> resultFields) {
            if (resultFields.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) resultFields.get(DdpMessageField.ERROR);
                listener.gotResults(new CallResultErrorInfo((Double)error.get("error"), (String) error.get("message"), (String) error.get("errorType"), (String) error.get("reason")), null);
            }else {
            	listener.gotResults(null, new CallResultSuccessInfo((Map<String, Object>) resultFields.get(DdpMessageField.RESULT)));
            }
        }
	}
}
