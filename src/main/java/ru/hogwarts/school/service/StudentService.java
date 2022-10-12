package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.validator.Validator;

import java.util.Collection;
@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
    public Student updateStudent(Long id, Student student) {
        studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return studentRepository.save(student);
    }

    public Student removeStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.deleteById(id);
        return student;
    }

    public Collection<Student> findStudentByAge(int age) {
        return studentRepository.findByAge(Validator.validateNumber(age));
    }
}
