package com.atharva.auth.adminservice.dao;

import com.atharva.auth.adminservice.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, String> {
}
