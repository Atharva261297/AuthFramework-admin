package com.atharva.auth.adminservice.dao;

import com.atharva.auth.adminservice.model.ProjectAdminRelation;
import com.atharva.auth.adminservice.model.RelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<ProjectAdminRelation, RelationId> {
}
