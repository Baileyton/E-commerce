package com.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class SignupDto {

    @Getter
    @Setter
    public static class Request {
        @NotBlank
        private String name;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String phone;
        @NotBlank
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String name;
        private String email;
        private String phone;
        private String address;
        private LocalDateTime createdAt;
    }
}