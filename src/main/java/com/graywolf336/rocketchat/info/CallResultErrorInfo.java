package com.graywolf336.rocketchat.info;

public class CallResultErrorInfo {
	private double code;
	private String message, reason, type;
	
	public CallResultErrorInfo(double code, String message, String reason, String type) {
		this.code = code;
		this.message = message;
		this.reason = reason;
		this.type = type;
	}
	
	public double getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String toString() {
		return "ERROR: " + this.message + ". " + this.type + ". " + this.reason + ". (" + this.code + ")";
	}
}
