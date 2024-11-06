package com.jivitHealcare.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BenificiaryCardDependents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gender;
    private int age;
    private String relation;


    @ManyToOne
    @JoinColumn(name = "benificiary_card_id")
    @JsonIgnore // Prevents recursion when serializing the parent object
        private AddBenificiary addBenificiary;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public AddBenificiary getAddBenificiary() {
        return addBenificiary;
    }

    public void setAddBenificiary(AddBenificiary addBenificiary) {
        this.addBenificiary = addBenificiary;
    }
}
