package com.lms.service;

import com.lms.model.BookDetails;

import java.util.List;

public interface BookService {

    BookDetails addBook(BookDetails bookDetails);
    void delete(String id);
    //List<BookDetails> findAll();
    BookDetails findById(String id);
    List<BookDetails> findByTitlePattern(String pattern);
    BookDetails findByTitle(String title);
    List<BookDetails> findAllBooksDescOrder();
}
