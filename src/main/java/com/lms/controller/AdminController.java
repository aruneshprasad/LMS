package com.lms.controller;

import com.lms.model.*;
import com.lms.service.BookService;
import com.lms.service.StudentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/lms")
public class AdminController {

    @Autowired
    private BookService bookService;
    @Autowired
    private StudentService studentService;

    List<BookDetails> books =null;
    BookDetails book = null;
    StudentDetails student = null;


    @RequestMapping(value = "/adminLogin", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkLogin(@RequestBody Admin admin) {

        String jwtToken = "";

        if (admin.getUserName() == null || admin.getPassword() == null) {
            return new ResponseEntity<String>(jwtToken, HttpStatus.UNAUTHORIZED);
        }

        if (!admin.getPassword().equals("password")){
            return new ResponseEntity<String>(jwtToken, HttpStatus.UNAUTHORIZED);
        }

        String adminName = admin.getUserName();
        jwtToken = Jwts.builder().setSubject(adminName).claim("roles", "admin").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "admin").compact();

        return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
    }

    @RequestMapping(value = "/authorizedAdmin/addBook", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBook(@RequestBody BookDetailsHolder bh) {

        if(bookService.findByTitle(bh.getTitle().toLowerCase()) != null){
            return new ResponseEntity<String>("Book with same title is already present", HttpStatus.EXPECTATION_FAILED);
        }
        else{
            book = new BookDetails();

            StringBuilder prefix= new StringBuilder("B");
            books = bookService.findAllBooksDescOrder();
            //System.out.println(books.size());
            if(!books.isEmpty()){
                Integer id = new Integer(books.get(0).getBookId().substring(1));
                //System.out.println(books.get(0).getAuthor());
                //System.out.println(books.get(0).getTitle());
                //System.out.println(id);
                id++;
                for(int i=id.toString().length();i<=3;i++)
                    prefix.append("0");
                prefix.append(id);
            }
            else{
                prefix.append("0001");
            }

            book.setbId(prefix.toString());
            book.setBookId(prefix.toString());
            book.setIsbn(bh.getIsbn());
            book.setTitle(bh.getTitle());
            book.setAuthor(bh.getAuthor());
            book.setPublishedYear(bh.getPublishedYear());
            book.setCount(bh.getCount());

            book = bookService.addBook(book);

            if(book==null){
                return new ResponseEntity<String>("BookDetails Details could not be saved.", HttpStatus.EXPECTATION_FAILED);
            }
        }
        return new ResponseEntity<BookDetails>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "/authorizedAdmin/searchBook/{pattern}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBooksByTitlePatternForAdmin(@PathVariable String pattern) {
        books = bookService.findByTitlePattern(new StringBuilder().append("%").append(pattern).append("%").toString().toLowerCase());
        return new ResponseEntity<List<BookDetails>>(books, HttpStatus.OK);
    }

    /*@RequestMapping(value = "/searchBook/{title}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBooksByTitle(@PathVariable String title) {
        book = bookService.findByTitle(title.toLowerCase());
        return new ResponseEntity<BookDetails>(book, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/authorizedAdmin/deleteBook/{id}", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBook(@PathVariable String id) {

        boolean deleteStatus = true;
        Iterator<StudentDetails> studentsList = studentService.findAll().iterator();
        while(studentsList.hasNext()){
            student = studentsList.next();
            //System.out.println(student.getsId());
            for (int i=0; i<student.getBooksBorrowed().size(); i++){
                if (student.getBooksBorrowed().get(i).getBookId().equals(id)){
                    deleteStatus = false;
                    break;
                }
                else continue;
            }
            if (!deleteStatus) break;
            else continue;
        }

        //System.out.println(deleteStatus);
        if (deleteStatus) bookService.delete(id);
        else return new ResponseEntity<String>("Book is not deleted", HttpStatus.EXPECTATION_FAILED);

        return new ResponseEntity<String>("Book is deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/authorizedAdmin/findBookById/{id}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findBookById(@PathVariable String id) {
       book = bookService.findById(id);

        if(book==null){
            return new ResponseEntity<String>("BookDetails Details could not be fetched.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BookDetails>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "/authorizedAdmin/updateBook", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBook(@RequestBody BookUpdateHolder buh) {

        BookDetails book = new BookDetails();

        book.setbId(buh.getBookId());
        book.setBookId(buh.getBookId());
        book.setIsbn(buh.getIsbn());
        book.setTitle(buh.getTitle());
        book.setAuthor(buh.getAuthor());
        book.setPublishedYear(buh.getPublishedYear());
        book.setCount(buh.getCount());

        book = bookService.addBook(book);

        if(book==null){
            return new ResponseEntity<String>("BookDetails Details could not be saved.", HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<BookDetails>(book, HttpStatus.OK);
    }


}
