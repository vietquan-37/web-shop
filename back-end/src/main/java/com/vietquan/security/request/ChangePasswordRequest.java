package com.vietquan.security.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    @NotNull
    @Pattern(
            regexp = "^[A-Z].{7,}$",
            message = "password letter have to be at least 8 character and first letter have to be Uppercase "
    )
    private String newPassword;
    private String confirmPassword;
}
