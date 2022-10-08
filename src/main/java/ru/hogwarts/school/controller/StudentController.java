package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id) {
        return service.findStudent(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return service.updateStudent(student);
    }

    @DeleteMapping("{id}")
    public Student deleteStudentInfo(@PathVariable Long id) {
        return service.removeStudent(id);
    }

    @GetMapping()
    public Collection<Student> getStudentByAge(@RequestParam int age) {
        return service.findStudentByAge(age);
    }
}
