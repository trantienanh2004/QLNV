package com.example.demo.baove.repository;

import com.example.demo.baove.entity.facility;
import com.example.demo.baove.entity.major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FacilityRepository extends JpaRepository<facility, UUID>{

}
