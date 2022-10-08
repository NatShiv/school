package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long keyID = 0L;

    public Student createStudent(Student student) {
        student.setId(++keyID);studentMap.put(keyID, student);
        return student;
    }

    public Student findStudent(Long id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return studentMap.get(id);
    }

    public Student updateStudent(Student student) {
        return studentMap.put(student.getId(), student);
    }

    public Student removeStudent(Long id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return studentMap.remove(id);
    }

    public Collection<Student> findStudentByAge(int age) {
        return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
