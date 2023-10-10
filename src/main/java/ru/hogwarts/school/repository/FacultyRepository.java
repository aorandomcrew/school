package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColor(String color);
    List<Faculty> findAllByColorOrNameIgnoreCase(String color, String name);
    Optional<Faculty> findByStudent_Id(Long id);
}
