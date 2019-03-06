package com.lms.service;

import com.lms.model.BookDetails;
import com.lms.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepo;


    @Override
    public BookDetails addBook(BookDetails bookDetails) {
        return bookRepo.save(bookDetails);

    }

    @Override
    public void delete(String id) {
        bookRepo.delete(id);
    }

    /*@Override
    public List<BookDetails> findAll() {
        List<BookDetails> bookList = new ArrayList<BookDetails>();
        Iterator<BookDetails> iter = bookRepo.findAll().iterator();
        while(iter.hasNext()){
            bookList.add(iter.next());
        }
        return bookList;
    }*/

    @Override
    public BookDetails findById(String id) {
        return bookRepo.findOne(id);
    }

    @Override
    public List<BookDetails> findByTitlePattern(String pattern) {
        return bookRepo.findByTitlePattern(pattern);
    }

    @Override
    public BookDetails findByTitle(String title) {
        return bookRepo.findByTitle(title);
    }

    @Override
    public List<BookDetails> findAllBooksDescOrder() {
        return bookRepo.findAllBooksDescOrder();
    }


}
