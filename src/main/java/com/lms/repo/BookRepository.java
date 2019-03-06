package com.lms.repo;

import com.lms.model.BookDetails;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "bookDetails")
public interface BookRepository extends CrudRepository<BookDetails, String> {

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and LOWER(title) LIKE $1")
    List<BookDetails> findByTitlePattern(String pattern);

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and LOWER(title) = $1")
    BookDetails findByTitle(String title);

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} ORDER BY bookId DESC")
    List<BookDetails> findAllBooksDescOrder();

}
