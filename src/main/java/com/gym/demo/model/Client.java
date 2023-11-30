package com.gym.demo.model;

import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 255, message = "Имя должно быть от 2 до 225 символов")
    private String name;

    @NotNull
    @Size(min = 2, max = 255, message = "Фамилия должна быть от 2 до 225 символов")
    private String surname;

    @Column(name = "mobilePhone")
    @Size(min = 10, max = 12)
    @Pattern(regexp = "^[0-9]{10,12}$", message = "Номер мобильного должен состоять из 10-12 цифр")
    private String mobilePhone;

    @Column(name = "date", nullable = false, updatable = false)
    private Date dateOfRegistration;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "subscription")
    private String subscription;

    public Long getId() {
        return id;
    }

    public Client() {
    }

    public Client(Long id, String name, String surname, String mobilePhone, Date dateOfBirth, String subscription) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mobilePhone = mobilePhone;
        this.dateOfBirth = dateOfBirth;
        this.subscription = subscription;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.dateOfBirth = dateFormat.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    @PrePersist
    protected void onCreate() {
        if (this.dateOfRegistration == null) {
            this.dateOfRegistration = new Date();
        }
    }
}

