package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@SpringBootTest(classes = SchoolApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    private ResponseEntity<Faculty> createFaculty(String name, String color) {
        ResponseEntity<Faculty> response = template.postForEntity("/faculty",
                new Faculty(null, name, color),
                Faculty.class);
        return response;
    }

    @Autowired
    TestRestTemplate template;
    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @AfterEach
    void clearDb() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void create() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("red");
    }

    @Test
    void read() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        response = template.getForEntity("/faculty/" + response.getBody().getId(), Faculty.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("math");
        assertThat(response.getBody().getColor()).isEqualTo("red");
    }

    @Test
    void update() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        response.getBody().setName("phis");
        response.getBody().setColor("blue");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isNotEqualTo("math");
        assertThat(response.getBody().getColor()).isNotEqualTo("red");
        assertThat(response.getBody().getName()).isEqualTo("phis");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    void delete() {
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        template.delete("/faculty/" + response.getBody().getId());
        response = template.getForEntity("/faculty/" + response.getBody().getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThatExceptionOfType(ru.hogwarts.school.exception.DataNotFoundException.class);
    }
    @Test
    void geByColor(){
        createFaculty("math", "red");
        createFaculty("financial", "red");
        createFaculty("phis", "blue");
        createFaculty("chemistry", "blue");

        ResponseEntity<Collection> response = template.getForEntity("/faculty//filtered?color=red",
                Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        Map<String,String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("color")).isEqualTo("red");
    }
    @Test
    void getByColorOrName() {
        createFaculty("math", "red");
        createFaculty("financial", "red");
        createFaculty("phis", "blue");
        createFaculty("chemistry", "blue");

        ResponseEntity<Collection> response = template.getForEntity("/faculty/by_color_or_name?colorOrName=red",
                Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        Map<String,String> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("color")).isEqualTo("red");
    }
    @Test
    void findByStudent(){
        ResponseEntity<Faculty> response = createFaculty("math", "red");
        Faculty faculty = response.getBody();
        Student student = new Student(null,"Danil", 19);
        student.setFaculty(faculty);
        ResponseEntity<Student> studentResponseEntity = template.postForEntity("/student", student, Student.class);
        Long studentId = studentResponseEntity.getBody().getId();
        response = template
                .getForEntity("/faculty/by-student?studentId=" + studentId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(faculty);
    }
}
