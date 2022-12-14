package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.entity.Faculty;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }


    @GetMapping("{id}")
    public Faculty getFacultyInfo(@PathVariable Long id) {
        return service.findFaculty(id);
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok().body(service.createFaculty(faculty));
    }

    @PutMapping("{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return service.updateFaculty(id, faculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteFacultyInfo(@PathVariable Long id) {
        return service.removeFaculty(id);
    }

    @GetMapping("/longName")
    public String getFacultyWhitLongName() {
        return service.getFacultyWhitLongName();
    }

    @GetMapping(params = "!nameOrColor")
    public Collection<Faculty> findByColor(@RequestParam(defaultValue = " ") String color) {
        return service.findByColor(color);
    }

    @GetMapping(params = "nameOrColor")
    public Collection<?> findByNameIgnoreCaseOrColorIgnoreCase(@RequestParam(defaultValue = " ") String nameOrColor) {
        if (nameOrColor.isBlank()) {
            throw new DataEntryError("Параметры запроса не должны быть пустыми.");
        }
        return service.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor);
    }

    @GetMapping("{facultyId}/students")
    public Collection<Student> findStudentByFaculty(@PathVariable Long facultyId) {
        return service.findStudentByFaculty(facultyId);
    }
}