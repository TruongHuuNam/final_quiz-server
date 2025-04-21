package com.huunam.identity_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     String id;
     String username;
//     String password; //khong ai tra ve password
     String firstName;
     String lastName;
     LocalDate dob;
     Set<RoleResponse> roles;
}
     