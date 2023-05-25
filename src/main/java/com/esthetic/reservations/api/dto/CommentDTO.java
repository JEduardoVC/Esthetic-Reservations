package com.esthetic.reservations.api.dto;

import java.sql.Date;

public class CommentDTO extends GenericModelDTO {

    private Integer rating;

    private String comment;

    private Date date;

    // private AppointmentDTO appointment;

    private UserEntityDTO client;

    private EmployeeDTO employee;

    public CommentDTO() {
    }

    public CommentDTO(Integer rating, String comment, Date date, UserEntityDTO client, EmployeeDTO employee) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.client = client;
        this.employee = employee;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserEntityDTO getClient() {
        return this.client;
    }

    public void setClient(UserEntityDTO client) {
        this.client = client;
    }

    public EmployeeDTO getEmployee() {
        return this.employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

}
