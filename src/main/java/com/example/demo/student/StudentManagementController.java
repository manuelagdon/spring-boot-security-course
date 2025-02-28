package com.example.demo.student;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
      new Student(1, "James Bond"),
      new Student(2, "Maria Jones"),
      new Student(3, "Anna Smith")
    );

    @GetMapping(path = "/all-students")
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @PostMapping(path = "/register")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println(student.toString());
    }

    @PostMapping(path = "/delete/{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println(studentId + " deleted");
    }

    @PutMapping(path = "/update/{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.printf("%s %s%n", studentId, student);
    }

    @GetMapping(path = "/v2/students/{studentId}")
    public Student getStudentAuthenticated(@PathVariable("studentId") Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Student " + studentId + " does not exists"
                ));
    }
}
