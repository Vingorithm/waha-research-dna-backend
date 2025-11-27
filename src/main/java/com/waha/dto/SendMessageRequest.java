package com.waha.dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String sessionId;
    private String phone;
    private String message;
}
