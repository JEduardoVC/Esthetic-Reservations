package com.esthetic.reservations.api.dto;

import java.sql.Date;

public class CommentMinDTO extends GenericModelDTO {

    private Integer rating;

    private String comment;

    private Date date;

    private Long appointmentId;

    private Long clientId;

    private Long employeeId;

    public CommentMinDTO() {
    }

    public CommentMinDTO(Integer rating, String comment, Date date, Long appointmentId, Long clientId, Long employeeId) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.employeeId = employeeId;
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

    public Long getAppointmentId() {
        return this.appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

}
