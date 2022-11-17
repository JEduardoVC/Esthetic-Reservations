package com.esthetic.reservations.api.dto;

public class GenericModelDTO {

    private Long id;

    public GenericModelDTO() {
    }

    public GenericModelDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
