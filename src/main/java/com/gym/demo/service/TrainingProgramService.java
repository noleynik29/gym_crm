package com.gym.demo.service;

import com.gym.demo.model.Client;
import com.gym.demo.model.TrainingProgram;
import com.gym.demo.repository.ClientRepository;
import com.gym.demo.repository.TrainingProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public TrainingProgramService(TrainingProgramRepository trainingProgramRepository, ClientRepository clientRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public TrainingProgram saveTrainingProgram(Long clientId, TrainingProgram trainingProgram) {
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            trainingProgram.setClientId(clientId);
            return trainingProgramRepository.save(trainingProgram);
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    @Transactional
    public void deleteTrainingProgram(Long id) {
        trainingProgramRepository.deleteById(id);
    }

    @Transactional
    public Optional<TrainingProgram> getLatestTrainingProgram(Long clientId) {
        return trainingProgramRepository.findLatestByClientId(clientId);
    }

    public List<TrainingProgram> getAllTrainingProgramByClientId(Long clientId) {
        return trainingProgramRepository.findAllByClientId(clientId);
    }
}
