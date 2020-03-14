package com.atharva.auth.adminservice.controller;

import com.atharva.auth.adminservice.model.AdminModel;
import com.atharva.auth.adminservice.service.AuthService;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public ErrorCodes register(@RequestHeader String admin_auth, @RequestBody AdminModel adminModel) {
        return authService.register(admin_auth,adminModel);
    }

    @GetMapping("/login")
    public ErrorCodes login() {
        return ErrorCodes.SUCCESS;
    }
}
