package com.atharva.auth.adminservice.model.ui;

import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.model.ProjectModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailModel {
    private String id;
    private String name;
    private AdminModel owner;
    private String key;
    private int userSize;
    private List<ProjectAdminsModel> admins;
    private boolean amIOwner;
    private boolean writeEnabled;

    public ProjectModel getProjectModel() {
        return new ProjectModel(id, name, owner.getId(), userSize);
    }
}
