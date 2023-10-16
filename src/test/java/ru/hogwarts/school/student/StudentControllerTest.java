package ru.hogwarts.school.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @SpyBean
    StudentService studentService;

    @Test
    void getById() throws Exception {
        Student student = new Student(1L, "Danil", 19);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Danil"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    void create() throws Exception {
        Student student = new Student(1L, "Danil", 19);
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Danil"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    void update() throws Exception {
        Student student = new Student(1L, "Danil", 19);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(ArgumentMatchers.any(Student.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders.post("/student/1")
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Danil"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    void delete() throws Exception {
        Student student = new Student(1L, "Danil", 19);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Danil"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    void getByAgeBetween() throws Exception {
        Student student1 = new Student(1L, "Danil", 19);
        Student student2 = new Student(2L, "Denis", 17);
        when(studentRepository.findAllByAgeBetween(10, 20))
                .thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/find-by-age-between?min=10&max=20")
                        .accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Danil"))
                .andExpect(jsonPath("$[0].age").value(19))
                .andExpect(jsonPath("$[1].name").value("Denis"))
                .andExpect(jsonPath("$[1].age").value(17));
    }
    @Test
    void findByFaculty() throws Exception{
        List<Student> students = Arrays.asList(
                new Student(1L, "Danil", 19),new Student(2L, "Denis", 17)
        );
        Faculty faculty = new Faculty(1L, "math", "red");
        faculty.setStudents(students);

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/by-faculty?facultyId=1")
                        .accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Danil"))
                .andExpect(jsonPath("$[1].name").value("Denis"));
    }
}
