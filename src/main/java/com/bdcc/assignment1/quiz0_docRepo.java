package com.bdcc.assignment1;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface quiz0_docRepo extends MongoRepository<Quiz0_doc,String> {
//
//    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, " +
//            " { 'state': { $regex: ?0, $options: 'i' } }, " +
//            " { 'keywords': { $regex: ?0, $options: 'i' } } " +
//            "] }")


    @Query("{'name':?0}")
    public List<Quiz0_doc> findByName(String name);
}
