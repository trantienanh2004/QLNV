package com.example.demo.baove.service;

import com.example.demo.baove.entity.*;
import com.example.demo.baove.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class x {

    @Autowired
    staff_major_facilityRepository staff_major_facilityRepository;
   @Autowired
    major_facilityRepository major_facilityRepository;
   @Autowired
   department_facilityRepository department_facilityRepository;
@Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    FacilityRepository facilityRepository;
    public List<StaffMajorFacilityDTO> hienthinhanvien(){
        return staff_major_facilityRepository.findAll().stream().map(staffMF -> new StaffMajorFacilityDTO(
                staffMF.getStaff().getStaffcode(),
                staffMF.getStaff().getName(),
                staffMF.getStaff().getAccountfe(),
                staffMF.getStaff().getAccountfpt(),
                staffMF.getMajorFacility().getMajor().getName()
                        + "-" + staffMF.getMajorFacility().getDepartmentFacility().getDepartment().getName()
                        + "-" + staffMF.getMajorFacility().getDepartmentFacility().getFacility().getName()
        )).collect(Collectors.toList());

    }

    public staffMajorFacility timkiemstaffMF(UUID id){
        return staff_major_facilityRepository.findById(id).orElse(null);
    }
    public void xoaBM(UUID id){
        staff_major_facilityRepository.deleteById(id);
    }
    public void xoadepartmentF(UUID id){
        department_facilityRepository.deleteById(id);
    }
    public void xoaMajoyF(UUID id){
        major_facilityRepository.deleteById(id);
    }

    public List<department> getalldepartment(){
        return departmentRepository.findAll();
    }
    public List<facility> getallfacility(){
        return facilityRepository.findAll();
    }
    public List<major> getallmajor(){
        return majorRepository.findAll();
    }

    public departmentFacility findByDepartmentAndFacility(UUID departmentId, UUID facilityId) {
        return department_facilityRepository.findByDepartmentIdAndFacilityId(departmentId, facilityId);
    }

    public departmentFacility saved(departmentFacility departmentFacility) {
        return department_facilityRepository.save(departmentFacility);
    }
    public staffMajorFacility saveSMF(staffMajorFacility staffMajorFacility) {
        return staff_major_facilityRepository.save(staffMajorFacility);
    }
    public majorFacility saveMF(majorFacility majorFacility) {
        return major_facilityRepository.save(majorFacility);
    }

}
