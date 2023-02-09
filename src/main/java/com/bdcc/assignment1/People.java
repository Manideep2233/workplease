package com.bdcc.assignment1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table
@Document
public class People {

//    @Id
//    @GeneratedValue

    @Id
    public String pid;
    public String name;
    public String state;
    public int salary;
    public int grade;
    public int room;
    public int telnum;
    public String picture;
    public String keywords;
    public String imageUrl;

}
