package ru.hogwarts.school.faculty;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;


@WebMvcTest(StudentController.class)
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
    StudentService studentService;

//    @Test
//    void getById() throws Exception {
//        Faculty faculty = new Faculty(1L, "math", "red");
//
//        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("math"))
//                .andExpect(jsonPath("$.color").value("red"));
//    }
}
