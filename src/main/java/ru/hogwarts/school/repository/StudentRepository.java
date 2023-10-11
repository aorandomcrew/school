package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(Integer age);
    List<Student> findAllByAgeBetween(int min, int max);
    Optional<Student> findAllByFaculty_Id(Long id);
}
