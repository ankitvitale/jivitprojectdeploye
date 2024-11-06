package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.AddBenificiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BenificiaeyDao extends JpaRepository<AddBenificiary ,Long> {
    @Query("SELECT MAX(e.id) FROM AddBenificiary e")
    Long findMaxId();
}
