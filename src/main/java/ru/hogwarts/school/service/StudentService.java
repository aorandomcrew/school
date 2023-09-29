package ru.hogwarts.school.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.DataNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;



@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
                .orElseThrow(DataNotFoundException:: new);
        studentRepository.delete(existStudent);
        return existStudent;
    }
    public Collection<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

}
