package com.jivitHealcare.service;


import com.jivitHealcare.Entity.AdminRegister;
import com.jivitHealcare.Entity.CleamRequest;
import com.jivitHealcare.Entity.Hospital;
import com.jivitHealcare.Entity.enums.CleamRequestStatus;
import com.jivitHealcare.Repo.AdminDao;
import com.jivitHealcare.Repo.CleamRequestDao;
import com.jivitHealcare.Repo.HealthCheckupDao;
import com.jivitHealcare.Repo.HospitalDao;
import com.jivitHealcare.Security.JwtAuthenticationFilter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CleamRequestService {
   public static String CURRENT_USER="";
    @Autowired
    private CleamRequestDao cleamRequestDao;

    @Autowired
    private HospitalDao hospitalDao;

    @Autowired
    private AdminDao adminDao;

    public CleamRequest createCleamRequest(CleamRequest cleamRequest) {
        return  cleamRequestDao.save(cleamRequest);
    }

    public List<CleamRequest> getAllCleamRequestByHospital() {

        String email = JwtAuthenticationFilter.CURRENT_USER;
        Hospital hospital = hospitalDao.findByEmail(email);

        if (hospital == null) {
            throw new RuntimeException("User not found");
        }

        return cleamRequestDao.findAllByHospital_Id(hospital.getId());
    }



    public ResponseEntity<CleamRequest> updateStatusRejected(Long id) {
        CleamRequest cleamRequest=cleamRequestDao.findById(id).orElseThrow();
        if(cleamRequest !=null){
            cleamRequest.setStatus(String.valueOf(CleamRequestStatus.Rejected));
            cleamRequestDao.save(cleamRequest);
        }

        return null;
    }


    public ResponseEntity<CleamRequest> updateStatusAuthorized(Long id) {
        CleamRequest cleamRequest=cleamRequestDao.findById(id).orElseThrow();
        if(cleamRequest !=null){

            cleamRequest.setStatus(String.valueOf(CleamRequestStatus.Authorized));
            cleamRequestDao.save(cleamRequest);
        }

        return null;
    }

    public List<CleamRequest> getAllCleamRequest() {
        return  cleamRequestDao.findAll();    }

    public CleamRequest getCleamRequestById(Long id) {
        return  cleamRequestDao.findById(id).orElse(null);   }

    public CleamRequest updateCleamRequest(CleamRequest existingCleamRequest) {
        return  cleamRequestDao.save(existingCleamRequest);
    }
}

