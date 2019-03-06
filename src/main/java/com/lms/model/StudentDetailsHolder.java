package com.lms.model;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsHolder {


    private String studentId;
    private String name;
    private String email;
    private String password;
    private List<BooksBorrowed> booksBorrowed = new ArrayList<>();

    public List<BooksBorrowed> getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(List<BooksBorrowed> booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPassword() { return password;   }

    public void setPassword(String password) { this.password = password;    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
