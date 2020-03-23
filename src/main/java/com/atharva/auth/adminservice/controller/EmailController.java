package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.service.EmailService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify/{userId}")
    public ErrorCodes emailVerified(@PathVariable(name = "userId") String userId) {
        return emailService.adminVerified(userId);
    }
}
