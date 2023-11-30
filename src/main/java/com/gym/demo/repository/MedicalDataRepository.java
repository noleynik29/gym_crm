package com.gym.demo.repository;

import com.gym.demo.model.MedicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalDataRepository extends JpaRepository<MedicalData, Long> {
    Optional<MedicalData> findByClientId(Long clientId);

    List<MedicalData> findAllByClientId(Long clientId);

    @Query("select md from MedicalData md where md.clientId = ?1 ORDER BY md.dateTheDataWasAdded DESC limit 1")
    Optional<MedicalData> findLatestByClientId(Long clientId);
}
