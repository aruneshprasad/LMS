package com.lms.controller;

import com.lms.model.*;

import com.lms.service.BookService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.service.StudentService;

import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin
@ComponentScans(value = { @ComponentScan("com.lms.repo"), @ComponentScan("com.lms.service") })
@RequestMapping(value = "/lms")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private BookService bookService;

    StudentDetails student = null;
    List<BookDetails> books =null;
    BookDetails book =null;
    BooksBorrowed bookBorrowed=null;


    @RequestMapping(value = "/registerStudent", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addStudent(@RequestBody StudentDetailsHolder sh) {

        if(studentService.findById(sh.getStudentId()) != null){
            return new ResponseEntity<String>("Student Id already exists", HttpStatus.EXPECTATION_FAILED);
        }
        else{
            student=new StudentDetails();

            student.setsId(sh.getStudentId());
            student.setStudentId(sh.getStudentId());
            student.setName(sh.getName());
            student.setEmail(sh.getEmail());
            student.setPassword(sh.getPassword());
            student.setBooksBorrowed(sh.getBooksBorrowed());

            student = studentService.addStudent(student);

            if(student==null){
                return new ResponseEntity<StudentDetails>(new StudentDetails(), HttpStatus.EXPECTATION_FAILED);
            }
        }

        return new ResponseEntity<StudentDetails>(student, HttpStatus.OK);
    }


    @RequestMapping(value = "/studentLogin", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> studentLogin(@RequestBody StudentLogin sLogin) {

        String jwtToken = null;

        if (sLogin.getStudentId() == null || sLogin.getPassword() == null) {
            return new ResponseEntity<String>(jwtToken, HttpStatus.UNAUTHORIZED);
        }

        StudentDetails studentDetails = studentService.findById(sLogin.getStudentId());
        //System.out.println(studentDetails.getName());

        if (!sLogin.getPassword().equals(studentDetails.getPassword())){
            return new ResponseEntity<String>(jwtToken, HttpStatus.UNAUTHORIZED);
        }

        String sId = sLogin.getStudentId();
        jwtToken = Jwts.builder().setSubject(sId).claim("roles", "studentDetails").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "student").compact();

        return new ResponseEntity<String>(jwtToken, HttpStatus.OK);

    }


    @RequestMapping(value = "/authorizedStudent/getBookBorrowedByStudent/{id}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBooksBorrowedByStudentById(@PathVariable String id) {
        student =  studentService.findById(id);
        return new ResponseEntity<List<BooksBorrowed>>(student.getBooksBorrowed(), HttpStatus.OK);
    }


    @RequestMapping(value = "/authorizedStudent/updateBookBorrowedByStudent/{studentId}/{bookId}", method = { RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBooksBorrowedByStudentById(@PathVariable String studentId, @PathVariable String bookId) {
        student =  studentService.findById(studentId);
        for (int i=0; i<student.getBooksBorrowed().size(); i++){
            if (student.getBooksBorrowed().get(i).getBookId().equals(bookId)){
                student.getBooksBorrowed().remove(i);
            }
        }

        book = bookService.findById(bookId);
        book.setCount(book.getCount()+1);
        bookService.addBook(book);

        studentService.addStudent(student);
        return new ResponseEntity<List<BooksBorrowed>>(student.getBooksBorrowed(), HttpStatus.OK);
    }


    @RequestMapping(value = "/authorizedStudent/addBooksBorrowedToStudent/{studentId}/{bookId}", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBooksBorrowedToStudentById(@PathVariable String studentId, @PathVariable String bookId) {
        student =  studentService.findById(studentId);

        for (int i=0; i<student.getBooksBorrowed().size(); i++){
            if (student.getBooksBorrowed().get(i).getBookId().equals(bookId)){
                return new ResponseEntity<String>("Book is already borrowed.", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        if (bookService.findById(bookId).getCount()==0){
            return new ResponseEntity<String>("Book is currently unavailable.", HttpStatus.EXPECTATION_FAILED);
        }
        else if(student.getBooksBorrowed().size()<5){
            book =bookService.findById(bookId);

            bookBorrowed=new BooksBorrowed();

            bookBorrowed.setBookId(book.getbId());
            bookBorrowed.setIsbn(book.getIsbn());
            bookBorrowed.setTitle(book.getTitle());
            bookBorrowed.setAuthor(book.getAuthor());
            bookBorrowed.setPublishedYear(book.getPublishedYear());

            student.getBooksBorrowed().add(bookBorrowed);
            studentService.addStudent(student);

            book.setCount(book.getCount()-1);
            bookService.addBook(book);

        }
        else{
            return new ResponseEntity<String>("You cannot borrow more than 5 books", HttpStatus.METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity<BookDetails>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "/authorizedStudent/searchBook/{pattern}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBooksByTitlePatternForStudent(@PathVariable String pattern) {
        books = bookService.findByTitlePattern(new StringBuilder().append("%").append(pattern).append("%").toString().toLowerCase());
        return new ResponseEntity<List<BookDetails>>(books, HttpStatus.OK);
    }
}
