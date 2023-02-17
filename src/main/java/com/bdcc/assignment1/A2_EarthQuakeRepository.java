package com.bdcc.assignment1;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface A2_EarthQuakeRepository extends MongoRepository<A2_EarthQuake, String> {
}
