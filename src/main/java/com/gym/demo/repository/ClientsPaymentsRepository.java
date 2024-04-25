package com.gym.demo.repository;

import com.gym.demo.model.ClientsPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsPaymentsRepository extends JpaRepository<ClientsPayments, Long> {
    Optional<ClientsPayments> findFirstByOrderByDateOfCreation();
}
