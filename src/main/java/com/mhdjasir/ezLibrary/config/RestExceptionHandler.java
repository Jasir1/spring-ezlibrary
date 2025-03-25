//package com.mhdjasir.ezLibrary.config;
//
//import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class RestExceptionHandler {
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
////        BindingResult result = exception.getBindingResult();
////        List<FieldError> fieldErrors = result.getFieldErrors().stream().map(error ->
////                new FieldError(error.getField(), error.getCode())
////        ).toList();
////        final ErrorResponse errorResponse = new ErrorResponse();
////        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
////        errorResponse.setException(exception.getClass().getSimpleName());
////        errorResponse.setFieldErrors(fieldErrors);
////        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
////    }
//
////    @ExceptionHandler(ApplicationCustomException.class)
////    public ResponseEntity<CustomErrorResponse> handleCustom(final ApplicationCustomException exception) {
////        final CustomErrorResponse errorResponse = new CustomErrorResponse();
////        errorResponse.setHttpStatus(exception.getStatus().value());
////        errorResponse.setException(exception.getClass().getSimpleName());
////        errorResponse.setMessage(exception.getMessage());
////        return new ResponseEntity<>(errorResponse, exception.getStatus());
////    }
//
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
////        BindingResult result = exception.getBindingResult();
////        List<FieldError> fieldErrors = result.getFieldErrors().stream().map(error ->
////                new FieldError(error.getField(), error.getCode())
////        ).toList();
////        final ErrorResponse errorResponse = new ErrorResponse();
////        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST.value());
////        errorResponse.setException(exception.getClass().getSimpleName());
////        errorResponse.setFieldErrors(fieldErrors);
////        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
////    }
//
//    @ExceptionHandler(ApplicationCustomException.class)
//    public ResponseEntity<CustomErrorResponse> handleCustom(final ApplicationCustomException exception) {
//        final CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), "ApplicationCustomException", "Unauthorized access");
//        errorResponse.setHttpStatus(exception.getStatus().value());
//        errorResponse.setException(exception.getClass().getSimpleName());
//        errorResponse.setMessage(exception.getMessage());
//        return new ResponseEntity<>(errorResponse, exception.getStatus());
//    }
//}
