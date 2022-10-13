package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.model.Faculty;

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
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return service.createFaculty(faculty);
    }

    @PutMapping("{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        return service.updateFaculty(id, faculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteFacultyInfo(@PathVariable Long id) {
        return service.removeFaculty(id);
    }

    @GetMapping()
    public Collection<Faculty> FindFaculty(@RequestParam(defaultValue = "") String name,
                                           @RequestParam String color) {
        if (name.equals("")) {
            return service.findFacultyByColor(color);
        }
        return service.findFacultyByNameOrColor(name, color);
    }

}
