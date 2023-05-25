package com.esthetic.reservations.api.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "comment", uniqueConstraints = { @UniqueConstraint(columnNames = { "appointment_id" }) })
public class Comment extends BaseModel<Comment> {

    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "date")
    private Date date;

    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Appointment appointment;
    
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = UserEntity.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity client;
    
    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    public Comment() {
    }

    public Comment(Long id) {
        super(id);
    }

    public Comment(Long id, Integer rating, String comment, Date date, Appointment appointment, UserEntity client,
            Employee employee) {
        super(id);
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.appointment = appointment;
        this.client = client;
        this.employee = employee;
    }

    public Comment(Integer rating, String comment, Date date, Appointment appointment, UserEntity client,
            Employee employee) {
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.appointment = appointment;
        this.client = client;
        this.employee = employee;
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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

    public UserEntity getClient() {
        return this.client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void copy(Comment comment) {
        this.rating = comment.rating;
        this.comment = comment.comment;
        this.date = comment.date;
        this.client = comment.client;
        this.employee = comment.employee;
    }

}
