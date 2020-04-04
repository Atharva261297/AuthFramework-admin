package com.atharva.auth.adminservice.model.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginModel {
    private String id;
    private String pass;
}
