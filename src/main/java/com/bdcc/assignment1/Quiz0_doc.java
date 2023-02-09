package com.bdcc.assignment1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table
@Document
public class Quiz0_doc {
    @Id
    public String pid;
    public Integer num;
    public String name;
    public Integer year;
    public String picture;
    public String comments;
    public String imageUrl;
}
