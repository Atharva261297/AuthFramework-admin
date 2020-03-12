//package com.atharva.auth.adminservice.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "relation", schema = "project")
//public class ProjectAdminRelation {
//
//    @Id
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "project_id", referencedColumnName = "id")
//    private ProjectModel project;
//
//    @Id
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "admin_id", referencedColumnName = "id")
//    private AdminModel admin;
//
//    @Column(name = "rights")
//    private AdminRights rights;
//
//}
