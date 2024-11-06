package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalDao extends JpaRepository<Hospital,Long> {
    Hospital findByEmail(String email);

    Optional<Hospital> findByEmailAndOtp(String email, String otp);
}
