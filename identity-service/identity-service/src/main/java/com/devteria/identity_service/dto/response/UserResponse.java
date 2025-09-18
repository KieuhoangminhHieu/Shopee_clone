package com.devteria.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    String lastName;
    String username;
    LocalDate dob;
    String email;
    String phoneNumber;
    String address;
    Set<RoleResponse> roles;
}
