package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotBlank;

public class AllowedDTO {

    @NotBlank(message = "Se requiere la url.")
    private String url;

    public AllowedDTO() {
    }

    public AllowedDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
