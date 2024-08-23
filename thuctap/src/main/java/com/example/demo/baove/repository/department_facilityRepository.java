package com.example.demo.baove.repository;

import com.example.demo.baove.entity.departmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface department_facilityRepository extends JpaRepository<departmentFacility, UUID> {
    departmentFacility findByDepartmentIdAndFacilityId(UUID departmentId, UUID facilityId);
}
