package ru.hogwarts.school.controller;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping()
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student read(@PathVariable("id") Long id) {
        return studentService.read(id);
    }

    @PostMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student remove(@PathVariable("id") Long id) {
        return studentService.delete(id);
    }

    @GetMapping("/filtered")
    public Collection<Student> getByAge(@RequestParam("age") int age){
        return studentService.getByAge(age);
    }

    @GetMapping("/find-by-age-between")
    public Collection<Student> getByAgeBetween(@RequestParam("min") int min, @RequestParam("max") int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/by-faculty")
    public Student findtByFaculty(@RequestParam("facultyId") Long id){
        return studentService.findByFacultyId(id);
    }
}
