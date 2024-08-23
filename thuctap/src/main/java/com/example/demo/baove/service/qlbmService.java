package com.example.demo.baove.service;

import com.example.demo.baove.entity.StaffMajorFacilityDTO;
import com.example.demo.baove.entity.staff;
import com.example.demo.baove.entity.staffMajorFacility;
import com.example.demo.baove.repository.FacilityRepository;
import com.example.demo.baove.repository.StaffRepository;
import com.example.demo.baove.repository.staff_major_facilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class qlbmService {
    @Autowired
    staff_major_facilityRepository staff_major_facilityRepository;

    @Autowired
    StaffRepository StaffRepository;
    @Autowired
    FacilityRepository facilityRepository;

    private static int size = 5;

    public List<staffMajorFacility> GetAllStaff(){
        return staff_major_facilityRepository.findAll();
    }
    public Page<staffMajorFacility> Page(int pageNo){
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(pageNo,size,sort);
        return staff_major_facilityRepository.findAll(pageable);
    }

    public staffMajorFacility timkiemStaffMF(UUID id){
        return staff_major_facilityRepository.findById(id).orElse(null);
    }


}
