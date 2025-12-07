package com.waha.event;

public record WebhookEvent(String event, Object data) {}