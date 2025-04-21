package com.huunam.identity_service.exception;

import com.huunam.identity_service.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    //tap trung runtime exception ve day de xu ly, unexpected error
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse); // tra ve cho client
    }

    //tap trung App exception ve day de xu ly
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse); // tra ve cho client
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();//lay enumkey ra

        ErrorCode errorCode = ErrorCode.INVALID_KEY; //check valid message key
        Map<String,Object> attributes =null;
        try { 
            errorCode = ErrorCode.valueOf(enumKey); //lay error code tu enumkey

            var constrainViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);
            //unwrap ra thi duoc mot object chua nhung thong tin la attribute
            attributes = constrainViolation.getConstraintDescriptor().getAttributes(); //noi dung cua cac annotation

            log.info(attributes.toString());
        }
        catch (IllegalArgumentException e){

        }
        //create api response
        ApiResponse apiResponse = new ApiResponse();
        //set code and message
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage()); //check if non null validation then map errorCode else return errorCode

        return  ResponseEntity.badRequest().body(apiResponse); //return bad request for client
    }

    private String mapAttribute(String message, Map<String,Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE)); //get attribute min

        return message.replace("{" + MIN_ATTRIBUTE + "}" , minValue); //replace min value in the errorCode = value
    }

}
