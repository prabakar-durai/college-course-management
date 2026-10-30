package com.prabakar.collegecourse.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected Department(){

    }

    public void setName(String name) {

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException(
                    "com.prabakar.collegecourse.domain.Department name can not be empty");
        }
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


   public  Department(Long id, String name, String code){
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public  void  displayDetails(){
        System.out.println(id + " ~ " + code + " ~ " + name);
    }

    @Override
    public String toString(){
        return (id + " ~ " + code + " ~ " + name);

    }

}
