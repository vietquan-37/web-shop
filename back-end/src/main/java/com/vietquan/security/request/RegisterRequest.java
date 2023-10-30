package com.vietquan.security.request;

import com.vietquan.security.enumPackage.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(
        staticName = "build"
)
public class RegisterRequest {
    @NotNull(
            message = "first name can be null"
    )
    private String firstname;
    @NotNull(
            message = "last name can be null"
    )
    private String lastname;
    @NotNull
    @Email(
            message = "invalid email"
    )
    private String email;
    @NotNull
    @Pattern(
            regexp = "^[A-Z].{7,}$",
            message = "password letter have to be at least 8 character and first letter have to be Uppercase "
    )
    private String password;

    private boolean mfaEnable;


}
