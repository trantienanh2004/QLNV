package com.example.demo.baove.repository;

import com.example.demo.baove.entity.major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MajorRepository extends JpaRepository<major, UUID> {
}
