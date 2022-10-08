package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Студент с номером %s не найден.",ex.getId()));
    }
    public ResponseEntity<String> handleFacultyNotFoundException(FacultyNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Факультет с номером %s не найден.",ex.getId()));
    }
}
