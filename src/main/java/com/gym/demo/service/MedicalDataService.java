package com.gym.demo.service;

import com.gym.demo.model.Client;
import com.gym.demo.model.MedicalData;
import com.gym.demo.repository.ClientRepository;
import com.gym.demo.repository.MedicalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalDataService {
    private final MedicalDataRepository medicalDataRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public MedicalDataService(MedicalDataRepository medicalDataRepository, ClientRepository clientRepository) {
        this.medicalDataRepository = medicalDataRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Optional<MedicalData> getLatestMedicalData(Long clientId) {
        return medicalDataRepository.findLatestByClientId(clientId);
    }

    public List<MedicalData> getAllMedicalDataByClientId(Long clientId) {
        return medicalDataRepository.findAllByClientId(clientId);
    }

    @Transactional
    public MedicalData saveMedicalData(Long clientId, MedicalData medicalData) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            medicalData.setClient(clientId);
            return medicalDataRepository.save(medicalData);
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    @Transactional
    public void deleteMedicalData(Long id) {
        medicalDataRepository.deleteById(id);
    }
}
