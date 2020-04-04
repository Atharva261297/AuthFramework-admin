package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.service.ProjectService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public ErrorCodes addProject(@RequestBody ProjectModel projectModel) {
        return projectService.registerProject(projectModel);
    }

    @PostMapping("/incrementUser/{count}")
    public void incrementUser(@RequestParam String projectId, @PathVariable(name = "count") int count) {
        projectService.updateUserCount(projectId, count);
    }
}
