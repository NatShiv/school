package ru.hogwarts.school.controller;

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

    @GetMapping(params = "color")
    public Collection<Faculty> findByColor(@RequestParam(defaultValue = " ") String color) {
        return service.findByColor(color);
    }
// Пришлось убрать этот метод тк метод findByNameIgnoreCaseOrColorIgnoreCase  выполнит эту функцию.
//А без создания другого ответвления пути это не работает

    @GetMapping(params = "nameOrColor")
    public Collection<Faculty> find(@RequestParam(defaultValue = " ") String nameOrColor){

        if (nameOrColor.isBlank()) {
            throw new DataEntryError("Параметры запроса не должны быть пустыми.");
        }

        return service.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor);
    }
}