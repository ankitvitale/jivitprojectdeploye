package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.Appointment;
import org.springframework.context.annotation.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentDao  extends JpaRepository<Appointment, Long> {
}