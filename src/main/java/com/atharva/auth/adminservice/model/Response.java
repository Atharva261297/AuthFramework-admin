package com.atharva.auth.adminservice.model;

import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private ErrorCodes code;
    private String data;
}
