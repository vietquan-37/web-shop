package com.vietquan.security.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String newPassword;
    private String confirmPassword;
}
