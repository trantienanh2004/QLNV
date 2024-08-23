package com.example.demo.baove.repository;

import com.example.demo.baove.entity.lichsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface lichsuRepository extends JpaRepository<lichsu,Integer> {
}
