package com.graywolf336.rocketchat.interfaces;

import com.graywolf336.rocketchat.info.CallResultErrorInfo;
import com.graywolf336.rocketchat.info.CallResultSuccessInfo;

@FunctionalInterface
public interface IRocketListener {
	public void gotResults(CallResultErrorInfo error, CallResultSuccessInfo result);
}
