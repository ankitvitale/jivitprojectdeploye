package com.jivitHealcare.service;

import com.jivitHealcare.Entity.Appointment;
import com.jivitHealcare.Entity.CleamRequest;
import com.jivitHealcare.Entity.HealthCheckupRequest;
import com.jivitHealcare.Entity.Hospital;
import com.jivitHealcare.Entity.enums.CleamRequestStatus;
import com.jivitHealcare.Repo.AppointmentDao;
import com.jivitHealcare.Repo.HealthCheckupDao;
import com.jivitHealcare.Repo.HospitalDao;
import com.jivitHealcare.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckupService {

    @Autowired
    private HealthCheckupDao healthCheckupDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private HospitalDao hospitalDao;
    public HealthCheckupRequest healthCheckupRequest(HealthCheckupRequest healthCheckupRequest) {
        return healthCheckupDao.save(healthCheckupRequest);
    }

    public List<HealthCheckupRequest> getAllHeathCheckupList() {
        String email = JwtAuthenticationFilter.CURRENT_USER;
        Hospital hospital = hospitalDao.findByEmail(email);

        if (hospital == null) {
            throw new RuntimeException("User not found");
        }

        return healthCheckupDao.findAllByHospitalid_Id(hospital.getId());
    }

    public HealthCheckupRequest getHeathCheckupById(Long id) {
        return healthCheckupDao.findById(id).orElseThrow();

    }

    public ResponseEntity<HealthCheckupRequest> updateHealthCheckupRequestStatusAuthorized(Long id) {
        HealthCheckupRequest healthCheckupRequest=healthCheckupDao.findById(id).orElseThrow();
        if(healthCheckupRequest !=null){

            healthCheckupRequest.setStatus(String.valueOf(CleamRequestStatus.Authorized));
            healthCheckupDao.save(healthCheckupRequest);
        }

        return null;
    }

    public ResponseEntity<HealthCheckupRequest> updateHealthCheckupRequestStatusReject(Long id) {
        HealthCheckupRequest healthCheckupRequest=healthCheckupDao.findById(id).orElseThrow();
        if(healthCheckupRequest !=null){

            healthCheckupRequest.setStatus(String.valueOf(CleamRequestStatus.Rejected));
            healthCheckupDao.save(healthCheckupRequest);
        }

        return null;
    }

    public ResponseEntity<HealthCheckupRequest> updateStatusAuthorized(Long id) {
        HealthCheckupRequest healthCheckupRequest=healthCheckupDao.findById(id).orElseThrow();
        if(healthCheckupRequest !=null){

            healthCheckupRequest.setStatus(String.valueOf(CleamRequestStatus.Authorized));
            healthCheckupDao.save(healthCheckupRequest);
        }

        return null;
    }

    public HealthCheckupRequest updateStatusRejected(Long id) {
        HealthCheckupRequest healthCheckupRequest=healthCheckupDao.findById(id).orElseThrow();
        if(healthCheckupRequest !=null){

            healthCheckupRequest.setStatus(String.valueOf(CleamRequestStatus.Rejected));
            healthCheckupDao.save(healthCheckupRequest);
        }

        return null;
    }

    public List<HealthCheckupRequest> adminHeathCheckupList() {
        return healthCheckupDao.findAll();
    }

    public ResponseEntity<Appointment> updateAppoinmentStatusAuthorized(Long id) {
        Appointment appointment=appointmentDao.findById(id).orElseThrow();
        if(appointment !=null){

            appointment.setStatus(String.valueOf(CleamRequestStatus.Authorized));
            appointmentDao.save(appointment);
        }

        return null;
    }

    public ResponseEntity<Appointment> updateAppoinmentStatusReject(Long id) {
        Appointment appointment=appointmentDao.findById(id).orElseThrow();
        if(appointment!=null){
            appointment.setStatus(String.valueOf(CleamRequestStatus.Rejected));
            appointmentDao.save(appointment);
        }
        return null;
    }
}


