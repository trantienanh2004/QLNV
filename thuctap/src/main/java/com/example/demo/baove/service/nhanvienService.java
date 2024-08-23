package com.example.demo.baove.service;

import com.example.demo.baove.entity.StaffMajorFacilityDTO;
import com.example.demo.baove.entity.lichsu;
import com.example.demo.baove.entity.staff;
import com.example.demo.baove.entity.staffMajorFacility;
import com.example.demo.baove.repository.FacilityRepository;
import com.example.demo.baove.repository.StaffRepository;
import com.example.demo.baove.repository.lichsuRepository;
import com.example.demo.baove.repository.staff_major_facilityRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class nhanvienService {
    @Autowired
    staff_major_facilityRepository staff_major_facilityRepository;

    @Autowired
    StaffRepository StaffRepository;
    @Autowired
    FacilityRepository facilityRepository;
    @Autowired
    x x;
    @Autowired
    lichsuRepository lichsuRepository;


    private static int size = 5;

public List<staff> GetAllStaff(){
    return StaffRepository.findAll();
}
    public Page<staff> PageStaff(int pageNo){
    Sort sort = Sort.by(Sort.Direction.ASC,"name");
        Pageable pageable = PageRequest.of(pageNo,size,sort);
        return StaffRepository.findAll(pageable);
    }

public staff timkiemStaff(UUID id){
    return StaffRepository.findById(id).orElse(null);
}



        public ResponseEntity<InputStreamResource> downloadStaff() throws IOException {
            List<StaffMajorFacilityDTO> staffList = x.hienthinhanvien();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Staff");

            Row headerRow = sheet.createRow(0);
            Cell cell = headerRow.createCell(0);
            cell.setCellValue("STT");
            cell = headerRow.createCell(1);
            cell.setCellValue("mã nhân viên");
            cell = headerRow.createCell(2);
            cell.setCellValue("tên nhân viên");
            cell = headerRow.createCell(3);
            cell.setCellValue("Account FE");
            cell = headerRow.createCell(4);
            cell.setCellValue("Account FPT");
            cell = headerRow.createCell(5);
            cell.setCellValue("chuyên ngành");


            int rowNum = 1;
            for (StaffMajorFacilityDTO nv : staffList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(nv.getStaffcode());
                row.createCell(2).setCellValue(nv.getName());
                row.createCell(3).setCellValue(nv.getAccountfe());
                row.createCell(4).setCellValue(nv.getAccountfpt());
                row.createCell(5).setCellValue(nv.getBomonchuyennganh());

            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=nhanvien.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(bais));
        }



    public Optional<staff> findByStaffCode(String staffCode) {
        return StaffRepository.findByStaffcode(staffCode);
    }


    public Optional<staff> findByAccountFe(String accountFe) {
        return StaffRepository.findByAccountfe(accountFe);
    }


    public Optional<staff> findByAccountFpt(String accountFpt) {
        return StaffRepository.findByAccountfpt(accountFpt);
    }


    public void addnhanvien(staff staff){
        StaffRepository.save(staff);
    }
    public void updateStaff(staff staff){
        StaffRepository.save(staff);
    }

    public void addLS(lichsu lichsu){
        lichsuRepository.save(lichsu);
    }

    public void importExcel(MultipartFile file) throws IOException {

        Path tempDir = Files.createTempDirectory("upload");
        Path tempFile = tempDir.resolve(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }


        lichsu lichsu = new lichsu();
        lichsu.setNgaytao(LocalDate.now());
        lichsu.setDuongdan(tempFile.toString());

        try (InputStream inputStream = new FileInputStream(tempFile.toFile());
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Bỏ qua dòng tiêu đề
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                staff staff = new staff();

                String staffcode = getCellValueAsString(row.getCell(0));
                String name = getCellValueAsString(row.getCell(1));
                String accountfe = getCellValueAsString(row.getCell(2));
                String accountfpt = getCellValueAsString(row.getCell(3));

                if (isValidEmail(accountfe, "@fe.edu.vn") && isValidEmail(accountfpt, "@fpt.edu.vn")) {
                    staff.setStaffcode(staffcode);
                    staff.setName(name);
                    staff.setAccountfe(accountfe);
                    staff.setAccountfpt(accountfpt);
                    staff.setStatus((byte)1);
                    this.addnhanvien(staff);
                    lichsu.setNoidung("import thành công ban ghi");

                } else {
                    System.out.println("Invalid email format for staff: " + staffcode);
                    lichsu.setNoidung("Invalid email format for staff: " + staffcode);
                }
            }
        }

        this.addLS(lichsu);

        Files.deleteIfExists(tempFile);
    }

public List<lichsu> getallLS(){
    return lichsuRepository.findAll();
}
    private boolean isValidEmail(String email, String domain) {
        return email != null && email.endsWith(domain);
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

}
