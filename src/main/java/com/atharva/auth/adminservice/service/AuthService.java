package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.client.AuthFeignClient;
import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class AuthService {

    private Map<String, String> authKeys = new HashMap<>();

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

    public ErrorCodes login(String auth) {
        return client.login(auth);
    }

    public ErrorCodes verify(String userId, String authKey) {
        if (authKeys.getOrDefault(userId, StringUtils.EMPTY).equals(authKey)) {
            return ErrorCodes.SUCCESS;
        } else {
            return ErrorCodes.AUTH_KEY_NOT_VALID;
        }
    }

    public String generateKey(String userId) {
        String key = UUID.randomUUID().toString();
        authKeys.put(userId, key);
        return key;
    }
}
