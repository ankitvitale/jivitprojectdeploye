package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.CleamRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CleamRequestDao extends JpaRepository<CleamRequest ,Long> {
    List<CleamRequest> findAllByHospital_Id(Long userId);


}
