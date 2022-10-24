package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;


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
    void testStudents() {
        student.facultySet(faculty);
        student2.facultySet(faculty2);

        when(facultyRepository.save(eq(faculty))).thenReturn(faculty);
        when(facultyRepository.save(eq(faculty2))).thenReturn(faculty2);
        when(facultyRepository.findById(eq(Long.valueOf(1l)))).thenReturn(Optional.of(faculty));
        when(facultyRepository.findById(eq(Long.valueOf(2l)))).thenReturn(Optional.of(faculty2));
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(any(), any())).thenReturn(Collections.singleton(faculty));
        when(facultyRepository.findStudents(eq(facultyId))).thenReturn(Collections.singleton(student));
    }

    @Test
    void createTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    Faculty facultyResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty);
                    assertThat(facultyResult.getId()).isEqualTo(faculty.getId());
                });
    }


    @Test
    void updateTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty2)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    Faculty facultyResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty2);
                    assertThat(facultyResult.getId()).isEqualTo(faculty2.getId());
                });
    }

    @Test
    void getTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty2)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    Faculty facultyResult = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), Faculty.class);
                    assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty2);
                    assertThat(facultyResult.getId()).isEqualTo(faculty2.getId());
                });
    }
}