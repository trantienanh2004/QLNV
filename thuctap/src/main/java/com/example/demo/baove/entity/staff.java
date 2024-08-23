package com.example.demo.baove.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
public class staff {

        @Id
        @Column(name = "id", columnDefinition = "uniqueidentifier")
        @GeneratedValue(strategy = GenerationType.AUTO)

        private UUID id;


        @Column(name = "status")
        private Byte status;

        @Column(name = "created_date")
        private Long createddate;

        @Column(name = "last_modified_date")
        private Long lastmodifieddate;

        @NotBlank(message = "Code không được để trống")
        @Size(max = 15, message = "Code không được dài hơn 15 ký tự")
        @Column(name = "staff_code")
        private String staffcode;

        @NotBlank(message = "Name không được để trống")
        @Size(max = 100, message = "Name không được dài hơn 100 ký tự")
        @Column(name = "name")
        private String name;

        @NotBlank(message = "Email FE không được để trống")
        @Size(max = 100, message = "Email FE không được dài hơn 100 ký tự")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@fe\\.edu\\.vn$", message = "Email FE phải kết thúc với đuôi @fe.edu.vn")
        @Column(name = "account_fe")
        private String accountfe;

        @NotBlank(message = "Email FPT không được để trống")
        @Size(max = 100, message = "Email FPT không được dài hơn 100 ký tự")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@fpt\\.edu\\.vn$", message = "Email FPT phải kết thúc với đuôi @fpt.edu.vn")
        @Column(name = "account_fpt")
        private String accountfpt;

}
