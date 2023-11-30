package com.gym.demo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Medical_data")
public class MedicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientId")
    private Long clientId;

    @Column(name = "medicalConclusion")
    private String medicalConclusion;

    @Column(name = "recommendationsForActivities")
    private String recommendationsForActivities;

    @Column(name = "pressure")
    private String pressure;

    @Column(name = "dateTheDataWasAdded")
    private Date dateTheDataWasAdded;

    @Column(name = "pulse")
    private String pulse;

    @Column(name = "heartbeat")
    private String heartbeat;

    public MedicalData() {
    }

    public MedicalData(Long id, String medicalConclusion, String recommendationsForActivities, String pressure, String pulse, String heartbeat) {
        this.id = id;
        this.medicalConclusion = medicalConclusion;
        this.recommendationsForActivities = recommendationsForActivities;
        this.pressure = pressure;
        this.pulse = pulse;
        this.heartbeat = heartbeat;
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

    public void setClient(Long clientId) {
        this.clientId = clientId;
    }

    public String getMedicalConclusion() {
        return medicalConclusion;
    }

    public void setMedicalConclusion(String medicalConclusion) {
        this.medicalConclusion = medicalConclusion;
    }

    public String getRecommendationsForActivities() {
        return recommendationsForActivities;
    }

    public void setRecommendationsForActivities(String recommendationsForActivities) {
        this.recommendationsForActivities = recommendationsForActivities;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getPressure() {
        return pressure;
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

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getPulse() {
        return pulse;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }
}
