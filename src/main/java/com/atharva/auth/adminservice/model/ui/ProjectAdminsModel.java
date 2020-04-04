package com.atharva.auth.adminservice.model.ui;

import com.atharva.auth.adminservice.model.AdminRights;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAdminsModel {
    private String id;
    private String name;
    private AdminRights rights;
}
