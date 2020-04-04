package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.client.AuthFeignClient;
import com.atharva.auth.adminservice.client.EmailFeignClient;
import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
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
    private EmailFeignClient emailClient;

    @Autowired
    private AdminRepository adminRepository;

    @Value("${project.name}")
    private String projectName;

    Logger log = LoggerFactory.getLogger(AuthService.class);

    public ErrorCodes register(String adminAuth, AdminModel adminModel) {
        ErrorCodes registerCode = client.register(adminAuth);
        log.debug("Register 2 : " + registerCode);
        if (registerCode == ErrorCodes.SUCCESS) {
            adminModel.setVerified(false);
            adminRepository.save(adminModel);
            return emailClient.senVerificationEmail(adminModel.getEmail(), adminModel.getId(), projectName);
        } else {
            return registerCode;
        }
    }

    @Transactional
    public ErrorCodes login(String auth) {
        ErrorCodes loginCode = client.login(auth);
        if (loginCode == ErrorCodes.SUCCESS) {
            final String[] split = auth.split(":");
            final String id = new String(Base64.getDecoder().decode(split[0]));
            if (adminRepository.getOne(id).isVerified()) {
                return ErrorCodes.SUCCESS;
            } else {
                return ErrorCodes.ACCOUNT_NOT_VERIFIED;
            }
        } else {
            return loginCode;
        }
    }

    @Transactional
    public ErrorCodes adminVerified(String userId) {
        adminRepository.getOne(userId).setVerified(true);
        return ErrorCodes.SUCCESS;
    }

    public ErrorCodes resetPassword(String auth) {
        return client.resetPassword(auth);
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

    public ErrorCodes sendResetPasswordMail(String id) {
        if (adminRepository.existsById(id)) {
            return emailClient.sendResetPassword(adminRepository.getOne(id).getEmail(), id, projectName);
        } else {
            return ErrorCodes.ID_INCORRECT;
        }
    }
}
