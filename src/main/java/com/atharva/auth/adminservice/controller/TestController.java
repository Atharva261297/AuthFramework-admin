package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.service.ProjectService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProjectService projectService;


}
