package com.lms.repo;

import com.lms.model.StudentLogin;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.repository.CrudRepository;

import com.lms.model.StudentDetails;

import java.util.List;


public interface StudentRepository extends CrudRepository<StudentDetails, String>{

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and studentId = $1 and $2 within #{#n1ql.bucket} ")
    List<StudentLogin> checkLogin(String studentId, String password);

}
