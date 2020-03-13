package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.client.AuthFeignClient;
import com.atharva.auth.adminservice.dao.ProjectRepository;
import com.atharva.auth.adminservice.model.ProjectModel;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Base64;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class ProjectService {

    @Autowired
    private AuthFeignClient client;

    @Autowired
    private ProjectRepository projectRepository;

    public ErrorCodes registerProject(ProjectModel projectModel) {
        String id = Base64.getEncoder().encodeToString(projectModel.getId().getBytes());
        String pass = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
        ErrorCodes code = client.registerProject(id + ":" + pass);
        if (code == ErrorCodes.SUCCESS) {
            projectRepository.save(projectModel);
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }
}
