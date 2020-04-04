package com.atharva.auth.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationId implements Serializable {
    private String projectId;
    private String adminId;
}
