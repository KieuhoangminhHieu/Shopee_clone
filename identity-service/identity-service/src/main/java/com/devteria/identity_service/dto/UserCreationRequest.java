package com.devteria.identity_service.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreationRequest {
    private String firstName;
    private String lastName;

    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;

    private LocalDate dob;

    @Email(message = "INVALID_EMAIL")
    private String email;

    @Size(min = 10, max = 15, message = "INVALID_PHONE_NUMBER")
    private String phoneNumber;

    private String address;
}
