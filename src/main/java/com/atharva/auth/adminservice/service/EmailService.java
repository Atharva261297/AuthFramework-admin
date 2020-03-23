package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class EmailService {

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    public ErrorCodes adminVerified(String userId) {
        adminRepository.getOne(userId).setVerified(true);
        return ErrorCodes.SUCCESS;
    }
}
