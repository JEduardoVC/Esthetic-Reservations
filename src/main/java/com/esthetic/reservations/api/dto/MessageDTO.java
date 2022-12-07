package com.esthetic.reservations.api.dto;

public class MessageDTO {
    private String message;


    public MessageDTO() {
    }


    public MessageDTO(String message) {
        this.message = message;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
