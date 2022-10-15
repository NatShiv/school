package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.validator.Validator;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student,Long id) {
        student=studentRepository.save(student);
        student.facultySet( facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id)));
        return student;
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
    public Faculty findFacultyByStudent(Long id) {
      Student  student= studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
      Faculty faculty =student.getFaculty();
      if (faculty==null){
            throw new DataEntryError("Студент не распределен на факультет.");
        }
        return faculty;
    }

    public Collection<Student> findStudentBetweenAge(int min, int max) {
        if (max<min){
            throw new DataEntryError("Минимальное значение возраста не может быть больше максимального.");
        }
        return studentRepository.findByAgeBetween(Validator.validateNumber(min),
                Validator.validateNumber(max));
    }
}
