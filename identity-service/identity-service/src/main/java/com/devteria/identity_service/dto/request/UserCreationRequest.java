package com.devteria.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import com.devteria.identity_service.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String firstName;
    String lastName;

    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    @Email(message = "INVALID_EMAIL")
    String email;

    @Size(min = 10, max = 15, message = "INVALID_PHONE_NUMBER")
    String phoneNumber;

    String address;
}
