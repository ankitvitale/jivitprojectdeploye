package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.HealthCheckupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthCheckupDao extends JpaRepository<HealthCheckupRequest ,Long> {

    List<HealthCheckupRequest> findAllByHospitalid_Id(Long id);
}
