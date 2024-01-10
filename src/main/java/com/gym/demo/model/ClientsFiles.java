package com.gym.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Clients_files")
public class ClientsFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @JoinColumn(name = "client_id")
    private Long clientId;

    public ClientsFiles() {
    }

    public ClientsFiles(String fileName, Long clientId) {
        this.fileName = fileName;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
