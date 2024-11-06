package com.jivitHealcare.Entity;

import com.jivitHealcare.Entity.enums.CleamRequestStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CleamRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;
    private String patientName;
    private String healthCardNo;
    private String departmentName;
    private String mobileNo;
    private String relationWithEmployee;
    private String address;

    // Part 2
    private String chiefComplaints;
    private String provisionalDiagnosis;
    private String planOfTreatmentMedical;
    private String planOfTreatmentSurgical;
    private String grAilment;
    private String grAilmentCode;
    private String dateOfAdmission;
    private String expectedLengthOfStay;
    private String classOfAccommodation;
    private String perDayRoomRent;
    private String expectedCostInvestigation;
    private String medicinesConsumablesCost;
    private String doctorFeeSurgeonAss;
    private String surgeonAnesthetistVisitCharges;
    private String totalExpenseHospitalization;
    private String nameOfDoctor;
    private String doctorRegistrationNumber;
    private Boolean alcoholAbuse;
    private Boolean mlcFirCopy;

    // Documents Upload
    private String aadharCard;
    private String jivatHealthCard;
    private String salaryACCheque;
    private String promissoryNote;

    private String cutumnbpramanpatra;
    private String parishitdocument;

    private String finalbill;
    private String dischargecard;
    private String massage;
    private  String status= String.valueOf(CleamRequestStatus.Pending);

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;  // Association with the user (Hospital)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHealthCardNo() {
        return healthCardNo;
    }

    public void setHealthCardNo(String healthCardNo) {
        this.healthCardNo = healthCardNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRelationWithEmployee() {
        return relationWithEmployee;
    }

    public void setRelationWithEmployee(String relationWithEmployee) {
        this.relationWithEmployee = relationWithEmployee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChiefComplaints() {
        return chiefComplaints;
    }

    public void setChiefComplaints(String chiefComplaints) {
        this.chiefComplaints = chiefComplaints;
    }

    public String getProvisionalDiagnosis() {
        return provisionalDiagnosis;
    }

    public void setProvisionalDiagnosis(String provisionalDiagnosis) {
        this.provisionalDiagnosis = provisionalDiagnosis;
    }

    public String getPlanOfTreatmentMedical() {
        return planOfTreatmentMedical;
    }

    public void setPlanOfTreatmentMedical(String planOfTreatmentMedical) {
        this.planOfTreatmentMedical = planOfTreatmentMedical;
    }

    public String getPlanOfTreatmentSurgical() {
        return planOfTreatmentSurgical;
    }

    public void setPlanOfTreatmentSurgical(String planOfTreatmentSurgical) {
        this.planOfTreatmentSurgical = planOfTreatmentSurgical;
    }

    public String getGrAilment() {
        return grAilment;
    }

    public void setGrAilment(String grAilment) {
        this.grAilment = grAilment;
    }

    public String getGrAilmentCode() {
        return grAilmentCode;
    }

    public void setGrAilmentCode(String grAilmentCode) {
        this.grAilmentCode = grAilmentCode;
    }

    public String getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getExpectedLengthOfStay() {
        return expectedLengthOfStay;
    }

    public void setExpectedLengthOfStay(String expectedLengthOfStay) {
        this.expectedLengthOfStay = expectedLengthOfStay;
    }

    public String getClassOfAccommodation() {
        return classOfAccommodation;
    }

    public void setClassOfAccommodation(String classOfAccommodation) {
        this.classOfAccommodation = classOfAccommodation;
    }

    public String getPerDayRoomRent() {
        return perDayRoomRent;
    }

    public void setPerDayRoomRent(String perDayRoomRent) {
        this.perDayRoomRent = perDayRoomRent;
    }

    public String getExpectedCostInvestigation() {
        return expectedCostInvestigation;
    }

    public void setExpectedCostInvestigation(String expectedCostInvestigation) {
        this.expectedCostInvestigation = expectedCostInvestigation;
    }

    public String getMedicinesConsumablesCost() {
        return medicinesConsumablesCost;
    }

    public void setMedicinesConsumablesCost(String medicinesConsumablesCost) {
        this.medicinesConsumablesCost = medicinesConsumablesCost;
    }

    public String getDoctorFeeSurgeonAss() {
        return doctorFeeSurgeonAss;
    }

    public void setDoctorFeeSurgeonAss(String doctorFeeSurgeonAss) {
        this.doctorFeeSurgeonAss = doctorFeeSurgeonAss;
    }

    public String getSurgeonAnesthetistVisitCharges() {
        return surgeonAnesthetistVisitCharges;
    }

    public void setSurgeonAnesthetistVisitCharges(String surgeonAnesthetistVisitCharges) {
        this.surgeonAnesthetistVisitCharges = surgeonAnesthetistVisitCharges;
    }

    public String getTotalExpenseHospitalization() {
        return totalExpenseHospitalization;
    }

    public void setTotalExpenseHospitalization(String totalExpenseHospitalization) {
        this.totalExpenseHospitalization = totalExpenseHospitalization;
    }

    public String getNameOfDoctor() {
        return nameOfDoctor;
    }

    public void setNameOfDoctor(String nameOfDoctor) {
        this.nameOfDoctor = nameOfDoctor;
    }

    public String getDoctorRegistrationNumber() {
        return doctorRegistrationNumber;
    }

    public void setDoctorRegistrationNumber(String doctorRegistrationNumber) {
        this.doctorRegistrationNumber = doctorRegistrationNumber;
    }

    public Boolean getAlcoholAbuse() {
        return alcoholAbuse;
    }

    public void setAlcoholAbuse(Boolean alcoholAbuse) {
        this.alcoholAbuse = alcoholAbuse;
    }

    public Boolean getMlcFirCopy() {
        return mlcFirCopy;
    }

    public void setMlcFirCopy(Boolean mlcFirCopy) {
        this.mlcFirCopy = mlcFirCopy;
    }

    public String getAadharCard() {
        return aadharCard;
    }

    public void setAadharCard(String aadharCard) {
        this.aadharCard = aadharCard;
    }

    public String getJivatHealthCard() {
        return jivatHealthCard;
    }

    public void setJivatHealthCard(String jivatHealthCard) {
        this.jivatHealthCard = jivatHealthCard;
    }

    public String getSalaryACCheque() {
        return salaryACCheque;
    }

    public void setSalaryACCheque(String salaryACCheque) {
        this.salaryACCheque = salaryACCheque;
    }

    public String getPromissoryNote() {
        return promissoryNote;
    }

    public void setPromissoryNote(String promissoryNote) {
        this.promissoryNote = promissoryNote;
    }

    public String getStatus() {
        return status;
    }

    public String getCutumnbpramanpatra() {
        return cutumnbpramanpatra;
    }

    public void setCutumnbpramanpatra(String cutumnbpramanpatra) {
        this.cutumnbpramanpatra = cutumnbpramanpatra;
    }

    public String getParishitdocument() {
        return parishitdocument;
    }

    public void setParishitdocument(String parishitdocument) {
        this.parishitdocument = parishitdocument;
    }

    public String getFinalbill() {
        return finalbill;
    }

    public void setFinalbill(String finalbill) {
        this.finalbill = finalbill;
    }

    public String getDischargecard() {
        return dischargecard;
    }

    public void setDischargecard(String dischargecard) {
        this.dischargecard = dischargecard;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
