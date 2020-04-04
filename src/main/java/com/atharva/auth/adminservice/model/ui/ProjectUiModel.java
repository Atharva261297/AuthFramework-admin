package com.atharva.auth.adminservice.model.ui;

import com.atharva.auth.adminservice.model.ProjectModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUiModel {
    private String id;
    private String name;
    private int userSize;
    private int adminSize;

    public ProjectUiModel(ProjectModel projectModel, int adminSize) {
        this.id = projectModel.getId();
        this.name = projectModel.getName();
        this.userSize = projectModel.getUserSize();
        this.adminSize = adminSize;
    }
}
