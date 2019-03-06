package com.lms.service;

import com.lms.model.StudentLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.model.StudentDetails;
import com.lms.repo.StudentRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepo;

    @Override
    public StudentDetails addStudent(StudentDetails student) {
       return studentRepo.save(student);

    }

    @Override
    public StudentDetails findById(String id) {
        return studentRepo.findOne(id);
    }

    /*@Override
    public List<StudentLogin> studentLogin(String studentId,String password) {
        return studentRepo.checkLogin(studentId,password);
    }*/

    @Override
    public List<StudentDetails> findAll() {
        List<StudentDetails> studentList = new ArrayList<StudentDetails>();
        Iterator<StudentDetails> iter = studentRepo.findAll().iterator();
        while(iter.hasNext()){
            studentList.add(iter.next());
        }
        return studentList;
    }

}
