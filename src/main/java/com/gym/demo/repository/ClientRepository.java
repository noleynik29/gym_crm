package com.gym.demo.repository;

import com.gym.demo.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    @Query("select c from Client c where c.name = ?1 OR c.surname = ?1 OR c.mobilePhone =?1")
    Page<Client> findByNameOrSurnameContainingIgnoreCase(String keyword, Pageable pageable);
}

