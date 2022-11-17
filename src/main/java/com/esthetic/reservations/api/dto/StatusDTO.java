package com.esthetic.reservations.api.dto;

public class StatusDTO extends GenericModelDTO {

    private String status_name;

    public StatusDTO() {
    }

    public StatusDTO(Long id, String status_name) {
        super(id);
        this.status_name = status_name;
    }

    public String getStatus_name() {
        return this.status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

}
