package com.atharva.auth.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins", schema = "project_data")
public class AdminModel {

    @Id
    private String id;

    @Column
    private String email;

    @Column
    private boolean verified;

    @Column
    private String name;
}
