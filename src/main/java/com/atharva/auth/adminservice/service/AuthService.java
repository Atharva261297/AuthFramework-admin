package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.client.AuthFeignClient;
import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class AuthService {

    @Autowired
    private AuthFeignClient client;

    @Autowired
    private AdminRepository adminRepository;

    public ErrorCodes register(String adminAuth, AdminModel adminModel) {
        ErrorCodes registerCode = client.register(adminAuth);
        if (registerCode == ErrorCodes.SUCCESS) {
            adminModel.setVerified(false);
            adminRepository.save(adminModel);
            return ErrorCodes.SUCCESS;
        } else {
            return registerCode;
        }
    }

    public ErrorCodes verify(String auth) {
        return client.login(auth);
    }
}
