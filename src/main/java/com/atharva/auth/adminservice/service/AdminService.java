package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.dao.ProjectRepository;
import com.atharva.auth.adminservice.model.AdminModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository dao;

    @Autowired
    private ProjectRepository prjDao;

    public AdminModel getDetails(String id) {
        return dao.getOne(id);
    }
}
