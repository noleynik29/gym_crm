package com.gym.demo.repository;

import com.gym.demo.model.ClientsFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsFilesRepository extends JpaRepository<ClientsFiles, Long> {
    List<ClientsFiles> findByClientId(Long clientId);

    Optional<ClientsFiles> findById(Long id);
}
