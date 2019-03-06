package com.lms.service;

import com.lms.model.StudentDetails;
import com.lms.model.StudentLogin;
import java.util.List;

public interface StudentService {

    StudentDetails addStudent(StudentDetails student);
    StudentDetails findById(String id);
    //List<StudentLogin> studentLogin(String studentId,String password);
    List<StudentDetails> findAll();

}
