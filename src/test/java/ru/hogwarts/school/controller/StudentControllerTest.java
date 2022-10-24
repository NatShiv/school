package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentService studentService;
    @SpyBean
    private FacultyService facultyService;
    @InjectMocks
    private StudentController controller;

    final String studentName = "Ivanov Ivan";
    final String studentNameNew = "Petrov Ivan";
    final int studentAge = 19;
    final long studentId = 1;
    Student student = new Student(studentId, studentName, studentAge);
    Student student2 = new Student(studentId, studentNameNew, studentAge);


    final String facultyName = "Slizerin";
    final String facultyColor = "green";
    final long facultyId = 1;
    final String facultyName2 = "griffindor";
    final String facultyColor2 = "red";
    final long facultyId2 = 2;
    Faculty faculty = new Faculty(facultyId, facultyName, facultyColor);
    Faculty faculty2 = new Faculty(facultyId2, facultyName2, facultyColor2);


    @BeforeEach
    public void testStudents() {
        student.facultySet(faculty);
        student2.facultySet(faculty2);
        when(studentRepository.save(eq(student))).thenReturn(student);
        when(studentRepository.save(eq(student2))).thenReturn(student2);
        when(studentRepository.findByAge(eq(studentAge))).thenReturn(Collections.singleton(student));
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(facultyRepository.findById(eq(Long.valueOf(1L)))).thenReturn(Optional.of(faculty));
        when(facultyRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.of(faculty2));

    }

    @Test
    public void createTest() {
        ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        assertThat(studentResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(studentResponseEntity.getBody().getId()).isEqualTo(student.getId());
        assertThat(studentResponseEntity.getBody().getFaculty().getId()).isEqualTo(faculty.getId());
    }

    @Test
    public void getTest() {
        ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity("http://localhost:" + port + "/student/" + studentId, Student.class);
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        assertThat(studentResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(student);
        assertThat(studentResponseEntity.getBody().getId()).isEqualTo(student.getId());
        assertThat(studentResponseEntity.getBody().getFaculty().getId()).isEqualTo(faculty.getId());
    }

    @Test
    public void updateTest() {

        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange("http://localhost:" + port + "/student/" + studentId,
                HttpMethod.PUT, new HttpEntity<>(student2), Student.class);
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
        assertThat(studentResponseEntity.getBody()).usingRecursiveComparison().isEqualTo(student2);
        assertThat(studentResponseEntity.getBody().getId()).isEqualTo(student.getId());
        assertThat(studentResponseEntity.getBody().getFaculty().getId()).isEqualTo(faculty2.getId());
    }

    @Test
    public void deleteTest() {

        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange("http://localhost:" + port + "/student/" + studentId,
                HttpMethod.DELETE, new HttpEntity<>(student2), Student.class);
        assertThat(studentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentResponseEntity.getBody()).isNotNull();
    }
}