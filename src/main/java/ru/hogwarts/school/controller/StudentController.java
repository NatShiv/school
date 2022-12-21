package ru.hogwarts.school.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/total")
    public int getTotalStudent() {
        return service.getTotalStudent();
    }
    @GetMapping("/averageAge")
    public Double getAverageAge() {
        return service.getAverageAgeStudents();
    }
    @GetMapping("/fiveEndStudents")
    public Collection<Student> findStudentByAge(){
        return service.getFiveEndStudents();
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @PutMapping("{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return service.updateStudent(id, student);
    }

    @DeleteMapping("{id}")
    public Student deleteStudentInfo(@PathVariable Long id) {
        return service.removeStudent(id);
    }
    @GetMapping("/studentsName/{liter}")
    public Collection<String> findStudentByFirstLiterInName(@PathVariable String liter){
        return service.findStudentByFirstLiterInName(liter);
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

    @GetMapping("/{id}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) {
        Pair<String, byte[]> pair = service.getAvatar(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getFirst()))
                .contentLength(pair.getSecond().length)
                .body(pair.getSecond());
    }
    @GetMapping("/studentsName")
    public void printStudentsName() {
        service.printStudentsName();
    }
    @GetMapping("/studentsNameSync")
    public void printStudentsNameSync() {
        service.printStudentsNameSynchr();
    }
}
