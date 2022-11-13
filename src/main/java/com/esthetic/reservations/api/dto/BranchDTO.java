package com.esthetic.reservations.api.dto;

import com.esthetic.reservations.api.model.UserEntity;

public class BranchDTO extends GenericModelDTO {

    private String name;

    private String location;

    private UserEntity id_owner;

    public BranchDTO() {
    }

    public BranchDTO(Long id, String name, String location, UserEntity id_owner) {
        super(id);
        this.name = name;
        this.location = location;
        this.id_owner = id_owner;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserEntity getId_owner() {
        return this.id_owner;
    }

    public void setId_owner(UserEntity id_owner) {
        this.id_owner = id_owner;
    }

}