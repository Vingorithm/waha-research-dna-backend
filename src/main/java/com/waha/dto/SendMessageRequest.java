package com.waha.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String session;
    private String phone;
    private String message;
    
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
