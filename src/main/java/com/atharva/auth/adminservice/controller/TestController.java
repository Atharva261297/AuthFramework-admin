package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.service.ProjectService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/register")
    public ErrorCodes registerAdmin(@RequestHeader String admin_auth, @RequestBody AdminModel adminModel) {
        return authService.register(admin_auth,adminModel);
    }

    @PostMapping("/project/add")
    public ErrorCodes addProject(@RequestBody ProjectModel projectModel) {
        return projectService.registerProject(projectModel);
    }
}
