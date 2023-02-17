package com.bdcc.assignment1;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class A2_InputDto {
    double mag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date2;
    double longitude;
    double latitude;
    double maxDistance;
}
