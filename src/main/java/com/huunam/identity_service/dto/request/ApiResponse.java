package com.huunam.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) //khi serialize object sang json thi nhung field null se khong them vao
public class ApiResponse <T> { //data type co the thay doi tuy thuoc vao tung API
    @Builder.Default
     int code = 1000;
     String message;
     T result;
}
