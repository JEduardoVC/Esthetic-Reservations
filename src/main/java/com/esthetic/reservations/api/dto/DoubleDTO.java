package com.esthetic.reservations.api.dto;

public class DoubleDTO {
    

    private Double value;


    public DoubleDTO() {
    }


    public DoubleDTO(Double value) {
        this.value = value;
    }


    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
