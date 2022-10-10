package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Студент с номером %d не найден.",ex.getId()));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<String> handleFacultyNotFoundException(FacultyNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Факультет с номером %d не найден.",ex.getId()));
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(DataEntryError.class)
    public ResponseEntity<String> handleDataEntryError(DataEntryError ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());}
}
