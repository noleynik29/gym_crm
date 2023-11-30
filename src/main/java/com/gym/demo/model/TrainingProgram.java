package com.gym.demo.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "training_program")
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "dateTheDataWasAdded")
    private Date dateTheDataWasAdded;

    @Column(name = "number")
    private List<String> number;

    @Column(name = "loads")
    private List<String> loads;

    @Column(name = "reps")
    private List<String> reps;

    @Column(name = "comment")
    private String comment;

    public TrainingProgram() {
    }

    public TrainingProgram(Long id, Long clientId, Date dateTheDataWasAdded, List<String> number, List<String> loads, List<String> reps, String comment) {
        this.id = id;
        this.clientId = clientId;
        this.dateTheDataWasAdded = dateTheDataWasAdded;
        this.number = number;
        this.loads = loads;
        this.reps = reps;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDateTheDataWasAdded() {
        return dateTheDataWasAdded;
    }

    @PrePersist
    protected void onCreate() {
        if (this.dateTheDataWasAdded == null) {
            this.dateTheDataWasAdded = new Date();
        }
    }

    public List<String> getNumber() {
        return number;
    }

    public void setNumber(List<String> number) {
        this.number = number;
    }

    public List<String> getLoads() {
        return loads;
    }

    public void setLoads(List<String> loads) {
        this.loads = loads;
    }

    public List<String> getReps() {
        return reps;
    }

    public void setReps(List<String> reps) {
        this.reps = reps;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TrainingProgram{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", dateTheDataWasAdded=" + dateTheDataWasAdded +
                ", number=" + number +
                ", loads=" + loads +
                ", reps=" + reps +
                ", comment='" + comment + '\'' +
                '}';
    }
}
