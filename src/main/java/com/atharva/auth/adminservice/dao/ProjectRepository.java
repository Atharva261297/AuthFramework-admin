package com.atharva.auth.adminservice.dao;

import com.atharva.auth.adminservice.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, String> {
}
