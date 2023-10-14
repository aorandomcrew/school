package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@SpringBootTest(classes = SchoolApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
        private ResponseEntity<Student> createStudent(String name, Integer age) {
            ResponseEntity<Student> response = template.postForEntity("/student",
                    new Student(null, name, age),
                    Student.class);
            return response;
        }
    @Autowired
    TestRestTemplate template;
    @Test
    void create(){
        ResponseEntity<Student> response = createStudent("Danil", 19);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Danil");
        assertThat(response.getBody().getAge()).isEqualTo(19);
    }

    @Test
    void read(){
        ResponseEntity<Student> response = createStudent("Danil", 19);
        response = template.getForEntity("/student/" + response.getBody().getId(), Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Danil");
        assertThat(response.getBody().getAge()).isEqualTo(19);
    }

    @Test
    void update() {
        ResponseEntity<Student> response = createStudent("Damil", 19);
        response.getBody().setName("Denis");
        response.getBody().setAge(17);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isNotEqualTo("Danil");
        assertThat(response.getBody().getAge()).isNotEqualTo(19);
        assertThat(response.getBody().getName()).isEqualTo("Denis");
        assertThat(response.getBody().getAge()).isEqualTo(17);
    }

    @Test
    void delete() {
        ResponseEntity<Student> response = createStudent("Danil", 19);
        template.delete("/student/" + response.getBody().getId());
        response = template.getForEntity("/student/" + response.getBody().getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThatExceptionOfType(ru.hogwarts.school.exception.DataNotFoundException.class);
    }
    @Test
    void getByAge(){
        createStudent("Danil", 19);
        createStudent("Denis", 17);
        createStudent("Eugene", 18);

        ResponseEntity<Collection> response = template.getForEntity("/student//filtered?age="+ 17,
                Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        Map<String,Integer> next = (HashMap) response.getBody().iterator().next();
        assertThat(next.get("age")).isEqualTo(17);
    }
}
