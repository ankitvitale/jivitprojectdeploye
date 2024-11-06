package com.jivitHealcare.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity

@Table(name = "benificiary") // Specify the table name if different from the class name
public class AddBenificiary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cardNo;
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;

    @Column(name = "date_of_joining", nullable = false)
    private LocalDate dateOfJoining;

    @Column(name = "date_of_retirement")
    private LocalDate dateOfRetirement;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

  //  @Column(name = "card_issue_date", nullable = false)
    private LocalDate cardIssueDate;

    @Column(name = "aadhar_no", unique = true, nullable = false)
    private String aadharNo;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "department_location", nullable = false)
    private String departmentLocation;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "gender", nullable = false)
    private String gender;



    @ElementCollection
    @OneToMany(mappedBy = "addBenificiary", cascade = CascadeType.ALL)
    private List<BenificiaryCardDependents> benificiaryCardDependents = new ArrayList<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public LocalDate getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(LocalDate dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getCardIssueDate() {
        return cardIssueDate;
    }

    public void setCardIssueDate(LocalDate cardIssueDate) {
        this.cardIssueDate = cardIssueDate;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLocation() {
        return departmentLocation;
    }

    public void setDepartmentLocation(String departmentLocation) {
        this.departmentLocation = departmentLocation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<BenificiaryCardDependents> getBenificiaryCardDependents() {
        return benificiaryCardDependents;
    }

    public void setBenificiaryCardDependents(List<BenificiaryCardDependents> benificiaryCardDependents) {
        this.benificiaryCardDependents = benificiaryCardDependents;
    }
}
