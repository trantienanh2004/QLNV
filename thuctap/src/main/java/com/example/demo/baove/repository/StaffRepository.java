package com.example.demo.baove.repository;

import com.example.demo.baove.entity.facility;
import com.example.demo.baove.entity.staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<staff, UUID> {
    Optional<staff> findByStaffcode(String staffcode);
    Optional<staff> findByAccountfe(String accountfe);
    Optional<staff> findByAccountfpt(String accountfpt);
}
