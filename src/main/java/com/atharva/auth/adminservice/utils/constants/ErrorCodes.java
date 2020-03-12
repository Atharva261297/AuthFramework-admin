package com.atharva.auth.adminservice.utils.constants;

public enum ErrorCodes {
    SUCCESS(200),

    PASS_INCORRECT(401),
    ID_INCORRECT(402),
    ID_ALREADY_EXITS(403),

    UNKNOWN(500);

    int code;
    ErrorCodes(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
