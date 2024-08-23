package com.example.demo.baove.repository;

import com.example.demo.baove.entity.staffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface staff_major_facilityRepository extends JpaRepository<staffMajorFacility, UUID> {
}
