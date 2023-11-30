package com.gym.demo.repository;

import com.gym.demo.model.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    Optional<TrainingProgram> findByClientId(Long clientId);

    List<TrainingProgram> findAllByClientId(Long clientId);

    @Query("select md from TrainingProgram md where md.clientId = ?1 ORDER BY md.dateTheDataWasAdded DESC limit 1")
    Optional<TrainingProgram> findLatestByClientId(Long clientId);
}
