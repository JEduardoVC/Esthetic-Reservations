package com.esthetic.reservations.api.dto;

import com.esthetic.reservations.api.model.Branch;

public class ServiceDTO extends GenericModelDTO{


    private String service_name;

    private Integer duration;

    private Double price;

    private Branch id_branch;

    public ServiceDTO() {
    }


    public ServiceDTO(Long id, String service_name, Integer duration, Double price, Branch id_branch) {
        super(id);
        this.service_name = service_name;
        this.duration = duration;
        this.price = price;
        this.id_branch = id_branch;
    }

    public String getService_name() {
        return this.service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Branch getId_branch() {
        return this.id_branch;
    }

    public void setId_branch(Branch id_branch) {
        this.id_branch = id_branch;
    }

}
