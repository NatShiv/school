package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long keyID = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++keyID);
        return facultyMap.put(keyID, faculty);
    }

    public Faculty findFaculty(Long id) {
        if (!facultyMap.containsKey(id)) {
            throw new FacultyNotFoundException(id);
        }
        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyMap.put(faculty.getId(), faculty);
    }

    public Faculty removeFaculty(Long id) {
        if (!facultyMap.containsKey(id)) {
            throw new FacultyNotFoundException(id);
        }
        return facultyMap.remove(id);
    }

    public Collection<Faculty> findFacultyByColor(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
