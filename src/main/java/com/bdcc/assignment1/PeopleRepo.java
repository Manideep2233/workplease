package com.bdcc.assignment1;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepo extends MongoRepository<People,String> {

//    @Query("{ $text: { $search: ?0 } }")
//@Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, " +
//        " { 'state': { $regex: ?0, $options: 'i' } }, " +
//        " { 'keywords': { $regex: ?0, $options: 'i' } } " +
//        "] }")
//    List<People> findByText(String text);
}
