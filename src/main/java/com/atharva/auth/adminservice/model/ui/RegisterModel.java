package com.atharva.auth.adminservice.model.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {

    private String id;
    private String name;
    private String email;
    private String pass;
}
