package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.AvatarStudent;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.DataEntryError;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.validator.Validator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {
    private static Logger logger= LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentService(StudentRepository studentRepository,FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

      public Student createStudent(Student student) {
          logger.debug("Вызван метод - создать студента.");
      facultyService.findFaculty(student.getFaculty().getId());
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {  logger.debug("Вызван метод - найти студента с номером {}.",id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("Студент с номером {} не найден", id);
            return new StudentNotFoundException(id);
        });
    }

    public Student updateStudent(Long id, Student student) {
        logger.debug("Вызван метод - изменить студента с номером {}.",id);
        facultyService.findFaculty(student.getFaculty().getId());
      findStudent(id);
        return studentRepository.save(student);
    }

    public Student removeStudent(Long id) {
        logger.debug("Вызван метод - удалить студента с номером {}.",id);
        Student student = findStudent(id);
        studentRepository.deleteById(id);
        return student;
    }

    public Collection<Student> findStudentByAge(int age) {
        logger.debug("Вызван метод - найти студентов с возрастом {}.",age);
        return studentRepository.findByAge(Validator.validateNumber(age));
    }

    public Faculty findFacultyByStudent(Long id) {
        logger.debug("Вызван метод - вывести факультет студента с номером {}.",id);
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.error("Студент с номером {} не распределен на факультет.",id);
            throw new DataEntryError("Студент не распределен на факультет.");
        }
        return faculty;
    }

    public Collection<Student> findStudentBetweenAge(int min, int max) {
        logger.debug("Вызван метод - найти студентов возраст которых лежит в пределах{} {}.",min,max);
        if (max < min) {
            logger.error("Минимальное значение возраста не может быть больше максимального.");
            throw new DataEntryError("Минимальное значение возраста не может быть больше максимального.");
        }
        return studentRepository.findByAgeBetween(Validator.validateNumber(min),
                Validator.validateNumber(max));
    }

    public Pair<String, byte[]> getAvatar(Long id) {
        logger.debug("Вызван метод - показать аватар студента с номером {}.",id);
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        AvatarStudent avatarStudent = student.avatarGet();
        try {
            return Pair.of(avatarStudent.getMediaType(), Files.readAllBytes(Paths.get(avatarStudent.getFilePath())));
        } catch (IOException e) {
            logger.error("Возникла проблема при чтении файла");
            throw new RuntimeException("Возникла проблема при чтении файла");
        }
    }
    public void saveAvatar(Long id,AvatarStudent avatarStudent){
        logger.debug("Вызван метод - сохранить аватар студенту с номером {}.",id);
        Student student = findStudent(id);
        student.avatarSet(avatarStudent);
        studentRepository.save(student);
    }

    public int getTotalStudent() {
        logger.debug("Вызван метод - показать число всех студентов школы.");
        return studentRepository.totalStudents();
    }

    public Double getAverageAgeStudents() {
        logger.debug("Вызван метод - показать средний возраст студентов." );
       return studentRepository.findAll()  //на реальном проекте так делать не стоит
                .stream()
                .mapToDouble(Student::getAge)
               .average().orElse(0);
        //  return studentRepository.averageAge();
    }

    public Collection<Student> getFiveEndStudents(){
        logger.debug("Вызван метод - показать 5 последних студентов в списке.");
        PageRequest pageRequest=PageRequest.of(0,5);
        return studentRepository.getFiveEndStudents(pageRequest);
    }

    public Collection<String> findStudentByFirstLiterInName(String liter) {
      return   studentRepository.findAll()  //на реальном проекте так делать не стоит
              .stream()
              .map(Student::getName)
              .filter(a->a.startsWith(liter))
              .map(String::toUpperCase)
              .sorted()
              .collect(Collectors.toList());
    }
}
