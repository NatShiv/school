package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
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
    public Student createStudent(@RequestBody Student student, @RequestParam(defaultValue = "null") Long id) {
        return service.createStudent(student, id);
    }

    @PutMapping("{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return service.updateStudent(id, student);
    }

    @DeleteMapping("{id}")
    public Student deleteStudentInfo(@PathVariable Long id) {
        return service.removeStudent(id);
    }

    @GetMapping("{id}/faculty")
    public Faculty findFacultyByStudent(@PathVariable Long id) {
        return service.findFacultyByStudent(id);
    }

    @GetMapping()
    public Collection<Student> findStudentByAge(@RequestParam int age,
                                                @RequestParam(defaultValue = "0") int max) {
        if (max == 0) {
            return service.findStudentByAge(age);
        }
        return service.findStudentBetweenAge(age, max);
    }
}
