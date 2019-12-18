package com.rest.services.restful.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Employee_Id")
    private Integer id;

    @Column(name = "Employee_Name")
    private String name;

    @Column(name = "Employee_Desc")
    private String description;
}
