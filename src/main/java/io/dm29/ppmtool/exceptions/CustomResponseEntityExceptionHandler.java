package io.dm29.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectIdException(ProjectIdException ex) {

        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex) {

        ProjectNotFoundExceptionResponse projectNotFoundExceptionResponse =
                new ProjectNotFoundExceptionResponse(ex.getMessage());

        return new ResponseEntity<>(projectNotFoundExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex) {
        UsernameAlreadyExistExceptionResponse usernameAlreadyExistExceptionResponse =
                new UsernameAlreadyExistExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(usernameAlreadyExistExceptionResponse, HttpStatus.BAD_REQUEST);

    }
}
