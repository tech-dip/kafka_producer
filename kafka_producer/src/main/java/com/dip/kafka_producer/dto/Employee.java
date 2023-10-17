package com.dip.kafka_producer.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Employee {
    private int empId;
    private String emp_firstname;
    private  String emp_lastname;

}
