package com.example.demo.baove.repository;

import com.example.demo.baove.entity.department;
import com.example.demo.baove.entity.staff;
import org.eclipse.tags.shaded.org.apache.bcel.generic.DDIV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<department, UUID> {
}
