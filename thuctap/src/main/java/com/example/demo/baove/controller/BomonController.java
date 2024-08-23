package com.example.demo.baove.controller;

import com.example.demo.baove.entity.*;
import com.example.demo.baove.service.nhanvienService;
import com.example.demo.baove.service.qlbmService;
import com.example.demo.baove.service.x;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trangchu")
public class BomonController {
@Autowired
    qlbmService qlbmService;
    @Autowired
    nhanvienService nhanvienService;
    @Autowired
     x x;

    UUID idday;
    @GetMapping("/bomon/{id}")
    public String hienthi(Model model, @PathVariable("id") UUID id) {
        staff staff = nhanvienService.timkiemStaff(id);


        List<staffMajorFacility> filteredBM = qlbmService.GetAllStaff().stream()
                .filter(bm -> bm.getStaff().getId().equals(staff.getId()))
                .collect(Collectors.toList());


        List<facility> allFacilities = x.getallfacility();


        List<facility> availableFacilities = allFacilities.stream()
                .filter(f -> filteredBM.stream()
                        .noneMatch(bm -> bm.getMajorFacility().getDepartmentFacility().getFacility().getId().equals(f.getId())))
                .collect(Collectors.toList());


        model.addAttribute("department", x.getalldepartment());
        model.addAttribute("major", x.getallmajor());
        model.addAttribute("facility", availableFacilities);
        model.addAttribute("staff", staff);
        model.addAttribute("ListBM", filteredBM);
        idday = id;
        return "qlbm";
    }

    @GetMapping("/delete/{id}")
    public String xoaBM(@PathVariable("id") UUID id) {
        try {

            staffMajorFacility staffMajorFacilities = x.timkiemstaffMF(id);

            if (staffMajorFacilities != null) {
                UUID majorFacilityId = staffMajorFacilities.getMajorFacility().getId();
                UUID departmentFacilityId = staffMajorFacilities.getMajorFacility().getDepartmentFacility().getId();

                x.xoaBM(staffMajorFacilities.getId());
                x.xoaMajoyF(majorFacilityId);
                x.xoadepartmentF(departmentFacilityId);

                return "redirect:/trangchu/bomon/" + idday;
            } else {
                return "redirect:/trangchu/bomon/" + idday;
            }
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return "redirect:/trangchu/bomon/" + idday;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/trangchu/bomon/" + idday;
        }
    }


    @PostMapping("/addBM")
    public String addBM(@RequestParam("departmentSelect") UUID departmentId,
                        @RequestParam("majorSelect") UUID majorId,
                        @RequestParam("facilitySelect") UUID facilityId) {

        staff staff = nhanvienService.timkiemStaff(idday);

        departmentFacility departmentFacility = x.findByDepartmentAndFacility(departmentId, facilityId);


        if (departmentFacility == null) {
            departmentFacility = new departmentFacility();
            departmentFacility.setDepartment(new department());
            departmentFacility.getDepartment().setId(departmentId);
            departmentFacility.setFacility(new facility());
            departmentFacility.getFacility().setId(facilityId);
            departmentFacility = x.saved(departmentFacility);
        }

        majorFacility majorFacility = new majorFacility();
        majorFacility.setDepartmentFacility(new departmentFacility());
        majorFacility.getDepartmentFacility().setId(departmentFacility.getId());
        majorFacility.setMajor(new major());
        majorFacility.getMajor().setId(majorId);
        majorFacility = x.saveMF(majorFacility);

        staffMajorFacility staffMajorFacility = new staffMajorFacility();
        staffMajorFacility.setMajorFacility(new majorFacility());
        staffMajorFacility.getMajorFacility().setId(majorFacility.getId());
        staffMajorFacility.setStaff(new staff());
        staffMajorFacility.getStaff().setId(staff.getId());
        x.saveSMF(staffMajorFacility);

        return "redirect:/trangchu/bomon/" + idday;
    }


}

