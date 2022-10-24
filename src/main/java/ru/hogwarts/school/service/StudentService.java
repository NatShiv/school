package ru.hogwarts.school.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.AvatarStudent;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.validator.Validator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

      public Student createStudent(Student student) {
        facultyRepository.findById(student.getFaculty().getId()).orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student updateStudent(Long id, Student student) {
        facultyRepository.findById(student.getFaculty().getId()).orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
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
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            throw new DataEntryError("Студент не распределен на факультет.");
        }
        return faculty;
    }

    public Collection<Student> findStudentBetweenAge(int min, int max) {
        if (max < min) {
            throw new DataEntryError("Минимальное значение возраста не может быть больше максимального.");
        }
        return studentRepository.findByAgeBetween(Validator.validateNumber(min),
                Validator.validateNumber(max));
    }

    public Pair<String, byte[]> getAvatar(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        AvatarStudent avatarStudent = student.avatarGet();
        try {
            return Pair.of(avatarStudent.getMediaType(), Files.readAllBytes(Paths.get(avatarStudent.getFilePath())));
        } catch (IOException e) {
            throw new RuntimeException("Возникла проблема при чтении файла");
        }
    }
    public void saveAvatar(Long id,AvatarStudent avatarStudent){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        student.avatarSet(avatarStudent);
        studentRepository.save(student);
    }

    public int getTotalStudent() {
        return studentRepository.totalStudents();
    }

    public int getAverageAgeStudents() {
        return studentRepository.averageAge();
    }

    public Collection<Student> getFiveEndStudents(){
        PageRequest pageRequest=PageRequest.of(0,5);
        return studentRepository.getFiveEndStudents(pageRequest);
    }
}
