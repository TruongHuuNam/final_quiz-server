package com.huunam.identity_service.dto.request;

import com.huunam.identity_service.validator.DobConstrant;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    //update khong can thay doi username
    String password;
    String firstName;
    String lastName;

    @DobConstrant(min = 18, message = "INVALID_DOB") //annotation customize
    LocalDate dob;
    List<String> Roles;

}
