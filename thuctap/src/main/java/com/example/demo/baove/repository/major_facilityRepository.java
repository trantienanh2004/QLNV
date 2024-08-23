package com.example.demo.baove.repository;

import com.example.demo.baove.entity.majorFacility;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface major_facilityRepository extends JpaRepository<majorFacility, UUID> {
}
