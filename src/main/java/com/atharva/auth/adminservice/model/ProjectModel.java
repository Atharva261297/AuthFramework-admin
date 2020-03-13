package com.atharva.auth.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "data", schema = "project")
public class ProjectModel {

    @Id
    private String id;

    @Column
    private String name;

    @Column(name = "owner_id")
    private String ownerId;

}
