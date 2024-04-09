package com.group12.carrierpigeon.components.chat;

/**
 * Represents a Chat Message with a sender and message.
 */
public class ChatMessageViewHolder {

    private String sender;
    private String message;


    public ChatMessageViewHolder(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
