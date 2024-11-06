package com.jivitHealcare.Entity;

import com.jivitHealcare.Entity.enums.CleamRequestStatus;
import jakarta.persistence.*;

@Entity
public class HealthCheckupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hospital;
    private String cardNo;
    private String employeeName;
    private String pesentName;
    private String depermentName;
    private String location;
    private String coupon;
    private  String doc1;
    private  String status= String.valueOf(CleamRequestStatus.Pending);

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalid;  // Association with the user (Hospital)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPesentName() {
        return pesentName;
    }

    public void setPesentName(String pesentName) {
        this.pesentName = pesentName;
    }

    public String getDepermentName() {
        return depermentName;
    }

    public void setDepermentName(String depermentName) {
        this.depermentName = depermentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDoc1() {
        return doc1;
    }

    public void setDoc1(String doc1) {
        this.doc1 = doc1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Hospital getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(Hospital hospitalid) {
        this.hospitalid = hospitalid;
    }
}
