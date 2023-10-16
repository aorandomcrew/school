package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.DataNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;


@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }


    public Faculty read(Long id) {
        return facultyRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }


    public Faculty update(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
        if (faculty.getName() != null) {
            existingFaculty.setName(faculty.getName());
        }
        if (faculty.getColor() != null) {
            existingFaculty.setColor(faculty.getColor());
        }
        return facultyRepository.save(existingFaculty);
    }

    public Faculty delete(Long id) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
        facultyRepository.delete(existingFaculty);
        return existingFaculty;
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getByColorOrNameIgnoreCase(String colorOrName) {
        return facultyRepository.findAllByColorOrNameIgnoreCase(colorOrName);
    }

    public Faculty findByStudentId(Long id){
        return facultyRepository.findByStudent_Id(id).orElseThrow(DataNotFoundException::new);
    }
}
