package com.lms.model;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Document
public class StudentDetails {

    @Id
    @Field
    private String sId;

    @Field
    private String studentId;

    @Field
    private String name;

    @Field
    private String email;

    @Field
    private String password;

    @Field
    private List<BooksBorrowed> booksBorrowed = new ArrayList<>();

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
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

    public List<BooksBorrowed> getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(List<BooksBorrowed> booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }
}
