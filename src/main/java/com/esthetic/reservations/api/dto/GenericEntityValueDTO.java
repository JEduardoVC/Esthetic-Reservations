package com.esthetic.reservations.api.dto;

public class GenericEntityValueDTO {

    private String value;

    public GenericEntityValueDTO() {
    }

    public GenericEntityValueDTO(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
