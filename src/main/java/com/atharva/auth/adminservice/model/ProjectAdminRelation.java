package com.atharva.auth.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relation", schema = "project")
public class ProjectAdminRelation {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Id
    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "rights")
    private AdminRights rights;

}
