package com.example.demo.baove.controller;

import com.example.demo.baove.entity.StaffMajorFacilityDTO;
import com.example.demo.baove.entity.staff;
import com.example.demo.baove.service.nhanvienService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("trangchu")
public class nhanvienController {
    @Autowired
    nhanvienService nhanvienService;
    @Autowired
    nhanvienService hinhAnhService;


    @GetMapping("/nhanvien")
    public String hienthi(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNo) {
        model.addAttribute("staff", new staff());
        model.addAttribute("staffupdate", new staff());
        Page<staff> pageResult = nhanvienService.PageStaff(pageNo);
        model.addAttribute("Listnhanvien", pageResult);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("lichsuList",nhanvienService.getallLS());
        return "index";
    }

    @GetMapping("/download_template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {

        ResponseEntity<InputStreamResource> responseEntity = nhanvienService.downloadStaff();

        InputStreamResource resource = responseEntity.getBody();
        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setHeader("Content-Disposition", responseEntity.getHeaders().getFirst("Content-Disposition"));

        try (InputStream inputStream = resource.getInputStream();
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    @GetMapping("/status/{id}")
    public String status(@PathVariable("id") UUID id) {
        staff staff = nhanvienService.timkiemStaff(id);
        // Kiểm tra và cập nhật trạng thái
        if (staff != null) {
            if (staff.getStatus() == 0) {
                staff.setStatus((byte) 1);
            } else {
                staff.setStatus((byte) 0);
            }


            nhanvienService.addnhanvien(staff);


        }
        return "redirect:/trangchu/nhanvien";
    }
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("staff") staff staff,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        staff.setStatus((byte) 1);

        if (result.hasErrors()) {
            return "redirect:/trangchu/nhanvien";
        }

        if (nhanvienService.findByStaffCode(staff.getStaffcode()).isPresent()) {
            redirectAttributes.addFlashAttribute("staffCodeError", "Mã nhân viên đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        if (nhanvienService.findByAccountFe(staff.getAccountfe()).isPresent()) {
            redirectAttributes.addFlashAttribute("accountFeError", "Email FE đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        if (nhanvienService.findByAccountFpt(staff.getAccountfpt()).isPresent()) {
            redirectAttributes.addFlashAttribute("accountFptError", "Email FPT đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        nhanvienService.addnhanvien(staff);
        return "redirect:/trangchu/nhanvien";
    }

    @PostMapping("/update")
    public String updatee(
            @Valid @ModelAttribute("staffupdate") staff staff,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "redirect:/trangchu/nhanvien";
        }

        staff existingStaff = nhanvienService.timkiemStaff(staff.getId());

        // Kiểm tra mã nhân viên mới
        if (!existingStaff.getStaffcode().equals(staff.getStaffcode()) &&
                nhanvienService.findByStaffCode(staff.getStaffcode()).isPresent()) {
            redirectAttributes.addFlashAttribute("staffCodeErrorup", "Mã nhân viên đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        // Kiểm tra email FE mới
        if (!existingStaff.getAccountfe().equals(staff.getAccountfe()) &&
                nhanvienService.findByAccountFe(staff.getAccountfe()).isPresent()) {
            redirectAttributes.addFlashAttribute("accountFeErrorup", "Email FE đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        // Kiểm tra email FPT mới
        if (!existingStaff.getAccountfpt().equals(staff.getAccountfpt()) &&
                nhanvienService.findByAccountFpt(staff.getAccountfpt()).isPresent()) {
            redirectAttributes.addFlashAttribute("accountFptErrorup", "Email FPT đã tồn tại");
            return "redirect:/trangchu/nhanvien";
        }

        // Cập nhật thông tin nhân viên
        existingStaff.setAccountfe(staff.getAccountfe());
        existingStaff.setAccountfpt(staff.getAccountfpt());
        existingStaff.setName(staff.getName());
        existingStaff.setStaffcode(staff.getStaffcode());
        nhanvienService.updateStaff(existingStaff);

        return "redirect:/trangchu/nhanvien";
    }


    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try {
            nhanvienService.importExcel(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/trangchu/nhanvien";
        }
        return "redirect:/trangchu/nhanvien";
    }


}