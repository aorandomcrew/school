package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/filtered")
    public Collection<Faculty> geByColor(@RequestParam("color") String color) {
        return facultyService.getByColor(color);
    }

    @PostMapping()
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping("/{id}")
    public Faculty read(@PathVariable("id") Long id) {
        return facultyService.read(id);
    }

    @PostMapping("/{id}")
    public Faculty update(@PathVariable("id") Long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    public Faculty remove(@PathVariable("id") Long id) {
        return facultyService.delete(id);
    }

    @GetMapping("/by_color_or_name")
    public Collection<Faculty> getByColorOrName(@RequestParam("colorOrName") String colorOrName) {
        return facultyService.getByColorOrNameIgnoreCase(colorOrName);
    }

    @GetMapping("/by-student")
    public Faculty findByStudent(@RequestParam("studentId") Long id) {
        return facultyService.findByStudentId(id);
    }
}
