package com.example.encrypto;

public class MessageDataModel {
    private String message = "";
    private String sender = "";

    // No-arg constructor needed by Firebase
    public MessageDataModel() {
    }

    public MessageDataModel(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    // Getter and setter for 'Message'
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter and setter for 'sender'
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
