package com.waha.dto;

import lombok.Data;

@Data
public class StatusTextRequest {
    private String id;  // optional
    private String contacts; // optional
    private String text;
    private String backgroundColor;
    private int font;
    private boolean linkPreview;
    private boolean linkPreviewHighQuality;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public int getFont() {
		return font;
	}
	public void setFont(int font) {
		this.font = font;
	}
	public boolean isLinkPreview() {
		return linkPreview;
	}
	public void setLinkPreview(boolean linkPreview) {
		this.linkPreview = linkPreview;
	}
	public boolean isLinkPreviewHighQuality() {
		return linkPreviewHighQuality;
	}
	public void setLinkPreviewHighQuality(boolean linkPreviewHighQuality) {
		this.linkPreviewHighQuality = linkPreviewHighQuality;
	}
}

