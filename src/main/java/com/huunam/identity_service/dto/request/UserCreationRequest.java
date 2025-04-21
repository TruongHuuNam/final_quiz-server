package com.huunam.identity_service.dto.request;

import com.huunam.identity_service.validator.DobConstrant;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

//map data tu client vao dto by getter setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //tao ra mot builder class cho mot object dto
@FieldDefaults(level = AccessLevel.PRIVATE) //modifier default is private if without define
public class UserCreationRequest { //validation info of user using library Spring Validation
    @Size(min=6,message = "USERNAME_INVALID")//truyen vao string vi khong the truyen nhu bth, name = key cua error code
     String username;

    @Size(min = 8, message = "PASSWORD_INVALID") //declare constraints every field //key enum must define correctly
     String password;
     String firstName;
     String lastName;

    //validate age
    @DobConstrant(min = 18, message = "INVALID_DOB") //annotation customize
     LocalDate dob;

    //another annotation in Validation like @Email,@NotNull, ...
    // or customize annotation to validate rules base on our business


}
