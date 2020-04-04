package com.atharva.auth.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relation", schema = "project_data")
@IdClass(RelationId.class)
public class ProjectAdminRelation implements Serializable {

    @Id
    @Column(name = "project_id")
    private String projectId;

    @Id
    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "rights")
    private AdminRights rights;

}
