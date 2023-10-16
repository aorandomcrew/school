package ru.hogwarts.school.faculty;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @SpyBean
    FacultyService facultyService;

    @Test
    void getById() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("math"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"));
    }

    @Test
    void create() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);


        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void update() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(ArgumentMatchers.any(Faculty.class))).thenReturn(faculty);


        mockMvc.perform(MockMvcRequestBuilders.post("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void delete() throws Exception {
        Faculty faculty = new Faculty(1L, "math", "red");
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1")
                        .content(objectMapper.writeValueAsString(faculty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("math"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void getByColorOrName() throws Exception {
        Faculty faculty1 = new Faculty(1L, "math", "red");
        Faculty faculty2 = new Faculty(1L, "financial", "red");
        Faculty faculty3 = new Faculty(1L, "phis", "blue");
        Faculty faculty4 = new Faculty(1L, "chemistry", "blue");

        when(facultyRepository.findAllByColorOrNameIgnoreCase("red"))
                .thenReturn(Arrays.asList(faculty1, faculty2));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty//by_color_or_name?colorOrName=red")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("math"))
                .andExpect(jsonPath("$[1].name").value("financial"));
    }

    @Test
    void findByStudent() throws Exception {
        Faculty faculty = new Faculty(1L, "phis", "blue");
        List<Student> students = Arrays.asList(
                new Student(1L, "Danil", 19), new Student(2L, "Denis", 17)
        );
        faculty.setStudents(students);
        when(facultyRepository.findByStudent_Id(1L)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-student?studentId=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("phis"));
    }
}
