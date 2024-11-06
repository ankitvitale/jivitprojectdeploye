package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.AdminRegister;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<AdminRegister, Long> {
    AdminRegister findByEmail(String email);

}
