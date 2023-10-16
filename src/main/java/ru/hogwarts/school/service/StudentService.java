package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.DataNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;


@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }


    public Student read(Long id) {
        return studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }


    public Student update(Long id, Student student) {
        Student existStudent = studentRepository.findById(id).orElseThrow(DataNotFoundException::new);
        Optional.ofNullable(student.getName()).ifPresent(existStudent::setName);
        Optional.ofNullable(student.getAge()).ifPresent(existStudent::setAge);
        return studentRepository.save(existStudent);
    }

    public Student delete(Long id) {
        Student existStudent = studentRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
        studentRepository.delete(existStudent);
        return existStudent;
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Collection<Student> findByFacultyId(Long id) {
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElseThrow(DataNotFoundException::new);
    }
}
