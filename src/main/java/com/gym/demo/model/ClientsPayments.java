package com.gym.demo.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Clients_payments")
public class ClientsPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="dateOfCreation")
    private Date dateOfCreation;

    @Column(name = "clients")
    private List<String> clients;

    @Column(name = "cash_payments")
    private List<BigDecimal> cashPayments;

    @Column(name = "card_payments")
    private List<BigDecimal> cardPayments;

    @Column(name = "dates")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<Date> dates;

    public ClientsPayments() {
    }

    public ClientsPayments(Long id, String name, Date dateOfCreation, List<String> clients, List<BigDecimal> cashPayments, List<BigDecimal> cardPayments, List<Date> dates) {
        this.id = id;
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.clients = clients;
        this.cashPayments = cashPayments;
        this.cardPayments = cardPayments;
        this.dates = dates;
    }

    public Long getId() {
        return id;
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

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    @PrePersist
    protected void onCreate() {
        if (this.dateOfCreation == null) {
            this.dateOfCreation = new Date();
        }
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    public List<BigDecimal> getCashPayments() {
        return cashPayments;
    }

    public void setCashPayments(List<BigDecimal> cashPayments) {
        this.cashPayments = cashPayments;
    }

    public List<BigDecimal> getCardPayments() {
        return cardPayments;
    }

    public void setCardPayments(List<BigDecimal> cardPayments) {
        this.cardPayments = cardPayments;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }
}