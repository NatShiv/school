package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public FacultyService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        return facultyRepository.save(faculty);
    }

    public Faculty removeFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.deleteById(id);
        return faculty;
    }
    public Collection<Faculty> findByColor(String color) {
        if (color.isBlank()) {
            throw new DataEntryError("Введите цвет факультета.");
        }
        return facultyRepository.findByColor(color);}

    public Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String nameOrColor) {
        if (nameOrColor.isBlank()) {
            throw new DataEntryError("Введите цвет или название факультета.");
        }
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor,nameOrColor);
    }
    public Collection<Student> findStudentByFaculty(Long id) {
        studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return facultyRepository.findStudents(id);
    }
}