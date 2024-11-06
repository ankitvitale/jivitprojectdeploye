package com.jivitHealcare.service;

import com.jivitHealcare.Entity.HospitalPayment;
import com.jivitHealcare.Repo.HospitalPaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalpaymenetService {
    @Autowired
    private HospitalPaymentDao hospitalPaymentDao;
    public HospitalPayment addHospitalPayment(HospitalPayment hospitalPayment) {
        return  hospitalPaymentDao.save(hospitalPayment);
    }

    public List<HospitalPayment> getAllHospitalPayments() {
        return hospitalPaymentDao.findAll();
    }
}
